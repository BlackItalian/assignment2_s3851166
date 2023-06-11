package main.java.database;

import main.java.model.User;

import java.sql.SQLException;

public interface UserDoa {
    void setupDatabase() throws SQLException;

    // TO DELETE AT END
    void deleteValues() throws SQLException;

    User createUser(String username, String password, String firstName, String lastName)
            throws SQLException;

    User editUser(String username, String password, String firstName, String lastName, int student_id)
            throws SQLException;

    User getUser(String username, String password) throws SQLException;

    boolean isEnrolled(int student_id, int course_id) throws SQLException;

    boolean checkClash(int student_id, int course_id, String newCourseDayOfLecture, String newCourseTimeOfLecture, Double newCourseDuration) throws SQLException;


    void enrollInCourse(int student_id, int course_id) throws SQLException;

    void withdrawFromCourse(int student_id, int course_id) throws SQLException;


}
