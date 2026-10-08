package com.courseregistration.controller;

import com.courseregistration.dao.CourseDAO;
import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.model.Registration;
import com.courseregistration.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Student Dashboard.
 */
@WebServlet(name = "StudentDashboardServlet", urlPatterns = {"/student/dashboard"})
public class StudentDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CourseDAO courseDAO;
    private RegistrationDAO registrationDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
        registrationDAO = new RegistrationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please login to access your dashboard.");
            return;
        }

        Student student = (Student) session.getAttribute("student");
        String studentId = student.getStudentId();

        int totalRegisteredCourses = registrationDAO.getStudentActiveRegistrationsCount(studentId);
        int availableCoursesCount = courseDAO.getActiveCoursesCount();
        List<Registration> studentRegistrations = registrationDAO.getStudentRegistrations(studentId);

        // Count pending vs approved for the student
        int pendingCount = 0;
        int approvedCount = 0;
        for (Registration reg : studentRegistrations) {
            if ("PENDING".equalsIgnoreCase(reg.getStatus())) {
                pendingCount++;
            } else if ("APPROVED".equalsIgnoreCase(reg.getStatus())) {
                approvedCount++;
            }
        }

        request.setAttribute("totalRegisteredCourses", totalRegisteredCourses);
        request.setAttribute("availableCoursesCount", availableCoursesCount);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("approvedCount", approvedCount);
        request.setAttribute("studentRegistrations", studentRegistrations);

        request.getRequestDispatcher("/student-dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
