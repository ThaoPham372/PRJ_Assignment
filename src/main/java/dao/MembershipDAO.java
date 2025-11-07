package dao;

import java.util.ArrayList;
import java.util.List;
import model.Membership;

/*
    Note: 
 */
public class MembershipDAO {

    GenericDAO<Membership> genericDAO;

    public MembershipDAO() {
        genericDAO = new GenericDAO<>(Membership.class);
    }

    public int save(Membership membership) {
        return genericDAO.save(membership);
    }

    public List<Membership> findAll() {
        List<Membership> memberships = genericDAO.findAll();
        return memberships != null ? memberships : new ArrayList<>(List.of());
    }

    public Membership findById(int id) {
        Membership membership = genericDAO.findById(id);
        return membership != null ? membership : null;
    }

    public Membership findByUserId(int id) {
        Membership membership = genericDAO.findByField("name", id);
        return membership;
    }

    public int update(Membership membership) {
        return genericDAO.update(membership);
    }

    public int delete(Membership membership) {
        membership.setStatus("INACTIVE");
        return genericDAO.update(membership);
    }

    public int deleteById(int id) {
        Membership membership = findById(id);
        if (membership != null) {
            membership.setStatus("INACTIVE");
            return genericDAO.update(membership);
        }
        return -1; // Indicate that the membership was not found
    }
}
