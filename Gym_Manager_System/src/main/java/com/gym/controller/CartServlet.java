package com.gym.controller;

import com.gym.service.shop.CartService;
import com.gym.service.shop.CartServiceImpl;
import com.gym.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for cart operations
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart/*"})
public class CartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.cartService = new CartServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = pathInfo == null || pathInfo.equals("/") ? "view" : pathInfo.substring(1);

        switch (action) {
            case "view":
                viewCart(request, response, userId);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[CartServlet] POST request received");
        System.out.println("[CartServlet] Request URI: " + request.getRequestURI());
        System.out.println("[CartServlet] Path Info: " + request.getPathInfo());
        
        if (!SessionUtil.isLoggedIn(request)) {
            System.out.println("[CartServlet] User not logged in, redirecting to login");
            String referer = request.getHeader("Referer");
            if (referer != null) {
                request.getSession().setAttribute("redirectAfterLogin", referer);
            }
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            System.out.println("[CartServlet] User ID is null, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        System.out.println("[CartServlet] User ID: " + userId);
        String pathInfo = request.getPathInfo();
        String action = pathInfo == null || pathInfo.equals("/") ? "add" : pathInfo.substring(1);
        System.out.println("[CartServlet] Action: '" + action + "'");
        System.out.println("[CartServlet] PathInfo: '" + pathInfo + "'");

        switch (action) {
            case "add":
                System.out.println("[CartServlet] Calling addToCart");
                addToCart(request, response, userId);
                break;
            case "update":
                System.out.println("[CartServlet] Calling updateCart");
                updateCart(request, response, userId);
                break;
            case "remove":
                System.out.println("[CartServlet] Calling removeFromCart");
                removeFromCart(request, response, userId);
                break;
            default:
                System.out.println("[CartServlet] Unknown action: '" + action + "', redirecting to cart");
                response.sendRedirect(request.getContextPath() + "/cart");
                break;
        }
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        var cartItems = cartService.view(userId);
        var total = cartService.calculateTotal(cartItems);

        request.setAttribute("cart", cartItems);
        request.setAttribute("total", total);

        // Check for error messages
        String error = request.getParameter("error");
        if (error != null) {
            if ("empty".equals(error)) {
                request.setAttribute("error", "Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi thanh toán.");
            } else if ("stock".equals(error)) {
                request.setAttribute("error", "Sản phẩm không đủ tồn kho. Vui lòng kiểm tra lại giỏ hàng.");
            }
        }

        request.getRequestDispatcher("/views/cart/cart.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        String redirectUrl = request.getContextPath() + "/cart";
        
        try {
            String productIdStr = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            System.out.println("[CartServlet] addToCart - productId: " + productIdStr + ", quantity: " + quantityStr);
            System.out.println("[CartServlet] Request URL: " + request.getRequestURL());
            System.out.println("[CartServlet] Request URI: " + request.getRequestURI());
            
            if (productIdStr == null || quantityStr == null) {
                System.err.println("[CartServlet] Missing parameters: productId or quantity");
                request.getSession().setAttribute("error", "Thông tin sản phẩm không hợp lệ");
                System.out.println("[CartServlet] Redirecting to: " + redirectUrl);
                response.sendRedirect(redirectUrl);
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            Integer quantity = Integer.parseInt(quantityStr);

            System.out.println("[CartServlet] Adding product " + productId + " with quantity " + quantity + " to cart for user " + userId);
            cartService.add(userId, productId, quantity);

            request.getSession().setAttribute("message", "Đã thêm sản phẩm vào giỏ hàng");
            System.out.println("[CartServlet] Product added successfully");
        } catch (NumberFormatException e) {
            System.err.println("[CartServlet] NumberFormatException: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            System.err.println("[CartServlet] Exception adding to cart: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Không thể thêm sản phẩm: " + e.getMessage());
        }

        // Always redirect to cart page - ensure response is not committed
        if (response.isCommitted()) {
            System.err.println("[CartServlet] WARNING: Response already committed! Cannot redirect.");
        } else {
            System.out.println("[CartServlet] Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(request.getParameter("productId"));
            Integer quantity = Integer.parseInt(request.getParameter("quantity"));

            cartService.setQuantity(userId, productId, quantity);

            request.getSession().setAttribute("message", "Đã cập nhật giỏ hàng");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Không thể cập nhật: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(request.getParameter("productId"));
            cartService.remove(userId, productId);

            request.getSession().setAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Không thể xóa: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}


