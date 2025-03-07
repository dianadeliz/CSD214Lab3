package com.trios.hrmanagementdiana;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    public static void loadScene(Stage stage, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(SceneLoader.class.getResource(fxmlFile));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSceneWithAdminName(Stage stage, String fxmlFile, String adminName) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Pass admin name to DashboardController
            DashboardController controller = loader.getController();
            controller.setLoggedInAdmin(adminName);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


