package com.courseregistration.controller;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Security Filter for role-based access control.
 * Prevents unauthorized access to Student and Admin areas.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/student/*", "/admin/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        // Allow public student endpoints
        if (path.startsWith("/student/login") || path.startsWith("/student/register")) {
            chain.doFilter(req, res);
            return;
        }

        // Allow public admin login endpoint
        if (path.startsWith("/admin/login")) {
            chain.doFilter(req, res);
            return;
        }

        // Protect Admin routes
        if (path.startsWith("/admin/")) {
            boolean isAdminLoggedIn = (session != null && session.getAttribute("admin") != null);
            if (!isAdminLoggedIn) {
                response.sendRedirect(contextPath + "/admin-login.jsp?error=Admin session expired or unauthorized.");
                return;
            }
        }

        // Protect Student routes
        if (path.startsWith("/student/")) {
            boolean isStudentLoggedIn = (session != null && session.getAttribute("student") != null);
            if (!isStudentLoggedIn) {
                response.sendRedirect(contextPath + "/login.jsp?error=Please login as a student.");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
