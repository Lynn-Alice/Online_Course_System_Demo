package model;

import java.time.LocalDateTime;

public class Enrollment {
    private String studentId;
    private String courseId;
    private LocalDateTime enrollmentTime;

    public Enrollment(String studentId, String courseId, LocalDateTime enrollmentTime) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentTime = enrollmentTime;
    }

    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public LocalDateTime getEnrollmentTime() { return enrollmentTime; }

    public void setEnrollmentTime(LocalDateTime enrollmentTime) {
        this.enrollmentTime = enrollmentTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment that = (Enrollment) obj;
        return studentId.equals(that.studentId) && courseId.equals(that.courseId);
    }

    @Override
    public int hashCode() {
        return 31 * studentId.hashCode() + courseId.hashCode();
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", enrollmentTime=" + enrollmentTime +
                '}';
    }
}