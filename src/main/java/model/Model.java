package main.java.model;

import java.sql.SQLException;

public class Model {

    private UserDoa userDoa;
    private User currentuser;

    public Model() {
        userDoa = new UserDOAImplementation();
    }

    public void setupDatabase() throws SQLException {
        userDoa.setupDatabase();
        userDoa.populateCourseTable();
    }

    public UserDoa getUserDoa() {
        return userDoa;
    }

    public User getCurrentuser() {
        return this.currentuser;
    }

    public void setCurrentuser(User user) {
        currentuser = user;
    }

    // TO DELET AT END
    public void deleteValues() throws SQLException {
        userDoa.deleteValues();
    }
    // TO DELET AT END
}