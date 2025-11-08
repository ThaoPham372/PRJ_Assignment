package dao;

import model.PasswordResetToken;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * DAO for PasswordResetToken
 * ✅ Extends GenericDAO for CRUD operations
 */
public class PasswordResetTokenDAO extends GenericDAO<PasswordResetToken> {
    
    public PasswordResetTokenDAO() {
        super(PasswordResetToken.class);
    }
    
    /**
     * Find valid token by token string
     * ✅ FIXED: Trim token to handle whitespace issues
     */
    public Optional<PasswordResetToken> findByToken(String token) {
        try {
            if (token == null) {
                return Optional.empty();
            }
            
            // ✅ Trim token to remove any whitespace
            String trimmedToken = token.trim();
            System.out.println("[PasswordResetTokenDAO] Searching for token: '" + trimmedToken + "' (length: " + trimmedToken.length() + ")");
            
            // Query all tokens and compare trimmed values (more reliable than TRIM in JPQL)
            String jpql = "SELECT t FROM PasswordResetToken t WHERE t.token = :token";
            TypedQuery<PasswordResetToken> query = em.createQuery(jpql, PasswordResetToken.class);
            query.setParameter("token", trimmedToken);
            
            PasswordResetToken result = query.getSingleResult();
            String dbToken = result.getToken();
            System.out.println("[PasswordResetTokenDAO] ✅ Token found in DB: '" + dbToken + "' (length: " + dbToken.length() + ")");
            
            // ✅ Double-check: compare trimmed values to handle any edge cases
            if (!dbToken.trim().equals(trimmedToken)) {
                System.err.println("[PasswordResetTokenDAO] ⚠️ Token mismatch after trim: input='" + trimmedToken + "', DB='" + dbToken.trim() + "'");
                return Optional.empty();
            }
            
            return Optional.of(result);
        } catch (NoResultException e) {
            System.err.println("[PasswordResetTokenDAO] ❌ Token not found: '" + (token != null ? token.trim() : "null") + "'");
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("[PasswordResetTokenDAO] Error finding token: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    /**
     * Find valid (unused & not expired) token by user email
     * ✅ MODIFIED: Rate limiting chỉ 30 giây thay vì 15 phút
     */
    public Optional<PasswordResetToken> findValidTokenByEmail(String email) {
        try {
            // ✅ Chỉ check tokens tạo trong 30 giây gần đây (rate limiting)
            LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
            
            String jpql = "SELECT t FROM PasswordResetToken t " +
                         "WHERE t.email = :email " +
                         "AND t.used = false " +
                         "AND t.expiresAt > :now " +
                         "AND t.createdAt > :thirtySecondsAgo " +  // ✅ Rate limiting: 30 seconds
                         "ORDER BY t.createdAt DESC";
            
            TypedQuery<PasswordResetToken> query = em.createQuery(jpql, PasswordResetToken.class);
            query.setParameter("email", email);
            query.setParameter("now", LocalDateTime.now());
            query.setParameter("thirtySecondsAgo", thirtySecondsAgo);
            query.setMaxResults(1);
            
            PasswordResetToken result = query.getSingleResult();
            System.out.println("[PasswordResetTokenDAO] Found valid token created at: " + result.getCreatedAt());
            return Optional.of(result);
        } catch (NoResultException e) {
            // No valid token found - OK, allow new request
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("[PasswordResetTokenDAO] Error finding valid token: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    /**
     * Mark token as used
     */
    public boolean markAsUsed(String token) {
        try {
            beginTransaction();
            
            PasswordResetToken tokenEntity = findByToken(token).orElse(null);
            if (tokenEntity == null) {
                rollbackTransaction();
                return false;
            }
            
            tokenEntity.setUsed(true);
            em.merge(tokenEntity);
            
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[PasswordResetTokenDAO] Error marking token as used: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Invalidate all tokens for a user
     */
    public int invalidateAllUserTokens(Integer userId) {
        try {
            beginTransaction();
            
            String jpql = "UPDATE PasswordResetToken t SET t.used = true WHERE t.userId = :userId AND t.used = false";
            int updated = em.createQuery(jpql)
                           .setParameter("userId", userId)
                           .executeUpdate();
            
            commitTransaction();
            return updated;
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[PasswordResetTokenDAO] Error invalidating tokens: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Clean up expired tokens (older than 24 hours)
     */
    public int cleanupExpiredTokens() {
        try {
            beginTransaction();
            
            String jpql = "DELETE FROM PasswordResetToken t WHERE t.expiresAt < :cutoff";
            LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
            
            int deleted = em.createQuery(jpql)
                           .setParameter("cutoff", cutoff)
                           .executeUpdate();
            
            commitTransaction();
            System.out.println("[PasswordResetTokenDAO] Cleaned up " + deleted + " expired tokens");
            return deleted;
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[PasswordResetTokenDAO] Error cleaning up tokens: " + e.getMessage());
            return 0;
        }
    }
}

