package main.java.database;

import main.java.model.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDOA {

    void populateCourseTable() throws SQLException;

    Course getFullCourseList(List<Course> courseList) throws SQLException;

    Course getSearchCourseList(List<Course> courseList, String search) throws SQLException;

    Course changeEnrollment(String upDown, int course_id) throws SQLException;

}
