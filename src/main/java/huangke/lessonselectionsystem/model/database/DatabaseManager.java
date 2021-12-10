package huangke.lessonselectionsystem.model.database;

import huangke.lessonselectionsystem.model.Student;

import java.sql.*;

public class DatabaseManager implements AutoCloseable {
    Connection connection;

    public DatabaseManager() throws SQLException {
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

    public Student queryStudent(String studentNumber) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM students WHERE student_number = ?;");
        prepareStatement.setString(1, studentNumber);

        ResultSet resultSet = prepareStatement.executeQuery();
        if (resultSet.next())
            return new Student(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        else
            return null;
    }
}
