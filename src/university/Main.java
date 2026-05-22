package university;

import university.enums.*;
import university.exceptions.*;
import university.models.*;
import university.patterns.AuthService;
import university.patterns.DataStorage;
import university.util.Messages;
import university.util.Messages.Key;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    private static final AuthService auth = AuthService.getInstance();
    private static final Scanner sc = new Scanner(System.in);

    private static final List<Course>         courses  = new ArrayList<>();
    private static final List<Student>        students = new ArrayList<>();
    private static final List<Teacher>        teachers = new ArrayList<>();
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
                case "L", "l" -> changeLanguage(null);
                case "0" -> {
                    DataStorage.getInstance().save(allUsers);
                    running = false;
                }
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
        System.out.println("\n  Goodbye! Data saved. System shutting down.");
        sc.close();
    }

    // ════════════════════════════════════════════════════════════════════════
    // SETUP
    // ════════════════════════════════════════════════════════════════════════
    private static void setupData() throws Exception {
        prof    = new Teacher("t001", "Asylzhan", "Izbassar", "pass123", Language.EN,
                              180_000, "CS Dept", TeacherPosition.PROFESSOR);
        alice   = new Student("s001", "Alice", "Abenova", "alice", Language.EN, 2, "CS");
        bob     = new Student("s002", "Bob", "Seitkali", "bob", Language.RU, 1, "SE");
        dana    = new GraduateStudent("g001", "Dana", "Nurova", "dana", Language.KZ, 5, "CS", DegreeType.MASTER);
        admin   = new Admin("a001", "Admin", "System", "adminpass", Language.EN, 250_000, "IT");
        manager = new Manager("m001", "Zarina", "Bekova", "mgrpass", Language.RU, 200_000, "Registrar", ManagerType.OR);
        dean    = new Manager("d001", "Bakyt", "Dzhaksybekov", "deanpass", Language.EN, 300_000, "CS Faculty", ManagerType.DEAN);
        support = new TechSupportSpecialist("ts001", "Timur", "Aliev", "techpass", Language.EN, 90_000, "IT Support");

        for (User u : List.of(prof, alice, bob, dana, admin, manager, dean, support)) {
            auth.registerUser(u);
            allUsers.add(u);
        }
        students.addAll(List.of(alice, bob, dana));
        teachers.add(prof);

        Course oop = new Course("CS101", "OOP in Java",       6, CourseType.MAJOR, 2, "CS");
        Course db  = new Course("CS202", "Databases",         5, CourseType.MAJOR, 2, "CS");
        Course ml  = new Course("CS303", "Machine Learning",  6, CourseType.MINOR, 3, "CS");
        oop.addTeacher(prof);
        db.addTeacher(prof);
        oop.addLesson(new Lesson(LessonType.LECTURE,  prof, "Room 204", LocalDateTime.of(2025, 9, 10, 9,  0), oop));
        oop.addLesson(new Lesson(LessonType.PRACTICE, prof, "Lab 101",  LocalDateTime.of(2025, 9, 12, 11, 0), oop));
        courses.addAll(List.of(oop, db, ml));
        alice.registerCourse(oop);
        alice.registerCourse(db);
        bob.registerCourse(oop);

        ResearchPaper p1 = new ResearchPaper("Deep Learning in NLP",       "IEEE Journal",   12, LocalDate.of(2022, 3,  15), "10.1109/001");
        ResearchPaper p2 = new ResearchPaper("Graph Neural Networks",       "ACM Computing",   8, LocalDate.of(2023, 6,   1), "10.1145/002");
        ResearchPaper p3 = new ResearchPaper("Transformer Architectures",   "Nature AI",      20, LocalDate.of(2021, 1,  10), "10.1038/003");
        p1.setCitations(15); p2.setCitations(8); p3.setCitations(3);
        p1.addAuthor(prof);  p2.addAuthor(prof); p3.addAuthor(prof);
        prof.addPaper(p1);   prof.addPaper(p2);  prof.addPaper(p3);

        dana.setSupervisor(prof);

        UniversityJournal journal = new UniversityJournal("IEEE Transactions on CS");
        journal.setNewsManager(manager);
        journal.subscribe(alice);
        journal.subscribe(dana);

        if (DataStorage.getInstance().hasSavedData()) {
            List<User> saved = DataStorage.getInstance().load();
            for (User u : saved) {
                allUsers.replaceAll(e -> e.getId().equals(u.getId()) ? u : e);
                students.replaceAll(e -> e.getId().equals(u.getId()) ? (Student) u : e);
                auth.updateUser(u);
                switch (u.getId()) {
                    case "t001" -> { prof    = (Teacher) u; if (!teachers.contains(prof)) teachers.add(prof); }
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
        support.addRequest(r1); support.addRequest(r2);
        requests.addAll(List.of(r1, r2));
        admin.writeLog("System initialized");
    }

    // ════════════════════════════════════════════════════════════════════════
    // LANGUAGE SWITCH
    // ════════════════════════════════════════════════════════════════════════
    private static void changeLanguage(User user) {
        System.out.println("\n  1. English");
        System.out.println("  2. Русский");
        System.out.println("  3. Қазақша");
        System.out.print(Messages.m(Key.CHOICE));
        Language chosen = switch (sc.nextLine().trim()) {
            case "2" -> Language.RU;
            case "3" -> Language.KZ;
            default  -> Language.EN;
        };
        Messages.setLanguage(chosen);
        if (user != null) user.setLanguage(chosen);
        System.out.println(Messages.m(Key.LANGUAGE_CHANGED));
    }

    // ════════════════════════════════════════════════════════════════════════
    // LOGIN
    // ════════════════════════════════════════════════════════════════════════
    private static void loginFlow() {
        System.out.println(Messages.m(Key.LOGIN_TITLE));
        System.out.print(Messages.m(Key.LOGIN_ID));
        String id   = sc.nextLine().trim();
        System.out.print(Messages.m(Key.LOGIN_PASS));
        String pass = sc.nextLine().trim();
        try {
            User user = auth.login(id, pass);
            // Switch UI to user's preferred language
            Messages.setLanguage(user.getLanguage());
            admin.writeLog("Login: " + user.getFullName() + " (" + user.getClass().getSimpleName() + ")");

            if      (user instanceof GraduateStudent g) gradStudentMenu(g);
            else if (user instanceof Student s)          studentMenu(s);
            else if (user instanceof Teacher t)          teacherMenu(t);
            else if (user instanceof Admin a)            adminMenu(a);
            else if (user instanceof Manager m)          managerMenu(m);
            else if (user instanceof TechSupportSpecialist ts) techSupportMenu(ts);
            else System.out.println("  No menu for this role.");

        } catch (AuthenticationException e) {
            admin.writeLog("Failed login attempt for ID: " + id);
            System.out.println(Messages.m(Key.LOGIN_FAILED) + e.getMessage());
        }
        auth.logout();
        admin.writeLog("Logout: " + id);
        System.out.println(Messages.m(Key.LOGGED_OUT));
        // Reset to EN after logout
        Messages.setLanguage(Language.EN);
    }

    // ════════════════════════════════════════════════════════════════════════
    // STUDENT MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void studentMenu(Student s) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  STUDENT: " + pad(s.getFullName(), 22) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println(Messages.m(Key.OPT_VIEW_COURSES));
            System.out.println(Messages.m(Key.OPT_REGISTER_COURSE));
            System.out.println(Messages.m(Key.OPT_DROP_COURSE));
            System.out.println(Messages.m(Key.OPT_VIEW_MARKS));
            System.out.println(Messages.m(Key.OPT_VIEW_TRANSCRIPT));
            System.out.println(Messages.m(Key.OPT_TEACHER_INFO));
            System.out.println(Messages.m(Key.OPT_RATE_TEACHER));
            System.out.println(Messages.m(Key.OPT_CHANGE_LANG));
            System.out.println(Messages.m(Key.LOGOUT));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Messages.m(Key.CHOICE));
            switch (sc.nextLine().trim()) {
                case "1" -> listCourses();
                case "2" -> {
                    listCourses();
                    System.out.print(Messages.m(Key.ENTER_COURSE_ID));
                    String cid = sc.nextLine().trim();
                    findCourse(cid).ifPresentOrElse(c -> {
                        try {
                            s.registerCourse(c);
                            admin.writeLog("Student " + s.getFullName() + " registered for " + c.getName()
                                    + " (effective type for major '" + s.getMajor() + "': "
                                    + c.getEffectiveType(s.getMajor()) + ")");
                        }
                        catch (CreditLimitExceededException e) { System.out.println("  Error: " + e.getMessage()); }
                    }, () -> System.out.println(Messages.m(Key.COURSE_NOT_FOUND)));
                }
                case "3" -> {
                    System.out.println(Messages.m(Key.YOUR_COURSES));
                    s.getCourses().forEach(c -> System.out.println("    " + c.getCourseId() + " - " + c.getName()));
                    System.out.print(Messages.m(Key.ENTER_COURSE_ID));
                    String cid = sc.nextLine().trim();
                    findCourse(cid).ifPresentOrElse(s::dropCourse, () -> System.out.println(Messages.m(Key.COURSE_NOT_FOUND)));
                }
                case "4" -> s.viewMarks();
                case "5" -> s.viewTranscript();
                case "6" -> {
                    s.getCourses().forEach(c -> {
                        System.out.println("  Course: " + c.getName());
                        c.getTeachers().forEach(t -> System.out.printf("    Teacher: %s | pos: %s | rating: %.2f%n",
                                t.getFullName(), t.getPosition(), t.getRating()));
                    });
                }
                case "7" -> {
                    System.out.print(Messages.m(Key.RATING_PROMPT));
                    String tid = sc.nextLine().trim();
                    findTeacher(tid).ifPresentOrElse(t -> {
                        System.out.print(Messages.m(Key.ENTER_SCORE));
                        try {
                            double score = Double.parseDouble(sc.nextLine().trim());
                            s.rateTeacher(t, score);
                        } catch (NumberFormatException e) { System.out.println(Messages.m(Key.INVALID_NUMBER)); }
                    }, () -> System.out.println(Messages.m(Key.TEACHER_NOT_FOUND)));
                }
                case "L", "l" -> changeLanguage(s);
                case "0" -> active = false;
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // GRADUATE STUDENT MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void gradStudentMenu(GraduateStudent g) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  GRAD STUDENT: " + pad(g.getFullName(), 17) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println(Messages.m(Key.OPT_VIEW_COURSES));
            System.out.println(Messages.m(Key.OPT_REGISTER_COURSE));
            System.out.println(Messages.m(Key.OPT_VIEW_MARKS));
            System.out.println(Messages.m(Key.OPT_VIEW_SUPERVISOR));
            System.out.println(Messages.m(Key.OPT_MY_PAPERS));
            System.out.println(Messages.m(Key.OPT_H_INDEX));
            System.out.println(Messages.m(Key.OPT_CHANGE_LANG));
            System.out.println(Messages.m(Key.LOGOUT));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Messages.m(Key.CHOICE));
            switch (sc.nextLine().trim()) {
                case "1" -> listCourses();
                case "2" -> {
                    listCourses();
                    System.out.print(Messages.m(Key.ENTER_COURSE_ID));
                    String cid = sc.nextLine().trim();
                    findCourse(cid).ifPresentOrElse(c -> {
                        try { g.registerCourse(c); }
                        catch (CreditLimitExceededException e) { System.out.println("  Error: " + e.getMessage()); }
                    }, () -> System.out.println(Messages.m(Key.COURSE_NOT_FOUND)));
                }
                case "3" -> { g.viewMarks(); g.viewTranscript(); }
                case "4" -> {
                    Teacher sv = g.getSupervisor();
                    if (sv == null) System.out.println(Messages.m(Key.NO_SUPERVISOR));
                    else System.out.printf("  Supervisor: %s | h-index: %d%n", sv.getFullName(), sv.calculateHIndex());
                }
                case "5" -> {
                    List<ResearchPaper> papers = g.getPapers();
                    if (papers.isEmpty()) System.out.println(Messages.m(Key.NO_PAPERS));
                    else papers.forEach(p -> System.out.println("  " + p));
                }
                case "6" -> System.out.println(Messages.m(Key.H_INDEX_LABEL) + g.calculateHIndex());
                case "L", "l" -> changeLanguage(g);
                case "0" -> active = false;
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // TEACHER MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void teacherMenu(Teacher t) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  TEACHER: " + pad(t.getFullName(), 23) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println(Messages.m(Key.OPT_MY_COURSES));
            System.out.println(Messages.m(Key.OPT_ENROLLED));
            System.out.println(Messages.m(Key.OPT_PUT_MARK));
            System.out.println(Messages.m(Key.OPT_MARK_REPORT));
            System.out.println(Messages.m(Key.OPT_COMPLAINT));
            System.out.println(Messages.m(Key.OPT_RESEARCH_PAPERS));
            System.out.println("║  7. h-index                      ║");
            System.out.println(Messages.m(Key.OPT_SEND_MSG));
            System.out.println(Messages.m(Key.OPT_VIEW_MSG));
            System.out.println(Messages.m(Key.OPT_CHANGE_LANG));
            System.out.println(Messages.m(Key.LOGOUT));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Messages.m(Key.CHOICE));
            switch (sc.nextLine().trim()) {
                case "1" -> t.viewCourses();
                case "2" -> t.viewStudents();
                case "3" -> {
                    System.out.print(Messages.m(Key.STUDENT_ID));
                    String sid = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.COURSE_ID));
                    String cid = sc.nextLine().trim();
                    Optional<Student> student = findStudent(sid);
                    Optional<Course>  course  = findCourse(cid);
                    if (student.isEmpty()) { System.out.println(Messages.m(Key.STUDENT_NOT_FOUND)); break; }
                    if (course.isEmpty())  { System.out.println(Messages.m(Key.COURSE_NOT_FOUND));  break; }
                    try {
                        System.out.print(Messages.m(Key.ATTEST1));
                        double a1 = Double.parseDouble(sc.nextLine().trim());
                        System.out.print(Messages.m(Key.ATTEST2));
                        double a2 = Double.parseDouble(sc.nextLine().trim());
                        System.out.print(Messages.m(Key.FINAL_EXAM));
                        double fn = Double.parseDouble(sc.nextLine().trim());
                        Mark mark = new Mark(a1, a2, fn);
                        t.putMark(student.get(), course.get(), mark);
                        admin.writeLog("Mark set by " + t.getFullName() + " for " + student.get().getFullName()
                                + " in " + course.get().getName() + ": " + mark.getTotal());
                    } catch (NumberFormatException e) {
                        System.out.println(Messages.m(Key.INVALID_NUMBER));
                    } catch (IllegalArgumentException e) {
                        System.out.println("  Score out of range: " + e.getMessage());
                    }
                }
                case "4" -> {
                    System.out.print(Messages.m(Key.COURSE_ID));
                    findCourse(sc.nextLine().trim()).ifPresentOrElse(
                            t::generateMarkReport, () -> System.out.println(Messages.m(Key.COURSE_NOT_FOUND)));
                }
                case "5" -> {
                    System.out.print(Messages.m(Key.COMPLAINT_TEXT));
                    String desc = sc.nextLine().trim();
                    System.out.println(Messages.m(Key.URGENCY_PROMPT));
                    System.out.print(Messages.m(Key.CHOICE));
                    UrgencyLevel lvl = switch (sc.nextLine().trim()) {
                        case "1" -> UrgencyLevel.LOW;
                        case "3" -> UrgencyLevel.HIGH;
                        default  -> UrgencyLevel.MEDIUM;
                    };
                    t.sendComplaint(desc, lvl);
                    t.sendMessage(dean, "[COMPLAINT | " + lvl + "] " + desc);
                    System.out.println(Messages.m(Key.COMPLAINT_SENT));
                }
                case "6" -> {
                    System.out.println(Messages.m(Key.SORT_PROMPT));
                    System.out.print(Messages.m(Key.CHOICE));
                    Comparator<ResearchPaper> cmp = switch (sc.nextLine().trim()) {
                        case "2" -> ResearchPaper.BY_DATE;
                        case "3" -> ResearchPaper.BY_TITLE;
                        default  -> ResearchPaper.BY_CITATIONS;
                    };
                    t.printPapers(cmp);
                }
                case "7" -> System.out.println("  h-index: " + t.calculateHIndex());
                case "8" -> {
                    System.out.print(Messages.m(Key.RECIPIENT_ID));
                    String rid = sc.nextLine().trim();
                    findUser(rid).ifPresentOrElse(r -> {
                        if (r instanceof Employee emp) {
                            System.out.print(Messages.m(Key.MESSAGE_PROMPT));
                            t.sendMessage(emp, sc.nextLine().trim());
                        } else System.out.println(Messages.m(Key.NOT_EMPLOYEE));
                    }, () -> System.out.println(Messages.m(Key.USER_NOT_FOUND)));
                }
                case "9" -> t.viewMessages();
                case "L", "l" -> changeLanguage(t);
                case "0" -> active = false;
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // ADMIN MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void adminMenu(Admin a) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  ADMIN                           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println(Messages.m(Key.OPT_ALL_USERS));
            System.out.println(Messages.m(Key.OPT_LOGS));
            System.out.println(Messages.m(Key.OPT_ADD_STUDENT));
            System.out.println(Messages.m(Key.OPT_REMOVE_USER));
            System.out.println(Messages.m(Key.OPT_CHANGE_LANG));
            System.out.println(Messages.m(Key.LOGOUT));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Messages.m(Key.CHOICE));
            switch (sc.nextLine().trim()) {
                case "1" -> allUsers.forEach(u -> System.out.printf("  [%s] %s — %s%n",
                        u.getId(), u.getFullName(), u.getClass().getSimpleName()));
                case "2" -> a.viewLogs();
                case "3" -> {
                    System.out.print(Messages.m(Key.ENTER_ID));    String id = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_FNAME)); String fn = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_LNAME)); String ln = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_PASS));  String pw = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_YEAR));
                    int yr;
                    try { yr = Integer.parseInt(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println(Messages.m(Key.INVALID_YEAR)); break; }
                    System.out.print(Messages.m(Key.ENTER_MAJOR)); String mj = sc.nextLine().trim();
                    Student ns = new Student(id, fn, ln, pw, Language.EN, yr, mj);
                    auth.registerUser(ns); allUsers.add(ns); students.add(ns);
                    a.writeLog("Admin added student: " + id);
                    System.out.println(Messages.m(Key.STUDENT_ADDED));
                }
                case "4" -> {
                    System.out.print(Messages.m(Key.ENTER_ID));
                    String rid = sc.nextLine().trim();
                    findUser(rid).ifPresentOrElse(u -> {
                        allUsers.remove(u); students.remove(u);
                        a.writeLog("Admin removed user: " + rid);
                        System.out.println(Messages.m(Key.USER_REMOVED) + u.getFullName());
                    }, () -> System.out.println(Messages.m(Key.USER_NOT_FOUND)));
                }
                case "L", "l" -> changeLanguage(a);
                case "0" -> active = false;
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // MANAGER MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void managerMenu(Manager m) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  MANAGER: " + pad(m.getFullName(), 23) + "║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println(Messages.m(Key.OPT_ASSIGN_TEACHER));
            System.out.println(Messages.m(Key.OPT_APPROVE_REG));
            System.out.println(Messages.m(Key.OPT_ACADEMIC_REPORT));
            System.out.println(Messages.m(Key.OPT_STUDENTS_ALPHA));
            System.out.println(Messages.m(Key.OPT_CREATE_NEWS));
            System.out.println(Messages.m(Key.OPT_VIEW_REQUESTS));
            System.out.println("║  7. View messages                ║");
            System.out.println(Messages.m(Key.OPT_ADD_COURSE));
            System.out.println(Messages.m(Key.OPT_ADD_TEACHER));
            System.out.println(Messages.m(Key.OPT_CHANGE_LANG));
            System.out.println(Messages.m(Key.LOGOUT));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Messages.m(Key.CHOICE));
            switch (sc.nextLine().trim()) {
                case "1" -> {
                    listTeachers();
                    System.out.print(Messages.m(Key.TEACHER_ID));
                    String tid = sc.nextLine().trim();
                    listCourses();
                    System.out.print(Messages.m(Key.COURSE_ID));
                    String cid = sc.nextLine().trim();
                    findTeacher(tid).ifPresentOrElse(
                            t -> findCourse(cid).ifPresentOrElse(c -> m.assignCourse(t, c),
                                    () -> System.out.println(Messages.m(Key.COURSE_NOT_FOUND))),
                            () -> System.out.println(Messages.m(Key.TEACHER_NOT_FOUND)));
                }
                case "2" -> {
                    System.out.print(Messages.m(Key.STUDENT_ID));
                    String sid = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.COURSE_ID));
                    String cid = sc.nextLine().trim();
                    findStudent(sid).ifPresentOrElse(
                            s -> findCourse(cid).ifPresentOrElse(c -> m.approveRegistration(s, c),
                                    () -> System.out.println(Messages.m(Key.COURSE_NOT_FOUND))),
                            () -> System.out.println(Messages.m(Key.STUDENT_NOT_FOUND)));
                }
                case "3" -> m.createAcademicReport(students);
                case "4" -> {
                    System.out.println(Messages.m(Key.STUDENTS_ALPHA_HEADER));
                    m.viewStudentsAlphabetically(students);
                }
                case "5" -> {
                    System.out.print(Messages.m(Key.NEWS_TITLE));   String title   = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.NEWS_CONTENT)); String content = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.NEWS_TOPIC));   String topic   = sc.nextLine().trim();
                    m.createNews(title, content, topic);
                }
                case "6" -> m.viewRequests(requests);
                case "7" -> m.viewMessages();
                case "8" -> {
                    System.out.print(Messages.m(Key.ENTER_ID));      String cid   = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.COURSE_NAME));   String cname = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.CREDITS));
                    int cr;
                    try { cr = Integer.parseInt(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println(Messages.m(Key.INVALID_CREDITS)); break; }
                    System.out.println(Messages.m(Key.COURSE_TYPE_PROMPT));
                    System.out.print(Messages.m(Key.CHOICE));
                    CourseType ct = switch (sc.nextLine().trim()) {
                        case "2" -> CourseType.MINOR;
                        case "3" -> CourseType.FREE_ELECTIVE;
                        default  -> CourseType.MAJOR;
                    };
                    System.out.print(Messages.m(Key.YEAR_PROMPT));
                    int yr;
                    try { yr = Integer.parseInt(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println(Messages.m(Key.INVALID_YEAR)); break; }
                    System.out.print(Messages.m(Key.MAJOR_PROMPT)); String maj = sc.nextLine().trim();
                    if (findCourse(cid).isPresent()) { System.out.println(Messages.m(Key.COURSE_EXISTS)); break; }
                    Course nc = new Course(cid, cname, cr, ct, yr, maj);
                    courses.add(nc);
                    System.out.println(Messages.m(Key.COURSE_ADDED) + nc);
                }
                case "9" -> {
                    System.out.print(Messages.m(Key.TEACHER_ID));   String tid = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_FNAME));  String tfn = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_LNAME));  String tln = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.ENTER_PASS));   String tpw = sc.nextLine().trim();
                    System.out.print(Messages.m(Key.SALARY_PROMPT));
                    double sal;
                    try { sal = Double.parseDouble(sc.nextLine().trim()); }
                    catch (NumberFormatException e) { System.out.println(Messages.m(Key.INVALID_SALARY)); break; }
                    System.out.print(Messages.m(Key.DEPT_PROMPT));  String dep = sc.nextLine().trim();
                    System.out.println(Messages.m(Key.POSITION_PROMPT));
                    System.out.print(Messages.m(Key.CHOICE));
                    TeacherPosition pos = switch (sc.nextLine().trim()) {
                        case "2" -> TeacherPosition.LECTOR;
                        case "3" -> TeacherPosition.SENIOR_LECTOR;
                        case "4" -> TeacherPosition.PROFESSOR;
                        default  -> TeacherPosition.TUTOR;
                    };
                    if (findUser(tid).isPresent()) { System.out.println(Messages.m(Key.ID_EXISTS)); break; }
                    Teacher nt = new Teacher(tid, tfn, tln, tpw, Language.EN, sal, dep, pos);
                    auth.registerUser(nt); allUsers.add(nt); teachers.add(nt);
                    System.out.println(Messages.m(Key.TEACHER_ADDED) + nt.getFullName() + " (ID: " + tid + ")");
                }
                case "L", "l" -> changeLanguage(m);
                case "0" -> active = false;
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // TECH SUPPORT MENU
    // ════════════════════════════════════════════════════════════════════════
    private static void techSupportMenu(TechSupportSpecialist ts) {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║  TECH SUPPORT                    ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println(Messages.m(Key.OPT_NEW_REQUESTS));
            System.out.println(Messages.m(Key.OPT_ACCEPT));
            System.out.println(Messages.m(Key.OPT_REJECT));
            System.out.println(Messages.m(Key.OPT_MARK_DONE));
            System.out.println(Messages.m(Key.OPT_ALL_REQUESTS));
            System.out.println(Messages.m(Key.OPT_CHANGE_LANG));
            System.out.println(Messages.m(Key.LOGOUT));
            System.out.println("╚══════════════════════════════════╝");
            System.out.print(Messages.m(Key.CHOICE));
            String tsChoice = sc.nextLine().trim();
            switch (tsChoice) {
                case "1" -> ts.viewNewRequests();
                case "2", "3", "4" -> {
                    System.out.println(Messages.m(Key.ALL_REQUESTS));
                    for (int i = 0; i < requests.size(); i++)
                        System.out.printf("  [%d] %s%n", i + 1, requests.get(i));
                    System.out.print(Messages.m(Key.REQUEST_NUM));
                    try {
                        int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
                        if (idx < 0 || idx >= requests.size()) { System.out.println(Messages.m(Key.INVALID_IDX)); break; }
                        SupportRequest req = requests.get(idx);
                        switch (tsChoice) {
                            case "2" -> ts.acceptRequest(req);
                            case "3" -> ts.rejectRequest(req);
                            case "4" -> ts.markDone(req);
                        }
                    } catch (NumberFormatException e) { System.out.println(Messages.m(Key.INVALID_NUMBER)); }
                }
                case "5" -> requests.forEach(r -> System.out.println("  " + r));
                case "L", "l" -> changeLanguage(ts);
                case "0" -> active = false;
                default -> System.out.println(Messages.m(Key.UNKNOWN_OPTION));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ════════════════════════════════════════════════════════════════════════
    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║        KBTU University Information System        ║");
        System.out.println("║              Part C — Interactive Demo           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    private static void printMainMenu() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println(Messages.m(Key.MAIN_LOGIN));
        System.out.println("│  L. Change language                  │");
        System.out.println(Messages.m(Key.MAIN_EXIT));
        System.out.println("└──────────────────────────────────────┘");
        System.out.print(Messages.m(Key.CHOICE));
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

    private static Optional<Course>  findCourse(String id)  {
        return courses.stream().filter(c -> c.getCourseId().equalsIgnoreCase(id)).findFirst();
    }
    private static Optional<Student> findStudent(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
    }
    private static Optional<Teacher> findTeacher(String id) {
        return allUsers.stream().filter(u -> u instanceof Teacher && u.getId().equalsIgnoreCase(id))
                .map(u -> (Teacher) u).findFirst();
    }
    private static void listTeachers() {
        System.out.println("  Available teachers:");
        allUsers.stream().filter(u -> u instanceof Teacher).map(u -> (Teacher) u)
                .forEach(t -> System.out.printf("  %-8s %-25s %s%n", t.getId(), t.getFullName(), t.getPosition()));
    }
    private static Optional<User> findUser(String id) {
        return allUsers.stream().filter(u -> u.getId().equalsIgnoreCase(id)).findFirst();
    }
    private static String pad(String s, int len) {
        if (s.length() >= len) return s.substring(0, len);
        return s + " ".repeat(len - s.length());
    }
}