package com.courseregistration.controller;

import com.courseregistration.dao.CourseDAO;

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
 * Controller for deleting a course by Admin.
 */
@WebServlet(name = "DeleteCourseServlet", urlPatterns = {"/admin/delete-course"})
public class DeleteCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CourseDAO courseDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processDelete(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processDelete(request, response);
    }

    private void processDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=Missing course ID.");
            return;
        }

        try {
            int courseId = Integer.parseInt(idStr.trim());
            boolean success = courseDAO.deleteCourse(courseId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/courses?success=" +
                        URLEncoder.encode("Course deleted successfully.", StandardCharsets.UTF_8.name()));
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/courses?error=" +
                        URLEncoder.encode("Unable to delete course. It may have active dependencies.", StandardCharsets.UTF_8.name()));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=Invalid course ID format.");
        }
    }
}
