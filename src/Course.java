import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty grade = new SimpleStringProperty();

    public Course(String name) {
        this.name.set(name);
        this.grade.set("");
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }
}
