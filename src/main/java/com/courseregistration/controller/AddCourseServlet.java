package com.courseregistration.controller;

import com.courseregistration.dao.CourseDAO;
import com.courseregistration.model.Course;

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
 * Controller for adding a new course by Admin.
 */
@WebServlet(name = "AddCourseServlet", urlPatterns = {"/admin/add-course"})
public class AddCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CourseDAO courseDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
            return;
        }
        request.getRequestDispatcher("/add-course.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
            return;
        }

        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        String description = request.getParameter("description");
        String instructor = request.getParameter("instructor");
        String department = request.getParameter("department");
        String creditsStr = request.getParameter("credits");
        String maxSeatsStr = request.getParameter("maxSeats");
        String semester = request.getParameter("semester");
        String status = request.getParameter("status");

        // Preserve form state
        request.setAttribute("courseCode", courseCode);
        request.setAttribute("courseName", courseName);
        request.setAttribute("description", description);
        request.setAttribute("instructor", instructor);
        request.setAttribute("department", department);
        request.setAttribute("credits", creditsStr);
        request.setAttribute("maxSeats", maxSeatsStr);
        request.setAttribute("semester", semester);
        request.setAttribute("status", status);

        // Validation
        if (courseCode == null || courseCode.trim().isEmpty() ||
            courseName == null || courseName.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            instructor == null || instructor.trim().isEmpty() ||
            department == null || department.trim().isEmpty() ||
            creditsStr == null || creditsStr.trim().isEmpty() ||
            maxSeatsStr == null || maxSeatsStr.trim().isEmpty() ||
            semester == null || semester.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All required fields must be filled.");
            request.getRequestDispatcher("/add-course.jsp").forward(request, response);
            return;
        }

        courseCode = courseCode.trim().toUpperCase();

        int credits;
        int maxSeats;
        try {
            credits = Integer.parseInt(creditsStr.trim());
            maxSeats = Integer.parseInt(maxSeatsStr.trim());

            if (credits <= 0 || maxSeats <= 0) {
                request.setAttribute("errorMessage", "Credits and Maximum Seats must be positive numbers.");
                request.getRequestDispatcher("/add-course.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Credits and Maximum Seats must be valid integers.");
            request.getRequestDispatcher("/add-course.jsp").forward(request, response);
            return;
        }

        // Check unique course code
        if (courseDAO.isCourseCodeExists(courseCode, 0)) {
            request.setAttribute("errorMessage", "Course code '" + courseCode + "' already exists.");
            request.getRequestDispatcher("/add-course.jsp").forward(request, response);
            return;
        }

        Course course = new Course(courseCode, courseName, description, instructor, department,
                credits, maxSeats, maxSeats, semester, (status != null && !status.isEmpty()) ? status : "ACTIVE");

        boolean success = courseDAO.addCourse(course);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=" +
                    URLEncoder.encode("Course " + courseCode + " added successfully.", StandardCharsets.UTF_8.name()));
        } else {
            request.setAttribute("errorMessage", "Database error: unable to add course.");
            request.getRequestDispatcher("/add-course.jsp").forward(request, response);
        }
    }
}
