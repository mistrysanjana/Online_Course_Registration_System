package com.courseregistration.controller;

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
 * Controller for viewing student's registered courses and their status.
 */
@WebServlet(name = "MyCoursesServlet", urlPatterns = {"/student/my-courses"})
public class MyCoursesServlet extends HttpServlet {
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
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please login to view your courses.");
            return;
        }

        Student student = (Student) session.getAttribute("student");
        List<Registration> registrations = registrationDAO.getStudentRegistrations(student.getStudentId());

        request.setAttribute("registrations", registrations);
        request.getRequestDispatcher("/my-courses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
