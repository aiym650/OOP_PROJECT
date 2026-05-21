package university.models;

import university.enums.*;
import university.exceptions.SupervisorRequirementException;
import university.interfaces.Researcher;

import java.util.*;

public class Teacher extends Employee implements Researcher, Comparable<Teacher> {

    private static final long serialVersionUID = 1L;

    private TeacherPosition position;
    private final List<Course> courses          = new ArrayList<>();
    private double rating;                         
    private int ratingCount;
    private final List<ResearchPaper>   papers   = new ArrayList<>();
    private final List<ResearchProject> projects = new ArrayList<>();

    public Teacher(String id, String firstName, String lastName,
                   String password, Language language,
                   double salary, String department,
                   TeacherPosition position) {
        super(id, firstName, lastName, password, language, salary, department);
        this.position = position;
    }

    public void putMark(Student student, Course course, Mark mark) {
        if (!courses.contains(course)) {
            System.out.println("Error: " + getFullName() + " does not teach " + course.getName());
            return;
        }
        student.receiveMark(course, mark);
        System.out.printf("[Mark] %s → %s in %s: %s%n",
                getFullName(), student.getFullName(), course.getName(), mark);
    }

    public void generateMarkReport(Course course) {
        System.out.println("=== Mark Report: " + course.getName() + " ===");
        course.getStudents().forEach(s -> {
            Mark m = s.getMarks().get(course);
            System.out.printf("  %s: %s%n", s.getFullName(), m != null ? m : "No mark");
        });
    }

    public void viewStudents() {
        courses.forEach(c -> {
            System.out.println("Course: " + c.getName());
            c.getStudents().forEach(s -> System.out.println("  - " + s.getFullName()));
        });
    }

    public void viewCourses() {
        System.out.println("Courses taught by " + getFullName() + ":");
        courses.forEach(c -> System.out.println("  " + c));
    }

    public void sendComplaint(String description, UrgencyLevel urgency) {
        System.out.printf("[Complaint | %s] From %s: %s%n", urgency, getFullName(), description);
    }

    public void sendComplaint(String description, UrgencyLevel urgency, Manager dean) {
        System.out.printf("[Complaint | %s] From %s → Dean %s: %s%n",
                urgency, getFullName(), dean.getFullName(), description);
        sendMessage(dean, "[COMPLAINT | " + urgency + "] " + description);
    }

    public void receiveRating(double score) {
        rating = (rating * ratingCount + score) / (++ratingCount);
    }

    @Override
    public int calculateHIndex() {
        List<Integer> sorted = papers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();
        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        papers.stream()
              .sorted(comparator)
              .forEach(p -> System.out.println(p.getCitation(Format.PLAIN_TEXT)));
    }

    @Override
    public List<ResearchProject> getProjects() { return Collections.unmodifiableList(projects); }

    @Override
    public List<ResearchPaper> getPapers()     { return Collections.unmodifiableList(papers); }

    @Override
    public void addPaper(ResearchPaper paper)  { papers.add(paper); }

    @Override
    public void addToProject(ResearchProject project) { projects.add(project); }

    public void validateSupervisorQualification() throws SupervisorRequirementException {
        int h = calculateHIndex();
        if (h < 3) throw new SupervisorRequirementException(getFullName(), h);
    }

    @Override
    public int compareTo(Teacher other) {
        return Double.compare(other.rating, this.rating);
    }

    @Override
    public String getInfo() {
        return String.format("Teacher{%s, pos=%s, rating=%.2f, courses=%d, papers=%d, h-index=%d}",
                getFullName(), position, rating, courses.size(), papers.size(), calculateHIndex());
    }

    public TeacherPosition getPosition()          { return position; }
    public void setPosition(TeacherPosition p)    { this.position = p; }
    public double getRating()                     { return rating; }
    public List<Course> getCourses()              { return Collections.unmodifiableList(courses); }
    public void addCourse(Course course)          { courses.add(course); }
}