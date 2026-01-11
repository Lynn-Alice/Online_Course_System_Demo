package service;

import model.Student;
import model.Teacher;
import model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static UserService instance;
    private Map<String, User> users;

    private UserService() {
        users = new HashMap<>();
        initializeUsers();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private void initializeUsers() {
        //Example Users
        Student student1 = new Student("S001", "student1", "123456", "赵欣欣");
        Student student2 = new Student("S002", "student2", "123456", "陈晨晨");

        Teacher teacher1 = new Teacher("T001", "teacher1", "123456", "孙萌萌");
        Teacher teacher2 = new Teacher("T002", "teacher2", "123456", "姚依依");

        users.put(student1.getUsername(), student1);
        users.put(student2.getUsername(), student2);
        users.put(teacher1.getUsername(), teacher1);
        users.put(teacher2.getUsername(), teacher2);
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserById(String id) {
        for (User user : users.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
}