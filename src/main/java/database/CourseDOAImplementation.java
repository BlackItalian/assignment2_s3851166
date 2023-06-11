package main.java.database;

import main.java.model.Course;
import main.java.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDOAImplementation implements CourseDOA {
    private final String TABLE_NAME_COURSES = "Courses";
    private final String TABLE_NAME_ENROLLEDCOURSES = "Enrolled";


    public CourseDOAImplementation() {
    }

    @Override
    public void populateCourseTable() throws SQLException {
        int defaultValue = 0;
        int lineNumber = 0;

        //check if table is populated
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + TABLE_NAME_COURSES);
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                if (rowCount > 0) {
                    System.out.println("Database is already populated.");
                    return;
                }
            }

            //populate table in not empty
            String sql = "INSERT INTO " + TABLE_NAME_COURSES + " (coursename, capacity, year, delivery, dayoflecture, timeoflecture, durationoflecture) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/csv/course.csv"));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (lineNumber <= 0) {
                        lineNumber++;
                        continue;
                    }
                    String[] values = line.split(",");
                    int intValue = defaultValue;

                    try {
                        intValue = Integer.parseInt(values[1]);
                    } catch (NumberFormatException e) {
                        if (!values[1].equals("N/A")) {
                            throw e;
                        }
                    }

                    pstmt.setString(1, values[0]);
                    pstmt.setInt(2, intValue);
                    pstmt.setString(3, values[2]);
                    pstmt.setString(4, values[3]);
                    pstmt.setString(5, values[4]);
                    pstmt.setString(6, values[5]);
                    pstmt.setDouble(7, Double.parseDouble(values[6]));
                    pstmt.executeUpdate();
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //Select * from course where student_id = currentuser.studentid in the mixed table

    @Override
    public Course getFullCourseList(List<Course> courseList) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME_COURSES;

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourse_id(rs.getInt("course_id"));
                    course.setCourseName(rs.getString("coursename"));
                    course.setCapacity(rs.getInt("capacity"));
                    course.setYear(rs.getString("year"));
                    course.setDeliveryMethod(rs.getString("delivery"));
                    course.setDayOfLecture(rs.getString("dayoflecture"));
                    course.setTimeOfLecture(rs.getString("timeoflecture"));
                    course.setDurationOfLecture(rs.getDouble("durationoflecture"));
                    course.setEnrolled(rs.getInt("enrolled"));
                    courseList.add(course);
                }
                rs.close();
                return null;
            }
        }
    }

    public Course getSearchCourseList(List<Course> courseList, String search) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME_COURSES + " WHERE coursename LIKE '%" + search + "%'";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourse_id(rs.getInt("course_id"));
                    course.setCourseName(rs.getString("coursename"));
                    course.setCapacity(rs.getInt("capacity"));
                    course.setYear(rs.getString("year"));
                    course.setDeliveryMethod(rs.getString("delivery"));
                    course.setDayOfLecture(rs.getString("dayoflecture"));
                    course.setTimeOfLecture(rs.getString("timeoflecture"));
                    course.setDurationOfLecture(rs.getDouble("durationoflecture"));
                    course.setEnrolled(rs.getInt("enrolled"));
                    courseList.add(course);
                }
                rs.close();
                return null;
            }
        }
    }

    public Course getEnrolledCourseList(List<Course> courseList, int student_id) throws SQLException {
        String sql = "SELECT c.* FROM " + TABLE_NAME_COURSES + " c " +
                "JOIN " + TABLE_NAME_ENROLLEDCOURSES + " ec ON c.course_id = ec.course_id " +
                "WHERE ec.student_id = " + student_id;

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourse_id(rs.getInt("course_id"));
                    course.setCourseName(rs.getString("coursename"));
                    course.setCapacity(rs.getInt("capacity"));
                    course.setYear(rs.getString("year"));
                    course.setDeliveryMethod(rs.getString("delivery"));
                    course.setDayOfLecture(rs.getString("dayoflecture"));
                    course.setTimeOfLecture(rs.getString("timeoflecture"));
                    course.setDurationOfLecture(rs.getDouble("durationoflecture"));
                    course.setEnrolled(rs.getInt("enrolled"));
                    courseList.add(course);
                }
                rs.close();
                return null;
            }
        }
    }

    public Course changeEnrollment(String upDown, int course_id) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME_COURSES + " SET enrolled = enrolled " + upDown + " 1 WHERE course_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, course_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
