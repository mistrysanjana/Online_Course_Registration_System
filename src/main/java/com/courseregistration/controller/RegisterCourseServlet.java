package com.courseregistration.controller;

import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.model.Student;

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
 * Controller for Student Course Registration.
 */
@WebServlet(name = "RegisterCourseServlet", urlPatterns = {"/student/register-course"})
public class RegisterCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RegistrationDAO registrationDAO;

    @Override
    public void init() {
        registrationDAO = new RegistrationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRegistration(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRegistration(request, response);
    }

    private void processRegistration(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        // 1. Check whether student is logged in
        if (session == null || session.getAttribute("student") == null) {
            String redirectUrl = request.getContextPath() + "/login.jsp?error=" +
                    URLEncoder.encode("Please log in to register for courses.", StandardCharsets.UTF_8.name());
            response.sendRedirect(redirectUrl);
            return;
        }

        Student student = (Student) session.getAttribute("student");
        String courseIdStr = request.getParameter("courseId");

        // 2. Validate course ID parameter
        if (courseIdStr == null || courseIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/courses?error=" +
                    URLEncoder.encode("Invalid course selection.", StandardCharsets.UTF_8.name()));
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdStr.trim());

            // 3. Perform registration via transactional DAO
            int result = registrationDAO.registerCourse(student.getStudentId(), courseId);

            switch (result) {
                case RegistrationDAO.RESULT_SUCCESS:
                    response.sendRedirect(request.getContextPath() + "/student/my-courses?success=" +
                            URLEncoder.encode("Course registration successful! Your request is pending approval.", StandardCharsets.UTF_8.name()));
                    break;

                case RegistrationDAO.RESULT_ALREADY_REGISTERED:
                    response.sendRedirect(request.getContextPath() + "/courses?error=" +
                            URLEncoder.encode("You are already registered for this course.", StandardCharsets.UTF_8.name()));
                    break;

                case RegistrationDAO.RESULT_COURSE_FULL:
                    response.sendRedirect(request.getContextPath() + "/courses?error=" +
                            URLEncoder.encode("This course is currently full.", StandardCharsets.UTF_8.name()));
                    break;

                case RegistrationDAO.RESULT_COURSE_NOT_FOUND:
                    response.sendRedirect(request.getContextPath() + "/courses?error=" +
                            URLEncoder.encode("The requested course does not exist.", StandardCharsets.UTF_8.name()));
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/courses?error=" +
                            URLEncoder.encode("Unable to register for course. Please try again later.", StandardCharsets.UTF_8.name()));
                    break;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/courses?error=" +
                    URLEncoder.encode("Invalid course ID format.", StandardCharsets.UTF_8.name()));
        }
    }
}
