/**
 * Online Course Registration System - JavaScript Helper
 * Provides client-side validation and confirmation modals
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Auto-dismiss alerts after 6 seconds
    const alerts = document.querySelectorAll(".alert");
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = "opacity 0.5s ease";
            alert.style.opacity = "0";
            setTimeout(function () {
                if (alert.parentNode) {
                    alert.parentNode.removeChild(alert);
                }
            }, 500);
        }, 6000);
    });

    // 2. Alert close button listener
    document.querySelectorAll(".alert-close").forEach(function (btn) {
        btn.addEventListener("click", function () {
            const parent = this.closest(".alert");
            if (parent) {
                parent.remove();
            }
        });
    });
});

/**
 * Validates Student Registration Form
 */
function validateRegistrationForm() {
    const studentId = document.getElementById("studentId")?.value.trim();
    const fullName = document.getElementById("fullName")?.value.trim();
    const email = document.getElementById("email")?.value.trim();
    const password = document.getElementById("password")?.value;
    const department = document.getElementById("department")?.value.trim();
    const year = document.getElementById("year")?.value;

    if (!studentId || !fullName || !email || !password || !department || !year) {
        alert("Please fill in all required fields.");
        return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert("Please enter a valid email address.");
        return false;
    }

    if (password.length < 6) {
        alert("Password must be at least 6 characters long.");
        return false;
    }

    const yearNum = parseInt(year, 10);
    if (isNaN(yearNum) || yearNum < 1 || yearNum > 5) {
        alert("Please select a valid academic year (1 - 5).");
        return false;
    }

    return true;
}

/**
 * Validates Course Creation / Editing Form
 */
function validateCourseForm() {
    const courseCode = document.getElementById("courseCode")?.value.trim();
    const courseName = document.getElementById("courseName")?.value.trim();
    const credits = parseInt(document.getElementById("credits")?.value, 10);
    const maxSeats = parseInt(document.getElementById("maxSeats")?.value, 10);

    if (!courseCode || !courseName) {
        alert("Course Code and Course Name are required.");
        return false;
    }

    if (isNaN(credits) || credits <= 0) {
        alert("Credits must be a positive integer greater than 0.");
        return false;
    }

    if (isNaN(maxSeats) || maxSeats <= 0) {
        alert("Maximum seats must be greater than 0.");
        return false;
    }

    return true;
}

/**
 * Confirms deletion of a course
 */
function confirmCourseDelete(courseCode, courseName) {
    return confirm("Are you sure you want to delete course: " + courseCode + " - " + courseName + "?\nThis will also remove all associated student registrations!");
}

/**
 * Confirms registration cancellation
 */
function confirmRegistrationCancel(courseCode) {
    return confirm("Are you sure you want to cancel your registration for " + courseCode + "?\nThis will release your reserved seat.");
}

/**
 * Confirms status update by Admin
 */
function confirmStatusUpdate(action, studentName, courseCode) {
    return confirm("Are you sure you want to " + action + " registration for student '" + studentName + "' in course '" + courseCode + "'?");
}
