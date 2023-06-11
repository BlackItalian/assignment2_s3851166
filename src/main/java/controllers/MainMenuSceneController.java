package main.java.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.model.Course;
import main.java.model.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    private final Stage stage;
    private final Model model;

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
            if (selectedProperty == null) {
                selectedProperty = new SimpleBooleanProperty(false);
                course.setSelected(selectedProperty);
            }

            return selectedProperty;
        });

        //Disable courses over capacity
        tblCourses.setRowFactory(tv -> {
            return new TableRow<Course>() {
                @Override
                protected void updateItem(Course course, boolean empty) {
                    super.updateItem(course, empty);

                    if (course == null || empty) {
                        setDisable(false);
                        setStyle("");
                    } else {
                        boolean exceededCapacity = course.getEnrolled() >= course.getCapacity();
                        setDisable(exceededCapacity);

                        if (exceededCapacity) {
                            setStyle("-fx-opacity: 0.5; -fx-background-color: #CCCCCC;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
        columnCheck.setCellFactory(CheckBoxTableCell.forTableColumn(columnCheck));

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
            btnWithdrawCourses.setDisable(true);
            btnEnroll.setDisable(false);
            List<Course> courseList = new ArrayList<>();
            try {
                model.getCourseDOA().getFullCourseList(courseList);
                tblCourses.getItems().clear();
                tblCourses.getItems().addAll(courseList);
            } catch (SQLException e) {
                lblPlaceholder.setText("Cannot get course list");
            }
        });

        btnSearchCourses.setOnAction(event -> {
            btnWithdrawCourses.setDisable(true);
            btnEnroll.setDisable(false);
            List<Course> courseList = new ArrayList<>();
            String search = txtSearchCourses.getText();
            try {
                model.getCourseDOA().getSearchCourseList(courseList, search);
                tblCourses.getItems().clear();
                tblCourses.getItems().addAll(courseList);
                txtSearchCourses.clear();
            } catch (SQLException e) {
                lblPlaceholder.setText("Cannot get course list");
            }
            if (courseList.isEmpty()) {
                tblCourses.setPlaceholder(new Label("No results"));
            }
        });

        btnEnrolledCourses.setOnAction(event -> {
            btnWithdrawCourses.setDisable(false);
            btnEnroll.setDisable(true);
            List<Course> courseList = new ArrayList<>();
            int student_id = model.getCurrentuser().getStudent_id();
            try {
                model.getCourseDOA().getEnrolledCourseList(courseList, student_id);
                tblCourses.getItems().clear();
                tblCourses.getItems().addAll(courseList);
            } catch (SQLException e) {
                lblPlaceholder.setText("Cannot get course list");
            }
            if (courseList.isEmpty()) {
                tblCourses.setPlaceholder(new Label("Not yet enrolled in any Courses"));
            }
        });

        btnWithdrawCourses.setOnAction(event -> {
            int student_id = model.getCurrentuser().getStudent_id();
            for (Course course : tblCourses.getItems()) {
                int course_id = 0;
                BooleanProperty selectedProperty = course.isSelected();
                if (selectedProperty != null && selectedProperty.get()) {
                    course_id = course.getCourse_id();
                    try {
                        if (model.getUserDoa().isEnrolled(student_id, course_id)) {
                            try {
                                model.getUserDoa().withdrawFromCourse(student_id, course_id);
                                lblPlaceholder.setText("Student " + model.getCurrentuser().getUsername() + " has withdrawn from " + course.getCourseName());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            String upDown = "-";
                            try {
                                model.getCourseDOA().changeEnrollment(upDown, course.getCourse_id());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            btnEnrolledCourses.fire();
        });

        btnExportCourses.setOnAction(event -> {
            List<Course> courseList = new ArrayList<>();
            int student_id = model.getCurrentuser().getStudent_id();
            try {
                model.getCourseDOA().getEnrolledCourseList(courseList, student_id);
                tblCourses.getItems().clear();
                tblCourses.getItems().addAll(courseList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/exports/enrolledCourses.txt", false))) {
                for (Course course : courseList) {
                    String courseData = String.join(",", Integer.toString(course.getCourse_id()), course.getCourseName(), Integer.toString(course.getCapacity()), course.getYear(), course.getDeliveryMethod(), course.getDayOfLecture(), course.getTimeOfLecture(), Double.toString(course.getDurationOfLecture()));
                    writer.write(courseData);
                    writer.newLine();
                    lblPlaceholder.setText("Enrolled courses exported to text file");
                }
            } catch (IOException e) {
                lblPlaceholder.setText("No valid file path to export");
            }
        });

        btnEnroll.setOnAction(event -> {
            int student_id = model.getCurrentuser().getStudent_id();
            for (Course course : tblCourses.getItems()) {
                int course_id = 0;
                BooleanProperty selectedProperty = course.isSelected();
                if (selectedProperty != null && selectedProperty.get()) {
                    course_id = course.getCourse_id();
                    String newCourseDayOfLecture = course.getDayOfLecture();
                    String newCourseTimeOfLecture = course.getTimeOfLecture();
                    double newCourseDuration = course.getDurationOfLecture();
                    try {
                        if (model.getUserDoa().checkClash(student_id, course_id, newCourseDayOfLecture, newCourseTimeOfLecture, newCourseDuration)) {
                            lblPlaceholder.setText("Student " + model.getCurrentuser().getUsername() + " cannot enroll in " + course.getCourseName());
                            break;
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
        lblStudentId.setText("Student_id: " + Integer.toString(model.getCurrentuser().getStudent_id()));
        lblName.setText("Name: " + model.getCurrentuser().getFirstName() + " " + model.getCurrentuser().getLastName());
    }
}
