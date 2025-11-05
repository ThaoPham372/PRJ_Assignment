
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

import model.Admin;
import model.Trainer;
import model.User;
import service.AdminService;
import service.TrainerService;
import service.UserService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/account-management")
public class AccountManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("editAccount".equals(req.getParameter("action"))) {
            int id = Integer.parseInt(req.getParameter("id"));
            UserService userService = new UserService();
            User user = userService.getUserById(id);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(user));
            return;
        }
        if ("filterAccounts".equals(req.getParameter("action"))) {
            filterAccounts(req, resp);
            return;
        }
        getAccounts(req);
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
        getAccounts(req);

        if (!InputValidator.isValidUsername(req.getParameter("username")) ||
                !InputValidator.isValidEmail(req.getParameter("email"))) {
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
        getAccounts(req);
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

    private void getAccounts(HttpServletRequest req) {
        String message = (String) req.getSession().getAttribute("message");
        if (message != null) {
            req.getSession().removeAttribute("message"); // Xóa để hiển thị 1 lần duy nhất
        }
        UserService userService = new UserService();
        List<User> users = userService.getAll();
        req.setAttribute("accounts", users);
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
                    getAccounts(req);
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
                    getAccounts(req);
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

    private void filterAccounts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = req.getParameter("role");
        String status = req.getParameter("status");

        UserService userService = new UserService();
        List<User> users = userService.getAll();

        if (role != null && !role.equals("all")) {
            users.removeIf(user -> !role.equalsIgnoreCase(user.getDtype()));
        }

        if (status != null && !status.equals("all")) {
            users.removeIf(user -> !status.equalsIgnoreCase(user.getStatus()));
        }

        req.setAttribute("accounts", users);
        req.setAttribute("role", role);
        req.setAttribute("status", status);
        req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, resp);
    }
}
