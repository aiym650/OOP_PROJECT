package university.models;

import university.enums.Language;
import university.enums.ManagerType;

import java.util.*;

public class Manager extends Employee {

    private static final long serialVersionUID = 1L;

    private ManagerType type;
    private final List<News> newsList = new ArrayList<>();

    public Manager(String id, String firstName, String lastName,
                   String password, Language language,
                   double salary, String department, ManagerType type) {
        super(id, firstName, lastName, password, language, salary, department);
        this.type = type;
    }

    public void assignCourse(Teacher teacher, Course course) {
        course.addTeacher(teacher);
        System.out.printf("[Assign] %s assigned to course %s%n",
                teacher.getFullName(), course.getName());
    }

    public void approveRegistration(Student student, Course course) {
        System.out.printf("[Approved] %s registration for %s%n",
                student.getFullName(), course.getName());
    }

    public void createNews(String title, String content, String topic) {
        News news = new News(title, content, topic, this);
        newsList.add(news);
        System.out.println("[News created] " + title);
    }

    public void manageNews() {
        System.out.println("=== News managed by " + getFullName() + " ===");
        newsList.forEach(System.out::println);
    }

    public void createAcademicReport(List<Student> students) {
        System.out.println("=== Academic Report ===");
        students.stream()
                .sorted()                
                .forEach(s -> System.out.printf("  %s GPA=%.2f%n",
                        s.getFullName(), s.getGpa()));
    }

    public void viewStudentsAlphabetically(List<Student> students) {
        students.stream()
                .sorted(Comparator.comparing(User::getLastName)
                        .thenComparing(User::getFirstName))
                .forEach(s -> System.out.println("  " + s.getFullName()));
    }

    public void viewRequests(List<SupportRequest> requests) {
        System.out.println("=== Pending Requests ===");
        requests.forEach(System.out::println);
    }

    @Override
    public String getInfo() {
        return String.format("Manager{%s, type=%s, dept=%s}", getFullName(), type, getDepartment());
    }

    public ManagerType getType() { return type; }
}