
package service;

import dao.MembershipDAO;
import model.Membership;
import java.util.List;

/*
    Note: 
 */
public class MembershipService {

    private final MembershipDAO membershipDAO;

    public MembershipService() {
        membershipDAO = new MembershipDAO();
    }

    // Membership
    public List<Membership> getAll() {
        return membershipDAO.findAll();
    }

    public Membership getMembershipById(int id) {
        return membershipDAO.findById(id);
    }

    public Membership getMembershipByUserId(int id) {
        return membershipDAO.findByUserId(id);
    }

    public int add(Membership user) {
        return membershipDAO.save(user);
    }

    public int update(Membership user) {
        return membershipDAO.update(user);
    }

    public int delete(Membership user) {
        return membershipDAO.delete(user);
    }

    public int deleteById(int id) {
        return membershipDAO.deleteById(id);
    }
}
