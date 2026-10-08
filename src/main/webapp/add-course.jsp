<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Admin" %>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login?error=Admin access required.");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Course - Admin Portal</title>
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
                <h1 class="page-title">Add New Academic Course</h1>
                <p class="page-subtitle">Publish a new course into the registration directory</p>
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
                <h2 class="card-title">Course Information Form</h2>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/add-course" method="POST" onsubmit="return validateCourseForm();">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="courseCode" class="form-label">Course Code <span class="required">*</span></label>
                            <input type="text" id="courseCode" name="courseCode" class="form-control" 
                                   placeholder="e.g. CS101" 
                                   value="<%= request.getAttribute("courseCode") != null ? request.getAttribute("courseCode") : "" %>" 
                                   required autofocus>
                            <small class="form-text">Unique uppercase alphanumeric identifier.</small>
                        </div>

                        <div class="form-group">
                            <label for="courseName" class="form-label">Course Title <span class="required">*</span></label>
                            <input type="text" id="courseName" name="courseName" class="form-control" 
                                   placeholder="e.g. Programming Fundamentals" 
                                   value="<%= request.getAttribute("courseName") != null ? request.getAttribute("courseName") : "" %>" 
                                   required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Description &amp; Syllabus Overview <span class="required">*</span></label>
                        <textarea id="description" name="description" class="form-control" rows="4" 
                                  placeholder="Detailed course scope, learning objectives, and prerequisites..." required><%= request.getAttribute("description") != null ? request.getAttribute("description") : "" %></textarea>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="instructor" class="form-label">Instructor Name <span class="required">*</span></label>
                            <input type="text" id="instructor" name="instructor" class="form-control" 
                                   placeholder="e.g. Dr. Alan Turing" 
                                   value="<%= request.getAttribute("instructor") != null ? request.getAttribute("instructor") : "" %>" 
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="department" class="form-label">Academic Department <span class="required">*</span></label>
                            <select id="department" name="department" class="form-control" required>
                                <option value="">-- Select Department --</option>
                                <option value="Computer Science" <%= "Computer Science".equals(request.getAttribute("department")) ? "selected" : "" %>>Computer Science</option>
                                <option value="Information Technology" <%= "Information Technology".equals(request.getAttribute("department")) ? "selected" : "" %>>Information Technology</option>
                                <option value="Electronics & Comm." <%= "Electronics & Comm.".equals(request.getAttribute("department")) ? "selected" : "" %>>Electronics &amp; Comm.</option>
                                <option value="Mechanical Engineering" <%= "Mechanical Engineering".equals(request.getAttribute("department")) ? "selected" : "" %>>Mechanical Engineering</option>
                                <option value="Civil Engineering" <%= "Civil Engineering".equals(request.getAttribute("department")) ? "selected" : "" %>>Civil Engineering</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="credits" class="form-label">Credits <span class="required">*</span></label>
                            <input type="number" id="credits" name="credits" class="form-control" 
                                   min="1" max="10" placeholder="e.g. 4" 
                                   value="<%= request.getAttribute("credits") != null ? request.getAttribute("credits") : "4" %>" 
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="maxSeats" class="form-label">Maximum Seats <span class="required">*</span></label>
                            <input type="number" id="maxSeats" name="maxSeats" class="form-control" 
                                   min="1" max="500" placeholder="e.g. 60" 
                                   value="<%= request.getAttribute("maxSeats") != null ? request.getAttribute("maxSeats") : "60" %>" 
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="semester" class="form-label">Semester <span class="required">*</span></label>
                            <select id="semester" name="semester" class="form-control" required>
                                <option value="Semester 1">Semester 1</option>
                                <option value="Semester 2">Semester 2</option>
                                <option value="Semester 3">Semester 3</option>
                                <option value="Semester 4">Semester 4</option>
                                <option value="Semester 5">Semester 5</option>
                                <option value="Semester 6">Semester 6</option>
                                <option value="Semester 7">Semester 7</option>
                                <option value="Semester 8">Semester 8</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="status" class="form-label">Initial Status</label>
                        <select id="status" name="status" class="form-control">
                            <option value="ACTIVE" selected>ACTIVE (Available for student enrollment)</option>
                            <option value="INACTIVE">INACTIVE (Hidden / Unpublished)</option>
                        </select>
                    </div>

                    <div style="display: flex; gap: 1rem; margin-top: 1.5rem;">
                        <button type="submit" class="btn btn-primary" style="flex: 1;">
                            Save &amp; Publish Course
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
