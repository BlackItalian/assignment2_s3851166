package main.java.model;

import main.java.database.CourseDOA;
import main.java.database.CourseDOAImplementation;
import main.java.database.UserDOAImplementation;
import main.java.database.UserDoa;

import java.sql.SQLException;

public class Model {

    private UserDoa userDoa;
    private CourseDOA courseDOA;
    private User currentuser;

    public Model() {
        userDoa = new UserDOAImplementation();
        courseDOA = new CourseDOAImplementation();
    }

    public void setupDatabase() throws SQLException {
        userDoa.setupDatabase();
        courseDOA.populateCourseTable();
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

    public CourseDOA getCourseDOA() {
        return courseDOA;
    }


    // TO DELET AT END
    public void deleteValues() throws SQLException {
        userDoa.deleteValues();
    }
    // TO DELET AT END
}