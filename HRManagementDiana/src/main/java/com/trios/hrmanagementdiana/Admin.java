package com.trios.hrmanagementdiana;

public class Admin {

    private int id;
    private String adminEmail;
    private String adminName;
    private String passwordHash;

    // Constructor
    public Admin(int id, String adminName, String adminEmail, String password) {
        this.id = id;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.passwordHash = password;
    }

    // Getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminName='" + adminName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}