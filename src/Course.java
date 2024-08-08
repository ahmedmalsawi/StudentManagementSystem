public class Course {
    private static int idCounter = 0;
    private int id;
    private String name;
    private String grade;

    public Course(String name) {
        this.id = ++idCounter;
        this.name = name;
        this.grade = "";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return name;
    }
}
