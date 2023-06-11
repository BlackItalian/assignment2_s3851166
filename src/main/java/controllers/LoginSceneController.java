package main.java.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.model.Model;
import main.java.model.User;

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

    @FXML
    private Label lblInfo;

    private Stage stage;
    private Model model;

    public LoginSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        btnSignIn.setOnAction(event -> {
            if (!tfUsername.getText().isEmpty() && !tfPassword.getText().isEmpty()) {
                User user;
                try {
                    user = model.getUserDoa().getUser(tfUsername.getText(), tfPassword.getText());
                    if (user != null) {
                        model.setCurrentuser(user);

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/MainMenuScene.fxml"));
                            Callback<Class<?>, Object> controllerFactory = param -> {
                                return new MainMenuSceneController(stage, model);
                            };
                            loader.setControllerFactory(controllerFactory);
                            GridPane root = loader.load();
                            MainMenuSceneController mainMenuSceneController = loader.getController();
                            mainMenuSceneController.showStage(root);
                            tfUsername.clear();
                            tfPassword.clear();
                        } catch (Exception e) {
                            lblInfo.setText(e.getMessage());
                        }
                    } else {
                        lblInfo.setText("Invalid Credentials");
                    }
                } catch (SQLException e) {
                    lblInfo.setText("User not found");
                }
            } else {
                lblInfo.setText("Please fill in both fields");
            }
            tfUsername.clear();
            tfPassword.clear();
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
                Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                stage.setTitle("Error");
                stage.setScene(scene);
                stage.show();
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

