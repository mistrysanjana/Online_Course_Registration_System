package com.courseregistration.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an Administrator in the system.
 */
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private int adminId;
    private String username;
    private String password;
    private Timestamp createdAt;

    public Admin() {
    }

    public Admin(int adminId, String username, String password) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
    }

    public Admin(int adminId, String username, String password, Timestamp createdAt) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                '}';
    }
}
