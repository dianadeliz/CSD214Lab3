package com.trios.hrmanagementdiana;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SalaryDAOTest {
    private Connection connection;
    private SalaryDAO salaryDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DatabaseConnection.getConnection();
        salaryDAO = new SalaryDAO();
        setupTestData();
    }

    @BeforeEach
    void clearTableBeforeTest() {
        salaryDAO.clearSalaries();
        setupTestData();
    }

    @AfterAll
    void closeDatabase() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // Insert sample test data
    private void setupTestData() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Salary");
            stmt.execute("DELETE FROM EmployeeDetail");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO EmployeeDetail (id, employee_name, employee_email, position, salary) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setInt(1, 1);
            stmt.setString(2, "Alice Johnson");
            stmt.setString(3, "alice@example.com");
            stmt.setString(4, "Software Engineer");
            stmt.setDouble(5, 72000);
            stmt.executeUpdate();

            stmt.setInt(1, 2);
            stmt.setString(2, "Bob Smith");
            stmt.setString(3, "bob@example.com");
            stmt.setString(4, "Project Manager");
            stmt.setDouble(5, 90000);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert Salary Records
        salaryDAO.addSalary(1, 6000, LocalDate.of(2024, 1, 1));
        salaryDAO.addSalary(1, 6000, LocalDate.of(2024, 2, 1));
        salaryDAO.addSalary(1, 6000, LocalDate.of(2024, 3, 1));
        salaryDAO.addSalary(2, 7500, LocalDate.of(2024, 1, 1));
        salaryDAO.addSalary(2, 7500, LocalDate.of(2024, 2, 1));
        salaryDAO.addSalary(2, 7500, LocalDate.of(2024, 3, 1));
    }

    @Test
    void testGetAllSalaries() {
        List<Salary> salaries = salaryDAO.getAllSalaries();
        assertFalse(salaries.isEmpty(), "Salary records should not be empty");
    }

    @Test
    void testCalculateYearlySalary() {
        double yearlySalaryAlice = salaryDAO.calculateYearlySalary(1);
        assertEquals(18000, yearlySalaryAlice, "Yearly salary for Alice should be 18,000");

        double yearlySalaryBob = salaryDAO.calculateYearlySalary(2);
        assertEquals(22500, yearlySalaryBob, "Yearly salary for Bob should be 22,500");
    }

    @Test
    void testAddSalary() {
        salaryDAO.addSalary(1, 7000, LocalDate.of(2024, 4, 1));
        double updatedYearlySalary = salaryDAO.calculateYearlySalary(1);
        assertEquals(25000, updatedYearlySalary, "Updated yearly salary for Alice should be 25,000");
    }
}