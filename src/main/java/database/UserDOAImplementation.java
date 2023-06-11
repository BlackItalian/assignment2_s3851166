package main.java.database;

import main.java.model.User;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UserDOAImplementation implements UserDoa {

    private final String TABLE_NAME_USERS = "Users";
    private final String TABLE_NAME_COURSES = "Courses";
    private final String TABLE_NAME_ENROLLEDCOURSES = "Enrolled";

    public UserDOAImplementation() {
    }

    @Override
    public void setupDatabase() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {

            //Create table to store user accounts

            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USERS + " (student_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username VARCHAR(10) NOT NULL," + "password VARCHAR(8) NOT NULL," + "fname VARCHAR(10) NOT NULL,"
                    + "lname VARCHAR(10) NOT NULL)";

            //Create table to store course data

            String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_COURSES + " (course_id INTEGER PRIMARY KEY AUTOINCREMENT," + "coursename VARCHAR(30) NOT NULL,"
                    + "capacity INTEGER DEFAULT NULL," + "year VARCHAR(8) NOT NULL," + "delivery VARCHAR(14) NOT NULL,"
                    + "dayoflecture VARCHAR(12) NOT NULL," + "timeoflecture TEXT NOT NULL,"
                    + "durationoflecture DOUBLE NOT NULL," + "enrolled INTEGER DEFAULT 0)";

            //Create table for storing users enrolled courses

            String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ENROLLEDCOURSES + " (student_id INT NOT NULL,"
                    + "course_id INT NOT NULL," + "PRIMARY KEY (student_id, course_id),"
                    + "FOREIGN KEY (student_id) REFERENCES Users(student_id),"
                    + "FOREIGN KEY (course_id) REFERENCES Courses(course_id))";

            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
        }
    }


    // TO DELETE AT END

    public void deleteValues() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            String sql = "DELETE FROM " + TABLE_NAME_ENROLLEDCOURSES;
            stmt.executeUpdate(sql);
        }
    }
    // TO DELET AT END

    @Override
    public User createUser(String username, String password, String firstName, String lastName)
            throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME_USERS + " (username, password, fname, lname) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int student_id = 0;
            if (rs.next()) {
                student_id = rs.getInt(1);
            }
            return new User(username, password, firstName, lastName, student_id);
        }
    }

    @Override
    public User editUser(String username, String password, String firstName, String lastName, int student_id)
            throws SQLException {
        String sql = "UPDATE " + TABLE_NAME_USERS + " SET fname = ?, lname = ?, password = ? WHERE student_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, password);
            stmt.setInt(4, student_id);
            stmt.executeUpdate();
            return new User(username, password, firstName, lastName, student_id);
        }
    }

    @Override
    public User getUser(String username, String password) throws SQLException {

        String sql = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE username = ? AND password = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("fname"));
                    user.setLastName(rs.getString("lname"));
                    user.setStudent_id(rs.getInt("student_id"));
                    return user;
                }
                return null;
            }
        }
    }

    public boolean checkClash(int student_id, int course_id, String newCourseDayOfLecture, String newCourseTimeOfLecture, Double newCourseDuration) {
        String sql = "SELECT c.* FROM " + TABLE_NAME_COURSES + " c " +
                "JOIN " + TABLE_NAME_ENROLLEDCOURSES + " ec ON c.course_id = ec.course_id " +
                "WHERE ec.student_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, student_id);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                // Check for time clash
                String existingDayOfLecture = resultSet.getString("dayoflecture");
                String existingTimeOfLecture = resultSet.getString("timeoflecture");
                double existingDurationOfLecture = resultSet.getDouble("durationoflecture");

                // Calculate the end time of the existing lecture
                LocalTime existingEndTime = LocalTime.parse(existingTimeOfLecture, formatter).plusMinutes((long) (existingDurationOfLecture * 60));

                // Parse the new course's time and calculate the end time
                LocalTime newCourseStartTime = LocalTime.parse(newCourseTimeOfLecture, formatter);
                LocalTime newCourseEndTime = newCourseStartTime.plusMinutes((long) (newCourseDuration * 60));

                // Check for day clash
                if (existingDayOfLecture.equals(newCourseDayOfLecture)) {
                    // Check for time overlap
                    if ((newCourseStartTime.isBefore(existingEndTime) && newCourseEndTime.isAfter(LocalTime.parse(existingTimeOfLecture, formatter))) ||
                            (newCourseStartTime.equals(existingEndTime) || newCourseEndTime.equals(existingTimeOfLecture))) {
                        return true; // Time clash detected, student is enrolled in a course with overlapping time
                    }
                }
            }

            return false; // No time clash found, student is not enrolled in any conflicting course
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEnrolled(int student_id, int course_id) {
        String sql = "SELECT * FROM " + TABLE_NAME_ENROLLEDCOURSES + " WHERE student_id = ? AND course_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, student_id);
            stmt.setInt(2, course_id);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void enrollInCourse(int student_id, int course_id) {
        String sql = "INSERT INTO " + TABLE_NAME_ENROLLEDCOURSES + " (student_id, course_id) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, student_id);
            statement.setInt(2, course_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdrawFromCourse(int student_id, int course_id) {
        String sql = "DELETE FROM " + TABLE_NAME_ENROLLEDCOURSES + " WHERE student_id = ? AND course_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, student_id);
            statement.setInt(2, course_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
