import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {

    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();

        JMenu studentMenu = new JMenu("Student");
        JMenuItem addStudentItem = new JMenuItem("Add Student");
        JMenuItem updateStudentItem = new JMenuItem("Update Student");
        JMenuItem viewStudentItem = new JMenuItem("View Student Details");

        studentMenu.add(addStudentItem);
        studentMenu.add(updateStudentItem);
        studentMenu.add(viewStudentItem);

        JMenu courseMenu = new JMenu("Course");
        JMenuItem enrollItem = new JMenuItem("Enroll in Course");
        JMenuItem gradeItem = new JMenuItem("Assign Grades");

        courseMenu.add(enrollItem);
        courseMenu.add(gradeItem);

        menuBar.add(studentMenu);
        menuBar.add(courseMenu);

        setJMenuBar(menuBar);

        addStudentItem.addActionListener(e -> showAddStudentForm());
        updateStudentItem.addActionListener(e -> showUpdateStudentForm());
        viewStudentItem.addActionListener(e -> showViewStudentDetails());
        enrollItem.addActionListener(e -> showEnrollForm());
        gradeItem.addActionListener(e -> showAssignGradeForm());
    }

    private void showAddStudentForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);

        JButton submitButton = new JButton("Add Student");
        submitButton.addActionListener(e -> addStudent(nameField.getText(), ageField.getText()));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(submitButton);

        setContentPane(panel);
        validate();
    }

    private void showUpdateStudentForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Student> studentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);

        JButton submitButton = new JButton("Update Student");
        submitButton.addActionListener(e -> updateStudent((Student) studentComboBox.getSelectedItem(), nameField.getText(), ageField.getText()));

        panel.add(new JLabel("Select Student:"));
        panel.add(studentComboBox);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(submitButton);

        setContentPane(panel);
        validate();
    }

    private void showViewStudentDetails() {
        JPanel panel = new JPanel();

        String[] columnNames = {"ID", "Name", "Age"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        for (Student student : students) {
            Object[] row = {student.getId(), student.getName(), student.getAge()};
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        setContentPane(panel);
        validate();
    }

    private void showEnrollForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Course> courseComboBox = new JComboBox<>(courses.toArray(new Course[0]));
        JTable studentTable = new JTable(new DefaultTableModel(new String[]{"ID", "Name"}, 0));
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();

        for (Student student : students) {
            Object[] row = {student.getId(), student.getName()};
            model.addRow(row);
        }

        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(e -> enrollStudentInCourse((Course) courseComboBox.getSelectedItem(), studentTable.getSelectedRow()));

        panel.add(new JLabel("Select Course:"));
        panel.add(courseComboBox);
        panel.add(new JScrollPane(studentTable));
        panel.add(enrollButton);

        setContentPane(panel);
        validate();
    }

    private void showAssignGradeForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Student> studentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        JTable courseTable = new JTable(new DefaultTableModel(new String[]{"ID", "Name", "Grade"}, 0));
        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();

        JButton assignButton = new JButton("Assign Grade");
        JTextField gradeField = new JTextField(5);

        studentComboBox.addActionListener(e -> {
            model.setRowCount(0);
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();
            for (Course course : selectedStudent.getCourses()) {
                Object[] row = {course.getId(), course.getName(), course.getGrade()};
                model.addRow(row);
            }
        });

        assignButton.addActionListener(e -> assignGrade((Student) studentComboBox.getSelectedItem(), courseTable.getSelectedRow(), gradeField.getText()));

        panel.add(new JLabel("Select Student:"));
        panel.add(studentComboBox);
        panel.add(new JScrollPane(courseTable));
        panel.add(new JLabel("Grade:"));
        panel.add(gradeField);
        panel.add(assignButton);

        setContentPane(panel);
        validate();
    }

    private void addStudent(String name, String age) {
        students.add(new Student(name, Integer.parseInt(age)));
        JOptionPane.showMessageDialog(this, "Student added successfully!");
    }

    private void updateStudent(Student student, String name, String age) {
        student.setName(name);
        student.setAge(Integer.parseInt(age));
        JOptionPane.showMessageDialog(this, "Student updated successfully!");
    }

    private void enrollStudentInCourse(Course course, int studentIndex) {
        if (studentIndex >= 0) {
            Student student = students.get(studentIndex);
            student.addCourse(course);
            JOptionPane.showMessageDialog(this, "Student enrolled successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to enroll.");
        }
    }

    private void assignGrade(Student student, int courseIndex, String grade) {
        if (courseIndex >= 0) {
            student.getCourses().get(courseIndex).setGrade(grade);
            JOptionPane.showMessageDialog(this, "Grade assigned successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course to assign a grade.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }
}
