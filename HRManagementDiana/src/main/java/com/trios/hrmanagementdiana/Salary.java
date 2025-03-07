package com.trios.hrmanagementdiana;

import java.time.LocalDate;

public class Salary {
    private int id;
    private int employeeId;
    private double amount;
    private LocalDate paymentDate;

    public Salary(int id, int employeeId, double amount, LocalDate paymentDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // Getters
    public int getId() { return id; }
    public int getEmployeeId() { return employeeId; }
    public double getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Salary{id=" + id + ", employeeId=" + employeeId + ", amount=" + amount + ", paymentDate=" + paymentDate + '}';
    }
}