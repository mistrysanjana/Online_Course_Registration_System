package com.courseregistration.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Reusable JDBC Database Connection Utility.
 * Manages MySQL database connections and resource cleanup.
 */
public class DBConnection {

    // Database Configuration
    // These can be overridden via JVM system properties:
    // -Ddb.url=... -Ddb.user=... -Ddb.password=...
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/course_registration_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "root"; // Change to your local MySQL password if needed

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Load MySQL JDBC Driver class
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] MySQL JDBC Driver not found in classpath: " + e.getMessage());
            // Fallback for older MySQL connector versions
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.err.println("[DBConnection] Could not find com.mysql.jdbc.Driver either: " + ex.getMessage());
            }
        }
    }

    /**
     * Obtains a new database Connection.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", DEFAULT_URL);
        String user = System.getProperty("db.user", DEFAULT_USER);
        String password = System.getProperty("db.password", DEFAULT_PASSWORD);

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Safely closes a Connection.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Error closing Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Safely closes a Statement / PreparedStatement.
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Error closing Statement: " + e.getMessage());
            }
        }
    }

    /**
     * Safely closes a ResultSet.
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Error closing ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * Convenience method to safely close ResultSet, Statement, and Connection together.
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        close(rs);
        close(stmt);
        close(conn);
    }

    /**
     * Convenience method to safely close Statement and Connection together.
     */
    public static void close(Statement stmt, Connection conn) {
        close(stmt);
        close(conn);
    }
}
