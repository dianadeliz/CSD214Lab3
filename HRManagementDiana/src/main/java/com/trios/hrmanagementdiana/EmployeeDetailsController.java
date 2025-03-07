package com.trios.hrmanagementdiana;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class EmployeeDetailsController {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    @FXML
    private Button createButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteButton;

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        salaryColumn.setCellValueFactory(cellData -> cellData.getValue().salaryProperty().asObject());

        // Ensure TableView is bound to the ObservableList
        employeeTable.setItems(employeeList);

        // Loading the data from the database
        loadEmployees();

        // Setting button actions
        createButton.setOnAction(event -> handleCreateEmployee());
        updateButton.setOnAction(event -> handleUpdateEmployee());
        viewButton.setOnAction(event -> handleViewEmployee());
        deleteButton.setOnAction(event -> handleDeleteEmployee());
        backButton.setOnAction(event -> backAction());

        // Debugging: printing confirmation
        System.out.println("EmployeeDetailsController initialized successfully.");
    }

    private void loadEmployees() {
        List<Employee> employees = employeeDAO.getAllEmployees();

        System.out.println("Total employees retrieved: " + employees.size()); // Debugging

        employeeList.setAll(employees);  // Update the ObservableList
        // No need to call employeeTable.setItems(employeeList) again.
    }

    @FXML
    private void handleCreateEmployee() {
        // Create a new Dialog to collect employee details
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Create Employee");
        dialog.setHeaderText("Enter the details of the new employee");

        // Create form fields for name, email, position, and salary
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField positionField = new TextField();
        positionField.setPromptText("Position");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        // Add fields to the dialog's content
        dialog.getDialogPane().setContent(new VBox(10, nameField, emailField, positionField, salaryField));

        // Add OK and Cancel buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Handle OK button click
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String name = nameField.getText();
                String email = emailField.getText();
                String position = positionField.getText();
                String salaryText = salaryField.getText();

                // Validate inputs
                if (name.isEmpty() || email.isEmpty() || position.isEmpty() || salaryText.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Incomplete Details", "All fields must be filled out.");
                    return null;
                }

                try {
                    double salary = Double.parseDouble(salaryText);
                    // Create and return the new employee
                    return new Employee(0, name, email, position, salary);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Please enter a valid numeric value for salary.");
                    return null;
                }
            }
            return null;
        });

        // Show the dialog and get the result
        Optional<Employee> result = dialog.showAndWait();
        result.ifPresent(employee -> {
            employeeDAO.addEmployee(employee);  // Add the new employee to the database
            loadEmployees();  // Reload the employee list
        });
    }


    @FXML
    private void handleUpdateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            // Dialogs for updating employee data
            TextInputDialog nameDialog = new TextInputDialog(selectedEmployee.getName());
            nameDialog.setTitle("Update Employee");
            nameDialog.setHeaderText("Update Employee Details");
            nameDialog.setContentText("Name:");
            String name = nameDialog.showAndWait().orElse(null);

            TextInputDialog emailDialog = new TextInputDialog(selectedEmployee.getEmail());
            emailDialog.setTitle("Update Employee");
            emailDialog.setHeaderText("Update Employee Details");
            emailDialog.setContentText("Email:");
            String email = emailDialog.showAndWait().orElse(null);

            TextInputDialog positionDialog = new TextInputDialog(selectedEmployee.getPosition());
            positionDialog.setTitle("Update Employee");
            positionDialog.setHeaderText("Update Employee Details");
            positionDialog.setContentText("Position:");
            String position = positionDialog.showAndWait().orElse(null);

            TextInputDialog salaryDialog = new TextInputDialog(String.valueOf(selectedEmployee.getSalary()));
            salaryDialog.setTitle("Update Employee");
            salaryDialog.setHeaderText("Update Employee Details");
            salaryDialog.setContentText("Salary:");
            String salaryInput = salaryDialog.showAndWait().orElse(null);

            //validation of inputs
            if (name != null && !name.isEmpty() &&
                    email != null && !email.isEmpty() &&
                    position != null && !position.isEmpty() &&
                    salaryInput != null && !salaryInput.isEmpty()) {

                try {
                    double salary = Double.parseDouble(salaryInput);

                    // Update employee object
                    selectedEmployee.setName(name);
                    selectedEmployee.setEmail(email);
                    selectedEmployee.setPosition(position);
                    selectedEmployee.setSalary(salary);

                    // Update in database
                    employeeDAO.updateEmployee(selectedEmployee);

                    // Reload data
                    loadEmployees();
                } catch (NumberFormatException e) {
                    showAlert("Invalid Salary", "Please enter a valid numeric value for salary.");
                }
            } else {
                showAlert("Incomplete Details", "All fields must be filled.");
            }
        } else {
            showAlert("No Employee Selected", "Please select an employee to update.");
        }
    }

    //helper method to show alerts
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //view employees button action
    @FXML
    private void handleViewEmployee() {
        loadEmployees();  // Reload employee data

        if (employeeList.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "No Data", "No employee records found in the database.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Data Loaded", "All employee records have been refreshed.");
        }
    }

    // helper method for alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //back button action
    @FXML
    private void backAction() {
        SceneLoader.loadScene((Stage) backButton.getScene().getWindow(), "dashboard.fxml");
    }

    //delete button action
    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedEmployee.getName() + "?",
                    ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().filter(ButtonType.YES::equals).isPresent()) {
                employeeDAO.deleteEmployee(selectedEmployee.getId());
                employeeList.remove(selectedEmployee);
            }
        }
    }
}
