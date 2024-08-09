import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty name = new SimpleStringProperty();

    public Course(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }
}
