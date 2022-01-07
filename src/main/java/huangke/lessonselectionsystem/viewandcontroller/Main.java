package huangke.lessonselectionsystem.viewandcontroller;

import huangke.lessonselectionsystem.model.*;
import huangke.lessonselectionsystem.model.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        administratorButton.addActionListener(e -> showAdministratorLoginFrame());
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
                        showLessonSelectionFrame(student); // 选课界面
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

    private static JTable createLessonTable(List<Lesson> lessons, TableLessonListener tableLessonListener) {
        Object[] columnNames = new Object[]{"课程ID", "课程名", "课序号", "教师", "星期", "第几节课", "教室"}; // 设置一维数组,含有这些信息，对应数据库里的表头
        var size = lessons.size();
        Object[][] data = new Object[size][];
        for (int i = 0; i < size; i++)
            data[i] = Utils.lessonToJTableObjectArray(lessons.get(i)); // 把Lesson对象转换成Object数组

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
                tableLessonListener.lessonClicked(lessons.get(row));
            }
        });

        return table;
    }

    private static List<Lesson> tryGetAllLessons(Frame frame) {
        try {
            return databaseConnection.queryAllLessons();
        } catch (SQLException ex) {
            ex.printStackTrace();
            new JDialog(frame, "发生未知 SQL 错误！")
                    .setVisible(true);
            return Collections.emptyList();
        }
    }

    private static List<Lesson> tryGetSelectedLessons(Frame frame, String studentNumber) {
        try {
            return databaseConnection.querySelectedLessons(studentNumber);
        } catch (SQLException ex) {
            ex.printStackTrace();
            new JDialog(frame, "发生未知 SQL 错误！")
                    .setVisible(true);
            return Collections.emptyList();
        }
    }

    private static void showLessonSelectionFrame(Student student) {
        JFrame frame = defaultFrame("学生选课");

        JTabbedPane tabbedPane = new JTabbedPane(); // 设置标签页

        List<Lesson> lessons = tryGetAllLessons(frame);
        var table = createLessonTable(lessons, lesson -> showConfirmDialog(frame, student, lesson));
        tabbedPane.addTab("选课", new JScrollPane(table));

        List<Lesson> selectedLessons = tryGetSelectedLessons(frame, student.getStudentNumber());

        var selectedLessonsTable = createLessonTable(selectedLessons, lesson -> {/* TODO */});
        tabbedPane.addTab("已选课程", new JScrollPane(selectedLessonsTable));

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

    // 确认选课界面
    private static void showConfirmDialog(Frame owner, Student student, Lesson lesson) {
        JDialog dialog = new JDialog(owner, "确认选课吗？"); // 弹出对话框“确认选课吗？”
        dialog.setLayout(new GridLayout(0, 1));

        dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        JTextArea textArea = new JTextArea(Utils.getLessonDisplayString(lesson));
        textArea.setEditable(false);
        dialog.add(textArea);

        JButton button = new JButton("确认选课");
        button.addActionListener(e -> {
            try {
                databaseConnection.addLessonSelection(student.getStudentNumber(), lesson.getCourse().getCourseId(), lesson.getLessonIndex());
                dialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                new JDialog(dialog, "发生 SQL 错误！")
                        .setVisible(true);
            }
        });
        dialog.add(button);

        dialog.setVisible(true);
    }

    private static void showAdministratorLoginFrame() {
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
        var frame = defaultFrame("管理员管理");

        var tabbedPane = new JTabbedPane(); // 设置标签页

        var panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        var lessons = tryGetAllLessons(frame);
        var table = createLessonTable(lessons, Main::showAdministratorLessonFrame);
        panel.add(new JScrollPane(table));

        List<StudentWithoutPassword> students;
        try {
            students = databaseConnection.queryAllStudentWithoutPasswords();
        } catch (SQLException ex) {
            ex.printStackTrace();
            new JDialog(frame, "发生未知 SQL 错误！")
                    .setVisible(true);
            students = Collections.emptyList();
        }

        var addCourseButton = new JButton("添加课程");
        addCourseButton.addActionListener(e -> showAddCourseFrame());
        panel.add(addCourseButton);

        var addLessonButton = new JButton("添加排课");
        panel.add(addLessonButton);

        tabbedPane.addTab("课程管理", panel);

        var table2 = new JTable(
                students.stream().map(Utils::studentWithoutPasswordToJTableObjectArray)
                        .collect(Collectors.toList()).toArray(new Object[students.size()][]),
                new Object[]{"学号", "姓名"}
        );
        List<StudentWithoutPassword> finalStudents = students;
        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                int column = table.columnAtPoint(point);
                showAdministratorStudentFrame(finalStudents.get(row));
            }
        });
        tabbedPane.addTab("学生管理", new JScrollPane(table2));
        frame.add(tabbedPane);

        frame.setVisible(true);
    }

    private static class Input extends JPanel {
        JLabel label;
        JTextField textField;

        public Input(String labelText) {
            label = new JLabel(labelText);
            textField = new JTextField();

            setLayout(new GridLayout(1, 0));
            add(label);
            add(textField);
        }
    }

    private static void showAddCourseFrame() {
        JFrame frame = defaultFrame("管理课程");
        frame.setLayout(new GridLayout(0, 1));

        var courseIdInput = new Input("课程号");
        frame.add(courseIdInput);
        var nameInput = new Input("课程名");
        frame.add(nameInput);
        var introductionInput = new Input("课程介绍");
        frame.add(introductionInput);

        var confirmAddButton = new JButton("确认添加");
        confirmAddButton.addActionListener(e -> {
            try {
                databaseConnection.addCourse(new Course(
                        Short.parseShort(courseIdInput.textField.getText()),
                        nameInput.textField.getText(),
                        introductionInput.textField.getText())
                );
                frame.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                new JDialog(frame, "发生 SQL 错误！")
                        .setVisible(true);
            }
        });
        frame.add(confirmAddButton);

        frame.setVisible(true);
    }

    private static void showAdministratorLessonFrame(Lesson lesson) {
        JFrame frame = defaultFrame("管理课程");
        frame.setLayout(new GridLayout(0, 1));

        var textArea = new JTextArea(Utils.getLessonDisplayString(lesson));
        textArea.setEditable(false);
        frame.add(textArea);

        var deleteButton = new JButton("删除此排课");
        deleteButton.addActionListener(e -> {
            try {
                databaseConnection.deleteLesson(lesson.getCourse().getCourseId(), lesson.getLessonIndex());
                frame.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                new JDialog(frame, "发生未知 SQL 错误！")
                        .setVisible(true);
            }
        });
        frame.add(deleteButton);

        frame.setVisible(true);
    }

    private static void showAdministratorStudentFrame(StudentWithoutPassword student) {
        JFrame frame = defaultFrame("管理学生");
        frame.setLayout(new GridLayout(0, 1));

        var textArea = new JTextArea(Utils.getStudentDisplayString(student));
        textArea.setEditable(false);
        frame.add(textArea);

        var deleteButton = new JButton("删除学生");
        deleteButton.addActionListener(e -> {
            try {
                databaseConnection.deleteStudent(student.getStudentNumber());
                frame.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                new JDialog(frame, "发生未知 SQL 错误！")
                        .setVisible(true);
            }
        });
        frame.add(deleteButton);

        frame.setVisible(true);
    }

    private interface TableLessonListener {
        void lessonClicked(Lesson lesson);
    }
}
