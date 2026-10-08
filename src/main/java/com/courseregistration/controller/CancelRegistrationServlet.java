package com.courseregistration.controller;

import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.model.Student;

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
 * Controller for Student cancelling a pending course registration.
 */
@WebServlet(name = "CancelRegistrationServlet", urlPatterns = {"/student/cancel-registration"})
public class CancelRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RegistrationDAO registrationDAO;

    @Override
    public void init() {
        registrationDAO = new RegistrationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processCancellation(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processCancellation(request, response);
    }

    private void processCancellation(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=" +
                    URLEncoder.encode("Please log in first.", StandardCharsets.UTF_8.name()));
            return;
        }

        Student student = (Student) session.getAttribute("student");
        String regIdStr = request.getParameter("registrationId");

        if (regIdStr == null || regIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/student/my-courses?error=" +
                    URLEncoder.encode("Invalid registration reference.", StandardCharsets.UTF_8.name()));
            return;
        }

        try {
            int registrationId = Integer.parseInt(regIdStr.trim());
            boolean success = registrationDAO.cancelRegistration(registrationId, student.getStudentId());

            if (success) {
                response.sendRedirect(request.getContextPath() + "/student/my-courses?success=" +
                        URLEncoder.encode("Registration cancelled successfully. Seat has been released.", StandardCharsets.UTF_8.name()));
            } else {
                response.sendRedirect(request.getContextPath() + "/student/my-courses?error=" +
                        URLEncoder.encode("Unable to cancel registration. Only PENDING registrations can be cancelled.", StandardCharsets.UTF_8.name()));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/student/my-courses?error=" +
                    URLEncoder.encode("Invalid registration ID format.", StandardCharsets.UTF_8.name()));
        }
    }
}
