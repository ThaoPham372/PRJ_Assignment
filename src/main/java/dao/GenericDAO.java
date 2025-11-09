package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.User;

/**
 * Generic DAO with CRUD operations for all entities
 * Supports both Integer and Long ID types
 */
public class GenericDAO<T> extends BaseDAO {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        super();
        this.entityClass = entityClass;
    }
    
    // ==================== CREATE ====================
    
    /**
     * Save entity and return ID as int (backward compatibility)
     */
    public int save(T entity) {
        try {
            beginTransaction();
            em.persist(entity);
            commitTransaction();
            
            if(entity instanceof User) {
                return ((User) entity).getId();
            }
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
        return -1;
    }
    
    /**
     * Persist entity (returns void, more flexible)
     */
    public void persist(T entity) {
        try {
            beginTransaction();
            em.persist(entity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to persist entity", e);
        }
    }
    
    /**
     * Persist entity with shared EntityManager (for transactions)
     */
    public void persist(T entity, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            emToUse.persist(entity);
            if (sharedEm == null) {
                commitTransaction();
            } else {
                emToUse.flush();
            }
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            throw new RuntimeException("Failed to persist entity", e);
        }
    }
    
    // ==================== READ ====================
    
    /**
     * Find by Integer ID
     */
    public T findById(int id) {
        return em.find(entityClass, id);
    }
    
    /**
     * Find by Integer ID (Optional)
     */
    public Optional<T> findByIdOptional(int id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    /**
     * Find all entities
     */
    public List<T> findAll() {
        try {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        } catch (Exception e){
            System.err.println("Error finding all " + entityClass.getSimpleName() + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Find entity by field value
     */
    public T findByField(String fieldName, Object value){
        try{
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :val", entityClass)
                     .setParameter("val", value)
                     .getSingleResult();
        } catch(NoResultException e){
            return null;
        }
    }
    
    /**
     * Find entities by field value (multiple results)
     */
    public List<T> findAllByField(String fieldName, Object value){
        try{
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :val", entityClass)
                     .setParameter("val", value)
                     .getResultList();
        } catch(Exception e){
            System.err.println("Error finding by field " + fieldName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Update entity and return ID as int (backward compatibility)
     */
    public int update(T entity) {
        try {
            beginTransaction();
            em.merge(entity);
            commitTransaction();
            
            if(entity instanceof User)
                return ((User) entity).getId();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to update entity", e);
        }
        return -1;
    }
    
    /**
     * Merge entity (returns void, more flexible)
     */
    public void merge(T entity) {
        try {
            beginTransaction();
            em.merge(entity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to merge entity", e);
        }
    }
    
    /**
     * Merge entity with shared EntityManager (for transactions)
     */
    public T merge(T entity, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            T merged = emToUse.merge(entity);
            if (sharedEm == null) {
                commitTransaction();
            }
            return merged;
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            throw new RuntimeException("Failed to merge entity", e);
        }
    }
    
    // ==================== DELETE ====================
    
    /**
     * Delete entity and return ID as int (backward compatibility)
     */
    public int delete(T entity) {
        try {
            beginTransaction();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            commitTransaction();
            
            if(entity instanceof User) 
                return ((User)entity).getId();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to delete entity", e);
        }
        return -1;
    }
    
    /**
     * Remove entity (returns void, more flexible)
     */
    public void remove(T entity) {
        try {
            beginTransaction();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to remove entity", e);
        }
    }
    
    /**
     * Delete by ID
     */
    public void deleteById(Object id) {
        try {
            beginTransaction();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to delete entity by ID", e);
        }
    }
    
    // ==================== UTILITY ====================
    
    /**
     * Count all entities
     */
    public long count() {
        try {
            return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting " + entityClass.getSimpleName() + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Get EntityManager for advanced operations
     */
    protected EntityManager getEntityManager() {
        return em;
    }
}
