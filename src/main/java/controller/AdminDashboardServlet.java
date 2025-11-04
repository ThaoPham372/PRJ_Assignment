package controller;

import com.google.gson.Gson;

import Validator.InputValidator;
import dao.UserDAO;
import model.Admin;
import model.Trainer;
import model.User;
import service.AdminService;
import service.PasswordService;
import service.TrainerService;
import service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Member;
import model.Membership;
import service.MemberService;
import service.MembershipService;

/**
 * AdminDashboardServlet - Servlet để điều hướng các trang admin Đây là servlet
 * giả để test hiển thị dashboard admin
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
        "/admin/home",
        "/admin/dashboard",
        "/admin/profile",
        "/admin/account-management",
        "/admin/member-management",
        "/admin/service-schedule",
        "/admin/trainer-management",
        "/admin/order-management",
        "/admin/payment-finance",
        "/admin/reports"
})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n\nAdminDashboardServlet DO GET CALLED");
        System.out.println("> Request URL: " + request.getRequestURL().toString());
        // Get the request URI to determine which page to display
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Check if user is logged in and has ADMIN role
        HttpSession session = request.getSession(false);
        // if (session == null || session.getAttribute("isLoggedIn") == null) {
        // response.sendRedirect(request.getContextPath() + "/views/login.jsp");
        // return;
        // }

        // Check if user has ADMIN role
        // @SuppressWarnings("unchecked")
        // java.util.List<String> userRoles = (java.util.List<String>)
        // session.getAttribute("userRoles");
        // if (userRoles == null || !userRoles.contains("ADMIN")) {
        // response.sendRedirect(request.getContextPath() + "/home");
        // return;
        // }
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "edit" -> {
                System.out.println("\n\nEDIT CALL");
                int id = Integer.parseInt(request.getParameter("id"));
                UserService userService = new UserService();
                User user = userService.getUserById(id);
                String json = new Gson().toJson(user);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(json);
                return;
            }
            case "deleteMembership" -> {
                deleteMembership(request, response);
                return;
            }
            case "filter" -> {
                System.out.println("url query: " + request.getQueryString());
                System.out.println("\n\n\n\n\n\nFILTER CALL");
                String role = request.getParameter("role");
                String status = request.getParameter("status");

                UserService userService = new UserService();
                List<User> users = userService.getAll();

                if (role != null && !role.equals("all")) {
                    users.removeIf(user -> !user.getDtype().equalsIgnoreCase(role));
                }

                if (status != null && !status.equals("all")) {
                    users.removeIf(user -> !user.getStatus().equalsIgnoreCase(status));
                }

                request.setAttribute("accounts", users);
                request.setAttribute("role", role);
                request.setAttribute("status", status);
                request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
                return;
            }
        }

        String jspPath;

        switch (path) {
            case "/admin/home" ->
                jspPath = "/views/admin/admin_home.jsp";
            case "/admin/dashboard" ->
                jspPath = "/views/admin/dashboard.jsp";
            case "/admin/profile" ->
                jspPath = "/views/admin/profile.jsp";
            case "/admin/account-management" -> {
                getAccounts(request);
                jspPath = "/views/admin/account_management.jsp";
            }
            case "/admin/member-management" -> {
                getMembership(request, response);
                jspPath = "/views/admin/member_management.jsp";
            }
            case "/admin/service-schedule" ->
                jspPath = "/views/admin/service_schedule.jsp";
            case "/admin/trainer-management" ->
                jspPath = "/views/admin/trainer_management.jsp";
            case "/admin/order-management" ->
                jspPath = "/views/admin/order_management.jsp";
            case "/admin/payment-finance" ->
                jspPath = "/views/admin/payment_finance.jsp";
            case "/admin/reports" ->
                jspPath = "/views/admin/reports.jsp";
            default ->
                jspPath = "/views/admin/admin_home.jsp";
        }

        // Set page title attribute
        request.setAttribute("pageTitle", "Admin Dashboard - GymFit");

        // Forward to the appropriate JSP
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests (for forms, updates, etc.)
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "addMembership" ->
                addMembership(request, response);
            case "addAccount" ->
                addAccount(request, response);

            case "updateAdmin" ->
                updateAdmin(request, response);
            case "updateAccount" ->
                updateAccount(request, response);
            case "updateAdminPassword" ->
                updateAdminPassword(request, response);

            case "deleteAccount" ->
                deleteAccount(request, response);
            default ->
                System.out.println("Action is null");
        }
    }

    public void deleteMembership(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("\n\n## DELETE MEMBERSHIP");
        int membershipId = Integer.parseInt(request.getParameter("membershipId"));
        MembershipService membershipService = new MembershipService();
        System.out.println("> Membership: " + membershipService.getMembershipById(membershipId));
        membershipService.deleteById(membershipId);
        return;
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet for GymFit Management System";
    }

    private void addAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        getAccounts(request);

        if (!InputValidator.isValidUsername(request.getParameter("username")) ||
                !InputValidator.isValidEmail(request.getParameter("email"))) {
            request.setAttribute("error", "Invalid username format or username already exists.");
            request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
            return;
        }

        String role = request.getParameter("role");

        if (role.equalsIgnoreCase("admin")) {
            this.addAdmin(request, response);
        } else if (role.equalsIgnoreCase("trainer")) {
            this.addTrainer(request, response);
        } else {
            this.addUser(request, response);
        }

        request.setAttribute("message", "Add account successful!");
        request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
    }

    private void addAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminService adminService = new AdminService();
        Admin admin = new Admin();
        this.getFormValue(request, admin);
        adminService.add(admin);
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        this.getFormValue(request, user);
        UserService userService = new UserService();
        userService.add(user);
    }

    private void addTrainer(HttpServletRequest request, HttpServletResponse response) {
        Trainer trainer = new Trainer();
        this.getFormValue(request, trainer);
        TrainerService trainerService = new TrainerService();
        trainerService.add(trainer);
    }

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminService adminService = new AdminService();
        int id = Integer.parseInt(request.getParameter("id"));
        Admin admin = adminService.getAdminById(id);

        System.out.println("\nID: " + id + " - Admin found: " + admin);

        this.getFormValue(request, admin);
        adminService.update(admin);
        request.getSession().setAttribute("user", admin);
        request.setAttribute("message", "> Update successful!");
    }

    private void updateAdminPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PasswordService passwordService = new PasswordService();
        int id = Integer.parseInt(request.getParameter("id"));
        AdminService adminService = new AdminService();
        Admin admin = adminService.getAdminById(id);

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println("currentPassword: " + currentPassword);
        System.out.println("newPassword: " + newPassword);
        System.out.println("confirmPassword: " + confirmPassword);

        String userPassword = admin.getPassword();
        String hashNewP = passwordService.hashPassword(newPassword);

        boolean isCurrentPasswordCorrect = passwordService.verifyPassword(currentPassword, userPassword);

        if (!newPassword.equals(confirmPassword) || !isCurrentPasswordCorrect) {
            request.setAttribute("message",
                    "new password not equal confirm password or current password is not correctly!");
            request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
        } else {
            admin.setPassword(hashNewP);
            adminService.update(admin);
            System.out.println("Update password successfully!");
        }

        request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
    }

    private void getAccounts(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("message");
        if (message != null) {
            request.getSession().removeAttribute("message"); // Xóa để hiển thị 1 lần duy nhất
        }
        UserService userService = new UserService();
        List<User> users = userService.getAll();
        request.setAttribute("accounts", users);
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        userService.delete(user);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getFormValue(HttpServletRequest request, User obj) {
        PasswordService passwordService = new PasswordService();

        if (request.getParameter("name") != null) {
            obj.setName(request.getParameter("name").trim());
        }

        if (request.getParameter("email") != null) {
            obj.setEmail(request.getParameter("email").trim());
        }

        if (request.getParameter("phone") != null) {
            obj.setPhone(request.getParameter("phone").trim());
        }

        if (request.getParameter("birthday") != null && !request.getParameter("birthday").trim().isEmpty()) {
            try {
                Date dob = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(request.getParameter("birthday").trim());
                obj.setDob(dob);
            } catch (Exception e) {
                System.out.println("Error: Admin DB Servlet -> setAdmin()");
            }
        }

        if (request.getParameter("address") != null) {
            obj.setAddress(request.getParameter("address").trim());
        }

        if (request.getParameter("role") != null) {
            obj.setRole(request.getParameter("role").trim());
        }

        if (request.getParameter("username") != null) {
            obj.setUsername(request.getParameter("username").trim());
        }

        String passwordStr = request.getParameter("password");
        if (passwordStr != null && !passwordStr.isEmpty() && passwordService.isValidPassword(passwordStr)) {
            String passwordHash = passwordService.hashPassword(request.getParameter("password"));
            obj.setPassword(passwordHash);
        }

        if (request.getParameter("status") != null) {
            obj.setStatus(request.getParameter("status").trim());
        }

        if (request.getParameter("emailVerified") != null) {
            // admin.setEmailVerified(request.getParameter("emailVerified").equals("true"));
        }

        if (request.getParameter("failedLoginAttempts") != null) {
            try {
                obj.setFailedLoginAttempts(Integer.valueOf(request.getParameter("failedLoginAttempts")));
            } catch (NumberFormatException e) {
                obj.setFailedLoginAttempts(0);
            }
        }

        if (request.getParameter("createdDate") != null) {
            obj.setCreatedDate(parseDateTime(request.getParameter("createdDate")));
        }
        if (request.getParameter("lastLogin") != null) {
            obj.setLastLogin(parseDateTime(request.getParameter("lastLogin")));
        }
        if (request.getParameter("lockedUntil") != null) {
            obj.setLockedUntil(parseDateTime(request.getParameter("lockedUntil")));
        }
    }

    Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateTimeStr);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkInfoExist(request, response);

        int id = Integer.parseInt(request.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);

        this.getFormValue(request, user);

        userService.update(user);

        response.setStatus(HttpServletResponse.SC_OK);
        getAccounts(request);
        request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
    }

    private void checkInfoExist(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        UserService userService = new UserService();
        User user = userService.getUserById(id);

        if (!username.trim().equalsIgnoreCase(user.getUsername())) {
            if (!InputValidator.isValidUsername(username)) {
                try {
                    getAccounts(request);
                    request.setAttribute("errorMessage", "Invalid username format or username already exists.");
                    request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(AdminDashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }

        if (!email.trim().equalsIgnoreCase(user.getEmail())) {
            if (!InputValidator.isValidEmail(email)) {
                try {
                    getAccounts(request);
                    request.setAttribute("errorMessage", "Invalid email format or email already exists.");
                    request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(AdminDashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }
    }

    private void addMembership(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = false;
        try {
            String username = request.getParameter("username");
            String phone = request.getParameter("phone");
            String packageIdStr = request.getParameter("package_id");
            String startDateStr = request.getParameter("startDate");

            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không được để trống.");
            }
            if (packageIdStr == null || packageIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Gói tập không được để trống.");
            }
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone bắt đầu không được để trống.");
            }

            int packageId;
            try {
                packageId = Integer.parseInt(packageIdStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Gói tập không hợp lệ (phải là số nguyên).", e);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // không cho ngày sai kiểu 2025-13-50

            Date startDate;
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Định dạng ngày không hợp lệ. Phải là yyyy-MM-dd.", e);
            }

            createMembership(username, phone, packageId, startDate);
            success = true;
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Lỗi dữ liệu đầu vào: " + e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            System.err.println("🔥 Lỗi không xác định khi thêm membership:");
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi thêm membership.");
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/member-management");
        }

    }

    private void createMembership(String username, String phone, int packageId, Date startDate) {
        MemberService memberService = new MemberService();
        Member member = new Member();
        member.setUsername(username);
        member.setName(username);
        member.setEmail(username + "@gmail.com");
        member.setPhone(phone);
        int memberId = memberService.add(member);

        if (memberId > -1) {
            Membership membership = new Membership();
            membership.setPackageId(packageId);
            membership.setUserId(member.getUserId());
            membership.setStartDate(startDate);

            Date now = new Date();
            membership.setEndDate(new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000)); // + 30 ngay

            MembershipService membershipService = new MembershipService();
            membershipService.add(membership);
        }
    }

    private void getMembership(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n\nGet Membership CALLED");
        MembershipService membershipService = new MembershipService();
        List<Membership> memberships = membershipService.getAll();
        getMemberInfo(memberships);
        System.out.println("> Total memberships: " + memberships.size());
        request.setAttribute("memberships", memberships);
    }

    private void getMemberInfo(List<Membership> memberships) {
        MemberService memberService = new MemberService();
        for (Membership m : memberships) {
            Member mem = memberService.getMemberById(m.getUserId());
            if (mem != null) {
                m.setName(mem.getName());
                m.setEmail(mem.getEmail());
                m.setPhone(mem.getPhone());
                m.setPackageType("Standard");
            }
        }
    }

}
