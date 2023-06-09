package main.java.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import java.util.InputMismatchException;

public class CreateUserSceneController {

    @FXML
    private Button btnCreateUser;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblInfo;

    private Stage stage;
    private Model model;

    public CreateUserSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    @FXML
    public void initialize() {
            btnExit.setOnAction(event -> {
                Platform.exit();
            });

            btnCreateUser.setOnAction(event -> {
                try {
                    if (!txtUsername.getText().isEmpty() && !txtFirstName.getText().isEmpty()
                            && !txtLastName.getText().isEmpty()
                            && !txtPassword.getText().isEmpty()) {
                        User user;
                        try {
                            user = model.getUserDoa().createUser(txtUsername.getText(), txtPassword.getText(),
                                    txtFirstName.getText(), txtLastName.getText());

                            if (user != null) {
                                lblInfo.setText("Created" + user.getUsername());
                                try {
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("/main/resources/fxml/LoginScene.fxml"));

                                    Callback<Class<?>, Object> controllerFactory = param -> {
                                        return new LoginSceneController(stage, model);
                                    };

                                    loader.setControllerFactory(controllerFactory);
                                    GridPane root = loader.load();

                                    LoginSceneController loginSceneController = loader.getController();
                                    loginSceneController.showStage(root);
                                    System.out.println(user);

                                } catch (IOException e) {
                                    Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                                    stage.setTitle("Error");
                                    stage.setScene(scene);
                                    stage.show();
                                }
                            } else {
                                lblInfo.setText("Cannot create user");
                            }

                        } catch (SQLException e) {
                            lblInfo.setText(e.getMessage());
                            System.out.println("here");
                            System.out.println(e);
                        }
                    } else {
                        lblInfo.setText("Please fill out all fields to continue");
                    }
                } catch (InputMismatchException e) {
                    lblInfo.setText("Invalid Input");
                }
            });
        }
    public void showStage(Pane root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Create a new user");
        stage.show();
        stage.centerOnScreen();
    }
}
