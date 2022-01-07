package huangke.lessonselectionsystem.model.database;

import huangke.lessonselectionsystem.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection implements AutoCloseable {
    Connection connection;

    public DatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/lesson_selection_system",
                "root",
                "huangke"
        );
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    private Student getStudent(ResultSet resultSet) throws SQLException {
        return new Student(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
    }

    //查询学生登录信息
    public Student queryStudent(String studentNumber) throws SQLException {
        try (PreparedStatement prepareStatement =
                     connection.prepareStatement("SELECT * FROM students WHERE student_number = ?;")) {
            prepareStatement.setString(1, studentNumber);

            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next())
                return getStudent(resultSet);
            else
                return null;
        }
    }

    //查询管理员登录信息
    public Administrator queryAdministrator(String username) throws SQLException {
        try (PreparedStatement prepareStatement =
                     connection.prepareStatement("SELECT * FROM administrators WHERE username = ?;")) {
            //执行操作：查询username为？的情况。
            prepareStatement.setString(1, username); // 设置？的值为传进来的参数username。

            ResultSet resultSet = prepareStatement.executeQuery(); // 执行查询，将结果存入resultSet
            if (resultSet.next())
                return new Administrator(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));//如果resultSet不为空，返回一个新的Administrator
            else
                return null;//如果resultSet为空，返回null。
        }
    }

    private List<Lesson> getLessons(ResultSet resultSet) throws SQLException {
        ArrayList<Lesson> lessons = new ArrayList<>();

        while (resultSet.next())
            lessons.add(new Lesson(
                    new Course(resultSet.getShort(1), resultSet.getString(8), resultSet.getString(9)),
                    resultSet.getByte(2), resultSet.getString(3),
                    Weekday.values()[resultSet.getByte(4)], resultSet.getByte(5), resultSet.getByte(6),
                    resultSet.getString(7)));
        return lessons;
    }

    // 查询课程信息
    public List<Lesson> queryAllLessons() throws SQLException {
        try (PreparedStatement prepareStatement =
                     connection.prepareStatement("SELECT * FROM lessons JOIN courses USING (course_id);")) {
            //查询所有课程

            ResultSet resultSet = prepareStatement.executeQuery(); // 执行查询，将结果存入resultSet
            return getLessons(resultSet);
        }
    }

    public List<Lesson> querySelectedLessons(String studentNumber) throws SQLException {
        try (PreparedStatement prepareStatement = connection.prepareStatement(
                "SELECT * FROM lessons " +
                        "JOIN courses USING (course_id) " +
                        "JOIN course_selections USING (course_id, lesson_index) " +
                        "WHERE student_number = ?;"
        )) {
            prepareStatement.setString(1, studentNumber);

            var resultSet = prepareStatement.executeQuery();
            return getLessons(resultSet);
        }
    }

    public List<StudentWithoutPassword> queryStudentWithoutPasswords() throws SQLException {
        try (var prepareStatement =
                     connection.prepareStatement("SELECT student_number, name FROM students;")) {
            var resultSet = prepareStatement.executeQuery(); // 执行查询，将结果存入resultSet
            var students = new ArrayList<StudentWithoutPassword>();
            while (resultSet.next())
                students.add(getStudentWithoutPassword(resultSet));

            return students;
        }
    }

    private StudentWithoutPassword getStudentWithoutPassword(ResultSet resultSet) throws SQLException {
        return new StudentWithoutPassword(resultSet.getString(1), resultSet.getString(2));
    }

    public void addStudent(Student student) throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("INSERT INTO students VALUES (?, ?, ?);")) {
            preparedStatement.setString(1, student.getStudentNumber());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getName());

            preparedStatement.executeUpdate();
        }
    }

    public void addCourse(Course course) throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("INSERT INTO courses VALUES (?, ?, ?);")) {
            preparedStatement.setShort(1, course.getCourseId());
            preparedStatement.setString(2, course.getName());
            preparedStatement.setString(3, course.getIntroduction());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * @param lesson lesson with its course ID only
     */
    public void addLesson(LessonWithCourseId lesson) throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("INSERT INTO lessons VALUES (?, ?, ?, ?, ?, ?, ?);")) {
            preparedStatement.setShort(1, lesson.getCourse());
            preparedStatement.setByte(2, lesson.getLessonIndex());
            preparedStatement.setString(3, lesson.getTeacher());
            preparedStatement.setByte(4, (byte) lesson.getWeekday().ordinal());
            preparedStatement.setByte(5, lesson.getDayStartIndex());
            preparedStatement.setByte(6, lesson.getDayEndIndex());
            preparedStatement.setString(7, lesson.getLocation());

            preparedStatement.executeUpdate();
        }
    }

    public void addLessonSelection(String studentNumber, short courseId, byte lessonIndex) throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("INSERT INTO course_selections VALUES (?, ?, ?);")) {
            preparedStatement.setString(1, studentNumber);
            preparedStatement.setShort(2, courseId);
            preparedStatement.setByte(3, lessonIndex);

            preparedStatement.executeUpdate();
        }
    }

    public void deleteLesson(short courseId, byte lessonIndex) throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("DELETE FROM course_selections WHERE course_id = ? and lesson_index = ?;")) {
            preparedStatement.setShort(1, courseId);
            preparedStatement.setByte(2, lessonIndex);

            preparedStatement.executeUpdate();
        }
    }

    public List<StudentWithoutPassword> queryAllStudentWithoutPasswords() throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("SELECT student_number, name FROM students;")) {
            var resultSet = preparedStatement.executeQuery();
            var students = new ArrayList<StudentWithoutPassword>();
            while (resultSet.next())
                students.add(getStudentWithoutPassword(resultSet));
            return students;
        }
    }

    public void deleteStudent(String studentNumber) throws SQLException {
        try (var preparedStatement =
                     connection.prepareStatement("DELETE FROM students WHERE student_number = ?;")) {
            preparedStatement.setString(1, studentNumber);
            preparedStatement.executeUpdate();
        }
    }
}
