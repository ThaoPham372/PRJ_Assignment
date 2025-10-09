package controller;

import DAO.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MemberManagementServlet handles member management operations
 * CRUD operations for gym members
 */
@WebServlet(name = "MemberManagementServlet", urlPatterns = {"/admin/members"})
public class MemberManagementServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MemberManagementServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role) && !"manager".equals(role) && !"employee".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            
            // Get pagination parameters
            int page = 0;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam) - 1; // Convert to 0-based
                    if (page < 0) page = 0;
                } catch (NumberFormatException e) {
                    page = 0;
                }
            }

            // Get filter parameters
            String search = request.getParameter("search");
            String statusFilter = request.getParameter("status");
            String packageFilter = request.getParameter("package");

            // Get members list
            List<User> members = userDAO.getAllUsers(page, pageSize);
            
            // Get statistics
            int totalMembers = userDAO.getUserCount();
            request.setAttribute("totalMembers", totalMembers);
            request.setAttribute("activeMembers", Math.max(0, totalMembers - 50));
            request.setAttribute("newThisMonth", "45");
            request.setAttribute("expiredMembers", "355");

            // Set request attributes
            request.setAttribute("members", members);
            request.setAttribute("currentPage", page + 1);
            request.setAttribute("totalPages", (int) Math.ceil((double) totalMembers / pageSize));
            request.setAttribute("search", search);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("packageFilter", packageFilter);

            // Forward to members page
            request.getRequestDispatcher("/views/admin/members.jsp").forward(request, response);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error in member management", e);
            request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in member management", e);
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role) && !"manager".equals(role) && !"employee".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/members");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();

            switch (action) {
                case "add":
                    handleAddMember(request, response, userDAO);
                    break;
                case "update":
                    handleUpdateMember(request, response, userDAO);
                    break;
                case "delete":
                    handleDeleteMember(request, response, userDAO);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/members");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error in member management POST", e);
            request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in member management POST", e);
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    private void handleAddMember(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException, SQLException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String packageType = request.getParameter("packageType");
        String address = request.getParameter("address");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validation
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            packageType == null || packageType.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc.");
            doGet(request, response);
            return;
        }

        // Check if username already exists
        if (username != null && !username.trim().isEmpty() && userDAO.isUsernameExists(username)) {
            request.setAttribute("error", "Tên đăng nhập đã được sử dụng.");
            doGet(request, response);
            return;
        }

        // Check if email already exists
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("error", "Email đã được sử dụng.");
            doGet(request, response);
            return;
        }

        // Create new user
        User newUser = new User();
        newUser.setFullName(fullName.trim());
        newUser.setEmail(email.trim());
        newUser.setPhone(phone.trim());
        newUser.setPackageType(packageType);
        newUser.setAddress(address);
        newUser.setRole("member");
        newUser.setStatus("active");
        
        if (username != null && !username.trim().isEmpty()) {
            newUser.setUsername(username.trim());
            newUser.setPassword(password != null ? password : "123456"); // Default password
        } else {
            // Generate username from email
            String generatedUsername = email.split("@")[0];
            int counter = 1;
            while (userDAO.isUsernameExists(generatedUsername)) {
                generatedUsername = email.split("@")[0] + counter;
                counter++;
            }
            newUser.setUsername(generatedUsername);
            newUser.setPassword("123456"); // Default password
        }

        boolean success = userDAO.createUser(newUser);
        
        if (success) {
            logger.info("New member added by admin: " + newUser.getUsername());
            response.sendRedirect(request.getContextPath() + "/admin/members?success=added");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi thêm thành viên.");
            doGet(request, response);
        }
    }

    private void handleUpdateMember(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException, SQLException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/members");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            User user = userDAO.getUserById(id);
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/admin/members");
                return;
            }

            // Update user information
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String packageType = request.getParameter("packageType");
            String address = request.getParameter("address");
            String status = request.getParameter("status");

            if (fullName != null) user.setFullName(fullName.trim());
            if (email != null) user.setEmail(email.trim());
            if (phone != null) user.setPhone(phone.trim());
            if (packageType != null) user.setPackageType(packageType);
            if (address != null) user.setAddress(address);
            if (status != null) user.setStatus(status);

            boolean success = userDAO.updateUser(user);
            
            if (success) {
                logger.info("Member updated by admin: " + user.getUsername());
                response.sendRedirect(request.getContextPath() + "/admin/members?success=updated");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin thành viên.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/members");
        }
    }

    private void handleDeleteMember(HttpServletRequest request, HttpServletResponse response, UserDAO userDAO)
            throws ServletException, IOException, SQLException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/members");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean success = userDAO.deleteUser(id);
            
            if (success) {
                logger.info("Member deleted by admin: ID " + id);
                response.sendRedirect(request.getContextPath() + "/admin/members?success=deleted");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi xóa thành viên.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/members");
        }
    }
}

