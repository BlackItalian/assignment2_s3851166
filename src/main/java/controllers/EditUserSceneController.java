package main.java.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.model.Model;
import main.java.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class EditUserSceneController {

    @FXML
    private Button btnEditUser;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnCancel;
    
    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPassword;

    @FXML
    private Label lblInfo;

    private Stage stage;
    private Model model;

    public EditUserSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }
    
    @FXML
    public void initialize() {
    	
            btnExit.setOnAction(event -> {
                Platform.exit();
            });
            
            btnCancel.setOnAction(event -> {
            	try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/MainMenuScene.fxml"));

                    Callback<Class<?>, Object> controllerFactory = param -> {
                        return new MainMenuSceneController(stage, model);
                    };

                    loader.setControllerFactory(controllerFactory);
                    GridPane root = loader.load();

                    MainMenuSceneController mainMenuSceneController = loader.getController();
                    mainMenuSceneController.showStage(root);

                } catch (IOException e) {
                    Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                    stage.setTitle("Error");
                    stage.setScene(scene);
                    stage.show();
                }
            });
            
            btnEditUser.setOnAction(event -> {
                try {
                    if (!txtFirstName.getText().isEmpty()
                            && !txtLastName.getText().isEmpty()
                            && !txtPassword.getText().isEmpty()) {
                        User user;
                        try {
                            user = model.getUserDoa().editUser(model.getCurrentuser().getUsername(), txtPassword.getText(),
                                    txtFirstName.getText(), txtLastName.getText(), model.getCurrentuser().getStudent_id());

                            if (user != null) {
                                lblInfo.setText("Edited" + user.getUsername());
                                try {
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("/main/resources/fxml/MainMenuScene.fxml"));

                                    Callback<Class<?>, Object> controllerFactory = param -> {
                                        return new MainMenuSceneController(stage, model);
                                    };

                                    loader.setControllerFactory(controllerFactory);
                                    GridPane root = loader.load();

                                    MainMenuSceneController mainMenuSceneController = loader.getController();
                                    model.setCurrentuser(user);
                                    mainMenuSceneController.showStage(root);

                                } catch (IOException e) {
                                    Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                                    stage.setTitle("Error");
                                    stage.setScene(scene);
                                    stage.show();
                                }
                            } else {
                                lblInfo.setText("Cannot edit user");
                            }

                        } catch (SQLException e) {
                            lblInfo.setText(e.getMessage());
                        }
                    } else {
                        lblInfo.setText("Please fill out all fields");
                    }
                } catch (InputMismatchException e) {
                    lblInfo.setText("Invalid Input");
                }
            });
        }
    
    public void showStage(GridPane root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Edit user");
        stage.show();
        stage.centerOnScreen();
    }
}
