package huangke.lessonselectionsystem.model;

public class Student {
    String studentNumber, password, name;

    public Student(String studentNumber, String password, String name) {
        this.studentNumber = studentNumber;
        this.password = password;
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
