<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Course Registration System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <main class="main-content">
        <!-- Hero Section -->
        <section class="hero">
            <h1 class="hero-title">Online Course Registration System</h1>
            <p class="hero-subtitle">Register. Learn. Track Your Courses. A streamlined academic course enrollment portal for students and faculty administrators.</p>
            <div class="hero-cta">
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary" style="font-weight: 700;">
                    🔍 View Available Courses
                </a>
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary" style="background: #ffffff; color: #1e3a8a; font-weight: 700;">
                    🔑 Student Login
                </a>
                <a href="${pageContext.request.contextPath}/student-register.jsp" class="btn btn-secondary">
                    📝 Student Registration
                </a>
                <a href="${pageContext.request.contextPath}/admin-login.jsp" class="btn btn-secondary">
                    🛡️ Admin Login
                </a>
            </div>
        </section>

        <!-- System Overview & Features -->
        <div class="page-header" style="justify-content: center; text-align: center; margin-bottom: 2.5rem;">
            <div>
                <h2 class="page-title">Why Use Our Registration Platform?</h2>
                <p class="page-subtitle">Designed to eliminate manual paperwork and ensure fair, transparent seat allocation.</p>
            </div>
        </div>

        <section class="features-grid">
            <div class="feature-box">
                <div class="feature-icon">📚</div>
                <h3 class="feature-title">Diverse Course Catalog</h3>
                <p>Browse semester offerings across departments with real-time seat availability, credit counts, and instructor details.</p>
            </div>

            <div class="feature-box">
                <div class="feature-icon">⚡</div>
                <h3 class="feature-title">One-Click Enrollment</h3>
                <p>Seamless registration with atomic seat reservation. Prevents duplicate enrollments and overbooking automatically.</p>
            </div>

            <div class="feature-box">
                <div class="feature-icon">📊</div>
                <h3 class="feature-title">Live Status Tracking</h3>
                <p>Track your registration through distinct lifecycle stages: PENDING approval, APPROVED, REJECTED, or CANCELLED.</p>
            </div>

            <div class="feature-box">
                <div class="feature-icon">🛡️</div>
                <h3 class="feature-title">Administrator Controls</h3>
                <p>Faculty admins can create courses, monitor quotas, review student requests, and approve or reject registrations.</p>
            </div>
        </section>

        <!-- Quick Access Card -->
        <div class="card" style="text-align: center; padding: 2rem;">
            <h3 style="font-size: 1.35rem; color: var(--dark); margin-bottom: 0.5rem;">Ready to begin your semester registration?</h3>
            <p style="color: var(--secondary); margin-bottom: 1.5rem;">Create your student profile or log in with your university credentials today.</p>
            <div>
                <a href="${pageContext.request.contextPath}/student-register.jsp" class="btn btn-primary" style="margin-right: 0.5rem;">Get Started Now</a>
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary">Browse Courses First</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
