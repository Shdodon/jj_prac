package lesson1.hw.homework1;

public class Department {
    private String name;
    public Department(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name;
    }
}
