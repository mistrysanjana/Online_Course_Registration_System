package com.courseregistration.controller;

import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.model.Registration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Registration Management view.
 */
@WebServlet(name = "AdminRegistrationsServlet", urlPatterns = {"/admin/registrations"})
public class AdminRegistrationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RegistrationDAO registrationDAO;

    @Override
    public void init() {
        registrationDAO = new RegistrationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
            return;
        }

        List<Registration> registrations = registrationDAO.getAllRegistrations();
        request.setAttribute("registrations", registrations);
        request.getRequestDispatcher("/admin-registrations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
