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
    <title>Registration Status - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">Course Registration Status</h1>
                <p class="page-subtitle">Detailed breakdown of approval workflows for student: <strong><%= currentStudent.getFullName() %></strong></p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">
                    ➕ Register Another Course
                </a>
            </div>
        </div>

        <!-- Status Legend Guide -->
        <div class="card" style="margin-bottom: 1.5rem; background: #ffffff;">
            <div class="card-body" style="padding: 1rem 1.5rem; display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap; gap: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <span class="badge badge-pending">⏳ PENDING</span>
                    <span style="font-size: 0.85rem; color: var(--secondary);">Under administrative review</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <span class="badge badge-approved">✅ APPROVED</span>
                    <span style="font-size: 0.85rem; color: var(--secondary);">Seat confirmed &amp; enrolled</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <span class="badge badge-rejected">❌ REJECTED</span>
                    <span style="font-size: 0.85rem; color: var(--secondary);">Request declined by admin</span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <span class="badge badge-cancelled">⚪ CANCELLED</span>
                    <span style="font-size: 0.85rem; color: var(--secondary);">Voluntarily dropped</span>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Registration History &amp; Current Status</h2>
            </div>
            <div class="card-body" style="padding: 0;">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Registration ID</th>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Department</th>
                                <th>Submission Date</th>
                                <th>Current Status</th>
                                <th>Remarks / Details</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (registrations == null || registrations.isEmpty()) { %>
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 3rem; color: var(--secondary);">
                                        No registration requests submitted yet.
                                        <div style="margin-top: 1rem;">
                                            <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary btn-sm">Explore Courses</a>
                                        </div>
                                    </td>
                                </tr>
                            <% } else { 
                                for (Registration reg : registrations) { 
                            %>
                                <tr>
                                    <td><strong>#<%= reg.getRegistrationId() %></strong></td>
                                    <td><span class="course-code-badge"><%= reg.getCourseCode() %></span></td>
                                    <td>
                                        <strong><%= reg.getCourseName() %></strong>
                                        <div style="font-size: 0.8rem; color: var(--secondary);"><%= reg.getCredits() %> Credits &bull; <%= reg.getSemester() %></div>
                                    </td>
                                    <td><%= reg.getStudentDepartment() %></td>
                                    <td><%= reg.getRegistrationDate() != null ? reg.getRegistrationDate().toString().substring(0, 16) : "-" %></td>
                                    <td>
                                        <% if ("APPROVED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-approved" style="font-size: 0.82rem; padding: 0.4rem 0.8rem;">
                                                ✅ Approved
                                            </span>
                                        <% } else if ("PENDING".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-pending" style="font-size: 0.82rem; padding: 0.4rem 0.8rem;">
                                                ⏳ Pending
                                            </span>
                                        <% } else if ("REJECTED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span class="badge badge-rejected" style="font-size: 0.82rem; padding: 0.4rem 0.8rem;">
                                                ❌ Rejected
                                            </span>
                                        <% } else { %>
                                            <span class="badge badge-cancelled" style="font-size: 0.82rem; padding: 0.4rem 0.8rem;">
                                                ⚪ Cancelled
                                            </span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if ("APPROVED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span style="color: var(--success); font-size: 0.85rem;">Enrolled in class roster.</span>
                                        <% } else if ("PENDING".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span style="color: var(--warning); font-size: 0.85rem;">Awaiting department approval.</span>
                                        <% } else if ("REJECTED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <span style="color: var(--danger); font-size: 0.85rem;">Prerequisites or capacity limitation.</span>
                                        <% } else { %>
                                            <span style="color: var(--secondary); font-size: 0.85rem;">Cancelled by student.</span>
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
