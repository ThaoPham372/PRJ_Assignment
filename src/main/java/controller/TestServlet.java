
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Admin;
import model.Member;
import model.Trainer;
import model.User;
import service.AdminService;
import service.MemberService;
import service.TrainerService;
import service.UserService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin a = new Admin();
        AdminService adminService = new AdminService();
        a.setName("admin moi");
        a.setNote("Admin note 11/4");
        adminService.add(a);
        
        User u = new User();
        UserService userService = new UserService();
        u.setName("admin moi");
        u.setName("Name 11/4");
        userService.add(u);
        
        Trainer t = new Trainer();
        t.setSalary(9000d);
        TrainerService trainerService = new TrainerService();
        trainerService.add(t);
        
        Member s = new Member();
        s.setBmi(30f);
        MemberService studentService = new MemberService();
        studentService.add(s);
    }
    
}
