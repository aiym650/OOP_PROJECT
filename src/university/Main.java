package university;

import university.enums.*;
import university.exceptions.*;
import university.models.*;
import university.patterns.AuthService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     University System — Part B Demo      ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        System.out.println("=== 1. Singleton: AuthService ===");
        AuthService auth = AuthService.getInstance();
        AuthService auth2 = AuthService.getInstance();
        System.out.println("Same instance: " + (auth == auth2)); 

        System.out.println("\n=== 2. Factory: Creating users ===");
        Teacher prof = new Teacher("t001", "Asylzhan", "Izbassar",
                "pass123", Language.EN, 180_000, "CS Dept", TeacherPosition.PROFESSOR);

        Student alice = new Student("s001", "Alice", "Abenova",
                "alice", Language.EN, 2, "CS");

        Student bob = new Student("s002", "Bob", "Seitkali",
                "bob", Language.RU, 1, "SE");

        GraduateStudent gradStudent = new GraduateStudent("g001", "Dana", "Nurova",
                "dana", Language.KZ, 5, "CS", DegreeType.MASTER);

        Admin admin = new Admin("a001", "Admin", "System",
                "adminpass", Language.EN, 250_000, "IT");

        Manager manager = new Manager("m001", "Zarina", "Bekova",
                "mgrpass", Language.RU, 200_000, "Registrar", ManagerType.OR);

        TechSupportSpecialist support = new TechSupportSpecialist(
                "ts001", "Timur", "Aliev", "techpass",
                Language.EN, 90_000, "IT Support");

        auth.registerUser(prof);
        auth.registerUser(alice);
        auth.registerUser(bob);
        auth.registerUser(admin);

        System.out.println("Users registered: " + auth.getUserCount());

        System.out.println("\n=== 3. Authentication ===");
        try {
            auth.login("s001", "alice");  
            auth.logout();
            auth.login("s001", "wrong");  
        } catch (AuthenticationException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== 4. Course & Lesson ===");
        Course oop = new Course("CS101", "OOP in Java", 6,
                CourseType.MAJOR, 2, "CS");
        Course db  = new Course("CS202", "Databases", 5,
                CourseType.MAJOR, 2, "CS");

        oop.addTeacher(prof);
        oop.addLesson(new Lesson(LessonType.LECTURE, prof, "Room 204",
                LocalDateTime.of(2025, 9, 10, 9, 0), oop));
        oop.addLesson(new Lesson(LessonType.PRACTICE, prof, "Lab 101",
                LocalDateTime.of(2025, 9, 12, 11, 0), oop));

        System.out.println(oop.getInfo());

        System.out.println("\n=== 5. Course Registration ===");
        try {
            alice.registerCourse(oop);
            alice.registerCourse(db);
            System.out.println("Total credits: " + alice.getTotalCredits());

            Course dummy = new Course("XX999", "Dummy", 20, CourseType.FREE_ELECTIVE, 1, "CS");
            alice.registerCourse(dummy); 
        } catch (CreditLimitExceededException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== 6. Marks ===");
        bob.registerCourse(oop);
        prof.putMark(alice, oop, new Mark(80, 75, 90));
        prof.putMark(bob,   oop, new Mark(40, 45, 35)); // F → fail
        alice.viewTranscript();
        bob.viewTranscript();

        System.out.println("\n=== 7. Teacher Rating ===");
        alice.rateTeacher(prof, 5.0);
        bob.rateTeacher(prof, 3.0);
        System.out.printf("Prof rating: %.2f%n", prof.getRating());

        System.out.println("\n=== 8. Research ===");
        ResearchPaper paper1 = new ResearchPaper("Deep Learning in NLP",
                "IEEE Journal", 12, LocalDate.of(2022, 3, 15), "10.1109/001");
        ResearchPaper paper2 = new ResearchPaper("Graph Neural Networks",
                "ACM Computing", 8, LocalDate.of(2023, 6, 1), "10.1145/002");
        ResearchPaper paper3 = new ResearchPaper("Transformer Architectures",
                "Nature AI", 20, LocalDate.of(2021, 1, 10), "10.1038/003");

        paper1.setCitations(15);
        paper2.setCitations(8);
        paper3.setCitations(3);

        paper1.addAuthor(prof);
        paper2.addAuthor(prof);
        paper3.addAuthor(prof);

        prof.addPaper(paper1);
        prof.addPaper(paper2);
        prof.addPaper(paper3);

        System.out.println("Prof h-index: " + prof.calculateHIndex()); 

        System.out.println("\n=== 9. Graduate Student & Supervisor ===");
        try {
            gradStudent.setSupervisor(prof); 
            System.out.println("Supervisor set: " + gradStudent.getSupervisor().getFullName());
        } catch (SupervisorRequirementException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== 10. Research Project ===");
        ResearchProject project = new ResearchProject("AI in Education");
        project.addParticipant(prof);
        project.addParticipant(gradStudent);
        project.addPaper(paper1);
        System.out.println(project);

        System.out.println("\n=== 11. Citation Formats ===");
        System.out.println("PLAIN TEXT:");
        System.out.println(paper1.getCitation(Format.PLAIN_TEXT));
        System.out.println("BIBTEX:");
        System.out.println(paper1.getCitation(Format.BIBTEX));

        System.out.println("\n=== 12. Observer: Journal Subscriptions ===");
        UniversityJournal journal = new UniversityJournal("IEEE Transactions on CS");
        journal.subscribe(alice);
        journal.subscribe(gradStudent);
        journal.publishResearchPaper(paper2); 

        System.out.println("\n=== 13. Manager Reports ===");
        manager.assignCourse(prof, db);
        manager.createAcademicReport(java.util.List.of(alice, bob));

        System.out.println("\n=== 14. Tech Support ===");
        SupportRequest req = new SupportRequest(
                "Email not working", alice, UrgencyLevel.HIGH);
        support.addRequest(req);
        support.viewNewRequests();
        support.acceptRequest(req);

        System.out.println("\n=== 15. Admin Logs ===");
        admin.writeLog("System started");
        admin.writeLog("User alice logged in");
        admin.viewLogs();

        System.out.println("\n=== 16. Messaging ===");
        prof.sendMessage(manager, "Please approve Alice's registration.");
        manager.viewMessages();

        System.out.println("\n=== 17. News ===");
        manager.createNews("Semester Start", "Classes begin Sep 1", "Academic");

        System.out.println("\n=== 18. Student Organization ===");
        StudentOrg roboticsClub = new StudentOrg("Robotics Club", alice);
        alice.joinOrganization(roboticsClub);
        bob.joinOrganization(roboticsClub);
        System.out.println(roboticsClub);

        System.out.println("\n=== 19. Complaint ===");
        prof.sendComplaint("Classroom heating is broken", UrgencyLevel.MEDIUM);

        System.out.println("\n=== 20. Sorting Research Papers ===");
        prof.getPapers().stream()
            .sorted(ResearchPaper.BY_CITATIONS)
            .forEach(p -> System.out.printf("  [%d citations] %s%n",
                    p.getCitations(), p.getTitle()));

        System.out.println("\n✓ All Part B models demonstrated successfully.");
    }
}
