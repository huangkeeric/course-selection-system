package huangke.lessonselectionsystem.viewandcontroller;

import huangke.lessonselectionsystem.model.Course;
import huangke.lessonselectionsystem.model.Lesson;
import huangke.lessonselectionsystem.model.Student;
import huangke.lessonselectionsystem.model.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Main {
    public static final int WIDTH = 800, HEIGHT = 600;
    public static final int DIALOG_WIDTH = 400, DIALOG_HEIGHT = 400;

    static DatabaseConnection databaseConnection;

    static {
        try {
            databaseConnection = new DatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: remove this when database is ready
    private static final Lesson EXAMPLE_LESSON = new Lesson(
            new Course((short) 0, "C++", "本课程将为你讲解 C++ 程序设计入门。"),
            (byte) 0, "张三", (byte) 0, (byte) 1, "A101"
    );

    private static JFrame defaultFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setSize(WIDTH, HEIGHT);
        return frame;
    }

    public static void main(String[] args) {
        JFrame frame = defaultFrame("选课系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new GridLayout(0, 1));

        JButton studentButton = new JButton("学生端");
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentLoginFrame();
            }
        });
        frame.add(studentButton);
        JButton administratorButton = new JButton("管理员端");
        frame.add(administratorButton);

        frame.setVisible(true);


    }

    private static void showStudentLoginFrame() {
        JFrame frame = defaultFrame("登录");
        frame.setLayout(new GridLayout(0, 1));

        // numberPanel
        JPanel numberPanel = new JPanel(new GridLayout(1, 0));
        JLabel numberLabel = new JLabel("学号：");
        JTextField numberTextField = new JTextField();
        numberPanel.add(numberLabel);
        numberPanel.add(numberTextField);
        frame.add(numberPanel);

        // passwordPanel
        JPanel passwordPanel = new JPanel(new GridLayout(1, 0));
        JLabel passwordLabel = new JLabel("密码：");
        JTextField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        frame.add(passwordPanel);

        // loginButton
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Student student = databaseConnection.queryStudent(numberTextField.getText());

                    if (student != null)
                        if (passwordField.getText().equals(student.getPassword())) {
                            frame.dispose();
                            showCourseSelectionFrame();
                        } else
                            new JDialog(frame, "密码错误！")
                                    .setVisible(true);
                    else
                        new JDialog(frame, "学号不存在！")
                                .setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    new JDialog(frame, "发生未知 SQL 错误！")
                            .setVisible(true);
                }
            }
        });
        frame.add(loginButton);

        frame.setVisible(true);
    }

    private static void showCourseSelectionFrame() {
        JFrame frame = defaultFrame("学生选课");

        JTabbedPane tabbedPane = new JTabbedPane();

        Object[] columnNames = new Object[]{"课程ID", "课程名", "课序号", "教师", "第几节课", "教室"};
        Object[][] data = new Object[][]{
                Utils.lessonToJTableObjectArray(EXAMPLE_LESSON)
        };
        JTable table = new JTable(data, columnNames);
        /*table.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });*/
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                int column = table.rowAtPoint(point);

                showConfirmDialog(frame, EXAMPLE_LESSON);
            }
        });
        tabbedPane.addTab("选课", table);

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

    private static void showConfirmDialog(Frame owner, Lesson lesson) {
        JDialog dialog = new JDialog(owner, "确认选课吗？");
        dialog.setLayout(new GridLayout(0, 1));

        dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        // TODO: change the shown text
        JTextArea textArea = new JTextArea(lesson.toString().replace(' ', '\n'));
        textArea.setEditable(false);
        dialog.add(textArea);

        JButton button = new JButton("确认选课");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        dialog.add(button);

        dialog.setVisible(true);
    }
}
