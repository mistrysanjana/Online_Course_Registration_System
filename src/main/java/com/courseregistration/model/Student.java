package com.courseregistration.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a Student in the system.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String fullName;
    private String email;
    private String password;
    private String department;
    private int year;
    private Timestamp createdAt;

    // Default constructor
    public Student() {
    }

    // Constructor without timestamp (for registration)
    public Student(String studentId, String fullName, String email, String password, String department, int year) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.year = year;
    }

    // Full constructor
    public Student(String studentId, String fullName, String email, String password, String department, int year, Timestamp createdAt) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.year = year;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", year=" + year +
                '}';
    }
}
