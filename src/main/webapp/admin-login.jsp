<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrator Login - Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <div class="auth-container">
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

            <div class="card" style="border-top: 4px solid #ef4444;">
                <div class="card-header" style="text-align: center; display: block;">
                    <div style="font-size: 2.2rem; margin-bottom: 0.25rem;">🛡️</div>
                    <h2 class="card-title" style="font-size: 1.4rem;">Administrator Portal</h2>
                    <p class="page-subtitle">Authorized faculty and administrative personnel only</p>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/login" method="POST">
                        <div class="form-group">
                            <label for="username" class="form-label">Admin Username <span class="required">*</span></label>
                            <input type="text" id="username" name="username" class="form-control" 
                                   placeholder="admin" 
                                   value="<%= request.getAttribute("enteredUsername") != null ? request.getAttribute("enteredUsername") : "" %>" 
                                   required autofocus>
                        </div>

                        <div class="form-group">
                            <label for="password" class="form-label">Password <span class="required">*</span></label>
                            <input type="password" id="password" name="password" class="form-control" 
                                   placeholder="••••••••" required>
                        </div>

                        <button type="submit" class="btn btn-danger" style="width: 100%; margin-top: 0.5rem;">
                            Log In as Administrator
                        </button>
                    </form>

                    <!-- Helper note for college evaluation -->
                    <div style="margin-top: 1.5rem; background: #fef2f2; border: 1px dashed #fca5a5; padding: 0.75rem; border-radius: var(--radius-sm); font-size: 0.85rem; color: #991b1b;">
                        <strong>🔑 Default Credentials:</strong><br>
                        Username: <code>admin</code> &bull; Password: <code>admin123</code>
                    </div>
                </div>
            </div>

            <div style="text-align: center; font-size: 0.9rem; color: var(--secondary);">
                <a href="${pageContext.request.contextPath}/login.jsp" style="color: var(--primary);">Return to Student Portal</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
