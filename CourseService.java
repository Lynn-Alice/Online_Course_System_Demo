package service;

import model.Course;
import model.Teacher;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseService {
    private static CourseService instance;
    private Map<String, Course> courses;

    private CourseService() {
        courses = new HashMap<>();
        initializeCourses();
    }

    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }

    private void initializeCourses() {
        //Example Courses
        LocalDateTime now = LocalDateTime.now();

        Course course1 = new Course(
                "C001", "Java程序设计基础", "学习Java编程基础",
                "T001", "孙萌萌", "周一 8:00-10:00", "致远楼A101",
                now.minusDays(1), now.plusDays(30), 50
        );

        Course course2 = new Course(
                "C002", "数据结构", "学习基本数据结构",
                "T002", "姚依依", "周二 10:00-12:00", "笃实楼B201",
                now.minusDays(2), now.plusDays(28), 40
        );

        Course course3 = new Course(
                "C003", "数据库原理", "学习数据库设计与应用",
                "T001", "孙萌萌", "周三 14:00-16:00", "鸿博楼C301",
                now.minusDays(3), now.plusDays(25), 60
        );

        courses.put(course1.getId(), course1);
        courses.put(course2.getId(), course2);
        courses.put(course3.getId(), course3);

        Teacher teacher1 = (Teacher) UserService.getInstance().getUserById("T001");
        Teacher teacher2 = (Teacher) UserService.getInstance().getUserById("T002");

        teacher1.addTeachingCourse("C001");
        teacher1.addTeachingCourse("C003");
        teacher2.addTeachingCourse("C002");
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public Course getCourseById(String id) {
        return courses.get(id);
    }

    public List<Course> getCoursesByTeacher(String teacherId) {
        List<Course> teacherCourses = new ArrayList<>();
        for (Course course : courses.values()) {
            if (course.getTeacherId().equals(teacherId)) {
                teacherCourses.add(course);
            }
        }
        return teacherCourses;
    }

    public void addCourse(Course course) {
        courses.put(course.getId(), course);
        Teacher teacher = (Teacher) UserService.getInstance().getUserById(course.getTeacherId());
        if (teacher != null) {
            teacher.addTeachingCourse(course.getId());
        }
    }

    public void updateCourse(Course course) {
        courses.put(course.getId(), course);
    }

    public void deleteCourse(String courseId) {
        Course course = courses.remove(courseId);
        if (course != null) {
            Teacher teacher = (Teacher) UserService.getInstance().getUserById(course.getTeacherId());
            if (teacher != null) {
                teacher.removeTeachingCourse(courseId);
            }
        }
    }
}