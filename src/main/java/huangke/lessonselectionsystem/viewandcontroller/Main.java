package huangke.lessonselectionsystem.viewandcontroller;

import huangke.lessonselectionsystem.model.Administrator;
import huangke.lessonselectionsystem.model.Course;
import huangke.lessonselectionsystem.model.Lesson;
import huangke.lessonselectionsystem.model.Student;
import huangke.lessonselectionsystem.model.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
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

    // 设置了一个函数叫做defaultFrame,统一JFrame的长和宽的大小。
    private static JFrame defaultFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setSize(WIDTH, HEIGHT);
        return frame;
    }

    public static void main(String[] args) {
        JFrame frame = defaultFrame("选课系统"); // 创建窗口”选课系统“
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口后整个程序退出

        frame.setLayout(new GridLayout(0, 1)); // 设置为GridLayout格式（网格布局）

        JButton studentButton = new JButton("学生端"); // 创建按钮”学生端“
        studentButton.addActionListener(e -> showStudentLoginFrame());
        // 在点击studentButton按钮时执行shouStudentLoginFrame函数
        // 这个写法叫做lambda表达式，作用是把->后面的函数作为参数传给这个函数。在这里，传进来的函数是在每次鼠标点击按钮时被执行。
        /*studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentLoginFrame();
            }
        });*/
        frame.add(studentButton); // 把按钮添加到窗口中


        JButton administratorButton = new JButton("管理员端");
        administratorButton.addActionListener(e -> showadministratorLoginFrame());
        frame.add(administratorButton);

        frame.setVisible(true); // 设置窗口为可见


    }

    // 学生端登录界面
    private static void showStudentLoginFrame() {
        JFrame frame = defaultFrame("登录");
        frame.setLayout(new GridLayout(0, 1));

        // 学号面板
        JPanel numberPanel = new JPanel(new GridLayout(1, 0)); // 设置面板
        JLabel numberLabel = new JLabel("学号："); // 设置标签“学号"
        JTextField numberTextField = new JTextField(); // 设置输入框
        numberPanel.add(numberLabel);
        numberPanel.add(numberTextField);
        frame.add(numberPanel);

        // 密码面板
        JPanel passwordPanel = new JPanel(new GridLayout(1, 0));
        JLabel passwordLabel = new JLabel("密码：");
        JTextField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        frame.add(passwordPanel);

        // loginButton
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> {
            try {
                Student student = databaseConnection.queryStudent(numberTextField.getText());

                if (student != null)
                    if (passwordField.getText().equals(student.getPassword())) {
                        frame.dispose(); // 关掉窗口
                        showCourseSelectionFrame(); // 选课界面
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
        }); // 同上此处为lambda表达式，作用是把->后面的函数作为参数传给这个函数。在这里，传进来的函数是在每次鼠标点击按钮时被执行。
        frame.add(loginButton);

        frame.setVisible(true);
    }

    private static void showCourseSelectionFrame() {
        JFrame frame = defaultFrame("学生选课");

        JTabbedPane tabbedPane = new JTabbedPane(); // 设置标签页

        Object[] columnNames = new Object[]{"课程ID", "课程名", "课序号", "教师", "第几节课", "教室"}; // 设置一维数组,含有这些信息，对应数据库里的表头
        Object[][] data = new Object[][]{
                Utils.lessonToJTableObjectArray(EXAMPLE_LESSON) // 把Lesson对象转换成Object数组
        };
        JTable table = new JTable(data, columnNames); // 建立了一个表格，横着的为data，竖着的为columnNames
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
                int column = table.columnAtPoint(point);

                showConfirmDialog(frame, EXAMPLE_LESSON);
            }
        });
        tabbedPane.addTab("选课", table);

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

    // 确认选课界面
    private static void showConfirmDialog(Frame owner, Lesson lesson) {
        JDialog dialog = new JDialog(owner, "确认选课吗？"); // 弹出对话框“确认选课吗？”
        dialog.setLayout(new GridLayout(0, 1));

        dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        // TODO: change the shown text
        JTextArea textArea = new JTextArea(lesson.toString().replace(' ', '\n'));
        textArea.setEditable(false);
        dialog.add(textArea);

        JButton button = new JButton("确认选课");
        button.addActionListener(e -> {
            // TODO
        });
        dialog.add(button);

        dialog.setVisible(true);
    }

    private static void showadministratorLoginFrame() {
        JFrame frame = defaultFrame("登录");
        frame.setLayout(new GridLayout(0, 1));

        // 管理员账号面板
        JPanel numberPanel = new JPanel(new GridLayout(1, 0)); // 设置面板
        JLabel numberLabel = new JLabel("管理员账号："); // 设置标签“管理员账号"
        JTextField numberTextField = new JTextField(); // 设置输入框
        numberPanel.add(numberLabel);
        numberPanel.add(numberTextField);
        frame.add(numberPanel);

        // 密码面板
        JPanel passwordPanel = new JPanel(new GridLayout(1, 0));
        JLabel passwordLabel = new JLabel("密码：");
        JTextField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        frame.add(passwordPanel);

        //LoginButton
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> {
            try {
                Administrator administrator = databaseConnection.queryAdministrator(numberTextField.getText());

                if (administrator != null)
                    if (passwordField.getText().equals(administrator.getPassword())) {
                        frame.dispose(); // 关掉窗口
                        showAdministratorFrame(); // 跳转到管理员窗口
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
        }); // 同上此处为lambda表达式，作用是把->后面的函数作为参数传给这个函数。在这里，传进来的函数是在每次鼠标点击按钮时被执行。
        frame.add(loginButton);


        frame.setVisible(true);
    }

    private static void showAdministratorFrame() {
   /*     JFrame frame = defaultFrame("学生选课");

        JTabbedPane tabbedPane = new JTabbedPane(); // 设置标签页

        Object[] columnNames = new Object[]{"课程ID", "课程名", "课序号", "教师", "第几节课", "教室"}; // 设置一维数组,含有这些信息，对应数据库里的表头
        Object[][] data = new Object[][]{
                Utils.lessonToJTableObjectArray(EXAMPLE_LESSON) // 把Lesson对象转换成Object数组
        };
        JTable table = new JTable(data, columnNames); // 建立了一个表格，横着的为data，竖着的为columnNames
        *//*table.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });*//*
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                int column = table.columnAtPoint(point);

                showConfirmDialog(frame, EXAMPLE_LESSON);
            }
        });
        tabbedPane.addTab("选课", table);

        frame.add(tabbedPane);

        frame.setVisible(true);
    */
    }

}
