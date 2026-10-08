<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Course" %>
<%@ page import="com.courseregistration.model.Admin" %>
<%@ page import="java.util.List" %>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
        return;
    }

    @SuppressWarnings("unchecked")
    List<Course> courses = (List<Course>) request.getAttribute("courses");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Courses - Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">Course Management</h1>
                <p class="page-subtitle">Add, edit, inspect, and remove university course offerings</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/add-course" class="btn btn-primary">
                    ➕ Add New Course
                </a>
            </div>
        </div>

        <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">
                <span><%= request.getParameter("success") %></span>
                <button type="button" class="alert-close">&times;</button>
            </div>
        <% } %>

        <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger">
                <span><%= request.getParameter("error") %></span>
                <button type="button" class="alert-close">&times;</button>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <h2 class="card-title">All Course Offerings (<%= courses != null ? courses.size() : 0 %>)</h2>
            </div>
            <div class="card-body" style="padding: 0;">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Course Title</th>
                                <th>Department</th>
                                <th>Credits</th>
                                <th>Seats (Avail / Max)</th>
                                <th>Semester</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (courses == null || courses.isEmpty()) { %>
                                <tr>
                                    <td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">
                                        No courses in database. Click "Add New Course" above.
                                    </td>
                                </tr>
                            <% } else { 
                                for (Course c : courses) { 
                            %>
                                <tr>
                                    <td><span class="course-code-badge"><%= c.getCourseCode() %></span></td>
                                    <td>
                                        <strong><%= c.getCourseName() %></strong>
                                        <div style="font-size: 0.8rem; color: var(--secondary);">Instructor: <%= c.getInstructor() %></div>
                                    </td>
                                    <td><%= c.getDepartment() %></td>
                                    <td><%= c.getCredits() %></td>
                                    <td>
                                        <% if (c.isFull()) { %>
                                            <span class="badge badge-full"><%= c.getAvailableSeats() %> / <%= c.getMaxSeats() %> (Full)</span>
                                        <% } else { %>
                                            <span class="badge badge-seats"><%= c.getAvailableSeats() %> / <%= c.getMaxSeats() %></span>
                                        <% } %>
                                    </td>
                                    <td><%= c.getSemester() %></td>
                                    <td>
                                        <% if ("ACTIVE".equalsIgnoreCase(c.getStatus())) { %>
                                            <span class="badge badge-approved">Active</span>
                                        <% } else { %>
                                            <span class="badge badge-cancelled"><%= c.getStatus() %></span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <div style="display: flex; gap: 0.4rem;">
                                            <a href="${pageContext.request.contextPath}/admin/edit-course?id=<%= c.getCourseId() %>" class="btn btn-secondary btn-sm" title="Edit course parameters">
                                                ✏️ Edit
                                            </a>
                                            <form action="${pageContext.request.contextPath}/admin/delete-course" method="POST" 
                                                  onsubmit="return confirmCourseDelete('<%= c.getCourseCode() %>', '<%= c.getCourseName().replace("'", "\\'") %>');" style="display:inline;">
                                                <input type="hidden" name="id" value="<%= c.getCourseId() %>">
                                                <button type="submit" class="btn btn-danger btn-sm" title="Delete course from system">
                                                    🗑️ Delete
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            <%  } 
                               } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
