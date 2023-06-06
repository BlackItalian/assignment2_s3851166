package main.java.controllers;

import javafx.scene.Scene;


import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.model.Model;

public class MainMenuSceneController {
	@FXML
    private Button btnEditProfile;

    @FXML
    private Button btnEnrolledCourses;

    @FXML
    private Button btnExportCourses;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnSearchCourses;

    @FXML
    private Button btnViewCourses;

    @FXML
    private Button btnWithdrawCourses;

    @FXML
    private TableColumn<?, ?> columnCapacity;

    @FXML
    private TableColumn<?, ?> columnCheck;

    @FXML
    private TableColumn<?, ?> columnCourseName;

    @FXML
    private TableColumn<?, ?> columnDay;

    @FXML
    private TableColumn<?, ?> columnDelivery;

    @FXML
    private TableColumn<?, ?> columnDuration;

    @FXML
    private TableColumn<?, ?> columnTime;

    @FXML
    private TableColumn<?, ?> columnYear;

    @FXML
    private Label lblFirstName;

    @FXML
    private Label lblLastName;

    @FXML
    private Label lblStudentId;

    @FXML
    private TableView<?> tblCourses;

    @FXML
    private TextField txtSearchCourses;

    private Stage stage;
    private Model model;

    public MainMenuSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }
    
    @FXML 
    public void initialize() {

    	btnLogOut.setOnAction(event -> {
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
    	});
    	
    	btnViewCourses.setOnAction(event -> {
    	
    	});
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
