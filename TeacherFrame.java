package ui;

import model.Course;
import model.Student;
import model.Teacher;
import service.CourseService;
import service.EnrollmentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherFrame extends JFrame {
    private Teacher teacher;
    private JTable coursesTable;
    private DefaultTableModel coursesModel;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton viewStudentsButton;

    public TeacherFrame(Teacher teacher) {
        this.teacher = teacher;
        initComponents();
        setupWindow();
        loadTeachingCourses();
    }

    private void initComponents() {
        setTitle("教师课程管理系统 - " + teacher.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("你好，" + teacher.getName() + "老师", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(0, 102, 204));

        JLabel teachingLabel = new JLabel("当前授课课程数: " +
                CourseService.getInstance().getCoursesByTeacher(teacher.getId()).size(),
                SwingConstants.RIGHT);
        teachingLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.add(welcomeLabel);
        infoPanel.add(teachingLabel);

        topPanel.add(infoPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("退出登录");
        logoutButton.setBackground(new Color(0, 102, 204));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        topPanel.add(logoutButton, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columns = {"课程ID", "课程名称", "时间", "地点", "选课时间", "已选/上限", "状态"};
        coursesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coursesTable = new JTable(coursesModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.setRowHeight(30);
        coursesTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        coursesTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        coursesTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        coursesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        coursesTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        coursesTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        coursesTable.getColumnModel().getColumn(6).setPreferredWidth(80);

        coursesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("教授课程列表"));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton refreshButton = new JButton("刷新");
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setForeground(new Color(0, 102, 204));
        refreshButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        refreshButton.setOpaque(true);
        refreshButton.setContentAreaFilled(true);
        refreshButton.addActionListener(e -> loadTeachingCourses());

        JButton addCourseButton = new JButton("添加课程");
        addCourseButton.setBackground(new Color(40, 167, 69));
        addCourseButton.setForeground(Color.WHITE);
        addCourseButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        addCourseButton.setFocusPainted(false);
        addCourseButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addCourseButton.setOpaque(true);
        addCourseButton.setContentAreaFilled(true);
        addCourseButton.addActionListener(e -> addCourse());

        editCourseButton = new JButton("<html><font color='white'>编辑课程</font></html>");
        editCourseButton.setBackground(new Color(0, 102, 204));
        editCourseButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        editCourseButton.setFocusPainted(false);
        editCourseButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        editCourseButton.setOpaque(true);
        editCourseButton.setContentAreaFilled(true);
        editCourseButton.addActionListener(e -> editCourse());
        editCourseButton.setEnabled(false);

        deleteCourseButton = new JButton("<html><font color='white'>删除课程</font></html>");
        deleteCourseButton.setBackground(new Color(220, 53, 69));
        deleteCourseButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        deleteCourseButton.setFocusPainted(false);
        deleteCourseButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        deleteCourseButton.setOpaque(true);
        deleteCourseButton.setContentAreaFilled(true);
        deleteCourseButton.addActionListener(e -> deleteCourse());
        deleteCourseButton.setEnabled(false);

        viewStudentsButton = new JButton("<html><font color='white'>查看学生名单</font></html>");
        viewStudentsButton.setBackground(new Color(23, 162, 184));
        viewStudentsButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        viewStudentsButton.setFocusPainted(false);
        viewStudentsButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        viewStudentsButton.setOpaque(true);
        viewStudentsButton.setContentAreaFilled(true);
        viewStudentsButton.addActionListener(e -> viewStudents());
        viewStudentsButton.setEnabled(false);

        buttonPanel.add(refreshButton);
        buttonPanel.add(addCourseButton);
        buttonPanel.add(editCourseButton);
        buttonPanel.add(deleteCourseButton);
        buttonPanel.add(viewStudentsButton);

        return buttonPanel;
    }

    private void updateButtonStates() {
        int selectedRow = coursesTable.getSelectedRow();
        boolean hasSelection = selectedRow != -1;

        editCourseButton.setEnabled(hasSelection);
        deleteCourseButton.setEnabled(hasSelection);
        viewStudentsButton.setEnabled(hasSelection);
    }

    private void setupWindow() {
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadTeachingCourses() {
        coursesModel.setRowCount(0);
        List<Course> teachingCourses =
                CourseService.getInstance().getCoursesByTeacher(teacher.getId());

        for (Course course : teachingCourses) {
            String status = course.isEnrollmentOpen() ? "开放中" : "已结束";
            int enrolledCount =
                    EnrollmentService.getInstance().getEnrolledStudentsCount(course.getId());

            coursesModel.addRow(new Object[]{
                    course.getId(),
                    course.getName(),
                    course.getTime(),
                    course.getLocation(),
                    formatSelectionTime(course),
                    enrolledCount + "/" + course.getMaxStudents(),
                    status
            });
        }

        updateButtonStates();
    }

    private String formatSelectionTime(Course course) {
        return course.getSelectionStartTime().toLocalDate() + " 至 " +
                course.getSelectionEndTime().toLocalDate();
    }

    private void addCourse() {
        CourseManagementDialog dialog = new CourseManagementDialog(this, teacher);
        dialog.setVisible(true);
        loadTeachingCourses();
    }

    private void editCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的课程", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) coursesModel.getValueAt(selectedRow, 0);
        Course course = CourseService.getInstance().getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "课程不存在", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        CourseManagementDialog dialog = new CourseManagementDialog(this, teacher, course, true);
        dialog.setVisible(true);
        loadTeachingCourses();
    }

    private void deleteCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的课程", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) coursesModel.getValueAt(selectedRow, 0);
        String courseName = (String) coursesModel.getValueAt(selectedRow, 1);
        Course course = CourseService.getInstance().getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "课程不存在", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int enrolledCount =
                EnrollmentService.getInstance().getEnrolledStudentsCount(courseId);

        String warningMessage;
        if (enrolledCount > 0) {
            warningMessage = "确定要删除这门课程吗？\n" +
                    "课程名称：" + courseName + "\n" +
                    "注意：当前有 " + enrolledCount + " 名学生已选此课程，删除将清除所有选课记录！";
        } else {
            warningMessage = "确定要删除这门课程吗？\n" +
                    "课程名称：" + courseName;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                warningMessage,
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                enrolledCount > 0 ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            CourseService.getInstance().deleteCourse(courseId);
            JOptionPane.showMessageDialog(this, "课程删除成功", "成功",
                    JOptionPane.INFORMATION_MESSAGE);
            loadTeachingCourses();
        }
    }

    private void viewStudents() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要查看的课程", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) coursesModel.getValueAt(selectedRow, 0);
        String courseName = (String) coursesModel.getValueAt(selectedRow, 1);
        Course course = CourseService.getInstance().getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "课程不存在", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Student> students =
                EnrollmentService.getInstance().getCourseStudents(courseId);

        JDialog studentDialog = new JDialog(this, "学生名单 - " + courseName, true);
        studentDialog.setLayout(new BorderLayout(10, 10));
        studentDialog.setSize(500, 400);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel courseInfoLabel = new JLabel("课程: " + courseName + " (" + courseId + ")", SwingConstants.CENTER);
        courseInfoLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));

        JLabel countLabel = new JLabel("已选人数: " + students.size() + " / " + course.getMaxStudents(), SwingConstants.CENTER);
        countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        infoPanel.add(courseInfoLabel);
        infoPanel.add(countLabel);

        studentDialog.add(infoPanel, BorderLayout.NORTH);

        String[] columns = {"序号", "学号", "姓名", "用户名"};
        DefaultTableModel studentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            studentModel.addRow(new Object[]{
                    i + 1,
                    student.getId(),
                    student.getName(),
                    student.getUsername()
            });
        }

        JTable studentTable = new JTable(studentModel);
        studentTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        studentDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton closeButton = new JButton("关闭");
        closeButton.setBackground(Color.WHITE);
        closeButton.setForeground(new Color(0, 102, 204));
        closeButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        closeButton.setOpaque(true);
        closeButton.setContentAreaFilled(true);
        closeButton.addActionListener(e -> studentDialog.dispose());
        buttonPanel.add(closeButton);

        studentDialog.add(buttonPanel, BorderLayout.SOUTH);

        studentDialog.setLocationRelativeTo(this);
        studentDialog.setVisible(true);
    }
}