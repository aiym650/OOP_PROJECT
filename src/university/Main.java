package university;

import university.enums.*;
import university.exceptions.*;
import university.models.*;
import university.patterns.AuthService;
import university.patterns.DataStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    
    private static final AuthService auth = AuthService.getInstance();
    private static final Scanner sc = new Scanner(System.in);

    private static final List<Course>         courses  = new ArrayList<>();
    private static final List<Student>        students = new ArrayList<>();
    private static final List<SupportRequest> requests = new ArrayList<>();
    private static final List<User>           allUsers = new ArrayList<>();

    
    private static Teacher             prof;
    private static Student             alice, bob;
    private static GraduateStudent     dana;
    private static Admin               admin;
    private static Manager             manager, dean;
    private static TechSupportSpecialist support;

    public static void main(String[] args) throws Exception {
        setupData();
        printBanner();
        printAccounts();

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> loginFlow();
                case "0" -> {
                    DataStorage.save(allUsers);
                    running = false;
                }
                default  -> System.out.println("  Unknown option.");
            }
        }
        System.out.println("\n  Goodbye! Data saved. System shutting down.");
        sc.close();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SETUP
    // ════════════════════════════════════════════════════════════════════════
    private static void setupData() throws Exception {

        // ── Users ────────────────────────────────────────────────────────────
        prof = new Teacher("t001", "Asylzhan", "Izbassar",
                "pass123", Language.EN, 180_000, "CS Dept", TeacherPosition.PROFESSOR);

        alice = new Student("s001", "Alice", "Abenova",
                "alice", Language.EN, 2, "CS");
        bob   = new Student("s002", "Bob",   "Seitkali",
                "bob",   Language.RU, 1, "SE");
        dana  = new GraduateStudent("g001", "Dana", "Nurova",
                "dana", Language.KZ, 5, "CS", DegreeType.MASTER);

        admin   = new Admin("a001", "Admin", "System",
                "adminpass", Language.EN, 250_000, "IT");
        manager = new Manager("m001", "Zarina", "Bekova",
                "mgrpass", Language.RU, 200_000, "Registrar", ManagerType.OR);
        dean    = new Manager("d001", "Bakyt", "Dzhaksybekov",
                "deanpass", Language.EN, 300_000, "CS Faculty", ManagerType.DEAN);
        support = new TechSupportSpecialist("ts001", "Timur", "Aliev",
                "techpass", Language.EN, 90_000, "IT Support");

        for (User u : List.of(prof, alice, bob, dana, admin, manager, dean, support)) {
            auth.registerUser(u);
            allUsers.add(u);
        }
        students.addAll(List.of(alice, bob, dana));

        // ── Courses ──────────────────────────────────────────────────────────
        Course oop = new Course("CS101", "OOP in Java",  6, CourseType.MAJOR, 2, "CS");
        Course db  = new Course("CS202", "Databases",    5, CourseType.MAJOR, 2, "CS");
        Course ml  = new Course("CS303", "Machine Learning", 6, CourseType.MINOR, 3, "CS");

        oop.addTeacher(prof);
        db.addTeacher(prof);
        oop.addLesson(new Lesson(LessonType.LECTURE,  prof, "Room 204",
                LocalDateTime.of(2025, 9, 10, 9, 0),  oop));
        oop.addLesson(new Lesson(LessonType.PRACTICE, prof, "Lab 101",
                LocalDateTime.of(2025, 9, 12, 11, 0), oop));
        courses.addAll(List.of(oop, db, ml));

        
        alice.registerCourse(oop);
        alice.registerCourse(db);
        bob.registerCourse(oop);

        // ── Research papers for prof ─────────────────────────────────────────
        ResearchPaper p1 = new ResearchPaper("Deep Learning in NLP",
                "IEEE Journal", 12, LocalDate.of(2022, 3, 15), "10.1109/001");
        ResearchPaper p2 = new ResearchPaper("Graph Neural Networks",
                "ACM Computing", 8, LocalDate.of(2023, 6, 1),  "10.1145/002");
        ResearchPaper p3 = new ResearchPaper("Transformer Architectures",
                "Nature AI", 20, LocalDate.of(2021, 1, 10),    "10.1038/003");
        p1.setCitations(15); p2.setCitations(8); p3.setCitations(3);
        p1.addAuthor(prof);  p2.addAuthor(prof); p3.addAuthor(prof);
        prof.addPaper(p1);   prof.addPaper(p2);  prof.addPaper(p3);

        // supervisor
        dana.setSupervisor(prof);

        // journal
        UniversityJournal journal = new UniversityJournal("IEEE Transactions on CS");
        journal.setNewsManager(manager); 
        journal.subscribe(alice);
        journal.subscribe(dana);
        
     
        if (DataStorage.hasSavedData()) {
            List<User> saved = DataStorage.load();
            for (User u : saved) {
                allUsers.replaceAll(e -> e.getId().equals(u.getId()) ? u : e);
                students.replaceAll(e -> e.getId().equals(u.getId()) ? (Student) u : e);
                auth.updateUser(u); 
                switch (u.getId()) {
                    case "t001" -> prof    = (Teacher) u;
                    case "s001" -> alice   = (Student) u;
                    case "s002" -> bob     = (Student) u;
                    case "g001" -> dana    = (GraduateStudent) u;
                    case "a001" -> admin   = (Admin) u;
                    case "m001" -> manager = (Manager) u;
                    case "d001" -> dean    = (Manager) u;
                    case "ts001"-> support = (TechSupportSpecialist) u;
                }
            }
            System.out.println("  [Storage] Session restored!");
        }
        
        
        SupportRequest r1 = new SupportRequest("Projector broken in Room 204", alice, UrgencyLevel.HIGH);
        SupportRequest r2 = new SupportRequest("Printer offline in Lab 101",   bob,   UrgencyLevel.LOW);
        support.addRequest(r1);
        support.addRequest(r2);
        requests.addAll(List.of(r1, r2));

        admin.writeLog("System initialized");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LOGIN
    // ════════════════════════════════════════════════════════════════════════
    private static void loginFlow() {
        System.out.println("\n  ── Login ─────────────────────────");
        System.out.print("  User ID  : ");  String id   = sc.nextLine().trim();
        System.out.print("  Password : ");  String pass = sc.nextLine().trim();
        try {
            User user = auth.login(id, pass);
            admin.writeLog("Login: " + user.getFullName());

            if      (user instanceof Student s && !(user instanceof GraduateStudent))
                                                          studentMenu(s);
            else if (user instanceof GraduateStudent g)  gradStudentMenu(g);
            else if (user instanceof Teacher t)           teacherMenu(t);
            else if (user instanceof Admin a)             adminMenu(a);
            else if (user instanceof Manager m)           managerMenu(m);
            else if (user instanceof TechSupportSpecialist ts) techSupportMenu(ts);
            else System.out.println("  No menu for this role.");

        } catch (AuthenticationException e) {
            System.out.println("  Login failed: " + e.getMessage());
        }
        auth.logout();
        System.out.println("  Logged out.\n");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  STUDENT MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void studentMenu(Student s) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  STUDENT: " + pad(s.getFullName(), 22) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. View all courses             ║");
            System.out.println("║  2. Register for a course        ║");
            System.out.println("║  3. Drop a course                ║");
            System.out.println("║  4. View my marks                ║");
            System.out.println("║  5. View transcript              ║");
            System.out.println("║  6. View teacher info            ║");
            System.out.println("║  7. Rate a teacher               ║");
            System.out.println("║  0. Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> listCourses();
                case "2" -> {
                    listCourses();
                    System.out.print("  Enter Course ID: ");
                    String cid = sc.nextLine().trim();
                    findCourse(cid).ifPresentOrElse(c -> {
                        try {
                            s.registerCourse(c);
                        } catch (CreditLimitExceededException e) {
                            System.out.println("  Error: " + e.getMessage());
                        }
                    }, () -> System.out.println("  Course not found."));
                }
                case "3" -> {
                    System.out.println("  Your courses:");
                    s.getCourses().forEach(c -> System.out.println("    " + c.getCourseId() + " - " + c.getName()));
                    System.out.print("  Enter Course ID to drop: ");
                    String cid = sc.nextLine().trim();
                    findCourse(cid).ifPresentOrElse(s::dropCourse,
                            () -> System.out.println("  Course not found."));
                }
                case "4" -> s.viewMarks();
                case "5" -> s.viewTranscript();
                case "6" -> {
                    System.out.println("  Your courses:");
                    s.getCourses().forEach(c -> {
                        System.out.println("  Course: " + c.getName());
                        c.getTeachers().forEach(t ->
                            System.out.printf("    Teacher: %s | pos: %s | rating: %.2f%n",
                                t.getFullName(), t.getPosition(), t.getRating()));
                    });
                }
                case "7" -> {
                    System.out.print("  Teacher ID (e.g. t001): ");
                    String tid = sc.nextLine().trim();
                    findTeacher(tid).ifPresentOrElse(t -> {
                        System.out.print("  Score (1-5): ");
                        try {
                            double score = Double.parseDouble(sc.nextLine().trim());
                            s.rateTeacher(t, score);
                        } catch (NumberFormatException e) {
                            System.out.println("  Invalid score.");
                        }
                    }, () -> System.out.println("  Teacher not found."));
                }
                case "0" -> active = false;
                default  -> System.out.println("  Unknown option.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GRADUATE STUDENT MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void gradStudentMenu(GraduateStudent g) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  GRAD STUDENT: " + pad(g.getFullName(), 17) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. View all courses             ║");
            System.out.println("║  2. Register for a course        ║");
            System.out.println("║  3. View marks & transcript      ║");
            System.out.println("║  4. View supervisor              ║");
            System.out.println("║  5. View my research papers      ║");
            System.out.println("║  6. My h-index                   ║");
            System.out.println("║  0. Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> listCourses();
                case "2" -> {
                    listCourses();
                    System.out.print("  Enter Course ID: ");
                    String cid = sc.nextLine().trim();
                    findCourse(cid).ifPresentOrElse(c -> {
                        try { g.registerCourse(c); }
                        catch (CreditLimitExceededException e) {
                            System.out.println("  Error: " + e.getMessage());
                        }
                    }, () -> System.out.println("  Course not found."));
                }
                case "3" -> { g.viewMarks(); g.viewTranscript(); }
                case "4" -> {
                    Teacher sv = g.getSupervisor();
                    if (sv == null) System.out.println("  No supervisor assigned.");
                    else System.out.printf("  Supervisor: %s | h-index: %d%n",
                            sv.getFullName(), sv.calculateHIndex());
                }
                case "5" -> {
                    List<ResearchPaper> papers = g.getPapers();
                    if (papers.isEmpty()) System.out.println("  No papers yet.");
                    else papers.forEach(p -> System.out.println("  " + p));
                }
                case "6" -> System.out.println("  Your h-index: " + g.calculateHIndex());
                case "0" -> active = false;
                default  -> System.out.println("  Unknown option.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TEACHER MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void teacherMenu(Teacher t) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  TEACHER: " + pad(t.getFullName(), 23) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. View my courses              ║");
            System.out.println("║  2. View enrolled students       ║");
            System.out.println("║  3. Put mark for student         ║");
            System.out.println("║  4. Generate mark report         ║");
            System.out.println("║  5. Send complaint to dean       ║");
            System.out.println("║  6. My research papers           ║");
            System.out.println("║  7. My h-index                   ║");
            System.out.println("║  8. Send message                 ║");
            System.out.println("║  9. View messages                ║");
            System.out.println("║  0. Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> t.viewCourses();
                case "2" -> t.viewStudents();
                case "3" -> {
                    System.out.print("  Student ID: ");
                    String sid = sc.nextLine().trim();
                    System.out.print("  Course ID:  ");
                    String cid = sc.nextLine().trim();
                    Optional<Student> student = findStudent(sid);
                    Optional<Course>  course  = findCourse(cid);
                    if (student.isEmpty()) { System.out.println("  Student not found."); break; }
                    if (course.isEmpty())  { System.out.println("  Course not found.");  break; }
                    try {
                        System.out.print("  Attestation 1 (0-30): ");
                        double a1 = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("  Attestation 2 (0-30): ");
                        double a2 = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("  Final exam  (0-40): ");
                        double fn = Double.parseDouble(sc.nextLine().trim());
                        t.putMark(student.get(), course.get(), new Mark(a1, a2, fn));
                    } catch (NumberFormatException e) {
                        System.out.println("  Invalid number.");
                    }
                }
                case "4" -> {
                    System.out.print("  Course ID: ");
                    findCourse(sc.nextLine().trim()).ifPresentOrElse(
                            t::generateMarkReport,
                            () -> System.out.println("  Course not found."));
                }
                case "5" -> {
                    System.out.print("  Complaint text: ");
                    String desc = sc.nextLine().trim();
                    System.out.println("  Urgency: 1=LOW  2=MEDIUM  3=HIGH");
                    System.out.print("  Choice: ");
                    UrgencyLevel lvl = switch (sc.nextLine().trim()) {
                        case "1" -> UrgencyLevel.LOW;
                        case "3" -> UrgencyLevel.HIGH;
                        default  -> UrgencyLevel.MEDIUM;
                    };
                    t.sendComplaint(desc, lvl);
                    t.sendMessage(dean, "[COMPLAINT | " + lvl + "] " + desc);
                    System.out.println("  Complaint sent to Dean.");
                }
                case "6" -> {
                    System.out.println("  Sort by: 1=Citations  2=Date  3=Pages");
                    System.out.print("  Choice: ");
                    Comparator<ResearchPaper> cmp = switch (sc.nextLine().trim()) {
                        case "2" -> ResearchPaper.BY_DATE;
                        case "3" -> ResearchPaper.BY_TITLE; 
                        default  -> ResearchPaper.BY_CITATIONS;
                    };
                    t.printPapers(cmp);
                }
                case "7" -> System.out.println("  h-index: " + t.calculateHIndex());
                case "8" -> {
                    System.out.print("  Recipient ID: ");
                    String rid = sc.nextLine().trim();
                    findUser(rid).ifPresentOrElse(r -> {
                        if (r instanceof Employee emp) {
                            System.out.print("  Message: ");
                            t.sendMessage(emp, sc.nextLine().trim());
                        } else System.out.println("  Recipient is not an employee.");
                    }, () -> System.out.println("  User not found."));
                }
                case "9" -> t.viewMessages();
                case "0" -> active = false;
                default  -> System.out.println("  Unknown option.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ADMIN MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void adminMenu(Admin a) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  ADMIN                           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. View all users               ║");
            System.out.println("║  2. View system logs             ║");
            System.out.println("║  3. Add user (Student)           ║");
            System.out.println("║  4. Remove user by ID            ║");
            System.out.println("║  0. Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> allUsers.forEach(u ->
                        System.out.printf("  [%s] %s — %s%n",
                                u.getId(), u.getFullName(), u.getClass().getSimpleName()));
                case "2" -> a.viewLogs();
                case "3" -> {
                    System.out.print("  ID: ");       String id  = sc.nextLine().trim();
                    System.out.print("  First name: ");String fn  = sc.nextLine().trim();
                    System.out.print("  Last name: "); String ln  = sc.nextLine().trim();
                    System.out.print("  Password: ");  String pw  = sc.nextLine().trim();
                    System.out.print("  Year: ");      int yr;
                    try { yr = Integer.parseInt(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println("  Invalid year."); break; }
                    System.out.print("  Major: ");     String mj  = sc.nextLine().trim();
                    Student ns = new Student(id, fn, ln, pw, Language.EN, yr, mj);
                    auth.registerUser(ns);
                    allUsers.add(ns);
                    students.add(ns);
                    a.writeLog("Admin added student: " + id);
                    System.out.println("  Student " + ns.getFullName() + " added.");
                }
                case "4" -> {
                    System.out.print("  User ID to remove: ");
                    String rid = sc.nextLine().trim();
                    findUser(rid).ifPresentOrElse(u -> {
                        allUsers.remove(u);
                        students.remove(u);
                        a.writeLog("Admin removed user: " + rid);
                        System.out.println("  Removed: " + u.getFullName());
                    }, () -> System.out.println("  User not found."));
                }
                case "0" -> active = false;
                default  -> System.out.println("  Unknown option.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  MANAGER MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void managerMenu(Manager m) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  MANAGER: " + pad(m.getFullName(), 23) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Assign teacher to course     ║");
            System.out.println("║  2. Approve student registration ║");
            System.out.println("║  3. Academic report (by GPA)     ║");
            System.out.println("║  4. Students alphabetically      ║");
            System.out.println("║  5. Create news                  ║");
            System.out.println("║  6. View support requests        ║");
            System.out.println("║  7. View messages                ║");
            System.out.println("║  0. Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> {
                    System.out.print("  Teacher ID: "); String tid = sc.nextLine().trim();
                    System.out.print("  Course ID:  "); String cid = sc.nextLine().trim();
                    findTeacher(tid).ifPresentOrElse(t ->
                        findCourse(cid).ifPresentOrElse(c -> m.assignCourse(t, c),
                            () -> System.out.println("  Course not found.")),
                        () -> System.out.println("  Teacher not found."));
                }
                case "2" -> {
                    System.out.print("  Student ID: "); String sid = sc.nextLine().trim();
                    System.out.print("  Course ID:  "); String cid = sc.nextLine().trim();
                    findStudent(sid).ifPresentOrElse(s ->
                        findCourse(cid).ifPresentOrElse(c -> m.approveRegistration(s, c),
                            () -> System.out.println("  Course not found.")),
                        () -> System.out.println("  Student not found."));
                }
                case "3" -> m.createAcademicReport(students);
                case "4" -> {
                    System.out.println("  Students (alphabetical):");
                    m.viewStudentsAlphabetically(students);
                }
                case "5" -> {
                    System.out.print("  Title:   "); String title   = sc.nextLine().trim();
                    System.out.print("  Content: "); String content = sc.nextLine().trim();
                    System.out.print("  Topic (e.g. Research/Academic): ");
                    String topic = sc.nextLine().trim();
                    m.createNews(title, content, topic);
                }
                case "6" -> m.viewRequests(requests);
                case "7" -> m.viewMessages();
                case "0" -> active = false;
                default  -> System.out.println("  Unknown option.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TECH SUPPORT MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void techSupportMenu(TechSupportSpecialist ts) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  TECH SUPPORT                    ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. View new requests            ║");
            System.out.println("║  2. Accept request               ║");
            System.out.println("║  3. Reject request               ║");
            System.out.println("║  4. Mark request as Done         ║");
            System.out.println("║  5. View all requests            ║");
            System.out.println("║  0. Logout                       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            String tsChoice = sc.nextLine().trim();
            switch (tsChoice) {
                case "1" -> ts.viewNewRequests();
                case "2", "3", "4" -> {
                    System.out.println("  All requests:");
                    for (int i = 0; i < requests.size(); i++)
                        System.out.printf("  [%d] %s%n", i + 1, requests.get(i));
                    System.out.print("  Request number: ");
                    try {
                        int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (idx < 0 || idx >= requests.size()) {
                            System.out.println("  Invalid number."); break;
                        }
                        SupportRequest req = requests.get(idx);
                        switch (tsChoice) {
                            case "2" -> ts.acceptRequest(req);
                            case "3" -> ts.rejectRequest(req);
                            case "4" -> ts.markDone(req);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("  Invalid input.");
                    }
                }
                case "5" -> requests.forEach(r -> System.out.println("  " + r));
                case "0" -> active = false;
                default  -> System.out.println("  Unknown option.");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║        KBTU University Information System        ║");
        System.out.println("║              Part C — Interactive Demo           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    private static void printMainMenu() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│  1. Login                            │");
        System.out.println("│  0. Exit                             │");
        System.out.println("└──────────────────────────────────────┘");
        System.out.print("  Choice: ");
    }

    private static void printAccounts() {
        System.out.println("\n  ── Demo Accounts ─────────────────────────────────");
        System.out.println("  ID      Password    Role");
        System.out.println("  ─────────────────────────────────────────────────");
        System.out.println("  t001    pass123     Teacher (Professor)");
        System.out.println("  s001    alice       Student (Alice)");
        System.out.println("  s002    bob         Student (Bob)");
        System.out.println("  g001    dana        Graduate Student (Dana)");
        System.out.println("  a001    adminpass   Admin");
        System.out.println("  m001    mgrpass     Manager (OR)");
        System.out.println("  d001    deanpass    Manager (Dean)");
        System.out.println("  ts001   techpass    Tech Support");
        System.out.println("  ─────────────────────────────────────────────────");
    }

    private static void listCourses() {
        System.out.println("  Available courses:");
        courses.forEach(c -> System.out.printf("  %-8s %-30s %d credits [%s]%n",
                c.getCourseId(), c.getName(), c.getCredits(), c.getType()));
    }

    private static Optional<Course> findCourse(String id) {
        return courses.stream().filter(c -> c.getCourseId().equalsIgnoreCase(id)).findFirst();
    }

    private static Optional<Student> findStudent(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
    }

    private static Optional<Teacher> findTeacher(String id) {
        return allUsers.stream()
                .filter(u -> u instanceof Teacher && u.getId().equalsIgnoreCase(id))
                .map(u -> (Teacher) u).findFirst();
    }

    private static Optional<User> findUser(String id) {
        return allUsers.stream().filter(u -> u.getId().equalsIgnoreCase(id)).findFirst();
    }

    private static String pad(String s, int len) {
        if (s.length() >= len) return s.substring(0, len);
        return s + " ".repeat(len - s.length());
    }
}