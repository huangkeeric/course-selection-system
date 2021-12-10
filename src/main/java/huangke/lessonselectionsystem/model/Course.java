package huangke.lessonselectionsystem.model;

public class Course {
    short courseId;
    String name;
    String introduction;

    public Course(short courseId, String name, String introduction) {
        this.courseId = courseId;
        this.name = name;
        this.introduction = introduction;
    }

    public short getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + courseId +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
