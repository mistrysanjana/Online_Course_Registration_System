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
import java.util.List;

/**
 * Controller for Admin Course Management Table.
 */
@WebServlet(name = "AdminCoursesServlet", urlPatterns = {"/admin/courses"})
public class AdminCoursesServlet extends HttpServlet {
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

        List<Course> courses = courseDAO.getAllCourses();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/admin-courses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
