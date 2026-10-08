<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Registration - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="auth-container" style="max-width: 580px;">
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger">
                    <span><%= request.getAttribute("errorMessage") %></span>
                    <button type="button" class="alert-close">&times;</button>
                </div>
            <% } %>

            <div class="card">
                <div class="card-header" style="text-align: center; display: block;">
                    <h2 class="card-title" style="font-size: 1.4rem;">Student Registration</h2>
                    <p class="page-subtitle">Create your academic account to register for courses</p>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/student/register" method="POST" onsubmit="return validateRegistrationForm();">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="studentId" class="form-label">Student ID <span class="required">*</span></label>
                                <input type="text" id="studentId" name="studentId" class="form-control" 
                                       placeholder="e.g. STU1005 or CS202601" 
                                       value="<%= request.getAttribute("studentId") != null ? request.getAttribute("studentId") : "" %>" 
                                       required autofocus>
                                <small class="form-text">Your unique college roll/registration number.</small>
                            </div>

                            <div class="form-group">
                                <label for="fullName" class="form-label">Full Name <span class="required">*</span></label>
                                <input type="text" id="fullName" name="fullName" class="form-control" 
                                       placeholder="e.g. John Doe" 
                                       value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>" 
                                       required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                                <input type="email" id="email" name="email" class="form-control" 
                                       placeholder="student@example.com" 
                                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" 
                                       required>
                            </div>

                            <div class="form-group">
                                <label for="password" class="form-label">Password <span class="required">*</span></label>
                                <input type="password" id="password" name="password" class="form-control" 
                                       placeholder="At least 6 characters" minlength="6" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="department" class="form-label">Department <span class="required">*</span></label>
                                <select id="department" name="department" class="form-control" required>
                                    <option value="">-- Select Department --</option>
                                    <option value="Computer Science" <%= "Computer Science".equals(request.getAttribute("department")) ? "selected" : "" %>>Computer Science</option>
                                    <option value="Information Technology" <%= "Information Technology".equals(request.getAttribute("department")) ? "selected" : "" %>>Information Technology</option>
                                    <option value="Electronics & Comm." <%= "Electronics & Comm.".equals(request.getAttribute("department")) ? "selected" : "" %>>Electronics &amp; Comm.</option>
                                    <option value="Mechanical Engineering" <%= "Mechanical Engineering".equals(request.getAttribute("department")) ? "selected" : "" %>>Mechanical Engineering</option>
                                    <option value="Civil Engineering" <%= "Civil Engineering".equals(request.getAttribute("department")) ? "selected" : "" %>>Civil Engineering</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="year" class="form-label">Academic Year <span class="required">*</span></label>
                                <select id="year" name="year" class="form-control" required>
                                    <option value="">-- Select Year --</option>
                                    <option value="1" <%= "1".equals(request.getAttribute("year")) ? "selected" : "" %>>Year 1 (Freshman)</option>
                                    <option value="2" <%= "2".equals(request.getAttribute("year")) ? "selected" : "" %>>Year 2 (Sophomore)</option>
                                    <option value="3" <%= "3".equals(request.getAttribute("year")) ? "selected" : "" %>>Year 3 (Junior)</option>
                                    <option value="4" <%= "4".equals(request.getAttribute("year")) ? "selected" : "" %>>Year 4 (Senior)</option>
                                </select>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;">
                            Create Student Account
                        </button>
                    </form>
                </div>
            </div>

            <div style="text-align: center; font-size: 0.9rem; color: var(--secondary);">
                Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" style="color: var(--primary); font-weight: 600;">Log In</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
