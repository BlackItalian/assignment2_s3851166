package main.java.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Course {

    private int course_id;
    private String courseName;
    private int capacity;
    private String year;
    private String deliveryMethod;
    private String dayOfLecture;
    private String timeOfLecture;
    private Double durationOfLecture;
    private int enrolled;
    private BooleanProperty selected;

    public Course() {
    }

    public Course(int course_id, String courseName, int capacity, String year, String deliveryMethod, String dayOfLecture, String timeOfLecture, Double durationOfLecture, int enrolled) {
        this.course_id = course_id;
        this.courseName = courseName;
        this.capacity = capacity;
        this.year = year;
        this.deliveryMethod = deliveryMethod;
        this.dayOfLecture = dayOfLecture;
        this.timeOfLecture = timeOfLecture;
        this.durationOfLecture = durationOfLecture;
        this.enrolled = enrolled;
        this.selected = new SimpleBooleanProperty(false);
    }

    @Override
    public String toString() {
        return course_id + courseName + capacity + deliveryMethod + dayOfLecture + timeOfLecture + durationOfLecture + enrolled;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getDayOfLecture() {
        return dayOfLecture;
    }

    public void setDayOfLecture(String dayOfLecture) {
        this.dayOfLecture = dayOfLecture;
    }

    public String getTimeOfLecture() {
        return timeOfLecture;
    }

    public void setTimeOfLecture(String timeOfLecture) {
        this.timeOfLecture = timeOfLecture;
    }

    public Double getDurationOfLecture() {
        return durationOfLecture;
    }

    public void setDurationOfLecture(Double durationOfLecture) {
        this.durationOfLecture = durationOfLecture;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public BooleanProperty isSelected() {
        return selected;
    }

    public void setSelected(BooleanProperty selected) {
        this.selected = selected;
    }
}
