package model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private List<String> teachingCourseIds;

    public Teacher(String id, String username, String password, String name) {
        super(id, username, password, name);
        this.teachingCourseIds = new ArrayList<>();
    }

    public List<String> getTeachingCourseIds() {
        return teachingCourseIds;
    }

    public void addTeachingCourse(String courseId) {
        if (!teachingCourseIds.contains(courseId)) {
            teachingCourseIds.add(courseId);
        }
    }

    public void removeTeachingCourse(String courseId) {
        teachingCourseIds.remove(courseId);
    }
}