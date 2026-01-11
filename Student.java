package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String> enrolledCourseIds;

    public Student(String id, String username, String password, String name) {
        super(id, username, password, name);
        this.enrolledCourseIds = new ArrayList<>();
    }

    public List<String> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public void enrollCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    public void dropCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + getId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", name='" + getName() + '\'' +
                ", enrolledCourseIds=" + enrolledCourseIds +
                '}';
    }
}