package com.courseregistration.controller;

import com.courseregistration.dao.StudentDAO;
import com.courseregistration.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for Student Login.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/student/login", "/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("student") != null) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic Server-side validation
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and Password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        email = email.trim();

        // Attempt login
        Student student = studentDAO.loginStudent(email, password);

        if (student != null) {
            // Authentication successful: create session
            HttpSession session = request.getSession(true);
            session.setAttribute("student", student);
            session.setAttribute("userRole", "STUDENT");
            session.setAttribute("studentName", student.getFullName());
            session.setAttribute("studentId", student.getStudentId());

            // Prevent session fixation
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } else {
            // Authentication failed
            request.setAttribute("errorMessage", "Invalid email or password.");
            request.setAttribute("enteredEmail", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
