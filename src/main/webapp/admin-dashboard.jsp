<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Admin" %>
<%@ page import="com.courseregistration.model.Registration" %>
<%@ page import="java.util.List" %>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
        return;
    }

    Integer totalStudents = (Integer) request.getAttribute("totalStudents");
    if (totalStudents == null) totalStudents = 0;

    Integer totalCourses = (Integer) request.getAttribute("totalCourses");
    if (totalCourses == null) totalCourses = 0;

    Integer availableCourses = (Integer) request.getAttribute("availableCourses");
    if (availableCourses == null) availableCourses = 0;

    Integer totalRegistrations = (Integer) request.getAttribute("totalRegistrations");
    if (totalRegistrations == null) totalRegistrations = 0;

    Integer pendingRegistrations = (Integer) request.getAttribute("pendingRegistrations");
    if (pendingRegistrations == null) pendingRegistrations = 0;

    Integer approvedRegistrations = (Integer) request.getAttribute("approvedRegistrations");
    if (approvedRegistrations == null) approvedRegistrations = 0;

    @SuppressWarnings("unchecked")
    List<Registration> recentRegistrations = (List<Registration>) request.getAttribute("recentRegistrations");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">Admin Management Dashboard</h1>
                <p class="page-subtitle">Welcome, <strong><%= admin.getUsername() %></strong> &bull; Comprehensive system overview &amp; registration analytics</p>
            </div>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/admin/add-course" class="btn btn-primary">
                    ➕ Add New Course
                </a>
                <a href="${pageContext.request.contextPath}/admin/registrations" class="btn btn-secondary">
                    Review Registrations (<%= pendingRegistrations %>)
                </a>
            </div>
        </div>

        <!-- 6 Required Metric Statistics Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon icon-blue">👥</div>
                <div>
                    <div class="stat-value"><%= totalStudents %></div>
                    <div class="stat-label">Total Students</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-purple">📚</div>
                <div>
                    <div class="stat-value"><%= totalCourses %></div>
                    <div class="stat-label">Total Courses</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-green">📖</div>
                <div>
                    <div class="stat-value"><%= availableCourses %></div>
                    <div class="stat-label">Available Courses</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-blue">📝</div>
                <div>
                    <div class="stat-value"><%= totalRegistrations %></div>
                    <div class="stat-label">Total Registrations</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-amber">⏳</div>
                <div>
                    <div class="stat-value"><%= pendingRegistrations %></div>
                    <div class="stat-label">Pending Registrations</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-green">✅</div>
                <div>
                    <div class="stat-value"><%= approvedRegistrations %></div>
                    <div class="stat-label">Approved Registrations</div>
                </div>
            </div>
        </div>

        <!-- Quick Administration Controls -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Administrative Modules</h2>
            </div>
            <div class="card-body" style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/admin/add-course" class="btn btn-primary">
                    ➕ Add New Course
                </a>
                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">
                    🛠️ Manage Course Offerings
                </a>
                <a href="${pageContext.request.contextPath}/admin/registrations" class="btn btn-secondary">
                    📋 Process Student Registrations
                </a>
            </div>
        </div>

        <!-- Recent Registrations Activity Table -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Recent Student Registrations</h2>
                <a href="${pageContext.request.contextPath}/admin/registrations" class="btn btn-secondary btn-sm">Manage All Registrations</a>
            </div>
            <div class="card-body" style="padding: 0;">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Reg #</th>
                                <th>Student ID</th>
                                <th>Student Name</th>
                                <th>Course</th>
                                <th>Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (recentRegistrations == null || recentRegistrations.isEmpty()) { %>
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 2rem; color: var(--secondary);">
                                        No registration requests submitted yet.
                                    </td>
                                </tr>
                            <% } else { 
                                for (Registration reg : recentRegistrations) { 
                            %>
                                <tr>
                                    <td><strong>#<%= reg.getRegistrationId() %></strong></td>
                                    <td><%= reg.getStudentId() %></td>
                                    <td><strong><%= reg.getStudentName() %></strong></td>
                                    <td>
                                        <span class="course-code-badge"><%= reg.getCourseCode() %></span>
                                        <%= reg.getCourseName() %>
                                    </td>
                                    <td><%= reg.getRegistrationDate() != null ? reg.getRegistrationDate().toString().substring(0, 16) : "-" %></td>
                                    <td>
                                        <% if ("APPROVED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-approved">Approved</span>
                                        <% } else if ("PENDING".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-pending">Pending</span>
                                        <% } else if ("REJECTED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-rejected">Rejected</span>
                                        <% } else { %>
                                            <span class="badge badge-cancelled">Cancelled</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if ("PENDING".equalsIgnoreCase(reg.getStatus())) { %>
                                            <div style="display: flex; gap: 0.3rem;">
                                                <form action="${pageContext.request.contextPath}/admin/update-registration" method="POST" style="display:inline;">
                                                    <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                    <input type="hidden" name="status" value="APPROVED">
                                                    <button type="submit" class="btn btn-success btn-sm">Approve</button>
                                                </form>
                                                <form action="${pageContext.request.contextPath}/admin/update-registration" method="POST" style="display:inline;">
                                                    <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                    <input type="hidden" name="status" value="REJECTED">
                                                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                                </form>
                                            </div>
                                        <% } else { %>
                                            <span style="font-size: 0.85rem; color: var(--secondary);">Completed</span>
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
