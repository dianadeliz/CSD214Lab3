package com.trios.hrmanagementdiana;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initially load the login scene
        SceneLoader.loadScene(primaryStage, "login.fxml");
        primaryStage.setTitle("Name: Diana ID:23094277 Date: March 7th,2025");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
