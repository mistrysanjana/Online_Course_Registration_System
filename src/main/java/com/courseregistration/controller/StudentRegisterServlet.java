package com.courseregistration.controller;

import com.courseregistration.dao.StudentDAO;
import com.courseregistration.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Controller for Student Registration.
 */
@WebServlet(name = "StudentRegisterServlet", urlPatterns = {"/student/register"})
public class StudentRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/student-register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String studentId = request.getParameter("studentId");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String department = request.getParameter("department");
        String yearStr = request.getParameter("year");

        // Preserve form state on error
        request.setAttribute("studentId", studentId);
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("department", department);
        request.setAttribute("year", yearStr);

        // 1. Check required fields
        if (studentId == null || studentId.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            department == null || department.trim().isEmpty() ||
            yearStr == null || yearStr.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
            return;
        }

        studentId = studentId.trim();
        fullName = fullName.trim();
        email = email.trim();
        password = password.trim();
        department = department.trim();

        // 2. Validate Email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("errorMessage", "Please provide a valid email address.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
            return;
        }

        // 3. Validate Password length
        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
            return;
        }

        // 4. Validate Year
        int year;
        try {
            year = Integer.parseInt(yearStr.trim());
            if (year < 1 || year > 5) {
                request.setAttribute("errorMessage", "Academic year must be between 1 and 5.");
                request.getRequestDispatcher("/student-register.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Academic year must be a valid number.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
            return;
        }

        // 5. Check if Student ID already exists
        if (studentDAO.isStudentIdExists(studentId)) {
            request.setAttribute("errorMessage", "A student with ID '" + studentId + "' already exists.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
            return;
        }

        // 6. Check if Email already exists
        if (studentDAO.isEmailExists(email)) {
            request.setAttribute("errorMessage", "An account with email '" + email + "' already exists.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
            return;
        }

        // 7. Perform registration
        Student newStudent = new Student(studentId, fullName, email, password, department, year);
        boolean success = studentDAO.registerStudent(newStudent);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
        } else {
            request.setAttribute("errorMessage", "Unable to register student. Please check database connection and try again.");
            request.getRequestDispatcher("/student-register.jsp").forward(request, response);
        }
    }
}
