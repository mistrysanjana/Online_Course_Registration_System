<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Student" %>
<%@ page import="com.courseregistration.model.Admin" %>
<%
    Student currentStudent = (Student) session.getAttribute("student");
    Admin currentAdmin = (Admin) session.getAttribute("admin");
    String ctx = request.getContextPath();
%>
<nav class="navbar">
    <div class="nav-container">
        <a href="<%= ctx %>/" class="brand">
            <span class="brand-icon">🎓</span>
            <span>CourseReg</span>
        </a>

        <ul class="nav-links">
            <li><a href="<%= ctx %>/">Home</a></li>
            <li><a href="<%= ctx %>/courses">Courses</a></li>

            <% if (currentStudent != null) { %>
                <li><a href="<%= ctx %>/student/dashboard">Dashboard</a></li>
                <li><a href="<%= ctx %>/student/my-courses">My Courses</a></li>
                <li><a href="<%= ctx %>/student/registration-status">Status</a></li>
                <li>
                    <span class="user-badge">
                        👤 <%= currentStudent.getFullName() %>
                    </span>
                </li>
                <li><a href="<%= ctx %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>

            <% } else if (currentAdmin != null) { %>
                <li><a href="<%= ctx %>/admin/dashboard">Dashboard</a></li>
                <li><a href="<%= ctx %>/admin/courses">Manage Courses</a></li>
                <li><a href="<%= ctx %>/admin/registrations">Registrations</a></li>
                <li>
                    <span class="user-badge" style="background:#fee2e2; color:#991b1b;">
                        🛡️ Admin (<%= currentAdmin.getUsername() %>)
                    </span>
                </li>
                <li><a href="<%= ctx %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>

            <% } else { %>
                <li><a href="<%= ctx %>/login.jsp">Student Login</a></li>
                <li><a href="<%= ctx %>/student-register.jsp">Register</a></li>
                <li><a href="<%= ctx %>/admin-login.jsp" class="btn btn-secondary btn-sm">Admin Portal</a></li>
            <% } %>
        </ul>
    </div>
</nav>
