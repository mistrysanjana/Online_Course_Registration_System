<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.courseregistration.model.Course" %>
<%@ page import="com.courseregistration.model.Student" %>
<%
    Course course = (Course) request.getAttribute("course");
    if (course == null) {
        response.sendRedirect(request.getContextPath() + "/courses");
        return;
    }

    Student currentStudent = (Student) session.getAttribute("student");
    Boolean isAlreadyRegistered = (Boolean) request.getAttribute("isAlreadyRegistered");
    if (isAlreadyRegistered == null) isAlreadyRegistered = false;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= course.getCourseCode() %> - <%= course.getCourseName() %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/courses" style="color: var(--primary); text-decoration: none; font-weight: 600;">
                &larr; Back to Course Catalog
            </a>
        </div>

        <div class="card">
            <div class="card-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <span class="course-code-badge" style="font-size: 0.95rem; padding: 0.35rem 0.8rem;">
                        <%= course.getCourseCode() %>
                    </span>
                    <h1 class="card-title" style="font-size: 1.7rem; margin-top: 0.4rem;">
                        <%= course.getCourseName() %>
                    </h1>
                </div>
                <div>
                    <% if (course.isFull()) { %>
                        <span class="badge badge-full" style="font-size: 0.9rem; padding: 0.5rem 1rem;">Course Full (0 Seats)</span>
                    <% } else { %>
                        <span class="badge badge-approved" style="font-size: 0.9rem; padding: 0.5rem 1rem;">
                            <%= course.getAvailableSeats() %> Seats Available
                        </span>
                    <% } %>
                </div>
            </div>

            <div class="card-body">
                <!-- Course Details Grid -->
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; margin-bottom: 2rem; background: #f8fafc; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border);">
                    <div>
                        <div class="meta-label">Lead Instructor</div>
                        <div style="font-weight: 700; color: var(--dark); font-size: 1.05rem;"><%= course.getInstructor() %></div>
                    </div>
                    <div>
                        <div class="meta-label">Department</div>
                        <div style="font-weight: 700; color: var(--dark); font-size: 1.05rem;"><%= course.getDepartment() %></div>
                    </div>
                    <div>
                        <div class="meta-label">Academic Credits</div>
                        <div style="font-weight: 700; color: var(--dark); font-size: 1.05rem;"><%= course.getCredits() %> Credits</div>
                    </div>
                    <div>
                        <div class="meta-label">Offered Semester</div>
                        <div style="font-weight: 700; color: var(--dark); font-size: 1.05rem;"><%= course.getSemester() %></div>
                    </div>
                    <div>
                        <div class="meta-label">Total Capacity</div>
                        <div style="font-weight: 700; color: var(--dark); font-size: 1.05rem;"><%= course.getMaxSeats() %> Students</div>
                    </div>
                    <div>
                        <div class="meta-label">Available Seats</div>
                        <div style="font-weight: 700; color: <%= course.isFull() ? "var(--danger)" : "var(--success)" %>; font-size: 1.05rem;">
                            <%= course.getAvailableSeats() %> Remaining
                        </div>
                    </div>
                </div>

                <!-- Course Description -->
                <div style="margin-bottom: 2.5rem;">
                    <h3 style="font-size: 1.25rem; font-weight: 700; color: var(--dark); margin-bottom: 0.75rem;">Course Description &amp; Syllabus</h3>
                    <p style="font-size: 1.05rem; line-height: 1.8; color: var(--dark-muted);">
                        <%= course.getDescription() %>
                    </p>
                </div>

                <!-- Registration Action Box -->
                <div style="border-top: 1px solid var(--border); padding-top: 1.5rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <% if (isAlreadyRegistered) { %>
                            <span class="badge badge-approved" style="font-size: 0.9rem; padding: 0.4rem 0.8rem;">
                                ✓ You have already registered for this course
                            </span>
                        <% } else if (course.isFull()) { %>
                            <span class="badge badge-full" style="font-size: 0.9rem; padding: 0.4rem 0.8rem;">
                                No seats remaining for this semester
                            </span>
                        <% } else { %>
                            <span style="color: var(--secondary); font-size: 0.95rem;">
                                Instant seat reservation upon submission.
                            </span>
                        <% } %>
                    </div>

                    <div>
                        <% if (currentStudent != null) { %>
                            <% if (isAlreadyRegistered) { %>
                                <a href="${pageContext.request.contextPath}/student/my-courses" class="btn btn-secondary">
                                    View in My Courses
                                </a>
                            <% } else if (course.isFull()) { %>
                                <button class="btn btn-disabled" disabled>
                                    Course Full
                                </button>
                            <% } else { %>
                                <form action="${pageContext.request.contextPath}/student/register-course" method="POST" style="display: inline;">
                                    <input type="hidden" name="courseId" value="<%= course.getCourseId() %>">
                                    <button type="submit" class="btn btn-primary" style="font-size: 1.05rem; padding: 0.7rem 1.75rem;">
                                        Register for this Course
                                    </button>
                                </form>
                            <% } %>
                        <% } else { %>
                            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">
                                Log in to Register
                            </a>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
