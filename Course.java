package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String id;
    private String name;
    private String description;
    private String teacherId;
    private String teacherName;
    private String time;
    private String location;
    private LocalDateTime selectionStartTime;
    private LocalDateTime selectionEndTime;
    private int maxStudents;

    public Course(String id, String name, String description, String teacherId,
                  String teacherName, String time, String location,
                  LocalDateTime selectionStartTime, LocalDateTime selectionEndTime,
                  int maxStudents) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.time = time;
        this.location = location;
        this.selectionStartTime = selectionStartTime;
        this.selectionEndTime = selectionEndTime;
        this.maxStudents = maxStudents;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTeacherId() { return teacherId; }
    public String getTeacherName() { return teacherName; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public LocalDateTime getSelectionStartTime() { return selectionStartTime; }
    public LocalDateTime getSelectionEndTime() { return selectionEndTime; }
    public int getMaxStudents() { return maxStudents; }

    public void setDescription(String description) { this.description = description; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }

    public boolean isEnrollmentOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(selectionStartTime) && now.isBefore(selectionEndTime);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", maxStudents=" + maxStudents +
                '}';
    }
}