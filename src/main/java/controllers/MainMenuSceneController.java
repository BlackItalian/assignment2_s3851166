package main.java.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.model.Model;

public class MainMenuSceneController {
    private Stage stage;
    private Model model;

    public MainMenuSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Menu");
        stage.show();
        stage.centerOnScreen();
    }
}
