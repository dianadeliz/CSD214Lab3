package com.trios.hrmanagementdiana;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public String authenticateUser(String email, String password) {
        String query = "SELECT admin_name, password FROM Admin WHERE admin_email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String hashedPassword = hashPassword(password);

                if (hashedPassword.equals(storedHashedPassword)) {
                    return rs.getString("admin_name"); // Return the admin’s name if authentication is successful
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if authentication fails
    }

    // Helper function to hash passwords using SHA-256
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

    public void registerAdmin(String name, String email, String password) {
        String query = "INSERT INTO Admin (admin_name, admin_email, password) VALUES (?, ?, ?)";

        String hashedPassword = hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}