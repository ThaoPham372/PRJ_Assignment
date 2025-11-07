
package service;

import dao.MemberDAO;
import model.Member;
import java.util.List;

/*
    Note: 
 */
public class MemberService {
    
    private final MemberDAO memberDAO;
    
    public MemberService() {
        memberDAO = new MemberDAO();
    }
    
    //Member
    public List<Member> getAll() {
        return memberDAO.findAll();
    }
    
    public Member getById(int id) {
        return memberDAO.findById(id);
    }
    
    public Member getMemberByName(String name) {
        return memberDAO.findByName(name);
    }
    
    public Member getMemberByEmail(String email) {
        return memberDAO.findByEmail(email);
    }
    
    public int add(Member member) {
        return memberDAO.save(member);
    }

    public int update(Member member) {
        return memberDAO.update(member);
    }

    public int delete(Member member) {
        return memberDAO.delete(member);
    }
}
