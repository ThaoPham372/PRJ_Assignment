
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Member;
import model.Membership;
import model.Package;
import service.MemberService;
import service.MembershipService;
import service.PackageService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TRAINER\n\n");
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }
    
}
