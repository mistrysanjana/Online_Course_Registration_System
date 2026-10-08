<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Student" %>
<%@ page import="com.courseregistration.model.Registration" %>
<%@ page import="java.util.List" %>
<%
    Student student = (Student) session.getAttribute("student");
    if (student == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please login first.");
        return;
    }

    Integer totalRegistered = (Integer) request.getAttribute("totalRegisteredCourses");
    if (totalRegistered == null) totalRegistered = 0;

    Integer availableCourses = (Integer) request.getAttribute("availableCoursesCount");
    if (availableCourses == null) availableCourses = 0;

    Integer pendingCount = (Integer) request.getAttribute("pendingCount");
    if (pendingCount == null) pendingCount = 0;

    Integer approvedCount = (Integer) request.getAttribute("approvedCount");
    if (approvedCount == null) approvedCount = 0;

    @SuppressWarnings("unchecked")
    List<Registration> studentRegistrations = (List<Registration>) request.getAttribute("studentRegistrations");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">Welcome back, <%= student.getFullName() %>!</h1>
                <p class="page-subtitle">Student ID: <strong><%= student.getStudentId() %></strong> &bull; <%= student.getDepartment() %> &bull; Year <%= student.getYear() %></p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">
                    ➕ Browse &amp; Register Courses
                </a>
            </div>
        </div>

        <!-- Metric Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon icon-blue">📚</div>
                <div>
                    <div class="stat-value"><%= totalRegistered %></div>
                    <div class="stat-label">Total Registered Courses</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-amber">⏳</div>
                <div>
                    <div class="stat-value"><%= pendingCount %></div>
                    <div class="stat-label">Pending Approval</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-green">✅</div>
                <div>
                    <div class="stat-value"><%= approvedCount %></div>
                    <div class="stat-label">Approved Courses</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-purple">🎓</div>
                <div>
                    <div class="stat-value"><%= availableCourses %></div>
                    <div class="stat-label">Available Courses Catalog</div>
                </div>
            </div>
        </div>

        <!-- Quick Navigation Actions -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Quick Actions</h2>
            </div>
            <div class="card-body" style="display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">
                    🔍 Browse All Courses
                </a>
                <a href="${pageContext.request.contextPath}/student/my-courses" class="btn btn-secondary">
                    📋 View My Courses
                </a>
                <a href="${pageContext.request.contextPath}/student/registration-status" class="btn btn-secondary">
                    📊 Check Registration Status
                </a>
            </div>
        </div>

        <!-- Recent Registrations Preview -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">My Recent Registrations</h2>
                <a href="${pageContext.request.contextPath}/student/my-courses" class="btn btn-secondary btn-sm">View All</a>
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
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (studentRegistrations == null || studentRegistrations.isEmpty()) { %>
                                <tr>
                                    <td colspan="6" style="text-align: center; padding: 2rem; color: var(--secondary);">
                                        You have not registered for any courses yet.
                                        <div style="margin-top: 0.5rem;">
                                            <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary btn-sm">Browse Courses Now</a>
                                        </div>
                                    </td>
                                </tr>
                            <% } else { 
                                int count = 0;
                                for (Registration reg : studentRegistrations) {
                                    if (count++ >= 5) break;
                            %>
                                <tr>
                                    <td><strong>#<%= reg.getRegistrationId() %></strong></td>
                                    <td><span class="course-code-badge"><%= reg.getCourseCode() %></span></td>
                                    <td><%= reg.getCourseName() %></td>
                                    <td><%= reg.getCredits() %></td>
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
                                </tr>
                            <%   } 
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
