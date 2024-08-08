import java.util.ArrayList;
import java.util.List;

public class Student {
    private static int idCounter = 0;
    private int id;
    private String name;
    private int age;
    private List<Course> courses;

    public Student(String name, int age) {
        this.id = ++idCounter;
        this.name = name;
        this.age = age;
        this.courses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    @Override
    public String toString() {
        return name;
    }
}
