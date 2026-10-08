package com.courseregistration.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an Academic Course.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private int courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private String instructor;
    private String department;
    private int credits;
    private int maxSeats;
    private int availableSeats;
    private String semester;
    private String status; // 'ACTIVE', 'INACTIVE', 'CLOSED'
    private Timestamp createdAt;

    // Default constructor
    public Course() {
    }

    // Constructor without courseId and createdAt (for creating new courses)
    public Course(String courseCode, String courseName, String description, String instructor,
                  String department, int credits, int maxSeats, int availableSeats, String semester, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.department = department;
        this.credits = credits;
        this.maxSeats = maxSeats;
        this.availableSeats = availableSeats;
        this.semester = semester;
        this.status = status;
    }

    // Full constructor
    public Course(int courseId, String courseCode, String courseName, String description,
                  String instructor, String department, int credits, int maxSeats,
                  int availableSeats, String semester, String status, Timestamp createdAt) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.department = department;
        this.credits = credits;
        this.maxSeats = maxSeats;
        this.availableSeats = availableSeats;
        this.semester = semester;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Business helper methods
    public boolean isFull() {
        return availableSeats <= 0;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", instructor='" + instructor + '\'' +
                ", credits=" + credits +
                ", availableSeats=" + availableSeats +
                ", maxSeats=" + maxSeats +
                ", status='" + status + '\'' +
                '}';
    }
}
