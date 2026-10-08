package com.courseregistration.dao;

import com.courseregistration.model.Course;
import com.courseregistration.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course operations.
 */
public class CourseDAO {

    /**
     * Retrieves all courses ordered by course code.
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_code";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] getAllCourses error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return courses;
    }

    /**
     * Retrieves only active courses.
     */
    public List<Course> getActiveCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE status = 'ACTIVE' ORDER BY course_code";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] getActiveCourses error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return courses;
    }

    /**
     * Retrieves a single course by its primary key ID.
     */
    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] getCourseById error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return null;
    }

    /**
     * Retrieves course by course code.
     */
    public Course getCourseByCode(String courseCode) {
        String sql = "SELECT * FROM courses WHERE course_code = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] getCourseByCode error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return null;
    }

    /**
     * Checks if a course code already exists (optionally excluding a specific course ID for edits).
     */
    public boolean isCourseCodeExists(String courseCode, int excludeCourseId) {
        String sql = "SELECT 1 FROM courses WHERE course_code = ? AND course_id != ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            stmt.setInt(2, excludeCourseId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[CourseDAO] isCourseCodeExists error: " + e.getMessage());
            return false;
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
    }

    /**
     * Inserts a new course into the database.
     */
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, description, instructor, department, credits, max_seats, available_seats, semester, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, course.getCourseCode().trim().toUpperCase());
            stmt.setString(2, course.getCourseName().trim());
            stmt.setString(3, course.getDescription().trim());
            stmt.setString(4, course.getInstructor().trim());
            stmt.setString(5, course.getDepartment().trim());
            stmt.setInt(6, course.getCredits());
            stmt.setInt(7, course.getMaxSeats());
            // When adding, available seats usually default to max seats or specified amount
            int avail = course.getAvailableSeats() > 0 ? course.getAvailableSeats() : course.getMaxSeats();
            stmt.setInt(8, avail);
            stmt.setString(9, course.getSemester().trim());
            stmt.setString(10, course.getStatus() != null ? course.getStatus() : "ACTIVE");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] addCourse error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(stmt, conn);
        }
    }

    /**
     * Updates an existing course.
     */
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, description = ?, instructor = ?, " +
                     "department = ?, credits = ?, max_seats = ?, available_seats = ?, semester = ?, status = ? " +
                     "WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, course.getCourseCode().trim().toUpperCase());
            stmt.setString(2, course.getCourseName().trim());
            stmt.setString(3, course.getDescription().trim());
            stmt.setString(4, course.getInstructor().trim());
            stmt.setString(5, course.getDepartment().trim());
            stmt.setInt(6, course.getCredits());
            stmt.setInt(7, course.getMaxSeats());
            stmt.setInt(8, course.getAvailableSeats());
            stmt.setString(9, course.getSemester().trim());
            stmt.setString(10, course.getStatus());
            stmt.setInt(11, course.getCourseId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] updateCourse error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(stmt, conn);
        }
    }

    /**
     * Deletes a course by ID.
     */
    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] deleteCourse error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(stmt, conn);
        }
    }

    /**
     * Updates available seat count by delta (+1 or -1) atomically.
     *
     * @param courseId Course ID
     * @param delta    Amount to change (+1 or -1)
     * @return true if updated, false if bounds exceeded
     */
    public boolean updateAvailableSeats(int courseId, int delta) {
        String sql = "UPDATE courses SET available_seats = available_seats + ? " +
                     "WHERE course_id = ? AND (available_seats + ?) >= 0 AND (available_seats + ?) <= max_seats";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, delta);
            stmt.setInt(2, courseId);
            stmt.setInt(3, delta);
            stmt.setInt(4, delta);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] updateAvailableSeats error: " + e.getMessage());
            return false;
        } finally {
            DBConnection.close(stmt, conn);
        }
    }

    /**
     * Searches and filters courses by text search, department, and semester.
     */
    public List<Course> searchAndFilterCourses(String query, String department, String semester) {
        List<Course> courses = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM courses WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (LOWER(course_code) LIKE ? OR LOWER(course_name) LIKE ? OR LOWER(instructor) LIKE ?) ");
            String q = "%" + query.trim().toLowerCase() + "%";
            params.add(q);
            params.add(q);
            params.add(q);
        }

        if (department != null && !department.trim().isEmpty() && !"ALL".equalsIgnoreCase(department)) {
            sql.append("AND department = ? ");
            params.add(department.trim());
        }

        if (semester != null && !semester.trim().isEmpty() && !"ALL".equalsIgnoreCase(semester)) {
            sql.append("AND semester = ? ");
            params.add(semester.trim());
        }

        sql.append("ORDER BY course_code");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] searchAndFilterCourses error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return courses;
    }

    /**
     * Retrieves unique department names for filter drop-down.
     */
    public List<String> getAllDepartments() {
        List<String> depts = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM courses ORDER BY department";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                depts.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] getAllDepartments error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return depts;
    }

    /**
     * Retrieves unique semesters for filter drop-down.
     */
    public List<String> getAllSemesters() {
        List<String> sems = new ArrayList<>();
        String sql = "SELECT DISTINCT semester FROM courses ORDER BY semester";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sems.add(rs.getString("semester"));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] getAllSemesters error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return sems;
    }

    /**
     * Total courses count.
     */
    public int getTotalCoursesCount() {
        String sql = "SELECT COUNT(*) FROM courses";
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
            System.err.println("[CourseDAO] getTotalCoursesCount error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return 0;
    }

    /**
     * Active courses count.
     */
    public int getActiveCoursesCount() {
        String sql = "SELECT COUNT(*) FROM courses WHERE status = 'ACTIVE'";
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
            System.err.println("[CourseDAO] getActiveCoursesCount error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return 0;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseId(rs.getInt("course_id"));
        c.setCourseCode(rs.getString("course_code"));
        c.setCourseName(rs.getString("course_name"));
        c.setDescription(rs.getString("description"));
        c.setInstructor(rs.getString("instructor"));
        c.setDepartment(rs.getString("department"));
        c.setCredits(rs.getInt("credits"));
        c.setMaxSeats(rs.getInt("max_seats"));
        c.setAvailableSeats(rs.getInt("available_seats"));
        c.setSemester(rs.getString("semester"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
