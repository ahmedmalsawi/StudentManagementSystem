import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentManagementSystem extends Application {

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private TableView<Student> studentTable;
    private TableView<Course> courseTable;
    private TableView<Course> enrolledCoursesTable;
    private ComboBox<Student> studentComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management System");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 900, 600);

        // Top Menu
        MenuBar menuBar = new MenuBar();

        Menu studentMenu = new Menu("Student Management");
        MenuItem addStudentItem = new MenuItem("Add Student");
        addStudentItem.setOnAction(e -> showAddStudentDialog());
        MenuItem updateStudentItem = new MenuItem("Update Student");
        updateStudentItem.setOnAction(e -> showUpdateStudentDialog());
        MenuItem viewStudentsItem = new MenuItem("View Students");
        viewStudentsItem.setOnAction(e -> showViewStudentsTab());

        studentMenu.getItems().addAll(addStudentItem, updateStudentItem, viewStudentsItem);

        Menu courseMenu = new Menu("Course Management");
        MenuItem addCourseItem = new MenuItem("Add Course");
        addCourseItem.setOnAction(e -> showAddCourseDialog());
        MenuItem updateCourseItem = new MenuItem("Update Course");
        updateCourseItem.setOnAction(e -> showUpdateCourseDialog());
        MenuItem viewCoursesItem = new MenuItem("View Courses");
        viewCoursesItem.setOnAction(e -> showViewCoursesTab());

        courseMenu.getItems().addAll(addCourseItem, updateCourseItem, viewCoursesItem);

        Menu enrollmentMenu = new Menu("Enrollment Management");
        MenuItem enrollCourseItem = new MenuItem("Enroll in Course");
        enrollCourseItem.setOnAction(e -> showEnrollInCourseDialog());
        MenuItem assignGradeItem = new MenuItem("Assign Grades");
        assignGradeItem.setOnAction(e -> showAssignGradesDialog());
        MenuItem viewEnrolledCoursesItem = new MenuItem("View Enrolled Courses");
        viewEnrolledCoursesItem.setOnAction(e -> showViewEnrolledCourses());

        enrollmentMenu.getItems().addAll(enrollCourseItem, assignGradeItem, viewEnrolledCoursesItem);

        menuBar.getMenus().addAll(studentMenu, courseMenu, enrollmentMenu);
        root.setTop(menuBar);

        showViewStudentsTab(); // Default view

        primaryStage.setScene(scene);
        primaryStage.show();

        loadSampleData(); // Load some sample data
    }

    private void showAddStudentDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add New Student");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (isInteger(ageField.getText())) {
                    return new Student(nameField.getText(), Integer.parseInt(ageField.getText()));
                } else {
                    showErrorDialog("Age must be an integer.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> students.add(student));
    }

    private void showUpdateStudentDialog() {
        if (students.isEmpty()) {
            showErrorDialog("No students available to update.");
            return;
        }

        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Update Student");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Student> studentComboBox = new ComboBox<>(students);
        TextField nameField = new TextField();
        TextField ageField = new TextField();

        studentComboBox.setOnAction(e -> {
            Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                nameField.setText(selectedStudent.getName());
                ageField.setText(String.valueOf(selectedStudent.getAge()));
            }
        });

        grid.add(new Label("Select Student:"), 0, 0);
        grid.add(studentComboBox, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(ageField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                Student selectedStudent = studentComboBox.getSelectionModel().getSelectedItem();
                if (selectedStudent != null && isInteger(ageField.getText())) {
                    selectedStudent.setName(nameField.getText());
                    selectedStudent.setAge(Integer.parseInt(ageField.getText()));
                    refreshAllTabs();
                    return selectedStudent;
                } else {
                    showErrorDialog("Age must be an integer.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showViewStudentsTab() {
        studentTable = new TableView<>(students);

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(data -> data.getValue().ageProperty().asObject());

        TableColumn<Student, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(e -> {
                    Student student = getTableView().getItems().get(getIndex());
                    students.remove(student);
                    refreshAllTabs();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        studentTable.getColumns().addAll(nameColumn, ageColumn, deleteColumn);

        VBox vbox = new VBox(studentTable);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void showAddCourseDialog() {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Add New Course");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Course Name");

        grid.add(new Label("Course Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Course(nameField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(course -> courses.add(course));
    }

    private void showUpdateCourseDialog() {
        if (courses.isEmpty()) {
            showErrorDialog("No courses available to update.");
            return;
        }

        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Update Course");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Course> courseComboBox = new ComboBox<>(courses);
        TextField nameField = new TextField();

        courseComboBox.setOnAction(e -> {
            Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                nameField.setText(selectedCourse.getName());
            }
        });

        grid.add(new Label("Select Course:"), 0, 0);
        grid.add(courseComboBox, 1, 0);
        grid.add(new Label("Course Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
                if (selectedCourse != null) {
                    selectedCourse.setName(nameField.getText());
                    refreshAllTabs();
                    return selectedCourse;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showViewCoursesTab() {
        courseTable = new TableView<>(courses);

        TableColumn<Course, String> nameColumn = new TableColumn<>("Course Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Course, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(e -> {
                    Course course = getTableView().getItems().get(getIndex());
                    courses.remove(course);
                    refreshAllTabs();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        courseTable.getColumns().addAll(nameColumn, deleteColumn);

        VBox vbox = new VBox(courseTable);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void showEnrollInCourseDialog() {
        if (students.isEmpty() || courses.isEmpty()) {
            showErrorDialog("No students or courses available for enrollment.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Enroll in Course");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Course> courseComboBox = new ComboBox<>(courses);
        ComboBox<Student> studentComboBox = new ComboBox<>(students);

        grid.add(new Label("Select Course:"), 0, 0);
        grid.add(courseComboBox, 1, 0);
        grid.add(new Label("Select Student:"), 0, 1);
        grid.add(studentComboBox, 1, 1);

        Button enrollButton = new Button("Enroll");
        enrollButton.setOnAction(e -> {
            Course course = courseComboBox.getSelectionModel().getSelectedItem();
            Student student = studentComboBox.getSelectionModel().getSelectedItem();
            if (course != null && student != null) {
                enrollStudentInCourse(course, student);
                refreshAllTabs();
            } else {
                showErrorDialog("Please select both a course and a student to enroll.");
            }
        });

        grid.add(enrollButton, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        dialog.showAndWait();
    }

    private void showAssignGradesDialog() {
        if (students.isEmpty() || courses.isEmpty()) {
            showErrorDialog("No students or courses available for grade assignment.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Assign Grades");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Student> studentComboBox = new ComboBox<>(students);
        ComboBox<Course> courseComboBox = new ComboBox<>();
        TextField gradeField = new TextField();

        studentComboBox.setOnAction(e -> {
            Student student = studentComboBox.getSelectionModel().getSelectedItem();
            courseComboBox.getItems().clear();
            if (student != null) {
                courseComboBox.getItems().addAll(student.getCourses());
            }
        });

        grid.add(new Label("Select Student:"), 0, 0);
        grid.add(studentComboBox, 1, 0);
        grid.add(new Label("Select Course:"), 0, 1);
        grid.add(courseComboBox, 1, 1);
        grid.add(new Label("Grade:"), 0, 2);
        grid.add(gradeField, 1, 2);

        Button assignButton = new Button("Assign Grade");
        assignButton.setOnAction(e -> {
            Student student = studentComboBox.getSelectionModel().getSelectedItem();
            Course course = courseComboBox.getSelectionModel().getSelectedItem();
            if (student != null && course != null) {
                course.setGrade(gradeField.getText());
                refreshAllTabs();
            } else {
                showErrorDialog("Please select both a course and a student to assign a grade.");
            }
        });

        grid.add(assignButton, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        dialog.showAndWait();
    }

    private void showViewEnrolledCourses() {
        if (students.isEmpty()) {
            showErrorDialog("No students available to view enrollments.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("View Enrolled Courses");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        ComboBox<Student> studentComboBox = new ComboBox<>(students);
        enrolledCoursesTable = new TableView<>();

        studentComboBox.setOnAction(e -> refreshViewEnrolledCoursesTable(studentComboBox.getSelectionModel().getSelectedItem()));

        vbox.getChildren().addAll(new Label("Select Student:"), studentComboBox, enrolledCoursesTable);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void refreshViewEnrolledCoursesTable(Student student) {
        enrolledCoursesTable.getColumns().clear();
        if (student == null) return;

        TableColumn<Course, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Course, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(data -> data.getValue().gradeProperty());

        TableColumn<Course, Void> cancelColumn = new TableColumn<>("Cancel Enrollment");
        cancelColumn.setCellFactory(param -> new TableCell<>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setOnAction(e -> {
                    Course course = getTableView().getItems().get(getIndex());
                    student.getCourses().remove(course);
                    refreshViewEnrolledCoursesTable(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });

        enrolledCoursesTable.getColumns().addAll(courseNameColumn, gradeColumn, cancelColumn);
        enrolledCoursesTable.setItems(FXCollections.observableArrayList(student.getCourses()));
    }

    private void enrollStudentInCourse(Course course, Student student) {
        if (!student.getCourses().contains(course)) {
            student.addCourse(course);
            showInfoDialog("Student enrolled successfully!");
        } else {
            showErrorDialog("Student is already enrolled in this course.");
        }
    }

    private void refreshAllTabs() {
        if (studentTable != null) {
            studentTable.refresh();
        }
        if (courseTable != null) {
            courseTable.refresh();
        }
        if (enrolledCoursesTable != null) {
            enrolledCoursesTable.refresh();
        }
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadSampleData() {
        students.add(new Student("Ahmed Elsawi", 20));
        students.add(new Student("Marcellous Simeo", 22));
        students.add(new Student("Sabrina Marilyn", 21));

        courses.add(new Course("Mathematics"));
        courses.add(new Course("Physics"));
        courses.add(new Course("Chemistry"));
    }
}
