package ui;

import model.Student;
import service.EnrollmentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentFrame extends JFrame {
    private Student student;
    private CourseSelectionPanel courseSelectionPanel;
    private JTabbedPane tabbedPane;

    public StudentFrame(Student student) {
        this.student = student;
        initComponents();
        setupWindow();
    }

    private void initComponents() {
        setTitle("学生选课系统 - " + student.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.BOLD, 12));

        courseSelectionPanel = new CourseSelectionPanel(student);
        tabbedPane.addTab("可选课程", courseSelectionPanel);

        JPanel enrolledCoursesPanel = createEnrolledCoursesPanel();
        tabbedPane.addTab("已选课程", enrolledCoursesPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("你好，" + student.getName() + "同学", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(0, 102, 204));

        JLabel statusLabel = new JLabel("已选课程数: " +
                EnrollmentService.getInstance().getStudentCourses(student.getId()).size(),
                SwingConstants.RIGHT);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.add(welcomeLabel);
        infoPanel.add(statusLabel);

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

    private JPanel createEnrolledCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columns = {"课程ID", "课程名称", "授课教师", "时间", "地点", "选课状态"};
        DefaultTableModel enrolledCoursesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable enrolledCoursesTable = new JTable(enrolledCoursesModel);
        enrolledCoursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledCoursesTable.setRowHeight(25);

        loadEnrolledCourses(enrolledCoursesModel);

        JScrollPane scrollPane = new JScrollPane(enrolledCoursesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("已选课程"));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
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
        refreshButton.addActionListener(e -> loadEnrolledCourses(enrolledCoursesModel));

        JButton dropButton = new JButton("退课");
        dropButton.setBackground(new Color(220, 53, 69));
        dropButton.setForeground(Color.WHITE);
        dropButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        dropButton.setFocusPainted(false);
        dropButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        dropButton.setOpaque(true);
        dropButton.setContentAreaFilled(true);
        dropButton.addActionListener(e -> dropCourse(enrolledCoursesTable, enrolledCoursesModel));

        buttonPanel.add(refreshButton);
        buttonPanel.add(dropButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadEnrolledCourses(DefaultTableModel model) {
        model.setRowCount(0);
        List<model.Course> enrolledCourses = EnrollmentService.getInstance().getStudentCourses(student.getId());

        for (model.Course course : enrolledCourses) {
            String status = course.isEnrollmentOpen() ? "已选" : "选课已结束";

            model.addRow(new Object[]{
                    course.getId(),
                    course.getName(),
                    course.getTeacherName(),
                    course.getTime(),
                    course.getLocation(),
                    status
            });
        }
    }

    private void dropCourse(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要退的课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) model.getValueAt(selectedRow, 0);
        model.Course course = service.CourseService.getInstance().getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "课程不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要退这门课程吗？\n课程名称：" + course.getName(),
                "确认退课", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = EnrollmentService.getInstance().dropStudent(student.getId(), courseId);
            if (success) {
                JOptionPane.showMessageDialog(this, "退课成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadEnrolledCourses(model);

                if (courseSelectionPanel != null) {
                    courseSelectionPanel.refresh();
                }
            } else {
                JOptionPane.showMessageDialog(this, "退课失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupWindow() {
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}