package com.courseregistration.controller;

import com.courseregistration.dao.RegistrationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controller for Admin approving or rejecting a student registration.
 */
@WebServlet(name = "UpdateRegistrationServlet", urlPatterns = {"/admin/update-registration"})
public class UpdateRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RegistrationDAO registrationDAO;

    @Override
    public void init() {
        registrationDAO = new RegistrationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processUpdate(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processUpdate(request, response);
    }

    private void processUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
            return;
        }

        String regIdStr = request.getParameter("registrationId");
        String status = request.getParameter("status");

        if (regIdStr == null || status == null) {
            response.sendRedirect(request.getContextPath() + "/admin/registrations?error=Missing parameters.");
            return;
        }

        try {
            int registrationId = Integer.parseInt(regIdStr.trim());
            status = status.trim().toUpperCase();

            if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
                response.sendRedirect(request.getContextPath() + "/admin/registrations?error=Invalid status value.");
                return;
            }

            boolean success = registrationDAO.updateRegistrationStatus(registrationId, status);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/registrations?success=" +
                        URLEncoder.encode("Registration #" + registrationId + " status updated to " + status + ".", StandardCharsets.UTF_8.name()));
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/registrations?error=" +
                        URLEncoder.encode("Failed to update status. Course seats may be unavailable.", StandardCharsets.UTF_8.name()));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/registrations?error=Invalid registration ID format.");
        }
    }
}
