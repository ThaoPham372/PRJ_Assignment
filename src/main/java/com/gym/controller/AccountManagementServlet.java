package com.gym.controller;

import com.google.gson.Gson;
import com.gym.Validator.InputValidator;
import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;
import com.gym.service.AdminService;
import com.gym.service.MemberService;
import com.gym.service.TrainerService;
import com.gym.service.UserService;
import com.gym.util.FormUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/account-management")
public class AccountManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Need: fix edit account (JSON -> API)
        if ("editAccount".equals(req.getParameter("action"))) {
            int id = Integer.parseInt(req.getParameter("id"));
            UserService userService = new UserService();
            User user = userService.getUserById(id);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(user));
            return;
        }
        if ("filterAccounts".equals(req.getParameter("action"))) {
            List<User> users = filterAccounts(req, resp);
            req.setAttribute("accounts", users);
            req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
            return;
        } else {
            List<User> users = getAccounts();
            req.setAttribute("accounts", users);
            req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
            return;
        }

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

        userService.updateUser(user);

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
        userService.deleteUser(user.getId());
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private List<User> getAccounts() {
        MemberService memberService = new MemberService();
        TrainerService trainerService = new TrainerService();
        AdminService adminService = new AdminService();

        List<Member> members = memberService.getAll();
        List<Trainer> trainers = trainerService.getAll();
        List<Admin> admins = adminService.getAll();

        List<User> users = new ArrayList<>(members);
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
        trainerService.saveTrainer(trainer);
    }

    private List<User> filterAccounts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = req.getParameter("role");
        String status = req.getParameter("status");

        List<User> users = getAccounts();
        users = filterAccountsByRole(users, role);
        users = filterAccountsByStatus(users, status);

        req.setAttribute("role", role);
        req.setAttribute("status", status);

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
