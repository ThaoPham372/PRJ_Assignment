package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.User;
//import com.gym.model.shop.Product;
//import service.shop.ProductService;
//import cservice.shop.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * HomeServlet handles requests for the home page
 * Displays gym information, membership packages, coaches, and contact form
 * Loads featured products, user session information
 */
@WebServlet(name = "HomeServlet", urlPatterns = { "/home", "/HomeServlet" })
public class HomeServlet extends HttpServlet {
  
//  private ProductService productService;

//  @Override
//  public void init() throws ServletException {
//    super.init();
//    this.productService = new ProductServiceImpl();
//  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      // Load user session if available
      loadUserSession(request);
      
      // Load featured/latest products for home page
//      loadFeaturedProducts(request);
      
      // Placeholder for future features (news, feedback, etc.)
      // loadLatestNews(request);
      // loadFeaturedFeedback(request);
      
      // Forward đến home.jsp
      request.getRequestDispatcher("/home.jsp").forward(request, response);
    } catch (Exception e) {
      System.err.println("[HomeServlet] Error processing request: " + e.getMessage());
      e.printStackTrace();
      // Still forward to home.jsp even if there's an error
      request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Handle POST requests the same way as GET
    doGet(request, response);
  }

  /**
   * Load user session information if user is logged in
   */
  private void loadUserSession(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      User user = (User) session.getAttribute("user");
      if (user != null) {
        request.setAttribute("currentUser", user);
        request.setAttribute("isLoggedIn", true);
      } else {
        request.setAttribute("isLoggedIn", false);
      }
    } else {
      request.setAttribute("isLoggedIn", false);
    }
  }

  /**
   * Load featured/latest products to display on home page
   * Gets the 6 most recent active products
   */
//  private void loadFeaturedProducts(HttpServletRequest request) {
//    try {
//      // Get 6 latest products (no filter, just newest first)
////      List<Product> featuredProducts = productService.search(null, null, 1, 6);
//      
//      if (featuredProducts == null) {
//        featuredProducts = new ArrayList<>();
//      }
//      
//      request.setAttribute("featuredProducts", featuredProducts);
//    } catch (Exception e) {
//      System.err.println("[HomeServlet] Error loading featured products: " + e.getMessage());
//      // Set empty list on error
////      request.setAttribute("featuredProducts", new ArrayList<Product>());
//    }
//  }

 

  /**
   * Placeholder method for loading featured feedback/testimonials
   * TODO: Implement when Feedback model/DAO is available
   */
  /*
  private void loadFeaturedFeedback(HttpServletRequest request) {
    try {
      // Future implementation:
      // FeedbackService feedbackService = new FeedbackServiceImpl();
      // List<Feedback> featuredFeedback = feedbackService.findFeatured(3);
      // request.setAttribute("featuredFeedback", featuredFeedback);
    } catch (Exception e) {
      System.err.println("[HomeServlet] Error loading featured feedback: " + e.getMessage());
    }
  }
  */
}