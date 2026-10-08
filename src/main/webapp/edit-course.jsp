<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Course" %>
<%@ page import="com.courseregistration.model.Admin" %>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
        return;
    }

    Course course = (Course) request.getAttribute("course");
    if (course == null) {
        response.sendRedirect(request.getContextPath() + "/admin/courses?error=Course not found.");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Course <%= course.getCourseCode() %> - Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div style="margin-bottom: 1rem;">
            <a href="${pageContext.request.contextPath}/admin/courses" style="color: var(--primary); text-decoration: none; font-weight: 600;">
                &larr; Back to Courses List
            </a>
        </div>

        <div class="page-header">
            <div>
                <h1 class="page-title">Edit Course: <%= course.getCourseCode() %></h1>
                <p class="page-subtitle">Update syllabus, seat capacity, or semester assignment</p>
            </div>
        </div>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
                <span><%= request.getAttribute("errorMessage") %></span>
                <button type="button" class="alert-close">&times;</button>
            </div>
        <% } %>

        <div class="card" style="max-width: 800px; margin: 0 auto;">
            <div class="card-header">
                <h2 class="card-title">Modify Course Details (#<%= course.getCourseId() %>)</h2>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/edit-course" method="POST" onsubmit="return validateCourseForm();">
                    <input type="hidden" name="courseId" value="<%= course.getCourseId() %>">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="courseCode" class="form-label">Course Code <span class="required">*</span></label>
                            <input type="text" id="courseCode" name="courseCode" class="form-control" 
                                   value="<%= course.getCourseCode() %>" required>
                        </div>

                        <div class="form-group">
                            <label for="courseName" class="form-label">Course Title <span class="required">*</span></label>
                            <input type="text" id="courseName" name="courseName" class="form-control" 
                                   value="<%= course.getCourseName() %>" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Description &amp; Syllabus <span class="required">*</span></label>
                        <textarea id="description" name="description" class="form-control" rows="4" required><%= course.getDescription() %></textarea>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="instructor" class="form-label">Instructor <span class="required">*</span></label>
                            <input type="text" id="instructor" name="instructor" class="form-control" 
                                   value="<%= course.getInstructor() %>" required>
                        </div>

                        <div class="form-group">
                            <label for="department" class="form-label">Department <span class="required">*</span></label>
                            <select id="department" name="department" class="form-control" required>
                                <option value="Computer Science" <%= "Computer Science".equals(course.getDepartment()) ? "selected" : "" %>>Computer Science</option>
                                <option value="Information Technology" <%= "Information Technology".equals(course.getDepartment()) ? "selected" : "" %>>Information Technology</option>
                                <option value="Electronics & Comm." <%= "Electronics & Comm.".equals(course.getDepartment()) ? "selected" : "" %>>Electronics &amp; Comm.</option>
                                <option value="Mechanical Engineering" <%= "Mechanical Engineering".equals(course.getDepartment()) ? "selected" : "" %>>Mechanical Engineering</option>
                                <option value="Civil Engineering" <%= "Civil Engineering".equals(course.getDepartment()) ? "selected" : "" %>>Civil Engineering</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="credits" class="form-label">Credits <span class="required">*</span></label>
                            <input type="number" id="credits" name="credits" class="form-control" 
                                   min="1" max="10" value="<%= course.getCredits() %>" required>
                        </div>

                        <div class="form-group">
                            <label for="maxSeats" class="form-label">Maximum Capacity <span class="required">*</span></label>
                            <input type="number" id="maxSeats" name="maxSeats" class="form-control" 
                                   min="1" max="500" value="<%= course.getMaxSeats() %>" required>
                        </div>

                        <div class="form-group">
                            <label for="availableSeats" class="form-label">Available Seats <span class="required">*</span></label>
                            <input type="number" id="availableSeats" name="availableSeats" class="form-control" 
                                   min="0" max="500" value="<%= course.getAvailableSeats() %>" required>
                            <small class="form-text">Remaining open slots.</small>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="semester" class="form-label">Semester <span class="required">*</span></label>
                            <select id="semester" name="semester" class="form-control" required>
                                <% for (int i = 1; i <= 8; i++) { 
                                    String sem = "Semester " + i;
                                %>
                                    <option value="<%= sem %>" <%= sem.equals(course.getSemester()) ? "selected" : "" %>><%= sem %></option>
                                <% } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="status" class="form-label">Status <span class="required">*</span></label>
                            <select id="status" name="status" class="form-control" required>
                                <option value="ACTIVE" <%= "ACTIVE".equalsIgnoreCase(course.getStatus()) ? "selected" : "" %>>ACTIVE (Open for enrollment)</option>
                                <option value="INACTIVE" <%= "INACTIVE".equalsIgnoreCase(course.getStatus()) ? "selected" : "" %>>INACTIVE (Enrollment paused)</option>
                                <option value="CLOSED" <%= "CLOSED".equalsIgnoreCase(course.getStatus()) ? "selected" : "" %>>CLOSED (Registration ended)</option>
                            </select>
                        </div>
                    </div>

                    <div style="display: flex; gap: 1rem; margin-top: 1.5rem;">
                        <button type="submit" class="btn btn-primary" style="flex: 1;">
                            💾 Save Course Changes
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">
                            Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
