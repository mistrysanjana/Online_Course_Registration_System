package com.courseregistration.controller;

import com.courseregistration.dao.CourseDAO;
import com.courseregistration.dao.RegistrationDAO;
import com.courseregistration.model.Course;
import com.courseregistration.model.Registration;
import com.courseregistration.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for Course Catalog, search, and filtering.
 */
@WebServlet(name = "CourseServlet", urlPatterns = {"/courses", "/student/courses"})
public class CourseServlet extends HttpServlet {
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
        String query = request.getParameter("search");
        String department = request.getParameter("department");
        String semester = request.getParameter("semester");

        List<Course> courses;
        if ((query != null && !query.trim().isEmpty()) ||
            (department != null && !department.trim().isEmpty() && !"ALL".equalsIgnoreCase(department)) ||
            (semester != null && !semester.trim().isEmpty() && !"ALL".equalsIgnoreCase(semester))) {
            courses = courseDAO.searchAndFilterCourses(query, department, semester);
        } else {
            courses = courseDAO.getAllCourses();
        }

        List<String> departments = courseDAO.getAllDepartments();
        List<String> semesters = courseDAO.getAllSemesters();

        // Check if student is logged in to identify already registered courses
        HttpSession session = request.getSession(false);
        Set<Integer> registeredCourseIds = new HashSet<>();
        if (session != null && session.getAttribute("student") != null) {
            Student student = (Student) session.getAttribute("student");
            List<Registration> regs = registrationDAO.getStudentRegistrations(student.getStudentId());
            for (Registration reg : regs) {
                if ("PENDING".equalsIgnoreCase(reg.getStatus()) || "APPROVED".equalsIgnoreCase(reg.getStatus())) {
                    registeredCourseIds.add(reg.getCourseId());
                }
            }
        }

        request.setAttribute("courses", courses);
        request.setAttribute("departments", departments);
        request.setAttribute("semesters", semesters);
        request.setAttribute("selectedSearch", query != null ? query : "");
        request.setAttribute("selectedDept", department != null ? department : "ALL");
        request.setAttribute("selectedSem", semester != null ? semester : "ALL");
        request.setAttribute("registeredCourseIds", registeredCourseIds);

        request.getRequestDispatcher("/courses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
