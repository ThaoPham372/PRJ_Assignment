package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * EquipmentManagementServlet handles equipment management operations
 * CRUD operations for gym equipment
 */
@WebServlet(name = "EquipmentManagementServlet", urlPatterns = {"/admin/equipment"})
public class EquipmentManagementServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EquipmentManagementServlet.class.getName());

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
            // Mock data for equipment (in real app, this would come from database)
            List<Equipment> equipment = getMockEquipment();
            
            // Set statistics
            request.setAttribute("totalEquipment", equipment.size());
            request.setAttribute("workingEquipment", equipment.stream().mapToInt(e -> "working".equals(e.getStatus()) ? 1 : 0).sum());
            request.setAttribute("maintenanceEquipment", equipment.stream().mapToInt(e -> "maintenance".equals(e.getStatus()) ? 1 : 0).sum());
            request.setAttribute("brokenEquipment", equipment.stream().mapToInt(e -> "broken".equals(e.getStatus()) ? 1 : 0).sum());

            // Set request attributes
            request.setAttribute("equipment", equipment);

            // Forward to equipment page
            request.getRequestDispatcher("/views/admin/equipment.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error in equipment management: " + e.getMessage());
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
        if (!"admin".equals(role) && !"manager".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddEquipment(request, response);
                    break;
                case "update":
                    handleUpdateEquipment(request, response);
                    break;
                case "delete":
                    handleDeleteEquipment(request, response);
                    break;
                case "maintenance":
                    handleMaintenanceEquipment(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/equipment");
            }

        } catch (Exception e) {
            logger.severe("Error in equipment management POST: " + e.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    private void handleAddEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String manufacturer = request.getParameter("manufacturer");
        String model = request.getParameter("model");
        String location = request.getParameter("location");
        String purchaseDateStr = request.getParameter("purchaseDate");
        String priceStr = request.getParameter("price");
        String warrantyStr = request.getParameter("warranty");
        String description = request.getParameter("description");

        // Validation
        if (name == null || name.trim().isEmpty() ||
            category == null || category.trim().isEmpty() ||
            location == null || location.trim().isEmpty() ||
            priceStr == null || priceStr.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc.");
            doGet(request, response);
            return;
        }

        // Parse numeric values
        double price = 0.0;
        int warranty = 0;
        
        try {
            price = Double.parseDouble(priceStr);
            if (warrantyStr != null && !warrantyStr.trim().isEmpty()) {
                warranty = Integer.parseInt(warrantyStr);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá mua và thời gian bảo hành phải là số hợp lệ.");
            doGet(request, response);
            return;
        }

        // In real app, save to database
        logger.info("New equipment added by admin: " + name);
        
        // Redirect with success message
        response.sendRedirect(request.getContextPath() + "/admin/equipment?success=added");
    }

    private void handleUpdateEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // In real app, update equipment in database
            logger.info("Equipment updated by admin: ID " + id);
            
            response.sendRedirect(request.getContextPath() + "/admin/equipment?success=updated");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
        }
    }

    private void handleDeleteEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // In real app, delete equipment from database
            logger.info("Equipment deleted by admin: ID " + id);
            
            response.sendRedirect(request.getContextPath() + "/admin/equipment?success=deleted");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
        }
    }

    private void handleMaintenanceEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // In real app, update equipment status to maintenance
            logger.info("Equipment set to maintenance by admin: ID " + id);
            
            response.sendRedirect(request.getContextPath() + "/admin/equipment?success=maintenance");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/equipment");
        }
    }

    private List<Equipment> getMockEquipment() {
        List<Equipment> equipment = new ArrayList<>();
        
        // Mock equipment data
        String[] names = {
            "Máy Chạy Bộ TechnoGym", "Xe Đạp Tập Life Fitness", "Máy Tạ Đa Năng Hammer",
            "Ghế Tập Cơ Bụng", "Bộ Tạ Đơn (5-50kg)", "Máy Rowing Concept2",
            "Máy Elliptical Precor", "Cable Machine Multi-Function", "Dumbbell Set",
            "Bench Press Station", "Squat Rack Power", "Leg Press Machine",
            "Shoulder Press Machine", "Lat Pulldown Machine", "Cardio Stepper"
        };
        
        String[] categories = {
            "Cardio", "Strength", "Functional", "Phụ kiện", "Strength",
            "Cardio", "Cardio", "Strength", "Phụ kiện", "Strength",
            "Strength", "Strength", "Strength", "Strength", "Cardio"
        };
        
        String[] locations = {
            "Khu Cardio", "Khu Tạ", "Khu Functional", "Phòng Group Class", "Khu Tạ",
            "Khu Cardio", "Khu Cardio", "Khu Tạ", "Khu Tạ", "Khu Tạ",
            "Khu Tạ", "Khu Tạ", "Khu Tạ", "Khu Tạ", "Khu Cardio"
        };

        for (int i = 0; i < names.length; i++) {
            Equipment eq = new Equipment();
            eq.setId(i + 1);
            eq.setName(names[i]);
            eq.setCategory(categories[i]);
            eq.setLocation(locations[i]);
            eq.setManufacturer("Manufacturer " + (i + 1));
            eq.setModel("Model " + (i + 1));
            eq.setValue(25000000 + (i * 5000000));
            eq.setPurchaseDate("202" + (i % 4) + "-" + String.format("%02d", (i % 12) + 1) + "-" + String.format("%02d", (i % 28) + 1));
            eq.setMaintenanceDate("2024-" + String.format("%02d", ((i % 6) + 7) % 12 + 1) + "-" + String.format("%02d", (i % 28) + 1));
            
            // Set status based on ID
            if (i == 2 || i == 7 || i == 12) {
                eq.setStatus("maintenance");
            } else if (i == 6 || i == 14) {
                eq.setStatus("broken");
            } else {
                eq.setStatus("working");
            }
            
            equipment.add(eq);
        }
        
        return equipment;
    }

    // Inner class for Equipment (in real app, this would be a separate model class)
    public static class Equipment {
        private int id;
        private String name;
        private String category;
        private String manufacturer;
        private String model;
        private String location;
        private String status;
        private String purchaseDate;
        private String maintenanceDate;
        private double value;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
        
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
        
        public String getMaintenanceDate() { return maintenanceDate; }
        public void setMaintenanceDate(String maintenanceDate) { this.maintenanceDate = maintenanceDate; }
        
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
    }
}

