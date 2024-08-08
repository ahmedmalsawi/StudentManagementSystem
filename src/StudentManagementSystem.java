import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {

    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();

    private JComboBox<Student> assignGradeStudentComboBox;
    private JTable viewStudentsTable;
    private JTable viewCoursesTable;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadSampleData(); // Load some sample data
    }

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();

        // Student Management Menu
        JMenu studentMenu = new JMenu("Student Management");
        studentMenu.add(createMenuItem("Add Student", e -> showAddStudentDialog()));
        studentMenu.add(createMenuItem("Update Student", e -> showUpdateStudentDialog()));
        studentMenu.add(createMenuItem("View Students", e -> showViewStudentsTab()));

        // Course Management Menu
        JMenu courseMenu = new JMenu("Course Management");
        courseMenu.add(createMenuItem("Add Course", e -> showAddCourseDialog()));
        courseMenu.add(createMenuItem("Update Course", e -> showUpdateCourseDialog()));
        courseMenu.add(createMenuItem("View Courses", e -> showViewCoursesTab()));

        // Enrollment Management Menu
        JMenu enrollmentMenu = new JMenu("Enrollment Management");
        enrollmentMenu.add(createMenuItem("Enroll in Course", e -> showEnrollInCourseDialog()));
        enrollmentMenu.add(createMenuItem("Assign Grades", e -> showAssignGradesDialog()));
        enrollmentMenu.add(createMenuItem("View Enrolled Courses", e -> showViewEnrolledCourses()));

        menuBar.add(studentMenu);
        menuBar.add(courseMenu);
        menuBar.add(enrollmentMenu);

        setJMenuBar(menuBar);

        showViewStudentsTab(); // Default view
    }

    private JMenuItem createMenuItem(String title, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    private void showAddStudentDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);

        JLabel ageLabel = new JLabel("Age:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(ageLabel, gbc);

        JTextField ageField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(ageField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            addStudent(nameField.getText(), ageField.getText());
            refreshAllTabs();
        }
    }

    private void showUpdateStudentDialog() {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students available to update.");
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel selectLabel = new JLabel("Select Student:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(selectLabel, gbc);

        JComboBox<Student> updateStudentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(updateStudentComboBox, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        JLabel ageLabel = new JLabel("Age:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(ageLabel, gbc);

        JTextField ageField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(ageField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Student selectedStudent = (Student) updateStudentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                updateStudent(selectedStudent, nameField.getText(), ageField.getText());
                refreshAllTabs();
            }
        }
    }

    private void showViewStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Age", "Delete"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        viewStudentsTable = new JTable(model);

        viewStudentsTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        viewStudentsTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(viewStudentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshViewStudentsTable();

        setContentPane(panel);
        validate();
    }

    private void showAddCourseDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Course Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            addCourse(nameField.getText());
            refreshAllTabs();
        }
    }

    private void showUpdateCourseDialog() {
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses available to update.");
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel selectLabel = new JLabel("Select Course:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(selectLabel, gbc);

        JComboBox<Course> updateCourseComboBox = new JComboBox<>(courses.toArray(new Course[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(updateCourseComboBox, gbc);

        JLabel nameLabel = new JLabel("Course Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Course selectedCourse = (Course) updateCourseComboBox.getSelectedItem();
            if (selectedCourse != null) {
                updateCourse(selectedCourse, nameField.getText());
                refreshAllTabs();
            }
        }
    }

    private void showViewCoursesTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Delete"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        viewCoursesTable = new JTable(model);

        viewCoursesTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        viewCoursesTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(viewCoursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshViewCoursesTable();

        setContentPane(panel);
        validate();
    }

    private void showEnrollInCourseDialog() {
        if (students.isEmpty() || courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students or courses available for enrollment.");
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel courseLabel = new JLabel("Select Course:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(courseLabel, gbc);

        JComboBox<Course> courseComboBox = new JComboBox<>(courses.toArray(new Course[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(courseComboBox, gbc);

        JLabel studentLabel = new JLabel("Select Student:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(studentLabel, gbc);

        JComboBox<Student> enrollStudentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(enrollStudentComboBox, gbc);

        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(e -> {
            enrollStudentInCourse((Course) courseComboBox.getSelectedItem(), (Student) enrollStudentComboBox.getSelectedItem());
            refreshAllTabs();
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(enrollButton, gbc);

        setContentPane(panel);
        validate();
    }

    private void showAssignGradesDialog() {
        if (students.isEmpty() || courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students or courses available for grade assignment.");
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentLabel = new JLabel("Select Student:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(studentLabel, gbc);

        assignGradeStudentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(assignGradeStudentComboBox, gbc);

        JLabel courseLabel = new JLabel("Select Course:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(courseLabel, gbc);

        JComboBox<Course> courseComboBox = new JComboBox<>();
        assignGradeStudentComboBox.addActionListener(e -> {
            courseComboBox.removeAllItems();
            Student selectedStudent = (Student) assignGradeStudentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                for (Course course : selectedStudent.getCourses()) {
                    courseComboBox.addItem(course);
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(courseComboBox, gbc);

        JLabel gradeLabel = new JLabel("Grade:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(gradeLabel, gbc);

        JTextField gradeField = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(gradeField, gbc);

        JButton assignButton = new JButton("Assign Grade");
        assignButton.addActionListener(e -> {
            assignGrade((Student) assignGradeStudentComboBox.getSelectedItem(), (Course) courseComboBox.getSelectedItem(), gradeField.getText());
            refreshAllTabs();
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(assignButton, gbc);

        setContentPane(panel);
        validate();
    }

    private void showViewEnrolledCourses() {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students available to view enrollments.");
            return;
        }

        JPanel panel = new JPanel(new BorderLayout());

        JLabel studentLabel = new JLabel("Select Student:");
        JComboBox<Student> studentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        panel.add(studentLabel, BorderLayout.NORTH);
        panel.add(studentComboBox, BorderLayout.CENTER);

        JTextArea enrolledCoursesArea = new JTextArea(10, 30);
        enrolledCoursesArea.setEditable(false);
        panel.add(new JScrollPane(enrolledCoursesArea), BorderLayout.SOUTH);

        studentComboBox.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            if (selectedStudent != null) {
                StringBuilder sb = new StringBuilder();
                for (Course course : selectedStudent.getCourses()) {
                    sb.append(course.getName()).append("\n");
                }
                enrolledCoursesArea.setText(sb.toString());
            }
        });

        setContentPane(panel);
        validate();
    }

    private void addStudent(String name, String age) {
        students.add(new Student(name, Integer.parseInt(age)));
        JOptionPane.showMessageDialog(this, "Student added successfully!");
    }

    private void updateStudent(Student student, String name, String age) {
        if (student != null) {
            student.setName(name);
            student.setAge(Integer.parseInt(age));
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Student not found.");
        }
    }

    private void deleteStudent(Student student) {
        if (student != null) {
            students.remove(student);
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Student not found.");
        }
    }

    private void enrollStudentInCourse(Course course, Student student) {
        if (student != null && course != null) {
            student.addCourse(course);
            JOptionPane.showMessageDialog(this, "Student enrolled successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select both a course and a student to enroll.");
        }
    }

    private void assignGrade(Student student, Course course, String grade) {
        if (student != null && course != null) {
            course.setGrade(grade);
            JOptionPane.showMessageDialog(this, "Grade assigned successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select both a course and a student to assign a grade.");
        }
    }

    private void addCourse(String name) {
        courses.add(new Course(name));
        JOptionPane.showMessageDialog(this, "Course added successfully!");
    }

    private void updateCourse(Course course, String name) {
        if (course != null) {
            course.setName(name);
            JOptionPane.showMessageDialog(this, "Course updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Course not found.");
        }
    }

    private void deleteCourse(Course course) {
        if (course != null) {
            courses.remove(course);
            JOptionPane.showMessageDialog(this, "Course deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Course not found.");
        }
    }

    private void refreshAllTabs() {
        refreshViewStudentsTable();
        if (viewCoursesTable != null) {
            refreshViewCoursesTable();
        }
        if (assignGradeStudentComboBox != null) {
            assignGradeStudentComboBox.setModel(new DefaultComboBoxModel<>(students.toArray(new Student[0])));
        }
    }

    private void refreshViewStudentsTable() {
        DefaultTableModel model = (DefaultTableModel) viewStudentsTable.getModel();
        model.setRowCount(0); // Clear existing data
        for (Student student : students) {
            Object[] row = {
                    student.getId(),
                    student.getName(),
                    student.getAge(),
                    "Delete"
            };
            model.addRow(row);
        }
    }

    private void refreshViewCoursesTable() {
        DefaultTableModel model = (DefaultTableModel) viewCoursesTable.getModel();
        model.setRowCount(0); // Clear existing data
        for (Course course : courses) {
            Object[] row = {
                    course.getId(),
                    course.getName(),
                    "Delete"
            };
            model.addRow(row);
        }
    }

    private void loadSampleData() {
        // Sample Courses
        courses.add(new Course("Mathematics"));
        courses.add(new Course("Physics"));
        courses.add(new Course("Chemistry"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }

    // Custom renderer and editor for JTable buttons

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Delete" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Delete" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (table == viewStudentsTable) {
                        deleteStudent(students.get(row));
                    } else if (table == viewCoursesTable) {
                        deleteCourse(courses.get(row));
                    }
                    fireEditingStopped(); // Notify the JTable that editing has stopped
                    refreshAllTabs();
                }
            });
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
}
