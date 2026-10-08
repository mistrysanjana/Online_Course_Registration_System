<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Course" %>
<%@ page import="com.courseregistration.model.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%
    Student currentStudent = (Student) session.getAttribute("student");
    @SuppressWarnings("unchecked")
    List<Course> courses = (List<Course>) request.getAttribute("courses");
    @SuppressWarnings("unchecked")
    List<String> departments = (List<String>) request.getAttribute("departments");
    @SuppressWarnings("unchecked")
    List<String> semesters = (List<String>) request.getAttribute("semesters");
    @SuppressWarnings("unchecked")
    Set<Integer> registeredCourseIds = (Set<Integer>) request.getAttribute("registeredCourseIds");

    String selectedSearch = (String) request.getAttribute("selectedSearch");
    if (selectedSearch == null) selectedSearch = "";
    String selectedDept = (String) request.getAttribute("selectedDept");
    if (selectedDept == null) selectedDept = "ALL";
    String selectedSem = (String) request.getAttribute("selectedSem");
    if (selectedSem == null) selectedSem = "ALL";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Catalog - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 class="page-title">Available Courses</h1>
                <p class="page-subtitle">Explore semester offerings, view instructors, and register for courses</p>
            </div>
            <% if (currentStudent != null) { %>
                <a href="${pageContext.request.contextPath}/student/my-courses" class="btn btn-secondary">
                    📋 View My Enrolled Courses
                </a>
            <% } %>
        </div>

        <!-- Alert messages -->
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

        <!-- Filter & Search Bar -->
        <div class="filter-bar">
            <form action="${pageContext.request.contextPath}/courses" method="GET" class="filter-form">
                <div>
                    <label for="search" class="form-label">Search Courses</label>
                    <input type="text" id="search" name="search" class="form-control" 
                           placeholder="Search by code, title, or instructor..." 
                           value="<%= selectedSearch %>">
                </div>

                <div>
                    <label for="department" class="form-label">Department</label>
                    <select id="department" name="department" class="form-control">
                        <option value="ALL">All Departments</option>
                        <% if (departments != null) {
                            for (String dept : departments) { %>
                                <option value="<%= dept %>" <%= dept.equals(selectedDept) ? "selected" : "" %>><%= dept %></option>
                        <%  } 
                           } %>
                    </select>
                </div>

                <div>
                    <label for="semester" class="form-label">Semester</label>
                    <select id="semester" name="semester" class="form-control">
                        <option value="ALL">All Semesters</option>
                        <% if (semesters != null) {
                            for (String sem : semesters) { %>
                                <option value="<%= sem %>" <%= sem.equals(selectedSem) ? "selected" : "" %>><%= sem %></option>
                        <%  } 
                           } %>
                    </select>
                </div>

                <div style="display: flex; gap: 0.5rem;">
                    <button type="submit" class="btn btn-primary" style="height: 40px;">
                        🔍 Filter
                    </button>
                    <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary" style="height: 40px;" title="Reset Filters">
                        ↺ Reset
                    </a>
                </div>
            </form>
        </div>

        <!-- Course Cards Grid -->
        <% if (courses == null || courses.isEmpty()) { %>
            <div class="card" style="text-align: center; padding: 3rem;">
                <p style="font-size: 1.1rem; color: var(--secondary);">No courses found matching your criteria.</p>
                <div style="margin-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/courses" class="btn btn-primary">Clear Filters</a>
                </div>
            </div>
        <% } else { %>
            <div class="courses-grid">
                <% for (Course course : courses) { 
                    boolean isFull = course.isFull();
                    boolean isRegistered = (registeredCourseIds != null && registeredCourseIds.contains(course.getCourseId()));
                %>
                    <div class="course-card">
                        <div>
                            <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                                <span class="course-code-badge"><%= course.getCourseCode() %></span>
                                <% if (isRegistered) { %>
                                    <span class="badge badge-approved" title="You are currently registered">✓ Registered</span>
                                <% } else if (isFull) { %>
                                    <span class="badge badge-full">Course Full</span>
                                <% } else { %>
                                    <span class="badge badge-seats"><%= course.getAvailableSeats() %> / <%= course.getMaxSeats() %> Seats</span>
                                <% } %>
                            </div>

                            <h3 class="course-title"><%= course.getCourseName() %></h3>
                            <p class="course-desc"><%= course.getDescription() %></p>
                        </div>

                        <div>
                            <div class="course-meta">
                                <div class="meta-row">
                                    <span class="meta-label">Instructor:</span>
                                    <span><strong><%= course.getInstructor() %></strong></span>
                                </div>
                                <div class="meta-row">
                                    <span class="meta-label">Department:</span>
                                    <span><%= course.getDepartment() %></span>
                                </div>
                                <div class="meta-row">
                                    <span class="meta-label">Credits:</span>
                                    <span><%= course.getCredits() %> Credits</span>
                                </div>
                                <div class="meta-row">
                                    <span class="meta-label">Semester:</span>
                                    <span><%= course.getSemester() %></span>
                                </div>
                            </div>

                            <div class="course-actions">
                                <a href="${pageContext.request.contextPath}/course-details?id=<%= course.getCourseId() %>" class="btn btn-secondary btn-sm" style="flex: 1;">
                                    View Details
                                </a>

                                <% if (currentStudent != null) { %>
                                    <% if (isRegistered) { %>
                                        <button class="btn btn-disabled btn-sm" disabled style="flex: 1;">
                                            Registered
                                        </button>
                                    <% } else if (isFull) { %>
                                        <button class="btn btn-disabled btn-sm" disabled style="flex: 1;">
                                            Course Full
                                        </button>
                                    <% } else { %>
                                        <form action="${pageContext.request.contextPath}/student/register-course" method="POST" style="flex: 1;">
                                            <input type="hidden" name="courseId" value="<%= course.getCourseId() %>">
                                            <button type="submit" class="btn btn-primary btn-sm" style="width: 100%;">
                                                Register
                                            </button>
                                        </form>
                                    <% } %>
                                <% } else { %>
                                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-sm" style="flex: 1;">
                                        Login to Register
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
