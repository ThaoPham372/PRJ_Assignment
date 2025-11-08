package controller;

import Utils.FormUtils;
import Validator.InputValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import java.util.ArrayList;

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
@WebServlet(urlPatterns = "/admin/account-management")
public class AccountManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("\n\n\nACCOUNT MANAGEMENT SERVLET");
        System.out.println("URL: " + req.getRequestURL() + "");
        System.out.println("Query: " + req.getQueryString());
        System.out.println("\n");
        
        List<User> users = getAccounts();
        
        //Need: fix edit account (JSON -> API)
        if ("editAccount".equals(req.getParameter("action"))) {
            int id = Integer.parseInt(req.getParameter("id"));
            UserService userService = new UserService();
            User user = userService.getUserById(id);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(user));
            return;
        } else
        if ("filterAccounts".equals(req.getParameter("action"))) {
            System.out.println("FILTER ACCOUNTS\n\n");
            String roleFilter = req.getParameter("roleFilter");
            String statusFilter = req.getParameter("statusFilter");
            
            System.out.println("roleFilter: " + roleFilter);
            System.out.println("statusFilter: " + statusFilter);
            
            users = filterAccounts(users, roleFilter, statusFilter);
            System.out.println("END FILTER\n\n");
            
            req.setAttribute("roleFilter", roleFilter);
            req.setAttribute("statusFilter", statusFilter);
        } 
        
        req.setAttribute("accounts", users);
        req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addAccount":
                addAccount(req, resp);
                break;
            case "updateAccount":
                updateAccount(req, resp);
                break;
            case "deleteAccount":
                deleteAccount(req, resp);
                break;
            default:
                break;
        }
    }

    private void addAccount(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!InputValidator.isValidUsername(req.getParameter("username"))
                || !InputValidator.isValidEmail(req.getParameter("email"))) {
            req.setAttribute("error", "Invalid username format or username already exists.");
            req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
            return;
        }

        String role = req.getParameter("role");

        if (role.equalsIgnoreCase("admin")) {
            this.addAdmin(req, resp);
        } else if (role.equalsIgnoreCase("trainer")) {
            this.addTrainer(req, resp);
        } else {
            this.addUser(req, resp);
        }

        List<User> users = getAccounts();
        req.setAttribute("accounts", users);
        req.setAttribute("message", "Add account successful!");
        req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
    }

    private void updateAccount(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        checkInfoExist(req, resp);

        int id = Integer.parseInt(req.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);

        FormUtils.getFormValue(req, user);

        userService.update(user);

        resp.setStatus(HttpServletResponse.SC_OK);
        List<User> users = getAccounts();
        req.setAttribute("accounts", users);
        req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
    }

    private void deleteAccount(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        userService.delete(user);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private List<User> getAccounts() {
        MemberService memberService = new MemberService();
        TrainerService trainerService = new TrainerService();
        AdminService adminService = new AdminService();

        List<Member> members = memberService.getAll();
        List<Trainer> trainers = trainerService.getAll();
        List<Admin> admins = adminService.getAll();

        List<User> users = new ArrayList<>();
        users.addAll(trainers);
        users.addAll(members);
        users.addAll(admins);

        return users;
    }

    private void checkInfoExist(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        UserService userService = new UserService();
        User user = userService.getUserById(id);

        if (!username.trim().equalsIgnoreCase(user.getUsername())) {
            if (!InputValidator.isValidUsername(username)) {
                try {
                    List<User> users = getAccounts();
                    req.setAttribute("accounts", users);
                    req.setAttribute("errorMessage", "Invalid username format or username already exists.");
                    req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(AdminDashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }

        if (!email.trim().equalsIgnoreCase(user.getEmail())) {
            if (!InputValidator.isValidEmail(email)) {
                try {
                    List<User> users = getAccounts();
                    req.setAttribute("accounts", users);
                    req.setAttribute("errorMessage", "Invalid email format or email already exists.");
                    req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(AdminDashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }
    }

    private void addAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AdminService adminService = new AdminService();
        Admin admin = new Admin();
        FormUtils.getFormValue(req, admin);
        adminService.add(admin);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = new User();
        FormUtils.getFormValue(req, user);
        UserService userService = new UserService();
        userService.add(user);
    }

    private void addTrainer(HttpServletRequest req, HttpServletResponse resp) {
        Trainer trainer = new Trainer();
        FormUtils.getFormValue(req, trainer);
        TrainerService trainerService = new TrainerService();
        trainerService.add(trainer);
    }

    private List<User> filterAccounts(List<User> users, String roleFilter, String statusFilter) throws ServletException, IOException {
        if(roleFilter != null)
            users = filterAccountsByRole(users, roleFilter);
        if(statusFilter != null)
            users = filterAccountsByStatus(users, statusFilter);

        return users;
    }

    private List<User> filterAccountsByRole(List<User> users, String role) throws ServletException, IOException {
        if (role != null && !role.equals("all")) {
            users.removeIf(user -> !role.toLowerCase().equalsIgnoreCase(user.getRole()));
        }
        return users;
    }

    private List<User> filterAccountsByStatus(List<User> users, String status) throws ServletException, IOException {
        if (status != null && !status.equals("all")) {
            users.removeIf(user -> !status.toLowerCase().equalsIgnoreCase(user.getStatus()));
        }
        return users;
    }
}
