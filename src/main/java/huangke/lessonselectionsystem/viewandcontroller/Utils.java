package huangke.lessonselectionsystem.viewandcontroller;

import huangke.lessonselectionsystem.model.Course;
import huangke.lessonselectionsystem.model.Lesson;

public final class Utils {
    private Utils() {
    }

    public static Object[] lessonToJTableObjectArray(Lesson lesson) {
        Course course = lesson.getCourse();
        return new Object[]{course.getCourseId(), course.getName(), /*course.getIntroduction(),*/
                lesson.getLessonIndex(), lesson.getTeacher(),
                lesson.getDayStartIndex() + "-" + lesson.getDayEndIndex(), lesson.getLocation()};
    }
}
