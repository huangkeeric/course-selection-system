package huangke.lessonselectionsystem.model;

public class StudentWithoutPassword {
    String studentNumber, name;

    public StudentWithoutPassword(String studentNumber, String name) {
        this.studentNumber = studentNumber;
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getName() {
        return name;
    }
}
