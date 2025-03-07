package com.trios.hrmanagementdiana;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAO {
    private final Connection connection;

    public SalaryDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Get all salaries
    public List<Salary> getAllSalaries() {
        List<Salary> salaries = new ArrayList<>();
        String query = "SELECT id, employee_id, amount, payment_date FROM Salary";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Salary salary = new Salary(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date").toLocalDate()
                );
                salaries.add(salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salaries;
    }

    // Calculate yearly salary for a given employee ID
    public double calculateYearlySalary(int employeeId) {
        String query = "SELECT SUM(amount) AS total FROM Salary WHERE employee_id = ?";
        double yearlySalary = 0;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                yearlySalary = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yearlySalary;
    }

    // Add a salary record
    public void addSalary(int employeeId, double amount, LocalDate paymentDate) {
        String query = "INSERT INTO Salary (employee_id, amount, payment_date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, Date.valueOf(paymentDate));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Clear all salary records (useful for tests)
    public void clearSalaries() {
        String query = "DELETE FROM Salary";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
