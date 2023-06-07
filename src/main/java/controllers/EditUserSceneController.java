package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
            
            btnCancel.setAction(event -> {
            	try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/MainMenuScene.fxml"));

                    Callback<Class<?>, Object> controllerFactory = param -> {
                        return new MainMenuSceneController(stage, model);
                    };

                    loader.setControllerFactory(controllerFactory);
                    GridPane root = loader.load();

                    MainMenuSceneController mainMenuSceneController = loader.getController();
                    mainMenuSceneController.showStage(root);

                } catch (IOException | SQLException e) {
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
                            user = model.getUserDoa().editUser(txtPassword.getText(),
                                    txtFirstName.getText(), txtLastName.getText());

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
                                    mainMenuSceneController.showStage(root);
                                    System.out.println(user);

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
        stage.setTitle("Edit user");
        stage.show();
        stage.centerOnScreen();
    }
}
