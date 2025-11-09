
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

    public int add(Membership membership) {
        return membershipDAO.save(membership);
    }

    public int update(Membership membership) {
        return membershipDAO.update(membership);
    }

    public int delete(Membership membership) {
        return membershipDAO.delete(membership);
    }

    public int deleteById(int id) {
        return membershipDAO.deleteById(id);
    }

    //Chưa tối ưu
    public Membership getByMemberIdAndPackageId(int memberId, int packageId) { 
        List<Membership> memberships = membershipDAO.findAll();
        for(Membership m : memberships) {
            if (m.getMember().getId() == memberId &&
                m.getPackageO().getId() == packageId)
                return m;
        }
        return null;
    }
}
