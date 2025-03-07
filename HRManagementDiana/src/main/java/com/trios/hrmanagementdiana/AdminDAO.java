package com.trios.hrmanagementdiana;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // Login method
    public Admin loginAdmin(String email, String password) {
        String query = "SELECT * FROM Admin WHERE admin_email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String hashedPassword = hashPassword(password);

                if (hashedPassword.equals(storedHashedPassword)) {
                    int id = rs.getInt("admin_id");
                    String adminName = rs.getString("admin_name");
                    return new Admin(id, adminName, email, storedHashedPassword);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add new admin
    public void addAdmin(Admin admin) {
        String query = "INSERT INTO Admin (admin_name, admin_email, password) VALUES (?, ?, ?)";

        String hashedPassword = hashPassword(admin.getPasswordHash());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, admin.getAdminName());
            stmt.setString(2, admin.getAdminEmail());
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Admin> getAllAdmins() {
        List<Admin> adminList = new ArrayList<>();
        String query = "SELECT id, admin_name, admin_email FROM admin";  // Ensure table name is correct

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("admin_name");
                String email = rs.getString("admin_email");

                Admin admin = new Admin(id, name, email, "");  // No password needed
                adminList.add(admin);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getAllAdmins(): " + e.getMessage());
            e.printStackTrace();  // Print full stack trace for debugging
        } catch (Exception e) {
            System.err.println("Unexpected error in getAllAdmins(): " + e.getMessage());
            e.printStackTrace();
        }

        return adminList;
    }


    // Update admin details
    public void updateAdmin(Admin admin) {
        String query = "UPDATE Admin SET admin_name = ?, password = ? WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, admin.getAdminName());
            stmt.setString(2, admin.getPasswordHash());
            stmt.setInt(3, admin.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an admin
    public void deleteAdmin(int adminId) {
        String query = "DELETE FROM Admin WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, adminId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Password hashing method
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
