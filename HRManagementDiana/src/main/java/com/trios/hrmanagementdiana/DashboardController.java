package com.trios.hrmanagementdiana;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button adminButton;

    @FXML
    private Button employeeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button exitButton;

    private String loggedInAdminName;

    // Called when the scene is loaded
    public void initialize() {
        // Format and set the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        dateLabel.setText("Date: " + LocalDate.now().format(formatter));

        // Set button actions
        adminButton.setOnAction(event -> adminAction());
        employeeButton.setOnAction(event -> employeeAction());
        logoutButton.setOnAction(event -> logoutAction());
        exitButton.setOnAction(event -> exitAction());
    }

    // Method to load Admin Management Scene
    public void adminAction() {
        SceneLoader.loadScene((Stage) adminButton.getScene().getWindow(), "admin.fxml");
    }

    // Method to load Employee Management Scene
    public void employeeAction() {
        SceneLoader.loadScene((Stage) employeeButton.getScene().getWindow(), "employeedetails.fxml");
    }

    // Method for logout action
    public void logoutAction() {
        SceneLoader.loadScene((Stage) logoutButton.getScene().getWindow(), "login.fxml");
    }

    // Method to exit the application
    public void exitAction() {
        System.exit(0);
    }

    // Set the name of the logged-in admin and update the label
    public void setLoggedInAdmin(String adminName) {
        this.loggedInAdminName = adminName;
        welcomeLabel.setText("Welcome, " + adminName);
    }
}
