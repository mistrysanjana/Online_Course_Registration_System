package com.courseregistration.dao;

import com.courseregistration.model.Registration;
import com.courseregistration.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Registration operations.
 * Implements transaction management to preserve seat count consistency.
 */
public class RegistrationDAO {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_ALREADY_REGISTERED = -1;
    public static final int RESULT_COURSE_FULL = -2;
    public static final int RESULT_COURSE_NOT_FOUND = -3;
    public static final int RESULT_FAILED = 0;

    /**
     * Checks if a student is already actively registered (PENDING or APPROVED) for a course.
     */
    public boolean isAlreadyRegistered(String studentId, int courseId) {
        String sql = "SELECT 1 FROM registrations WHERE student_id = ? AND course_id = ? AND status IN ('PENDING', 'APPROVED')";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            stmt.setInt(2, courseId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] isAlreadyRegistered error: " + e.getMessage());
            return false;
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
    }

    /**
     * Registers a student for a course within a transactional block.
     * Decreases available seats by 1 and sets status to PENDING.
     *
     * @param studentId Student ID
     * @param courseId  Course ID
     * @return Result code (RESULT_SUCCESS, RESULT_ALREADY_REGISTERED, RESULT_COURSE_FULL, etc.)
     */
    public int registerCourse(String studentId, int courseId) {
        Connection conn = null;
        PreparedStatement checkCourseStmt = null;
        PreparedStatement checkRegStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateExistingStmt = null;
        PreparedStatement updateSeatStmt = null;
        ResultSet rsCourse = null;
        ResultSet rsReg = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Check Course availability and status with FOR UPDATE locking
            String checkCourseSql = "SELECT course_id, available_seats, status FROM courses WHERE course_id = ? FOR UPDATE";
            checkCourseStmt = conn.prepareStatement(checkCourseSql);
            checkCourseStmt.setInt(1, courseId);
            rsCourse = checkCourseStmt.executeQuery();

            if (!rsCourse.next()) {
                conn.rollback();
                return RESULT_COURSE_NOT_FOUND;
            }

            int availableSeats = rsCourse.getInt("available_seats");
            String courseStatus = rsCourse.getString("status");

            if (!"ACTIVE".equalsIgnoreCase(courseStatus) || availableSeats <= 0) {
                conn.rollback();
                return RESULT_COURSE_FULL;
            }

            // 2. Check if student has an existing registration record
            String checkRegSql = "SELECT registration_id, status FROM registrations WHERE student_id = ? AND course_id = ?";
            checkRegStmt = conn.prepareStatement(checkRegSql);
            checkRegStmt.setString(1, studentId);
            checkRegStmt.setInt(2, courseId);
            rsReg = checkRegStmt.executeQuery();

            if (rsReg.next()) {
                String existingStatus = rsReg.getString("status");
                if ("PENDING".equalsIgnoreCase(existingStatus) || "APPROVED".equalsIgnoreCase(existingStatus)) {
                    // Already registered
                    conn.rollback();
                    return RESULT_ALREADY_REGISTERED;
                } else {
                    // Previously CANCELLED or REJECTED: re-activate registration to PENDING
                    int existingId = rsReg.getInt("registration_id");
                    String reactivateSql = "UPDATE registrations SET status = 'PENDING', registration_date = NOW() WHERE registration_id = ?";
                    updateExistingStmt = conn.prepareStatement(reactivateSql);
                    updateExistingStmt.setInt(1, existingId);
                    updateExistingStmt.executeUpdate();
                }
            } else {
                // 3. Insert brand new registration record
                String insertSql = "INSERT INTO registrations (student_id, course_id, registration_date, status) VALUES (?, ?, NOW(), 'PENDING')";
                insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, studentId);
                insertStmt.setInt(2, courseId);
                insertStmt.executeUpdate();
            }

            // 4. Decrease available seat count by 1
            String updateSeatSql = "UPDATE courses SET available_seats = available_seats - 1 WHERE course_id = ? AND available_seats > 0";
            updateSeatStmt = conn.prepareStatement(updateSeatSql);
            updateSeatStmt.setInt(1, courseId);
            int seatRows = updateSeatStmt.executeUpdate();

            if (seatRows <= 0) {
                // No seat was decremented (race condition check)
                conn.rollback();
                return RESULT_COURSE_FULL;
            }

            // Commit transaction
            conn.commit();
            return RESULT_SUCCESS;

        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] registerCourse error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return RESULT_FAILED;
        } finally {
            DBConnection.close(rsCourse);
            DBConnection.close(rsReg);
            DBConnection.close(checkCourseStmt);
            DBConnection.close(checkRegStmt);
            DBConnection.close(insertStmt);
            DBConnection.close(updateExistingStmt);
            DBConnection.close(updateSeatStmt);
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cancels a pending registration by the student.
     * Updates status to CANCELLED and increases available seats by 1.
     *
     * @param registrationId Registration ID
     * @param studentId      Student ID (ensures students can only cancel their own)
     * @return true if successfully cancelled, false otherwise
     */
    public boolean cancelRegistration(int registrationId, String studentId) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateRegStmt = null;
        PreparedStatement updateSeatStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Verify that this registration belongs to the student and is PENDING
            String checkSql = "SELECT course_id, status FROM registrations WHERE registration_id = ? AND student_id = ? FOR UPDATE";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, registrationId);
            checkStmt.setString(2, studentId);
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int courseId = rs.getInt("course_id");
            String status = rs.getString("status");

            if (!"PENDING".equalsIgnoreCase(status)) {
                // Can only cancel pending registrations
                conn.rollback();
                return false;
            }

            // 2. Update status to CANCELLED
            String updateRegSql = "UPDATE registrations SET status = 'CANCELLED' WHERE registration_id = ?";
            updateRegStmt = conn.prepareStatement(updateRegSql);
            updateRegStmt.setInt(1, registrationId);
            updateRegStmt.executeUpdate();

            // 3. Increase course available seats by 1
            String updateSeatSql = "UPDATE courses SET available_seats = available_seats + 1 WHERE course_id = ? AND available_seats < max_seats";
            updateSeatStmt = conn.prepareStatement(updateSeatSql);
            updateSeatStmt.setInt(1, courseId);
            updateSeatStmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] cancelRegistration error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            DBConnection.close(rs);
            DBConnection.close(checkStmt);
            DBConnection.close(updateRegStmt);
            DBConnection.close(updateSeatStmt);
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Admin updates a registration status (APPROVE, REJECT).
     * Maintains seat count consistency when rejecting.
     *
     * @param registrationId Registration ID
     * @param newStatus      'APPROVED' or 'REJECTED'
     * @return true if updated successfully
     */
    public boolean updateRegistrationStatus(int registrationId, String newStatus) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateRegStmt = null;
        PreparedStatement updateSeatStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            String checkSql = "SELECT course_id, status FROM registrations WHERE registration_id = ? FOR UPDATE";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, registrationId);
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int courseId = rs.getInt("course_id");
            String oldStatus = rs.getString("status");

            // Update registration status
            String updateSql = "UPDATE registrations SET status = ? WHERE registration_id = ?";
            updateRegStmt = conn.prepareStatement(updateSql);
            updateRegStmt.setString(1, newStatus.toUpperCase());
            updateRegStmt.setInt(2, registrationId);
            updateRegStmt.executeUpdate();

            // If changing from active/pending to REJECTED or CANCELLED, release the seat (+1)
            if (("PENDING".equalsIgnoreCase(oldStatus) || "APPROVED".equalsIgnoreCase(oldStatus)) &&
                ("REJECTED".equalsIgnoreCase(newStatus) || "CANCELLED".equalsIgnoreCase(newStatus))) {
                String seatSql = "UPDATE courses SET available_seats = available_seats + 1 WHERE course_id = ? AND available_seats < max_seats";
                updateSeatStmt = conn.prepareStatement(seatSql);
                updateSeatStmt.setInt(1, courseId);
                updateSeatStmt.executeUpdate();
            } 
            // If changing from REJECTED/CANCELLED back to APPROVED or PENDING, consume seat (-1)
            else if (("REJECTED".equalsIgnoreCase(oldStatus) || "CANCELLED".equalsIgnoreCase(oldStatus)) &&
                     ("APPROVED".equalsIgnoreCase(newStatus) || "PENDING".equalsIgnoreCase(newStatus))) {
                String seatSql = "UPDATE courses SET available_seats = available_seats - 1 WHERE course_id = ? AND available_seats > 0";
                updateSeatStmt = conn.prepareStatement(seatSql);
                updateSeatStmt.setInt(1, courseId);
                int seatUpdated = updateSeatStmt.executeUpdate();
                if (seatUpdated <= 0) {
                    conn.rollback();
                    return false; // Course has no seat to allocate
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] updateRegistrationStatus error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            DBConnection.close(rs);
            DBConnection.close(checkStmt);
            DBConnection.close(updateRegStmt);
            DBConnection.close(updateSeatStmt);
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves all registrations for a specific student, joined with Course details.
     */
    public List<Registration> getStudentRegistrations(String studentId) {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT r.registration_id, r.student_id, r.course_id, r.registration_date, r.status, " +
                     "s.full_name, s.email, s.department AS student_dept, " +
                     "c.course_code, c.course_name, c.instructor, c.credits, c.semester " +
                     "FROM registrations r " +
                     "JOIN students s ON r.student_id = s.student_id " +
                     "JOIN courses c ON r.course_id = c.course_id " +
                     "WHERE r.student_id = ? " +
                     "ORDER BY r.registration_date DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToRegistration(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] getStudentRegistrations error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Retrieves all registrations across all students for Admin view.
     */
    public List<Registration> getAllRegistrations() {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT r.registration_id, r.student_id, r.course_id, r.registration_date, r.status, " +
                     "s.full_name, s.email, s.department AS student_dept, " +
                     "c.course_code, c.course_name, c.instructor, c.credits, c.semester " +
                     "FROM registrations r " +
                     "JOIN students s ON r.student_id = s.student_id " +
                     "JOIN courses c ON r.course_id = c.course_id " +
                     "ORDER BY r.registration_date DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(mapResultSetToRegistration(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] getAllRegistrations error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Total registrations count.
     */
    public int getTotalRegistrationsCount() {
        String sql = "SELECT COUNT(*) FROM registrations";
        return getCount(sql);
    }

    /**
     * Pending registrations count.
     */
    public int getPendingRegistrationsCount() {
        String sql = "SELECT COUNT(*) FROM registrations WHERE status = 'PENDING'";
        return getCount(sql);
    }

    /**
     * Approved registrations count.
     */
    public int getApprovedRegistrationsCount() {
        String sql = "SELECT COUNT(*) FROM registrations WHERE status = 'APPROVED'";
        return getCount(sql);
    }

    /**
     * Student's total registered courses count (PENDING + APPROVED).
     */
    public int getStudentActiveRegistrationsCount(String studentId) {
        String sql = "SELECT COUNT(*) FROM registrations WHERE student_id = ? AND status IN ('PENDING', 'APPROVED')";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[RegistrationDAO] getStudentActiveRegistrationsCount error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return 0;
    }

    private int getCount(String sql) {
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
            System.err.println("[RegistrationDAO] count error: " + e.getMessage());
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return 0;
    }

    private Registration mapResultSetToRegistration(ResultSet rs) throws SQLException {
        Registration reg = new Registration();
        reg.setRegistrationId(rs.getInt("registration_id"));
        reg.setStudentId(rs.getString("student_id"));
        reg.setCourseId(rs.getInt("course_id"));
        reg.setRegistrationDate(rs.getTimestamp("registration_date"));
        reg.setStatus(rs.getString("status"));

        reg.setStudentName(rs.getString("full_name"));
        reg.setStudentEmail(rs.getString("email"));
        reg.setStudentDepartment(rs.getString("student_dept"));

        reg.setCourseCode(rs.getString("course_code"));
        reg.setCourseName(rs.getString("course_name"));
        reg.setInstructor(rs.getString("instructor"));
        reg.setCredits(rs.getInt("credits"));
        reg.setSemester(rs.getString("semester"));

        return reg;
    }
}
