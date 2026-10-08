package com.courseregistration.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a Student Course Registration.
 * Includes both table fields and joined display fields.
 */
public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;

    // Table fields
    private int registrationId;
    private String studentId;
    private int courseId;
    private Timestamp registrationDate;
    private String status; // 'PENDING', 'APPROVED', 'REJECTED', 'CANCELLED'

    // Joined helper fields for views
    private String studentName;
    private String studentEmail;
    private String studentDepartment;
    private String courseCode;
    private String courseName;
    private String instructor;
    private int credits;
    private String semester;

    // Default constructor
    public Registration() {
    }

    // Basic constructor
    public Registration(int registrationId, String studentId, int courseId, Timestamp registrationDate, String status) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    // Full constructor with joined fields
    public Registration(int registrationId, String studentId, int courseId, Timestamp registrationDate, String status,
                        String studentName, String studentEmail, String studentDepartment,
                        String courseCode, String courseName, String instructor, int credits, String semester) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.registrationDate = registrationDate;
        this.status = status;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentDepartment = studentDepartment;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.credits = credits;
        this.semester = semester;
    }

    // Business helpers
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(status);
    }

    public boolean isRejected() {
        return "REJECTED".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }

    // Getters and Setters
    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentDepartment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "registrationId=" + registrationId +
                ", studentId='" + studentId + '\'' +
                ", courseId=" + courseId +
                ", status='" + status + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
