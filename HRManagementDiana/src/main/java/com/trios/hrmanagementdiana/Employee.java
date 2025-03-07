package com.trios.hrmanagementdiana;

import javafx.beans.property.*;

public class Employee {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty position;
    private final DoubleProperty salary;

    public Employee(int id, String name, String email, String position, double salary) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
    }

    //property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty positionProperty() { return position; }
    public DoubleProperty salaryProperty() { return salary; }

    //standard getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public String getPosition() { return position.get(); }
    public double getSalary() { return salary.get(); }

    //standard setters
    public void setName(String name) { this.name.set(name); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPosition(String position) { this.position.set(position); }
    public void setSalary(double salary) { this.salary.set(salary); }
}