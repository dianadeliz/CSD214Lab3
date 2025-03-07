package com.trios.hrmanagementdiana;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AdminController {

    @FXML
    private TableView<Admin> adminTable;
    @FXML
    private TableColumn<Admin, Integer> idColumn;
    @FXML
    private TableColumn<Admin, String> nameColumn;
    @FXML
    private TableColumn<Admin, String> emailColumn;
    @FXML
    private Button viewButton, createButton, updateButton, deleteButton, backButton;

    @FXML
    private Label welcomeLabel; // FXML label that shows the welcome message

    // Store logged-in admin name
    private String loggedInAdminName;

    // Method to set the logged-in admin name
    public void setLoggedInAdmin(String adminName) {
        this.loggedInAdminName = adminName;
        welcomeLabel.setText("Welcome, " + adminName);
    }

    private AdminDAO adminDAO = new AdminDAO();
    private ObservableList<Admin> adminList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdminName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdminEmail()));

        adminTable.setItems(adminList);
        viewButton.setOnAction(event -> handleViewAdmins());
        createButton.setOnAction(event -> handleCreateAdmin());
        updateButton.setOnAction(event -> handleUpdateAdmin());
        deleteButton.setOnAction(event -> handleDeleteAdmin());
    }

    @FXML
    private void handleViewAdmins() {
        adminList.clear();
        List<Admin> admin = adminDAO.getAllAdmins();

        if (admin.isEmpty()) {
            showAlert("No Admins", "No admins were found in the database.");
        } else {
            adminList.addAll(admin);
            adminTable.setItems(adminList);
        }
    }

    @FXML
    private void handleCreateAdmin() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Create Admin");
        nameDialog.setHeaderText("Enter Admin Name:");
        String name = nameDialog.showAndWait().orElse(null);

        if (name == null || name.trim().isEmpty()) {
            showAlert("Invalid Input", "Admin name cannot be empty.");
            return;
        }

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Create Admin");
        emailDialog.setHeaderText("Enter Admin Email:");
        String email = emailDialog.showAndWait().orElse(null);

        if (email == null || email.trim().isEmpty()) {
            showAlert("Invalid Input", "Admin email cannot be empty.");
            return;
        }

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Create Admin");
        passwordDialog.setHeaderText("Enter Password:");
        String password = passwordDialog.showAndWait().orElse(null);

        if (password == null || password.trim().isEmpty()) {
            showAlert("Invalid Input", "Password cannot be empty.");
            return;
        }

        String hashedPassword = hashPassword(password);
        Admin newAdmin = new Admin(0, name, email, hashedPassword);
        adminDAO.addAdmin(newAdmin);
        handleViewAdmins();
    }

    @FXML
    private void handleUpdateAdmin() {
        Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin == null) {
            showAlert("No Admin Selected", "Please select an admin to update.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog(selectedAdmin.getAdminName());
        nameDialog.setTitle("Update Admin");
        nameDialog.setHeaderText("Enter new Admin Name:");
        String newName = nameDialog.showAndWait().orElse(null);

        if (newName == null || newName.trim().isEmpty()) {
            showAlert("Invalid Input", "Admin name cannot be empty.");
            return;
        }

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Update Admin");
        passwordDialog.setHeaderText("Enter new Password (Leave blank to keep current):");
        String newPassword = passwordDialog.showAndWait().orElse(null);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String hashedPassword = hashPassword(newPassword);
            selectedAdmin.setPasswordHash(hashedPassword);
        }

        selectedAdmin.setAdminName(newName);
        adminDAO.updateAdmin(selectedAdmin);
        handleViewAdmins();
    }

    @FXML
    private void handleDeleteAdmin() {
        Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin == null) {
            showAlert("No Admin Selected", "Please select an admin to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Admin");
        confirmation.setHeaderText("Are you sure you want to delete this admin?");
        confirmation.setContentText("Admin: " + selectedAdmin.getAdminName());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                adminDAO.deleteAdmin(selectedAdmin.getId());
                handleViewAdmins();
            }
        });
    }

    @FXML
    private void backAction() {
        SceneLoader.loadScene((Stage) backButton.getScene().getWindow(), "dashboard.fxml");
    }

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}