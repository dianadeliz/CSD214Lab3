package com.trios.hrmanagementdiana;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private LoginDAO loginDAO;

    public void initialize() {
        loginDAO = new LoginDAO();
        loginButton.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (password.isEmpty()) {
            showAlert("Empty Password", "Password field cannot be empty.");
            return;
        }

        // Authenticate user and get the admin name
        String adminName = loginDAO.authenticateUser(email, password);

        if (adminName != null) {
            // Pass adminName to the dashboard
            SceneLoader.loadSceneWithAdminName((Stage) loginButton.getScene().getWindow(), "dashboard.fxml", adminName);
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}