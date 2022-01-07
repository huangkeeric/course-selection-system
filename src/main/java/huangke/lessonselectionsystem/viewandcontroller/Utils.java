package huangke.lessonselectionsystem.viewandcontroller;

import huangke.lessonselectionsystem.model.Course;
import huangke.lessonselectionsystem.model.Lesson;
import huangke.lessonselectionsystem.model.StudentWithoutPassword;

public final class Utils {
    private Utils() {
    }

    public static Object[] lessonToJTableObjectArray(Lesson lesson) {
        Course course = lesson.getCourse();
        return new Object[]{course.getCourseId(), course.getName(), /*course.getIntroduction(),*/
                lesson.getLessonIndex(), lesson.getTeacher(),
                lesson.getWeekday(),
                lesson.getDayStartIndex() + "-" + lesson.getDayEndIndex(), lesson.getLocation()};
    }

    public static String getLessonDisplayString(Lesson lesson) {
        var course = lesson.getCourse();
        return "课程号：" + course.getCourseId() + "\n" +
                "课程名：" + course.getName() + "\n" +
                "课程介绍：" + course.getIntroduction() + "\n" +
                "课序号：" + lesson.getDayEndIndex() + "\n" +
                "星期：" + lesson.getWeekday() + "\n" +
                "第几节课：" + lesson.getDayStartIndex() + "-" + lesson.getDayEndIndex() + "\n" +
                "地点：" + lesson.getLocation();
    }

    public static String getStudentDisplayString(StudentWithoutPassword student) {
        return "学号：" + student.getStudentNumber() + "\n" +
                "姓名：" + student.getName();
    }

    public static Object[] studentWithoutPasswordToJTableObjectArray(StudentWithoutPassword student) {
        return new Object[]{student.getStudentNumber(), student.getName()};
    }
}
