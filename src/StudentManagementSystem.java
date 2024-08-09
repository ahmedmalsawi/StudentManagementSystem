import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StudentManagementSystem extends Application {

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    
    private TableView<Student> studentTable;
    private TableView<Course> courseTable;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management System");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        // Top Menu
        MenuBar menuBar = new MenuBar();

        Menu studentMenu = new Menu("Student Management");
        MenuItem addStudentItem = new MenuItem("Add Student");
        addStudentItem.setOnAction(e -> showAddStudentDialog());
        studentMenu.getItems().add(addStudentItem);

        Menu courseMenu = new Menu("Course Management");
        MenuItem addCourseItem = new MenuItem("Add Course");
        addCourseItem.setOnAction(e -> showAddCourseDialog());
        courseMenu.getItems().add(addCourseItem);

        menuBar.getMenus().addAll(studentMenu, courseMenu);
        root.setTop(menuBar);

        // Center Content (Tables)
        HBox centerBox = new HBox(10);
        centerBox.setPadding(new Insets(10));

        studentTable = createStudentTable();
        courseTable = createCourseTable();

        centerBox.getChildren().addAll(new Label("Students"), studentTable, new Label("Courses"), courseTable);
        root.setCenter(centerBox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Student> createStudentTable() {
        TableView<Student> table = new TableView<>();
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(data -> data.getValue().ageProperty().asObject());
        table.getColumns().addAll(nameColumn, ageColumn);
        table.setItems(students);
        return table;
    }

    private TableView<Course> createCourseTable() {
        TableView<Course> table = new TableView<>();
        TableColumn<Course, String> nameColumn = new TableColumn<>("Course Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        table.getColumns().add(nameColumn);
        table.setItems(courses);
        return table;
    }

    private void showAddStudentDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add New Student");

        // Dialog layout
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
                return new Student(nameField.getText(), Integer.parseInt(ageField.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> students.add(student));
    }

    private void showAddCourseDialog() {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Add New Course");

        // Dialog layout
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

    public static void main(String[] args) {
        launch(args);
    }
}
