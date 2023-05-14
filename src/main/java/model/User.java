package main.java.model;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int student_id;

    public User() {
    }

    public User(String username, String password, String firstName, String lastName, int student_id) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.student_id = student_id;
    }

    @Override
    public String toString() {
        return firstName + lastName + password + username + student_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
}
