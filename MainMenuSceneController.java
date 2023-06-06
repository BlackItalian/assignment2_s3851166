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
    private Button btnEnroll;

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
    private Label lblName;

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
    	
    	/*
		-Grey out the Withdraw from courses button when not in the Enrolled Courses view
		-Add tickboxes to the CheckColum of the table to allow for enrolling/withdrawing
		-Grey out and dont have available checkboxes for courses that are at capacity
		-Use the placeholder label to give confirmation messages on enrolling, withdrawing and exporting
		-Grey out enroll button when not in wither the view all course of search course view
		-Enroll button checks for checkbox inside the last colum of the table
    	 */
    	btnLogOut.setOnAction(event -> {
    		try {
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
    	
    	btnEditUser.setOnAction(event -> {
    		try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/EditUserScene.fxml"));

                Callback<Class<?>, Object> controllerFactory = param -> {
                    return new EditUserSceneController(stage, model);
                };

                loader.setControllerFactory(controllerFactory);
                GridPane root = loader.load();

                EditUserSceneController editUderSceneController = loader.getController();
                editUserSceneController.showStage(root);

            } catch (IOException | SQLException e) {
                Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                stage.setTitle("Error");
                stage.setScene(scene);
                stage.show();
            }
    	});
    	
    	btnViewCourses.setOnAction(event -> {
    	
    	});
    	
    	btnSearchCourses.setOnAction(event -> {
        	
    	});
    	
    	btnEnrolledCourses.setOnAction(event -> {
        	
    	});
    	
    	btnWithdrawCourses.setOnAction(event -> {
        	
    	});
    	
    	btnExportCourses.setOnAction(event -> {
        	
    	});
    	
    	btnEnroll.setOnAction(event -> {
        	
    	});
    	
    }
    public void showStage(Pane root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Dashboard");
        stage.show();
        stage.centerOnScreen();
        lblStudentId.setText(model.getCurrentuser().getStudent_id();
        lblName.setText(model.getCurrentuser().getFirstName() + " " + model.getCurrentuser().getLastName());
    }
}
