package com.courseregistration.controller;

import com.courseregistration.dao.CourseDAO;
import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.dao.StudentDAO;
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
 * Controller for Admin Dashboard analytics and metrics.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private RegistrationDAO registrationDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
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

        int totalStudents = studentDAO.getTotalStudentsCount();
        int totalCourses = courseDAO.getTotalCoursesCount();
        int availableCourses = courseDAO.getActiveCoursesCount();
        int totalRegistrations = registrationDAO.getTotalRegistrationsCount();
        int pendingRegistrations = registrationDAO.getPendingRegistrationsCount();
        int approvedRegistrations = registrationDAO.getApprovedRegistrationsCount();

        List<Registration> allRegistrations = registrationDAO.getAllRegistrations();
        // Take top 5 for recent activity table
        List<Registration> recentRegistrations = allRegistrations.size() > 5 ?
                allRegistrations.subList(0, 5) : allRegistrations;

        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("totalCourses", totalCourses);
        request.setAttribute("availableCourses", availableCourses);
        request.setAttribute("totalRegistrations", totalRegistrations);
        request.setAttribute("pendingRegistrations", pendingRegistrations);
        request.setAttribute("approvedRegistrations", approvedRegistrations);
        request.setAttribute("recentRegistrations", recentRegistrations);

        request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
