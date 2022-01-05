package huangke.lessonselectionsystem.model.database;

import huangke.lessonselectionsystem.model.Administrator;
import huangke.lessonselectionsystem.model.Student;

import java.sql.*;

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

    //查询学生登录信息
    public Student queryStudent(String studentNumber) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM students WHERE student_number = ?;");
        prepareStatement.setString(1, studentNumber);

        ResultSet resultSet = prepareStatement.executeQuery();
        if (resultSet.next())
            return new Student(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        else
            return null;
    }

    //查询管理员登录信息
    public Administrator queryAdministrator(String username) throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM administrators WHERE username = ?;");//执行操作：查询username为？的情况。
        prepareStatement.setString(1, username); // 设置？的值为传进来的参数username。

        ResultSet resultSet = prepareStatement.executeQuery(); // 执行查询，将结果存入resultSet
        if (resultSet.next())
            return new Administrator(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));//如果resultSet不为空，返回一个新的Administrator
        else
            return null;//如果resultSet为空，返回null。
    }
    //查询选课信息
 /*  public List<Lesson> queryLessons() throws SQLException{
       PreparedStatement prepareStatement = connection.prepareStatement( "SELECT * FROM lessons JOIN courses USING (course_id);");//查询所有课程

       ResultSet resultSet = prepareStatement.executeQuery(); // 执行查询，将结果存入resultSet
       ArrayList<Lesson> lessons = new ArrayList<>();

       if (resultSet.next())
           return new Lesson(new Course(resultSet.getShort(1), resultSet.getString(2), resultSet.getString(3)), resultSet.getByte(2), resultSet.getString(3), resultSet.getByte(4), resultSet.getByte(5), resultSet.getString(6));//如果resultSet不为空，返回一个新的Lesson
       else
           return null;//如果resultSet为空，返回null。
   }*/
}
