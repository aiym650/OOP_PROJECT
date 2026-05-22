package university.models;

import university.enums.CourseType;

import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String courseId;
    private String name;
    private int credits;
    private CourseType type;
    private final List<Teacher> teachers         = new ArrayList<>();
    private final List<Lesson>  lessons          = new ArrayList<>();
    private final List<Student> enrolledStudents = new ArrayList<>();
    private int forYear;
    private String forMajor;

    public Course(String courseId, String name, int credits, CourseType type,
                  int forYear, String forMajor) {
        this.courseId = courseId;
        this.name     = name;
        this.credits  = credits;
        this.type     = type;
        this.forYear  = forYear;
        this.forMajor = forMajor;
    }

    public void addTeacher(Teacher teacher) {
        if (!teachers.contains(teacher)) {
            teachers.add(teacher);
            teacher.addCourse(this);
        }
    }

    public void addLesson(Lesson lesson) { lessons.add(lesson); }

    void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) enrolledStudents.add(student);
    }

    void unenrollStudent(Student student) { enrolledStudents.remove(student); }

    public CourseType getEffectiveType(String studentMajor) {
        if (type == CourseType.FREE_ELECTIVE) return CourseType.FREE_ELECTIVE;
        if (forMajor == null || forMajor.isEmpty()) return type;
        if (forMajor.equalsIgnoreCase(studentMajor)) return type;
        return CourseType.FREE_ELECTIVE;
    }

    public String getInfo() {
        return String.format("Course[%s] %s | %d cr | %s | Year %d | Major: %s | Teachers: %d | Students: %d",
                courseId, name, credits, type, forYear, forMajor, teachers.size(), enrolledStudents.size());
    }

    public String getCourseId()             { return courseId; }
    public String getName()                 { return name; }
    public int getCredits()                 { return credits; }
    public CourseType getType()             { return type; }
    public int getForYear()                 { return forYear; }
    public String getForMajor()             { return forMajor; }
    public List<Teacher> getTeachers()      { return Collections.unmodifiableList(teachers); }
    public List<Lesson>  getLessons()       { return Collections.unmodifiableList(lessons); }
    public List<Student> getStudents()      { return Collections.unmodifiableList(enrolledStudents); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course c = (Course) o;
        return Objects.equals(courseId, c.courseId);
    }

    @Override
    public int hashCode() { return Objects.hash(courseId); }

    @Override
    public String toString() {
        return String.format("Course{%s, '%s', %d cr, %s}", courseId, name, credits, type);
    }
}