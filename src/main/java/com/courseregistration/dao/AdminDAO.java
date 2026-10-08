package com.courseregistration.dao;

import com.courseregistration.model.Admin;
import com.courseregistration.util.DBConnection;
import com.courseregistration.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for Administrator operations.
 */
public class AdminDAO {

    /**
     * Authenticates an administrator.
     *
     * @param username Admin username
     * @param password Admin password
     * @return Admin object if credentials match, null otherwise
     */
    public Admin loginAdmin(String username, String password) {
        String sql = "SELECT admin_id, username, password, created_at FROM admins WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (PasswordUtil.verifyPassword(password, storedPassword)) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(storedPassword);
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] loginAdmin error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return null;
    }

    /**
     * Retrieves Admin by ID.
     */
    public Admin getAdminById(int adminId) {
        String sql = "SELECT admin_id, username, password, created_at FROM admins WHERE admin_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adminId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
                return admin;
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] getAdminById error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(rs, stmt, conn);
        }
        return null;
    }
}
