<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Registration" %>
<%@ page import="com.courseregistration.model.Student" %>
<%@ page import="java.util.List" %>
<%
    Student currentStudent = (Student) session.getAttribute("student");
    if (currentStudent == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please login first.");
        return;
    }

    @SuppressWarnings("unchecked")
    List<Registration> registrations = (List<Registration>) request.getAttribute("registrations");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Courses - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">My Registered Courses</h1>
                <p class="page-subtitle">Track your enrolled courses and active registration status</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">
                    ➕ Register New Course
                </a>
            </div>
        </div>

        <!-- Alert notifications -->
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
                <h2 class="card-title">Enrolled Courses List</h2>
            </div>
            <div class="card-body" style="padding: 0;">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Registration ID</th>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Credits</th>
                                <th>Instructor</th>
                                <th>Registration Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (registrations == null || registrations.isEmpty()) { %>
                                <tr>
                                    <td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">
                                        You have not registered for any courses yet.
                                        <div style="margin-top: 1rem;">
                                            <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary btn-sm">
                                                Browse Available Courses
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            <% } else { 
                                for (Registration reg : registrations) { 
                            %>
                                <tr>
                                    <td><strong>#<%= reg.getRegistrationId() %></strong></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/course-details?id=<%= reg.getCourseId() %>" class="course-code-badge" style="text-decoration: none;">
                                            <%= reg.getCourseCode() %>
                                        </a>
                                    </td>
                                    <td>
                                        <strong><%= reg.getCourseName() %></strong>
                                        <div style="font-size: 0.8rem; color: var(--secondary);"><%= reg.getSemester() %></div>
                                    </td>
                                    <td><%= reg.getCredits() %></td>
                                    <td><%= reg.getInstructor() %></td>
                                    <td><%= reg.getRegistrationDate() != null ? reg.getRegistrationDate().toString().substring(0, 16) : "-" %></td>
                                    <td>
                                        <% if ("APPROVED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-approved">✓ Approved</span>
                                        <% } else if ("PENDING".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-pending">⏳ Pending</span>
                                        <% } else if ("REJECTED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-rejected">✕ Rejected</span>
                                        <% } else { %>
                                            <span class="badge badge-cancelled">Cancelled</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if ("PENDING".equalsIgnoreCase(reg.getStatus())) { %>
                                            <form action="${pageContext.request.contextPath}/student/cancel-registration" method="POST" 
                                                  onsubmit="return confirmRegistrationCancel('<%= reg.getCourseCode() %>');" style="display:inline;">
                                                <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                <button type="submit" class="btn btn-danger btn-sm" title="Cancel this pending registration and release seat">
                                                    Cancel
                                                </button>
                                            </form>
                                        <% } else if ("APPROVED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span style="font-size: 0.85rem; color: var(--success); font-weight: 600;">Enrolled</span>
                                        <% } else { %>
                                            <span style="font-size: 0.85rem; color: var(--secondary);">-</span>
                                        <% } %>
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
