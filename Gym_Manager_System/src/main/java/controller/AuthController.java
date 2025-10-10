package controller;

import DAO.IUserDAO;
import DAO.UserDAO;
import DAO.IMemberDAO;
import DAO.MemberDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import model.User;
import model.Member;
import util.PasswordUtil;
import util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;
import java.sql.SQLException;
import DAO.DBConnection;

/**
 * AuthController - Xử lý authentication (login, register, logout)
 */
@WebServlet(name = "AuthController", urlPatterns = {"/auth/*"})
public class AuthController extends HttpServlet {
    
    private IUserDAO userDAO;
    private IMemberDAO memberDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.userDAO = new UserDAO();
        this.memberDAO = new MemberDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/views/login.jsp");
            return;
        }
        
        switch (pathInfo) {
            case "/login":
                showLoginPage(request, response);
                break;
            case "/register":
                showRegisterPage(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (pathInfo) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    /**
     * Hiển thị trang login
     */
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if already logged in
        if (SessionUtil.isLoggedIn(request)) {
            String role = SessionUtil.getRole(request);
            redirectToDashboard(response, role, request.getContextPath());
            return;
        }
        
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị trang register
     */
    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if already logged in
        if (SessionUtil.isLoggedIn(request)) {
            String role = SessionUtil.getRole(request);
            redirectToDashboard(response, role, request.getContextPath());
            return;
        }
        
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }
    
    /**
     * Xử lý login
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        // Validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Get user by username
            User user = userDAO.getUserByUsername(username.trim());
            
            if (user == null) {
                request.setAttribute("error", "Tên đăng nhập không tồn tại");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }
            
            // Check if account is active
            if (!"active".equals(user.getStatus())) {
                request.setAttribute("error", "Tài khoản đã bị khóa hoặc chưa được kích hoạt");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }
            
            // Verify password
            if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                request.setAttribute("error", "Mật khẩu không chính xác");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }
            
            // Login successful - Create session
            if ("member".equals(user.getRole())) {
                // Get member info
                Member member = memberDAO.getMemberByUserId(user.getUserId());
                if (member != null) {
                    SessionUtil.createUserSession(request, user, member.getMemberId(), member.getMemberCode());
                } else {
                    SessionUtil.createUserSession(request, user);
                }
            } else {
                SessionUtil.createUserSession(request, user);
            }
            
            // Set remember me cookie if checked
            if ("on".equals(remember)) {
                // TODO: Implement remember me functionality with secure cookie
            }
            
            // Redirect to appropriate dashboard
            redirectToDashboard(response, user.getRole(), request.getContextPath());
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Xử lý register
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String dobStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (username == null || username.trim().isEmpty()) {
            errors.append("Username không được để trống. ");
        } else if (username.length() < 4) {
            errors.append("Username phải có ít nhất 4 ký tự. ");
        }
        
        if (password == null || password.isEmpty()) {
            errors.append("Password không được để trống. ");
        } else if (!PasswordUtil.isPasswordStrong(password)) {
            errors.append(PasswordUtil.getPasswordStrengthMessage(password)).append(". ");
        }
        
        if (!password.equals(confirmPassword)) {
            errors.append("Password xác nhận không khớp. ");
        }
        
        if (email == null || email.trim().isEmpty()) {
            errors.append("Email không được để trống. ");
        } else if (!isValidEmail(email)) {
            errors.append("Email không hợp lệ. ");
        }
        
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.append("Họ tên không được để trống. ");
        }
        
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phone", phone);
            request.setAttribute("dateOfBirth", dobStr);
            request.setAttribute("gender", gender);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Try using stored procedure if available (3NF schema)
            boolean spRegistered = false;
            try (Connection conn = DBConnection.getConnection()) {
                if (conn != null) {
                    try (CallableStatement cs = conn.prepareCall("{call sp_register_member(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                        cs.setString(1, username.trim());
                        cs.setString(2, PasswordUtil.hashPassword(password));
                        cs.setString(3, email.trim());
                        cs.setString(4, fullName.trim());
                        cs.setString(5, phone != null ? phone.trim() : null);
                        if (dobStr != null && !dobStr.trim().isEmpty()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date dob = sdf.parse(dobStr);
                            cs.setDate(6, new java.sql.Date(dob.getTime()));
                        } else {
                            cs.setDate(6, null);
                        }
                        cs.setString(7, gender);
                        cs.registerOutParameter(8, Types.INTEGER); // @user_id OUTPUT
                        cs.registerOutParameter(9, Types.INTEGER); // @member_id OUTPUT
                        cs.registerOutParameter(10, Types.VARCHAR); // @member_code OUTPUT
                        cs.registerOutParameter(11, Types.NVARCHAR); // @error_message OUTPUT
                        cs.execute();
                        String spError = cs.getString(11);
                        if (spError == null || spError.trim().isEmpty()) {
                            spRegistered = true;
                        } else {
                            System.err.println("[AuthController] sp_register_member error: " + spError);
                        }
                    } catch (SQLException | ParseException spEx) {
                        // Stored procedure not available or failed; fallback to manual flow
                    }
                }
            }

            if (spRegistered) {
                request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }

            // Check username exists
            if (userDAO.isUsernameExists(username.trim())) {
                request.setAttribute("error", "Username đã tồn tại");
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Check email exists
            if (userDAO.isEmailExists(email.trim())) {
                request.setAttribute("error", "Email đã được sử dụng");
                request.setAttribute("username", username);
                request.setAttribute("fullName", fullName);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Create User object (manual fallback)
            User user = new User();
            user.setUsername(username.trim());
            user.setPassword(PasswordUtil.hashPassword(password)); // Hash password
            user.setEmail(email.trim());
            user.setFullName(fullName.trim());
            user.setPhone(phone != null ? phone.trim() : null);
            user.setGender(gender);
            user.setRole("member"); // Default role
            user.setStatus("active");
            
            // Parse date of birth
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dob = sdf.parse(dobStr);
                    user.setDateOfBirth(dob);
                } catch (ParseException e) {
                    // Ignore invalid date
                }
            }
            
            // Create user in database (manual fallback)
            boolean userCreated = userDAO.createUser(user);
            if (userCreated && user.getUserId() > 0 && (user.getStatus() == null || !"active".equals(user.getStatus()))) {
                userDAO.setUserStatus(user.getUserId(), "active");
            }
            
            if (!userCreated) {
                request.setAttribute("error", "Không thể tạo tài khoản. Vui lòng thử lại.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }
            
            // Ensure we have the generated user_id (fallback lookup if driver didn't return keys)
            if (user.getUserId() <= 0) {
                User createdUser = userDAO.getUserByUsername(user.getUsername());
                if (createdUser != null) {
                    user.setUserId(createdUser.getUserId());
                }
            }
            
            // Create Member record
            Member member = new Member();
            member.setUserId(user.getUserId());
            
            // Generate member code
            int lastNumber = memberDAO.getLastMemberNumber();
            String memberCode = "GYM" + java.time.Year.now().getValue() + 
                              String.format("%05d", lastNumber + 1);
            member.setMemberCode(memberCode);
            member.setRegistrationDate(new Date());
            member.setStatus("active");
            
            boolean memberCreated = memberDAO.createMember(member);
            
            if (!memberCreated) {
                // Double-check if member was actually created
                Member existing = memberDAO.getMemberByUserId(user.getUserId());
                if (existing == null) {
                    // As a fallback, continue registration using user account only
                    request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
                    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                    return;
                } else {
                    // Use existing member record
                    member = existing;
                }
            }
            
            // Registration successful
            request.removeAttribute("error");
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }
    
    /**
     * Xử lý logout
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        SessionUtil.invalidateSession(request);
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
    
    /**
     * Redirect to appropriate dashboard based on role
     */
    private void redirectToDashboard(HttpServletResponse response, String role, String contextPath)
            throws IOException {
        
        switch (role) {
            case "admin":
            case "manager":
                response.sendRedirect(contextPath + "/views/admin/dashboard.jsp");
                break;
            case "coach":
                response.sendRedirect(contextPath + "/views/coach/dashboard.jsp");
                break;
            case "member":
                response.sendRedirect(contextPath + "/views/member/dashboard.jsp");
                break;
            default:
                response.sendRedirect(contextPath + "/home.jsp");
                break;
        }
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}

