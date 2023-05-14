package main.java.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.database.UserDoa;
import main.java.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class LoginSceneController {

    @FXML
    private Button btnExit;

    @FXML
    private Button btnNewUser;

    @FXML
    private Button btnSignIn;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfUsername;

    private Stage stage;
    private Model model;
    private UserDoa udoa;

    public LoginSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        btnSignIn.setOnAction(event -> {
            try {
                model.deleteValues();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            /*if (!tfUsername.getText().isEmpty() && !tfPassword.getText().isEmpty()) {
                User user;
                try {
                    user = model.getUserDoa().getUser(tfUsername.getText(), tfPassword.getText());
                    if (user != null) {
                        model.setCurrentuser(user);

                        try {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/main/resources/fxml/CanvasScene.fxml"));
                            Callback<Class<?>, Object> controllerFactory = param -> {
                                return new CanvasSceneController(stage, model);
                            };

                            loader.setControllerFactory(controllerFactory);
                            AnchorPane root = loader.load();
                            CanvasSceneController canvasSceneController = loader.getController();
                            canvasSceneController.showStage(root);
                            tfUsername.clear();
                            tfPassword.clear();
                        } catch (Exception e) {
                            lbl.setText(e.getMessage());
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        // Show message of failed login
                    }
                } catch (SQLException e) {
                    // show message of exception
                }
            } else {
                // show message of empty fields
            }
            tfUsername.clear();
            tfPassword.clear();*/
        });

        btnNewUser.setOnAction(event ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/CreateUserScene.fxml"));

                Callback<Class<?>, Object> controllerFactory = param -> {
                    return new CreateUserSceneController(stage, model);
                };

                loader.setControllerFactory(controllerFactory);
                GridPane root = loader.load();

                CreateUserSceneController createUserSceneController = loader.getController();
                createUserSceneController.showStage(root);

                tfUsername.clear();
                tfPassword.clear();

            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.getStackTrace();
            }
        });
        btnExit.setOnAction(event -> {
            Platform.exit();
        });
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Log In");
        stage.show();
        stage.centerOnScreen();
    }
}

