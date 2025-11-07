package dao;

import java.util.List;
import model.Member;

/*
    Note: 
 */
public class MemberDAO {

    GenericDAO<Member> genericDAO;

    public MemberDAO() {
        genericDAO = new GenericDAO<>(Member.class);
    }

    public int save(Member member) {
        genericDAO.save(member);
        return member.getId();
    }

    public List<Member> findAll() {
        List<Member> members = genericDAO.findAll();
        return members != null ? members : List.of();
    }

    public Member findById(int id) {
        return genericDAO.findById(id);
    }

    public Member findByName(String name) {
        return genericDAO.findByField("name", name);
    }

    public Member findByEmail(String email) {
        return genericDAO.findByField("email", email);
    }

    public int existsByName(String name) {
        Member member = genericDAO.findByField("name", name);
        return member != null ? member.getId() : -1;
    }

    public int existsByEmail(String email) {
        Member member = genericDAO.findByField("email", email);
        return member != null ? member.getId() : -1;
    }

    public Member findByNameOrEmail(String nameOrEmail) {
        Member member = genericDAO.findByField("name", nameOrEmail);
        if (member != null)
            return member;
        member = genericDAO.findByField("email", nameOrEmail);
        return member != null ? member : null;
    }

    public int update(Member member) {
        return genericDAO.update(member);
    }

    public int delete(Member member) {
        member.setStatus("INACTIVE");
        return genericDAO.update(member);
    }

}
