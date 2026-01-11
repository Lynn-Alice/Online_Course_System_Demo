package ui;

import model.Course;
import model.Teacher;
import service.CourseService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CourseManagementDialog extends JDialog {
    private Course course;
    private Teacher teacher;
    private boolean isEditMode;

    private JTextField idField;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField timeField;
    private JTextField locationField;
    private JSpinner startDateSpinner;
    private JSpinner startTimeSpinner;
    private JSpinner endDateSpinner;
    private JSpinner endTimeSpinner;
    private JSpinner maxStudentsSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    public CourseManagementDialog(JFrame parent, Teacher teacher) {
        this(parent, teacher, null, false);
    }

    public CourseManagementDialog(JFrame parent, Teacher teacher, Course course, boolean isEditMode) {
        super(parent, isEditMode ? "编辑课程" : "添加课程", true);
        this.teacher = teacher;
        this.course = course;
        this.isEditMode = isEditMode;

        initComponents();
        setupWindow();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysLater = now.plusDays(30);

        if (isEditMode && course != null) {
            idField = new JTextField(course.getId(), 20);
            idField.setEditable(false);
            nameField = new JTextField(course.getName(), 20);
            descriptionArea = new JTextArea(course.getDescription(), 3, 20);
            timeField = new JTextField(course.getTime(), 20);
            locationField = new JTextField(course.getLocation(), 20);

            LocalDateTime startTime = course.getSelectionStartTime();
            startDateSpinner = createDateSpinner(startTime.toLocalDate());
            startTimeSpinner = createTimeSpinner(startTime.toLocalTime());

            LocalDateTime endTime = course.getSelectionEndTime();
            endDateSpinner = createDateSpinner(endTime.toLocalDate());
            endTimeSpinner = createTimeSpinner(endTime.toLocalTime());

            maxStudentsSpinner = new JSpinner(new SpinnerNumberModel(course.getMaxStudents(), 1, 200, 1));
        } else {
            idField = new JTextField(20);
            nameField = new JTextField(20);
            descriptionArea = new JTextArea(3, 20);
            timeField = new JTextField(20);
            locationField = new JTextField(20);

            startDateSpinner = createDateSpinner(now.toLocalDate());
            startTimeSpinner = createTimeSpinner(now.toLocalTime());

            endDateSpinner = createDateSpinner(thirtyDaysLater.toLocalDate());
            endTimeSpinner = createTimeSpinner(now.toLocalTime());

            maxStudentsSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 200, 1));
        }

        Font fieldFont = new Font("微软雅黑", Font.PLAIN, 12);
        idField.setFont(fieldFont);
        nameField.setFont(fieldFont);
        descriptionArea.setFont(fieldFont);
        timeField.setFont(fieldFont);
        locationField.setFont(fieldFont);

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel idLabel = new JLabel("课程ID:");
        idLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(idLabel, gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel nameLabel = new JLabel("课程名称:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel descLabel = new JLabel("课程简介:");
        descLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(descLabel, gbc);
        gbc.gridx = 1;
        add(descriptionScroll, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel timeLabel = new JLabel("上课时间:");
        timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(timeLabel, gbc);
        gbc.gridx = 1;
        add(timeField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel locationLabel = new JLabel("上课地点:");
        locationLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(locationLabel, gbc);
        gbc.gridx = 1;
        add(locationField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel startLabel = new JLabel("选课开始时间:");
        startLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(startLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        JPanel startTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        startTimePanel.setBackground(Color.WHITE);
        startTimePanel.add(startDateSpinner);
        startTimePanel.add(new JLabel(" "));
        startTimePanel.add(startTimeSpinner);
        add(startTimePanel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel endLabel = new JLabel("选课结束时间:");
        endLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(endLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        JPanel endTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        endTimePanel.setBackground(Color.WHITE);
        endTimePanel.add(endDateSpinner);
        endTimePanel.add(new JLabel(" "));
        endTimePanel.add(endTimeSpinner);
        add(endTimePanel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel maxLabel = new JLabel("最大人数:");
        maxLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        add(maxLabel, gbc);
        gbc.gridx = 1;
        add(maxStudentsSpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        saveButton = new JButton("保存");
        cancelButton = new JButton("取消");

        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        saveButton.setOpaque(true);
        saveButton.setContentAreaFilled(true);

        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(new Color(0, 102, 204));
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 12));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);

        saveButton.addActionListener(e -> saveCourse());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private void setupWindow() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        getRootPane().setDefaultButton(saveButton);
    }

    private JSpinner createDateSpinner(java.time.LocalDate date) {
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);

        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        dateModel.setValue(cal.getTime());

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, DATE_FORMAT);
        spinner.setEditor(dateEditor);
        spinner.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        spinner.setPreferredSize(new Dimension(120, 25));

        return spinner;
    }

    private JSpinner createTimeSpinner(java.time.LocalTime time) {
        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(timeModel);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, time.getHour());
        cal.set(Calendar.MINUTE, time.getMinute());
        cal.set(Calendar.SECOND, 0);
        timeModel.setValue(cal.getTime());

        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinner, TIME_FORMAT);
        spinner.setEditor(timeEditor);
        spinner.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        spinner.setPreferredSize(new Dimension(80, 25));

        return spinner;
    }

    private LocalDateTime getDateTimeFromSpinners(JSpinner dateSpinner, JSpinner timeSpinner) {
        try {
            Date dateValue = (Date) dateSpinner.getValue();
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(dateValue);

            Date timeValue = (Date) timeSpinner.getValue();
            Calendar timeCal = Calendar.getInstance();
            timeCal.setTime(timeValue);

            dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
            dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
            dateCal.set(Calendar.SECOND, 0);

            return LocalDateTime.ofInstant(dateCal.toInstant(), dateCal.getTimeZone().toZoneId());
        } catch (Exception e) {
            e.printStackTrace();
            return LocalDateTime.now();
        }
    }

    private void saveCourse() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String description = descriptionArea.getText().trim();
            String time = timeField.getText().trim();
            String location = locationField.getText().trim();
            int maxStudents = (Integer) maxStudentsSpinner.getValue();

            if (id.isEmpty() || name.isEmpty() || time.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写所有必填字段", "错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime startTime = getDateTimeFromSpinners(startDateSpinner, startTimeSpinner);
            LocalDateTime endTime = getDateTimeFromSpinners(endDateSpinner, endTimeSpinner);

            if (endTime.isBefore(startTime)) {
                JOptionPane.showMessageDialog(this,
                        "选课结束时间必须晚于开始时间", "错误",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isEditMode) {
                if (CourseService.getInstance().getCourseById(id) != null) {
                    JOptionPane.showMessageDialog(this, "课程ID已存在", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Course newCourse = new Course(id, name, description, teacher.getId(),
                        teacher.getName(), time, location, startTime, endTime, maxStudents);

                CourseService.getInstance().addCourse(newCourse);
                JOptionPane.showMessageDialog(this, "课程添加成功", "成功",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            else if (course != null) {
                if (!id.equals(course.getId())) {
                    if (CourseService.getInstance().getCourseById(id) != null) {
                        JOptionPane.showMessageDialog(this, "课程ID已存在", "错误",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Course updatedCourse = new Course(id, name, description, teacher.getId(),
                        teacher.getName(), time, location, startTime, endTime, maxStudents);

                CourseService.getInstance().deleteCourse(course.getId());
                CourseService.getInstance().addCourse(updatedCourse);

                JOptionPane.showMessageDialog(this, "课程更新成功", "成功",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存课程时发生错误: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Course showAddCourseDialog(JFrame parent, Teacher teacher) {
        CourseManagementDialog dialog = new CourseManagementDialog(parent, teacher);
        dialog.setVisible(true);
        return null;
    }

    public static Course showEditCourseDialog(JFrame parent, Teacher teacher, Course course) {
        CourseManagementDialog dialog = new CourseManagementDialog(parent, teacher, course, true);
        dialog.setVisible(true);
        return CourseService.getInstance().getCourseById(course.getId());
    }
}