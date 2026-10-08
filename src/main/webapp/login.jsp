<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Login - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="auth-container">
            <!-- Notifications -->
            <% if (request.getParameter("registered") != null) { %>
                <div class="alert alert-success">
                    <span>Account created successfully! Please log in with your credentials.</span>
                    <button type="button" class="alert-close">&times;</button>
                </div>
            <% } %>

            <% if (request.getParameter("message") != null) { %>
                <div class="alert alert-info">
                    <span><%= request.getParameter("message") %></span>
                    <button type="button" class="alert-close">&times;</button>
                </div>
            <% } %>

            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger">
                    <span><%= request.getAttribute("errorMessage") %></span>
                    <button type="button" class="alert-close">&times;</button>
                </div>
            <% } else if (request.getParameter("error") != null) { %>
                <div class="alert alert-danger">
                    <span><%= request.getParameter("error") %></span>
                    <button type="button" class="alert-close">&times;</button>
                </div>
            <% } %>

            <div class="card">
                <div class="card-header" style="text-align: center; display: block;">
                    <h2 class="card-title" style="font-size: 1.4rem;">Student Login</h2>
                    <p class="page-subtitle">Access your course registration dashboard</p>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/student/login" method="POST">
                        <div class="form-group">
                            <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                            <input type="email" id="email" name="email" class="form-control" 
                                   placeholder="student@example.com" 
                                   value="<%= request.getAttribute("enteredEmail") != null ? request.getAttribute("enteredEmail") : "" %>" 
                                   required autofocus>
                        </div>

                        <div class="form-group">
                            <label for="password" class="form-label">Password <span class="required">*</span></label>
                            <input type="password" id="password" name="password" class="form-control" 
                                   placeholder="Enter your password" required>
                        </div>

                        <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 0.5rem;">
                            Log In to Dashboard
                        </button>
                    </form>
                </div>
            </div>

            <div style="text-align: center; font-size: 0.9rem; color: var(--secondary);">
                <p>Don't have an account yet? <a href="${pageContext.request.contextPath}/student-register.jsp" style="color: var(--primary); font-weight: 600;">Register Here</a></p>
                <p style="margin-top: 0.5rem;">Faculty or Administrator? <a href="${pageContext.request.contextPath}/admin-login.jsp" style="color: var(--secondary); font-weight: 600;">Admin Login</a></p>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
