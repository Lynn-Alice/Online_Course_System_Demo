package ui;

import model.Course;
import model.Student;
import service.CourseService;
import service.EnrollmentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseSelectionPanel extends JPanel {
    private Student student;
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton enrollButton;

    public CourseSelectionPanel(Student student) {
        this.student = student;
        initComponents();
        loadCourses();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 240, 240));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel titleLabel = new JLabel("可选课程列表", SwingConstants.LEFT);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        refreshButton = new JButton("刷新");
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
        refreshButton.addActionListener(e -> loadCourses());
        titlePanel.add(refreshButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        String[] columns = {"课程ID", "课程名称", "授课教师", "时间", "地点", "选课时间", "已选/上限", "状态"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coursesTable = new JTable(tableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.setRowHeight(30);
        coursesTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        coursesTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        coursesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        coursesTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        coursesTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        coursesTable.getColumnModel().getColumn(5).setPreferredWidth(200);
        coursesTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        coursesTable.getColumnModel().getColumn(7).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("课程信息"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        enrollButton = new JButton("选课");
        enrollButton.setBackground(new Color(40, 167, 69));
        enrollButton.setForeground(Color.WHITE);
        enrollButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        enrollButton.setFocusPainted(false);
        enrollButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        enrollButton.setOpaque(true);
        enrollButton.setContentAreaFilled(true);
        enrollButton.addActionListener(e -> enrollCourse());

        JButton viewDetailsButton = new JButton("查看详情");
        viewDetailsButton.setBackground(new Color(0, 102, 204));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        viewDetailsButton.setOpaque(true);
        viewDetailsButton.setContentAreaFilled(true);
        viewDetailsButton.addActionListener(e -> viewCourseDetails());

        buttonPanel.add(enrollButton);
        buttonPanel.add(viewDetailsButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> allCourses = CourseService.getInstance().getAllCourses();

        for (Course course : allCourses) {
            boolean isEnrolled = EnrollmentService.getInstance().isStudentEnrolled(student.getId(), course.getId());

            if (!isEnrolled) {
                String status;
                if (!course.isEnrollmentOpen()) {
                    status = "选课时间外";
                } else if (EnrollmentService.getInstance().getEnrolledStudentsCount(course.getId()) >= course.getMaxStudents()) {
                    status = "人数已满";
                } else {
                    status = "可选";
                }

                tableModel.addRow(new Object[]{
                        course.getId(),
                        course.getName(),
                        course.getTeacherName(),
                        course.getTime(),
                        course.getLocation(),
                        formatSelectionTime(course),
                        getEnrollmentStatus(course),
                        status
                });
            }
        }

        coursesTable.repaint();
    }

    private String formatSelectionTime(Course course) {
        return course.getSelectionStartTime().toLocalDate() + " 至 " +
                course.getSelectionEndTime().toLocalDate();
    }

    private String getEnrollmentStatus(Course course) {
        int current = EnrollmentService.getInstance().getEnrolledStudentsCount(course.getId());
        return current + "/" + course.getMaxStudents();
    }

    private void enrollCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要选的课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        Course course = CourseService.getInstance().getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "课程不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!course.isEnrollmentOpen()) {
            JOptionPane.showMessageDialog(this,
                    "选课时间已过，无法选课",
                    "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (EnrollmentService.getInstance().getEnrolledStudentsCount(courseId) >= course.getMaxStudents()) {
            JOptionPane.showMessageDialog(this,
                    "该课程人数已满，无法选课",
                    "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String message = "课程详情：\n" +
                "课程名称：" + course.getName() + "\n" +
                "授课教师：" + course.getTeacherName() + "\n" +
                "上课时间：" + course.getTime() + "\n" +
                "上课地点：" + course.getLocation() + "\n" +
                "课程简介：" + (course.getDescription().length() > 50 ?
                course.getDescription().substring(0, 50) + "..." :
                course.getDescription()) + "\n\n" +
                "确定要选这门课程吗？";

        int confirm = JOptionPane.showConfirmDialog(this,
                message,
                "确认选课", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = EnrollmentService.getInstance().enrollStudent(student.getId(), courseId);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "选课成功！",
                        "成功", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this,
                        "选课失败，请重试",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewCourseDetails() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要查看的课程", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        Course course = CourseService.getInstance().getCourseById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(this, "课程不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog detailsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "课程详情", true);
        detailsDialog.setLayout(new BorderLayout(10, 10));
        detailsDialog.setSize(400, 350);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addDetailRow(infoPanel, gbc, 0, "课程ID：", course.getId());
        addDetailRow(infoPanel, gbc, 1, "课程名称：", course.getName());
        addDetailRow(infoPanel, gbc, 2, "授课教师：", course.getTeacherName());
        addDetailRow(infoPanel, gbc, 3, "上课时间：", course.getTime());
        addDetailRow(infoPanel, gbc, 4, "上课地点：", course.getLocation());
        addDetailRow(infoPanel, gbc, 5, "选课时间：",
                course.getSelectionStartTime().toLocalDate() + " 至 " +
                        course.getSelectionEndTime().toLocalDate());
        addDetailRow(infoPanel, gbc, 6, "课程容量：",
                EnrollmentService.getInstance().getEnrolledStudentsCount(courseId) +
                        "/" + course.getMaxStudents());

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        infoPanel.add(new JLabel("课程简介："), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextArea descriptionArea = new JTextArea(course.getDescription(), 5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(infoPanel.getBackground());
        infoPanel.add(new JScrollPane(descriptionArea), gbc);

        detailsDialog.add(infoPanel, BorderLayout.CENTER);

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
        closeButton.addActionListener(e -> detailsDialog.dispose());
        buttonPanel.add(closeButton);

        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(value), gbc);
    }

    public void setStudent(Student student) {
        this.student = student;
        loadCourses();
    }

    public void refresh() {
        loadCourses();
    }
}