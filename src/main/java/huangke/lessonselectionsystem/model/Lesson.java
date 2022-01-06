package huangke.lessonselectionsystem.model;

public class Lesson extends GenericLesson<Course> {
    public Lesson(Course course, byte lessonIndex, String teacher, Weekday weekday, byte dayStartIndex, byte dayEndIndex, String location) {
        super(course, lessonIndex, teacher, weekday, dayStartIndex, dayEndIndex, location);
    }
}
