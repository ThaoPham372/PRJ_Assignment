package com.gym.filter;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Development-only filter to bypass authentication for PT dashboard routes.
 * It seeds the HTTP session with a PT role so PT pages can be tested without
 * login.
 */
public class DevBypassAuthFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // No initialization required for dev bypass
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpSession session = httpRequest.getSession(true);

      Object isLoggedIn = session.getAttribute("isLoggedIn");
      if (isLoggedIn == null) {
        session.setAttribute("isLoggedIn", Boolean.TRUE);
      }

      @SuppressWarnings("unchecked")
      java.util.List<String> roles = (java.util.List<String>) session.getAttribute("userRoles");
      if (roles == null || !roles.contains("PT")) {
        session.setAttribute("userRoles", Arrays.asList("PT"));
      }

      if (session.getAttribute("userId") == null) {
        // Seed a synthetic user id for dev-only flows that expect one
        session.setAttribute("userId", 1L);
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    // No resources to clean up
  }
}
