package university.enums;
public enum TeacherPosition {
    TUTOR("Tutor"),
    LECTOR("Lecturer"),
    SENIOR_LECTOR("Senior Lecturer"),
    PROFESSOR("Professor");

    private final String title;

    TeacherPosition(String title) { this.title = title; }

    public String getTitle() { return title; }

    @Override
    public String toString() { return title; }
}