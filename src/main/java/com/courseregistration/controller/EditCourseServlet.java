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
 * Controller for editing an existing course by Admin.
 */
@WebServlet(name = "EditCourseServlet", urlPatterns = {"/admin/edit-course"})
public class EditCourseServlet extends HttpServlet {
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

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=Course ID missing.");
            return;
        }

        try {
            int courseId = Integer.parseInt(idStr.trim());
            Course course = courseDAO.getCourseById(courseId);

            if (course == null) {
                response.sendRedirect(request.getContextPath() + "/admin/courses?error=Course not found.");
                return;
            }

            request.setAttribute("course", course);
            request.getRequestDispatcher("/edit-course.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=Invalid course ID format.");
        }
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

        String courseIdStr = request.getParameter("courseId");
        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        String description = request.getParameter("description");
        String instructor = request.getParameter("instructor");
        String department = request.getParameter("department");
        String creditsStr = request.getParameter("credits");
        String maxSeatsStr = request.getParameter("maxSeats");
        String availableSeatsStr = request.getParameter("availableSeats");
        String semester = request.getParameter("semester");
        String status = request.getParameter("status");

        int courseId;
        try {
            courseId = Integer.parseInt(courseIdStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=Invalid course reference.");
            return;
        }

        if (courseCode == null || courseCode.trim().isEmpty() ||
            courseName == null || courseName.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            instructor == null || instructor.trim().isEmpty() ||
            department == null || department.trim().isEmpty() ||
            creditsStr == null || creditsStr.trim().isEmpty() ||
            maxSeatsStr == null || maxSeatsStr.trim().isEmpty() ||
            availableSeatsStr == null || availableSeatsStr.trim().isEmpty() ||
            semester == null || semester.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All required fields must be filled.");
            // Reload course object for view
            Course c = courseDAO.getCourseById(courseId);
            request.setAttribute("course", c);
            request.getRequestDispatcher("/edit-course.jsp").forward(request, response);
            return;
        }

        courseCode = courseCode.trim().toUpperCase();

        int credits;
        int maxSeats;
        int availableSeats;
        try {
            credits = Integer.parseInt(creditsStr.trim());
            maxSeats = Integer.parseInt(maxSeatsStr.trim());
            availableSeats = Integer.parseInt(availableSeatsStr.trim());

            if (credits <= 0 || maxSeats < 0 || availableSeats < 0) {
                request.setAttribute("errorMessage", "Credits and seats must be non-negative values.");
                Course c = courseDAO.getCourseById(courseId);
                request.setAttribute("course", c);
                request.getRequestDispatcher("/edit-course.jsp").forward(request, response);
                return;
            }

            if (availableSeats > maxSeats) {
                request.setAttribute("errorMessage", "Available seats cannot exceed maximum seats.");
                Course c = courseDAO.getCourseById(courseId);
                request.setAttribute("course", c);
                request.getRequestDispatcher("/edit-course.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Credits and seats must be valid integers.");
            Course c = courseDAO.getCourseById(courseId);
            request.setAttribute("course", c);
            request.getRequestDispatcher("/edit-course.jsp").forward(request, response);
            return;
        }

        // Check if code is taken by another course
        if (courseDAO.isCourseCodeExists(courseCode, courseId)) {
            request.setAttribute("errorMessage", "Course code '" + courseCode + "' is already assigned to another course.");
            Course c = courseDAO.getCourseById(courseId);
            request.setAttribute("course", c);
            request.getRequestDispatcher("/edit-course.jsp").forward(request, response);
            return;
        }

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseCode(courseCode);
        course.setCourseName(courseName.trim());
        course.setDescription(description.trim());
        course.setInstructor(instructor.trim());
        course.setDepartment(department.trim());
        course.setCredits(credits);
        course.setMaxSeats(maxSeats);
        course.setAvailableSeats(availableSeats);
        course.setSemester(semester.trim());
        course.setStatus(status != null ? status : "ACTIVE");

        boolean success = courseDAO.updateCourse(course);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=" +
                    URLEncoder.encode("Course " + courseCode + " updated successfully.", StandardCharsets.UTF_8.name()));
        } else {
            request.setAttribute("errorMessage", "Database error: unable to update course.");
            request.setAttribute("course", course);
            request.getRequestDispatcher("/edit-course.jsp").forward(request, response);
        }
    }
}
