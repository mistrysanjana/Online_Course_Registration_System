package com.courseregistration.controller;

import com.courseregistration.dao.AdminDAO;
import com.courseregistration.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for Admin Authentication.
 */
@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/login"})
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("admin") != null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and Password are required.");
            request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
            return;
        }

        username = username.trim();
        Admin admin = adminDAO.loginAdmin(username, password);

        if (admin != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", admin);
            session.setAttribute("userRole", "ADMIN");
            session.setAttribute("adminUsername", admin.getUsername());

            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            request.setAttribute("errorMessage", "Invalid administrator credentials.");
            request.setAttribute("enteredUsername", username);
            request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
        }
    }
}
