<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Registration" %>
<%@ page import="com.courseregistration.model.Admin" %>
<%@ page import="java.util.List" %>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
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
    <title>Manage Registrations - Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">Student Registration Management</h1>
                <p class="page-subtitle">Review, approve, or reject student enrollment requests</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
                    &larr; Back to Dashboard
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
                <h2 class="card-title">Registration Requests Directory (<%= registrations != null ? registrations.size() : 0 %>)</h2>
            </div>
            <div class="card-body" style="padding: 0;">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Reg #</th>
                                <th>Student ID</th>
                                <th>Student Name</th>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Submission Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (registrations == null || registrations.isEmpty()) { %>
                                <tr>
                                    <td colspan="8" style="text-align: center; padding: 3rem; color: var(--secondary);">
                                        No student course registrations found in the database.
                                    </td>
                                </tr>
                            <% } else { 
                                for (Registration reg : registrations) { 
                            %>
                                <tr>
                                    <td><strong>#<%= reg.getRegistrationId() %></strong></td>
                                    <td><%= reg.getStudentId() %></td>
                                    <td>
                                        <strong><%= reg.getStudentName() %></strong>
                                        <div style="font-size: 0.8rem; color: var(--secondary);"><%= reg.getStudentEmail() %></div>
                                    </td>
                                    <td><span class="course-code-badge"><%= reg.getCourseCode() %></span></td>
                                    <td><%= reg.getCourseName() %></td>
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
                                            <div style="display: flex; gap: 0.4rem;">
                                                <form action="${pageContext.request.contextPath}/admin/update-registration" method="POST" 
                                                      onsubmit="return confirmStatusUpdate('APPROVE', '<%= reg.getStudentName().replace("'", "\\'") %>', '<%= reg.getCourseCode() %>');" style="display:inline;">
                                                    <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                    <input type="hidden" name="status" value="APPROVED">
                                                    <button type="submit" class="btn btn-success btn-sm">
                                                        ✓ Approve
                                                    </button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/admin/update-registration" method="POST" 
                                                      onsubmit="return confirmStatusUpdate('REJECT', '<%= reg.getStudentName().replace("'", "\\'") %>', '<%= reg.getCourseCode() %>');" style="display:inline;">
                                                    <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                    <input type="hidden" name="status" value="REJECTED">
                                                    <button type="submit" class="btn btn-danger btn-sm">
                                                        ✕ Reject
                                                    </button>
                                                </form>
                                            </div>
                                        <% } else if ("APPROVED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <form action="${pageContext.request.contextPath}/admin/update-registration" method="POST" 
                                                  onsubmit="return confirmStatusUpdate('REVERT/REJECT', '<%= reg.getStudentName().replace("'", "\\'") %>', '<%= reg.getCourseCode() %>');" style="display:inline;">
                                                <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                <input type="hidden" name="status" value="REJECTED">
                                                <button type="submit" class="btn btn-secondary btn-sm" title="Revoke approval and release seat">
                                                    Revoke
                                                </button>
                                            </form>
                                        <% } else if ("REJECTED".equalsIgnoreCase(reg.getStatus())) { %>
                                            <form action="${pageContext.request.contextPath}/admin/update-registration" method="POST" 
                                                  onsubmit="return confirmStatusUpdate('RE-APPROVE', '<%= reg.getStudentName().replace("'", "\\'") %>', '<%= reg.getCourseCode() %>');" style="display:inline;">
                                                <input type="hidden" name="registrationId" value="<%= reg.getRegistrationId() %>">
                                                <input type="hidden" name="status" value="APPROVED">
                                                <button type="submit" class="btn btn-success btn-sm" title="Re-approve this registration">
                                                    Re-Approve
                                                </button>
                                            </form>
                                        <% } else { %>
                                            <span style="font-size: 0.85rem; color: var(--secondary);">Student Cancelled</span>
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
