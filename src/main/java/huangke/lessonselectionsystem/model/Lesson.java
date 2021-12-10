package huangke.lessonselectionsystem.model;

public class Lesson {
    Course course;
    byte lessonIndex;
    String teacher;
    Weekday weekday;
    byte dayStartIndex, dayEndIndex;
    String location;

    public Lesson(Course course, byte lessonIndex, String teacher, byte dayStartIndex, byte dayEndIndex, String location) {
        this.course = course;
        this.lessonIndex = lessonIndex;
        this.teacher = teacher;
        this.dayStartIndex = dayStartIndex;
        this.dayEndIndex = dayEndIndex;
        this.location = location;
    }

    public Course getCourse() {
        return course;
    }

    public byte getLessonIndex() {
        return lessonIndex;
    }

    public String getTeacher() {
        return teacher;
    }

    public byte getDayStartIndex() {
        return dayStartIndex;
    }

    public byte getDayEndIndex() {
        return dayEndIndex;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "course=" + course +
                ", lessonIndex=" + lessonIndex +
                ", teacher='" + teacher + '\'' +
                ", startIndex=" + dayStartIndex +
                ", endIndex=" + dayEndIndex +
                ", location='" + location + '\'' +
                '}';
    }
}
