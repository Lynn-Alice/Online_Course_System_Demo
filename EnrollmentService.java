package service;

import model.Course;
import model.Enrollment;
import model.Student;
import model.Teacher;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentService {
    private static EnrollmentService instance;
    private Map<String, List<Enrollment>> studentEnrollments;
    private Map<String, List<Enrollment>> courseEnrollments;

    private EnrollmentService() {
        studentEnrollments = new HashMap<>();
        courseEnrollments = new HashMap<>();
    }

    public static EnrollmentService getInstance() {
        if (instance == null) {
            instance = new EnrollmentService();
        }
        return instance;
    }

    public boolean enrollStudent(String studentId, String courseId) {
        Course course = CourseService.getInstance().getCourseById(courseId);

        if (course == null || !course.isEnrollmentOpen()) {
            return false;
        }

        if (isStudentEnrolled(studentId, courseId)) {
            return false;
        }

        if (getEnrolledStudentsCount(courseId) >= course.getMaxStudents()) {
            return false;
        }

        Enrollment enrollment = new Enrollment(studentId, courseId, LocalDateTime.now());

        studentEnrollments.putIfAbsent(studentId, new ArrayList<>());
        studentEnrollments.get(studentId).add(enrollment);

        courseEnrollments.putIfAbsent(courseId, new ArrayList<>());
        courseEnrollments.get(courseId).add(enrollment);

        return true;
    }

    public boolean dropStudent(String studentId, String courseId) {
        List<Enrollment> studentEnrollList = studentEnrollments.get(studentId);
        if (studentEnrollList != null) {
            Enrollment toRemove = null;
            for (Enrollment enrollment : studentEnrollList) {
                if (enrollment.getCourseId().equals(courseId)) {
                    toRemove = enrollment;
                    break;
                }
            }
            if (toRemove != null) {
                studentEnrollList.remove(toRemove);

                List<Enrollment> courseEnrollList = courseEnrollments.get(courseId);
                if (courseEnrollList != null) {
                    courseEnrollList.remove(toRemove);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isStudentEnrolled(String studentId, String courseId) {
        List<Enrollment> enrollments = studentEnrollments.get(studentId);
        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseId().equals(courseId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getEnrolledStudentsCount(String courseId) {
        List<Enrollment> enrollments = courseEnrollments.get(courseId);
        return enrollments != null ? enrollments.size() : 0;
    }

    public List<Course> getStudentCourses(String studentId) {
        List<Course> courses = new ArrayList<>();
        List<Enrollment> enrollments = studentEnrollments.get(studentId);

        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                Course course = CourseService.getInstance().getCourseById(enrollment.getCourseId());
                if (course != null) {
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    public List<Student> getCourseStudents(String courseId) {
        List<Student> students = new ArrayList<>();
        List<Enrollment> enrollments = courseEnrollments.get(courseId);

        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                Student student = (Student) UserService.getInstance().getUserById(enrollment.getStudentId());
                if (student != null) {
                    students.add(student);
                }
            }
        }
        return students;
    }

    public List<Enrollment> getStudentEnrollments(String studentId) {
        return studentEnrollments.getOrDefault(studentId, new ArrayList<>());
    }

    public List<Enrollment> getCourseEnrollments(String courseId) {
        return courseEnrollments.getOrDefault(courseId, new ArrayList<>());
    }
}