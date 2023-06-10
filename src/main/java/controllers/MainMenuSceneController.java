package main.java.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.database.UserDOAImplementation;
import main.java.model.Course;
import main.java.model.Model;
import main.java.model.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

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
    private TableColumn<Course, Integer> columnCapacity;

    @FXML
    private TableColumn<Course, Boolean> columnCheck;

    @FXML
    private TableColumn<Course, String> columnCourseName;

    @FXML
    private TableColumn<Course, String> columnDay;

    @FXML
    private TableColumn<Course, String> columnDelivery;

    @FXML
    private TableColumn<Course, Double> columnDuration;

    @FXML
    private TableColumn<Course, String> columnTime;

    @FXML
    private TableColumn<Course, String> columnYear;

    @FXML
    private Label lblName;

    @FXML
    private Label lblStudentId;
    @FXML
    private Label lblPlaceholder;

    @FXML
    private TableView<Course> tblCourses;

    @FXML
    private TextField txtSearchCourses;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    private Stage stage;
    private Model model;

    public MainMenuSceneController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        //Setup tableView Cells
        columnCapacity.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCapacity()));
        columnCourseName.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCourseName()));
        columnDay.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDayOfLecture()));
        columnDelivery.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDeliveryMethod()));
        columnDuration.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDurationOfLecture()));
        columnTime.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTimeOfLecture()));
        columnYear.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getYear()));
        columnCheck.setCellValueFactory(cellData -> cellData.getValue().isSelected());
        columnCheck.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            BooleanProperty selectedProperty = course.isSelected();

            // Ensure that the selected property is not null
            if (selectedProperty == null) {
                selectedProperty = new SimpleBooleanProperty(false);
                course.setSelected(selectedProperty);
            }

            return selectedProperty;
        });

        columnCheck.setCellFactory(CheckBoxTableCell.forTableColumn(columnCheck));

        //TODO
        //Grey out the Withdraw from courses button when not in the Enrolled Courses view
        //allow for withdrawing
        //Grey out and dont have available checkboxes for courses that are at capacity
        //Use the placeholder label to give confirmation messages on enrolling, withdrawing and exporting
        //Grey out enroll button when not in wither the view all course of search course view
        //Enroll button checks for checkbox inside the last colum of the table

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

            } catch (IOException e) {
                Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                stage.setTitle("Error");
                stage.setScene(scene);
                stage.show();
            }
        });

        btnEditProfile.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/EditUserScene.fxml"));

                Callback<Class<?>, Object> controllerFactory = param -> {
                    return new EditUserSceneController(stage, model);
                };

                loader.setControllerFactory(controllerFactory);
                GridPane root = loader.load();

                EditUserSceneController editUserSceneController = loader.getController();
                editUserSceneController.showStage(root);

            } catch (IOException e) {
                Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
                stage.setTitle("Error");
                stage.setScene(scene);
                stage.show();
            }
        });

        btnViewCourses.setOnAction(event -> {
            List<Course> courseList = new ArrayList<>();
            try {
                model.getCourseDOA().getFullCourseList(courseList);
                tblCourses.getItems().clear();
                tblCourses.getItems().addAll(courseList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        btnSearchCourses.setOnAction(event -> {
            List<Course> courseList = new ArrayList<>();
            String search = txtSearchCourses.getText();
            try {
                model.getCourseDOA().getSearchCourseList(courseList, search);
                tblCourses.getItems().clear();
                tblCourses.getItems().addAll(courseList);
                txtSearchCourses.clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        btnEnrolledCourses.setOnAction(event -> {

        });

        btnWithdrawCourses.setOnAction(event -> {
            try {
                model.getUserDoa().deleteValues();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        btnExportCourses.setOnAction(event -> {

        });

        btnEnroll.setOnAction(event -> {
            int student_id = model.getCurrentuser().getStudent_id();
            for (Course course : tblCourses.getItems()) {
                int course_id = 0;
                BooleanProperty selectedProperty = course.isSelected();
                if (selectedProperty != null && selectedProperty.get()) {
                    course_id = course.getCourse_id();
                    try {
                        if (model.getUserDoa().isEnrolled(student_id, course_id)) {
                            lblPlaceholder.setText("Student " + model.getCurrentuser().getUsername() + " is already enrolled in " + course.getCourseName());
                            return;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        model.getUserDoa().enrollInCourse(student_id, course_id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    lblPlaceholder.setText("Student " + student_id + " enrolled in course " + course.getCourseName());
                    String upDown = "+";

                    try {
                        model.getCourseDOA().changeEnrollment(upDown, course.getCourse_id());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Dashboard");
        stage.show();
        stage.centerOnScreen();
        lblStudentId.setText(Integer.toString(model.getCurrentuser().getStudent_id()));
        lblName.setText(model.getCurrentuser().getFirstName() + " " + model.getCurrentuser().getLastName());
    }
}
