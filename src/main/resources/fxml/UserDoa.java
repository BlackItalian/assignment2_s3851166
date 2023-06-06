package main.java.database;

import main.java.model.User;

import java.sql.SQLException;

public interface UserDoa {
    void setupDatabase() throws SQLException;
    void populateCourseTable() throws SQLException;

    // TO DELETE AT END
    void deleteValues() throws SQLException;

    User createUser(String username, String password, String firstName, String lastName)
            throws SQLException;

    User editUser(String password, String firstName, String lastName, int student_id)
            throws SQLException;

    User getUser(String username, String password) throws SQLException;


}
