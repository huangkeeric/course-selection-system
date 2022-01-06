package huangke.lessonselectionsystem.model;

public class LessonWithCourseId extends GenericLesson<Short> {
    public LessonWithCourseId(Short courseId, byte lessonIndex, String teacher, Weekday weekday, byte dayStartIndex, byte dayEndIndex, String location) {
        super(courseId, lessonIndex, teacher, weekday, dayStartIndex, dayEndIndex, location);
    }
}
