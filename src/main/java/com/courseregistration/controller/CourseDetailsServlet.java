package com.courseregistration.controller;

import com.courseregistration.dao.CourseDAO;
import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.model.Course;
import com.courseregistration.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for displaying full details of a specific course.
 */
@WebServlet(name = "CourseDetailsServlet", urlPatterns = {"/course-details"})
public class CourseDetailsServlet extends HttpServlet {
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
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/courses");
            return;
        }

        try {
            int courseId = Integer.parseInt(idStr.trim());
            Course course = courseDAO.getCourseById(courseId);

            if (course == null) {
                response.sendRedirect(request.getContextPath() + "/courses?error=Course not found.");
                return;
            }

            boolean isAlreadyRegistered = false;
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("student") != null) {
                Student student = (Student) session.getAttribute("student");
                isAlreadyRegistered = registrationDAO.isAlreadyRegistered(student.getStudentId(), courseId);
            }

            request.setAttribute("course", course);
            request.setAttribute("isAlreadyRegistered", isAlreadyRegistered);
            request.getRequestDispatcher("/course-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/courses?error=Invalid course ID.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
