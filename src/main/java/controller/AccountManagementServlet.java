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
    Lỗi:
     - Sau khi ấn edit -> ấn create account thì form tự điền
     - dữ liệu trả về của edit account
    cần thêm:
     - update account role => (xóa User hiện tại, new User mới) 
 */
@WebServlet(urlPatterns = "/admin/account-management")
public class AccountManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("\n\n\nACCOUNT MANAGEMENT SERVLET");
        System.out.println("URL: " + req.getRequestURL() + "");
        System.out.println("Query: " + req.getQueryString());
        System.out.println("\n");

        List<User> users = getAccounts();

        // Need: fix edit account (JSON -> API)
        if ("editAccount".equals(req.getParameter("action"))) {
            int id = Integer.parseInt(req.getParameter("id"));
            UserService userService = new UserService();
            User user = userService.getUserById(id);
            res.setContentType("application/json");
            res.getWriter().write(new Gson().toJson(user));
            return;
        } else if ("filterAccounts".equals(req.getParameter("action"))) {
            String roleFilter = req.getParameter("roleFilter");
            String statusFilter = req.getParameter("statusFilter");

            users = filterAccounts(users, roleFilter, statusFilter);

            req.setAttribute("roleFilter", roleFilter);
            req.setAttribute("statusFilter", statusFilter);
        }

        req.setAttribute("accounts", users);
        req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addAccount":
                addAccount(req, res);
                break;
            case "updateAccount":
                updateAccount(req, res);
                break;
            case "deleteAccount":
                deleteAccount(req, res);
                break;
            default:
                break;
        }

        List<User> users = getAccounts();
        req.setAttribute("accounts", users);
        req.getRequestDispatcher("/views/admin/account_management.jsp").forward(req, res);
    }

    private void addAccount(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        boolean isValidInput = isValidUsernameAndEmail(username, email); // Có thể đưa vào class InputValidator
        if (!isValidInput)
            return;

        String role = req.getParameter("role").toLowerCase();
        if (role == null)
            role = "member";

        createAccountByRole(role, req);

        req.setAttribute("message", "Add account successful!");
    }

    private void updateAccount(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        boolean isValidChangeUsernameAndEmail = checkInfoExist(req, res);
        if (!isValidChangeUsernameAndEmail)
            return;

        int id = Integer.parseInt(req.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);

        FormUtils.getFormValue(req, user);

        userService.update(user);

        res.setStatus(HttpServletResponse.SC_OK);
    }

    private void deleteAccount(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        userService.delete(user);
        res.setStatus(HttpServletResponse.SC_OK);
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

    private boolean checkInfoExist(HttpServletRequest req, HttpServletResponse res) {
        int id = Integer.parseInt(req.getParameter("id"));
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        UserService userService = new UserService();
        User user = userService.getUserById(id);

        if (!username.trim().equalsIgnoreCase(user.getUsername()))
            if (!InputValidator.isValidUsername(username)) {
                req.setAttribute("errorMessage", "Invalid username format or username already exists.");
                return false;
            }

        if (!email.trim().equalsIgnoreCase(user.getEmail()))
            if (!InputValidator.isValidEmail(email)) {
                req.setAttribute("errorMessage", "Invalid email format or email already exists.");
                return false;
            }

        return true;
    }

    private void addAdmin(HttpServletRequest req)
            throws ServletException, IOException {
        AdminService adminService = new AdminService();
        Admin admin = new Admin();
        FormUtils.getFormValue(req, admin);
        adminService.add(admin);
    }

    private void addMember(HttpServletRequest req)
            throws ServletException, IOException {
        Member member = new Member();
        FormUtils.getFormValue(req, member);
        MemberService memberService = new MemberService();
        memberService.add(member);
    }

    private void addTrainer(HttpServletRequest req) {
        Trainer trainer = new Trainer();
        FormUtils.getFormValue(req, trainer);
        TrainerService trainerService = new TrainerService();
        trainerService.add(trainer);
    }

    private List<User> filterAccounts(List<User> users, String roleFilter, String statusFilter)
            throws ServletException, IOException {
        if (roleFilter != null)
            users = filterAccountsByRole(users, roleFilter);
        if (statusFilter != null)
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

    private boolean isValidUsernameAndEmail(String username, String email) {
        return InputValidator.isValidUsername(username) && InputValidator.isValidEmail(email);
    }

    private void createAccountByRole(String role, HttpServletRequest req) throws ServletException, IOException {
        if (role.equalsIgnoreCase("admin")) {
            addAdmin(req);
        } else if (role.equalsIgnoreCase("trainer")) {
            addTrainer(req);
        } else {
            addMember(req);
        }
    }
}
