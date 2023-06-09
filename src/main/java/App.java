package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.controllers.LoginSceneController;
import main.java.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {

    private Model model;

    @Override
    public void init() {
        model = new Model();
    }

    @Override
    public void start(Stage stage) {
        try {
            model.setupDatabase();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/LoginScene.fxml"));

            Callback<Class<?>, Object> controllerFactory = param -> {
                return new LoginSceneController(stage, model);
            };

            loader.setControllerFactory(controllerFactory);
            GridPane root = loader.load();

            LoginSceneController loginSceneController = loader.getController();
            loginSceneController.showStage(root);

        } catch (IOException | SQLException e) {
            Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
            stage.setTitle("Error");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}