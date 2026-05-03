package university.models;

import university.enums.LessonType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LessonType type;
    private final Teacher teacher;
    private final String room;
    private final LocalDateTime dateTime;
    private final Course course;

    public Lesson(LessonType type, Teacher teacher, String room,
                  LocalDateTime dateTime, Course course) {
        this.type     = type;
        this.teacher  = teacher;
        this.room     = room;
        this.dateTime = dateTime;
        this.course   = course;
    }

    public LessonType getType()     { return type; }
    public Teacher getTeacher()     { return teacher; }
    public String getRoom()         { return room; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Course getCourse()       { return course; }

    public String getInfo() {
        return String.format("Lesson{%s | %s | room=%s | %s | teacher=%s}",
                type, course.getName(), room, dateTime, teacher.getFullName());
    }

    @Override
    public String toString() { return getInfo(); }
}