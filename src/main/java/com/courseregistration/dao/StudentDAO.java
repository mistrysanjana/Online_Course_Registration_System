package com.courseregistration.dao;

import com.courseregistration.model.Student;
import com.courseregistration.util.DBConnection;
import com.courseregistration.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations.
 */
public class StudentDAO {

    /**
     * Registers a new student.
     *
     * @param student Student object to register
     * @return true if successfully registered, false otherwise
     */
    public boolean registerStudent(Student student) {
        String sql = "INSERT INTO students (student_id, full_name, email, password, department, year) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getEmail());
            // Store hashed password
            stmt.setString(4, PasswordUtil.hashPassword(student.getPassword()));
            stmt.setString(5, student.getDepartment());
            stmt.setInt(6, student.getYear());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] registerStudent error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(stmt, conn);
        }
    }

    /**
     * Authenticates a student by email and password.
     *
     * @param email    Student email
     * @param password Raw input password
     * @return Student object if credentials are valid, null otherwise
     */
    public Student loginStudent(String email, String password) {
        String sql = "SELECT student_id, full_name, email, password, department, year, created_at FROM students WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (PasswordUtil.verifyPassword(password, storedPassword)) {
                    Student student = new Student();
                    student.setStudentId(rs.getString("student_id"));
                    student.setFullName(rs.getString("full_name"));
                    student.setEmail(rs.getString("email"));
                    student.setPassword(storedPassword);
                    student.setDepartment(rs.getString("department"));
                    student.setYear(rs.getInt("year"));
                    student.setCreatedAt(rs.getTimestamp("created_at"));
                    return student;
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] loginStudent error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return null;
    }

    /**
     * Retrieves student by ID.
     */
    public Student getStudentById(String studentId) {
        String sql = "SELECT student_id, full_name, email, password, department, year, created_at FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] getStudentById error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return null;
    }

    /**
     * Checks if a Student ID already exists.
     */
    public boolean isStudentIdExists(String studentId) {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[StudentDAO] isStudentIdExists error: " + e.getMessage());
            return false;
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
    }

    /**
     * Checks if an Email already exists.
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM students WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[StudentDAO] isEmailExists error: " + e.getMessage());
            return false;
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
    }

    /**
     * Returns total count of registered students.
     */
    public int getTotalStudentsCount() {
        String sql = "SELECT COUNT(*) FROM students";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] getTotalStudentsCount error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return 0;
    }

    /**
     * Returns list of all students.
     */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, full_name, email, password, department, year, created_at FROM students ORDER BY full_name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] getAllStudents error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getString("student_id"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setPassword(rs.getString("password"));
        s.setDepartment(rs.getString("department"));
        s.setYear(rs.getInt("year"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
