package university.models;

import university.enums.Language;
import university.exceptions.CreditLimitExceededException;
import university.exceptions.MaxFailsReachedException;

import java.util.*;

public class Student extends User implements Comparable<Student> {

    private static final long serialVersionUID = 1L;
    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS   = 3;

    private double gpa;
    private int totalCredits;
    private int failCount;
    private final List<Course>           courses       = new ArrayList<>();
    private final Map<Course, Mark>      marks         = new HashMap<>();
    private final List<StudentOrg>       organizations = new ArrayList<>();
    private int year;       
    private String major;   
    public Student(String id, String firstName, String lastName,
                   String password, Language language,
                   int year, String major) {
        super(id, firstName, lastName, password, language);
        this.year  = year;
        this.major = major;
    }

    public void registerCourse(Course course) throws CreditLimitExceededException {
        int newTotal = totalCredits + course.getCredits();
        if (newTotal > MAX_CREDITS) {
            throw new CreditLimitExceededException(newTotal, MAX_CREDITS);
        }
        if (!courses.contains(course)) {
            courses.add(course);
            course.enrollStudent(this);
            totalCredits += course.getCredits();
            System.out.printf("[Registered] %s → %s%n", getFullName(), course.getName());
        }
    }

    public void dropCourse(Course course) {
        if (courses.remove(course)) {
            course.unenrollStudent(this);
            totalCredits -= course.getCredits();
            System.out.printf("[Dropped] %s dropped %s%n", getFullName(), course.getName());
        }
    }

    void receiveMark(Course course, Mark mark) throws MaxFailsReachedException {
        marks.put(course, mark);

        if (mark.isFail()) {
            failCount++;
            if (failCount > MAX_FAILS) {
                throw new MaxFailsReachedException(getFullName(), failCount);
            }
        }

        recalculateGpa();
    }

  
    private void recalculateGpa() {
        if (marks.isEmpty()) { gpa = 0; return; }
        double sum = marks.values().stream()
                .mapToDouble(Mark::getGpaPoints)
                .sum();
        gpa = sum / marks.size();
    }

 
    public void viewTranscript() {
        System.out.println("=== Transcript: " + getFullName() + " ===");
        System.out.printf("  Year: %d | Major: %s | GPA: %.2f | Fails: %d%n",
                year, major, gpa, failCount);
        System.out.println("  Courses:");
        for (Map.Entry<Course, Mark> e : marks.entrySet()) {
            System.out.printf("    %-35s %s%n", e.getKey().getName(), e.getValue());
        }
    }

    public void viewMarks() {
        System.out.println("Marks for " + getFullName() + ":");
        marks.forEach((c, m) -> System.out.printf("  %s: %s%n", c.getName(), m));
    }

    public void rateTeacher(Teacher teacher, double score) {
        if (score < 1 || score > 5) { System.out.println("Rating must be 1–5."); return; }
        teacher.receiveRating(score);
        System.out.printf("[Rating] %s rated %s: %.1f/5%n",
                getFullName(), teacher.getFullName(), score);
    }

    public void joinOrganization(StudentOrg org) {
        org.addMember(this);
        organizations.add(org);
    }

    @Override
    public String getInfo() {
        return String.format("Student{%s, year=%d, major=%s, gpa=%.2f, credits=%d, fails=%d}",
                getFullName(), year, major, gpa, totalCredits, failCount);
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa);
    }

    public double getGpa()                     { return gpa; }
    public int getTotalCredits()               { return totalCredits; }
    public int getFailCount()                  { return failCount; }
    public int getYear()                       { return year; }
    public String getMajor()                   { return major; }
    public List<Course> getCourses()           { return Collections.unmodifiableList(courses); }
    public Map<Course, Mark> getMarks()        { return Collections.unmodifiableMap(marks); }
    public List<StudentOrg> getOrganizations() { return Collections.unmodifiableList(organizations); }
}