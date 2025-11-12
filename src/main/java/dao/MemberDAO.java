package dao;

import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Member;

/*
    Note: 
 */
public class MemberDAO extends GenericDAO<Member> {
    private static final Logger LOGGER = Logger.getLogger(MemberDAO.class.getName());

    public MemberDAO() {
        super(Member.class);
    }

    public int save(Member member) {
        super.save(member);
        return member.getId();
    }

    public List<Member> findAll() {
        List<Member> members = super.findAll();
        return members != null ? members : List.of();
    }

    public Member findById(int id) {
        return super.findById(id);
    }

    public Member findByName(String name) {
        return super.findByField("name", name);
    }
    
    public Member findByUsername(String username) {
        return super.findByField("username", username);
    }

    public Member findByEmail(String email) {
        return super.findByField("email", email);
    }

    public int existsByName(String name) {
        Member member = super.findByField("name", name);
        return member != null ? member.getId() : -1;
    }

    public int existsByEmail(String email) {
        Member member = super.findByField("email", email);
        return member != null ? member.getId() : -1;
    }

    public Member findByNameOrEmail(String nameOrEmail) {
        Member member = super.findByField("name", nameOrEmail);
        if (member != null)
            return member;
        member = super.findByField("email", nameOrEmail);
        return member != null ? member : null;
    }

    public int update(Member member) {
        return super.update(member);
    }

    public int delete(Member member) {
        member.setStatus("INACTIVE");
        return super.update(member);
    }

    /**
     * Count total members who have at least one membership
     * @return Total count of distinct members with membership
     */
    public long countMembersWithMembership() {
        try {
            String jpql = "SELECT COUNT(DISTINCT m.member.id) FROM Membership m";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting members with membership", e);
            return 0L;
        }
    }

    /**
     * Count total active members (status = 'active')
     * @return Total count of active members
     */
    public long countActiveMembers() {
        try {
            String jpql = "SELECT COUNT(m) FROM Member m WHERE m.status = :status";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("status", "active");
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting active members", e);
            return 0L;
        }
    }

}
