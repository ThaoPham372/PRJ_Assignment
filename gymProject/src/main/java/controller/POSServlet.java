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
 * POSServlet handles Point of Sale operations
 * Manages transactions, payments, and sales
 */
@WebServlet(name = "POSServlet", urlPatterns = {"/admin/pos"})
public class POSServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(POSServlet.class.getName());

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
            // Mock data for recent transactions
            List<Transaction> recentTransactions = getMockTransactions();
            
            // Set request attributes
            request.setAttribute("recentTransactions", recentTransactions);

            // Forward to POS page
            request.getRequestDispatcher("/views/admin/point-of-sale.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error in POS: " + e.getMessage());
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
            response.sendRedirect(request.getContextPath() + "/admin/pos");
            return;
        }

        try {
            switch (action) {
                case "process_payment":
                    handleProcessPayment(request, response);
                    break;
                case "search_customer":
                    handleSearchCustomer(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/pos");
            }

        } catch (Exception e) {
            logger.severe("Error in POS POST: " + e.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    private void handleProcessPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String customerInfo = request.getParameter("customerInfo");
        String items = request.getParameter("items");
        String totalAmount = request.getParameter("totalAmount");
        String paymentMethod = request.getParameter("paymentMethod");
        String receivedAmount = request.getParameter("receivedAmount");

        // Validation
        if (totalAmount == null || totalAmount.trim().isEmpty() ||
            paymentMethod == null || paymentMethod.trim().isEmpty()) {
            
            request.setAttribute("error", "Thông tin thanh toán không đầy đủ.");
            doGet(request, response);
            return;
        }

        try {
            double total = Double.parseDouble(totalAmount);
            double received = receivedAmount != null ? Double.parseDouble(receivedAmount) : total;
            
            // Validate payment
            if ("cash".equals(paymentMethod) && received < total) {
                request.setAttribute("error", "Số tiền nhận không đủ.");
                doGet(request, response);
                return;
            }

            // In real app, save transaction to database
            logger.info("Payment processed: " + totalAmount + " via " + paymentMethod);
            
            // Redirect with success message
            response.sendRedirect(request.getContextPath() + "/admin/pos?success=payment_processed");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Số tiền không hợp lệ.");
            doGet(request, response);
        }
    }

    private void handleSearchCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchTerm = request.getParameter("searchTerm");
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.getWriter().write("{\"error\": \"Vui lòng nhập từ khóa tìm kiếm\"}");
            return;
        }

        // In real app, search customer in database
        // For now, return mock data
        String jsonResponse = "{\"customers\": [" +
            "{\"id\": 1, \"name\": \"Nguyễn Văn A\", \"phone\": \"0901234567\", \"membership\": \"Premium\"}," +
            "{\"id\": 2, \"name\": \"Trần Thị B\", \"phone\": \"0912345678\", \"membership\": \"Basic\"}" +
            "]}";
        
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    private List<Transaction> getMockTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        
        // Mock transaction data
        String[] customers = {
            "Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Khách vãng lai"
        };
        
        String[] products = {
            "Premium Membership", "5 Buổi PT", "Protein Powder", "VIP Membership", "Energy Drink x2"
        };
        
        String[] amounts = {
            "500,000₫", "950,000₫", "1,000,000₫", "800,000₫", "50,000₫"
        };
        
        String[] paymentMethods = {
            "Tiền mặt", "Thẻ", "Chuyển khoản", "Tiền mặt", "Tiền mặt"
        };

        for (int i = 0; i < customers.length; i++) {
            Transaction transaction = new Transaction();
            transaction.setId(1001 + i);
            transaction.setTime("14:" + String.format("%02d", 30 + (i * 10)));
            transaction.setCustomer(customers[i]);
            transaction.setProduct(products[i]);
            transaction.setAmount(amounts[i]);
            transaction.setPaymentMethod(paymentMethods[i]);
            transaction.setStatus("Hoàn thành");
            
            transactions.add(transaction);
        }
        
        return transactions;
    }

    // Inner class for Transaction (in real app, this would be a separate model class)
    public static class Transaction {
        private int id;
        private String time;
        private String customer;
        private String product;
        private String amount;
        private String paymentMethod;
        private String status;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        
        public String getCustomer() { return customer; }
        public void setCustomer(String customer) { this.customer = customer; }
        
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
        
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

