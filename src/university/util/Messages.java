package university.util;

import university.enums.Language;
import java.util.EnumMap;
import java.util.Map;

public class Messages {

    private static Language current = Language.EN;

    private Messages() {}

    public static void setLanguage(Language lang) { current = lang; }
    public static Language getLanguage()          { return current; }

    // ── Keys ─────────────────────────────────────────────────────────────────
    public enum Key {
        // General
        CHOICE, LOGOUT, UNKNOWN_OPTION, INVALID_NUMBER, NOT_FOUND,
        COURSE_NOT_FOUND, STUDENT_NOT_FOUND, TEACHER_NOT_FOUND, USER_NOT_FOUND,
        LANGUAGE_CHANGED,

        // Login
        LOGIN_TITLE, LOGIN_ID, LOGIN_PASS, LOGIN_FAILED, LOGGED_OUT,

        // Main menu
        MAIN_LOGIN, MAIN_EXIT,

        // Student menu
        MENU_STUDENT, OPT_VIEW_COURSES, OPT_REGISTER_COURSE, OPT_DROP_COURSE,
        OPT_VIEW_MARKS, OPT_VIEW_TRANSCRIPT, OPT_TEACHER_INFO, OPT_RATE_TEACHER,
        OPT_CHANGE_LANG,
        ENTER_COURSE_ID, ENTER_SCORE, YOUR_COURSES, RATING_PROMPT,

        // Grad student menu
        MENU_GRAD, OPT_VIEW_SUPERVISOR, OPT_MY_PAPERS, OPT_H_INDEX,
        NO_SUPERVISOR, NO_PAPERS, H_INDEX_LABEL,

        // Teacher menu
        MENU_TEACHER, OPT_MY_COURSES, OPT_ENROLLED, OPT_PUT_MARK,
        OPT_MARK_REPORT, OPT_COMPLAINT, OPT_RESEARCH_PAPERS, OPT_SEND_MSG, OPT_VIEW_MSG,
        STUDENT_ID, COURSE_ID, ATTEST1, ATTEST2, FINAL_EXAM,
        COMPLAINT_TEXT, URGENCY_PROMPT, SORT_PROMPT, RECIPIENT_ID, MESSAGE_PROMPT,
        NOT_EMPLOYEE, COMPLAINT_SENT,

        // Admin menu
        MENU_ADMIN, OPT_ALL_USERS, OPT_LOGS, OPT_ADD_STUDENT, OPT_REMOVE_USER,
        ENTER_ID, ENTER_FNAME, ENTER_LNAME, ENTER_PASS, ENTER_YEAR, ENTER_MAJOR,
        STUDENT_ADDED, USER_REMOVED, INVALID_YEAR, ID_EXISTS,

        // Manager menu
        MENU_MANAGER, OPT_ASSIGN_TEACHER, OPT_APPROVE_REG, OPT_ACADEMIC_REPORT,
        OPT_STUDENTS_ALPHA, OPT_CREATE_NEWS, OPT_VIEW_REQUESTS, OPT_ADD_COURSE, OPT_ADD_TEACHER,
        TEACHER_ID, NEWS_TITLE, NEWS_CONTENT, NEWS_TOPIC,
        COURSE_NAME, CREDITS, COURSE_TYPE_PROMPT, YEAR_PROMPT, MAJOR_PROMPT,
        COURSE_ADDED, COURSE_EXISTS, INVALID_CREDITS,
        SALARY_PROMPT, DEPT_PROMPT, POSITION_PROMPT, TEACHER_ADDED, INVALID_SALARY,
        STUDENTS_ALPHA_HEADER,

        // Tech support menu
        MENU_TECH, OPT_NEW_REQUESTS, OPT_ACCEPT, OPT_REJECT, OPT_MARK_DONE, OPT_ALL_REQUESTS,
        ALL_REQUESTS, REQUEST_NUM, INVALID_IDX
    }

    // ── Translation tables ────────────────────────────────────────────────────
    private static final Map<Key, String[]> T = new EnumMap<>(Key.class);

    static {
        // format: {EN, RU, KZ}
        put(Key.CHOICE,            "  Choice: ",                       "  Выбор: ",                        "  Таңдау: ");
        put(Key.LOGOUT,            "  0. Logout",                      "  0. Выйти",                       "  0. Шығу");
        put(Key.UNKNOWN_OPTION,    "  Unknown option.",                 "  Неизвестный вариант.",            "  Белгісіз опция.");
        put(Key.INVALID_NUMBER,    "  Invalid number.",                 "  Неверное число.",                 "  Қате сан.");
        put(Key.NOT_FOUND,         "  Not found.",                     "  Не найдено.",                    "  Табылмады.");
        put(Key.COURSE_NOT_FOUND,  "  Course not found.",              "  Курс не найден.",                "  Курс табылмады.");
        put(Key.STUDENT_NOT_FOUND, "  Student not found.",             "  Студент не найден.",             "  Студент табылмады.");
        put(Key.TEACHER_NOT_FOUND, "  Teacher not found.",             "  Преподаватель не найден.",       "  Оқытушы табылмады.");
        put(Key.USER_NOT_FOUND,    "  User not found.",                "  Пользователь не найден.",        "  Пайдаланушы табылмады.");
        put(Key.LANGUAGE_CHANGED,  "  Language changed to English.",   "  Язык изменён на Русский.",       "  Тіл Қазақшаға өзгертілді.");

        put(Key.LOGIN_TITLE,       "\n  ── Login ─────────────────────────",  "\n  ── Вход ──────────────────────────",  "\n  ── Кіру ──────────────────────────");
        put(Key.LOGIN_ID,          "  User ID  : ",                    "  ID пользователя: ",              "  Пайдаланушы ID: ");
        put(Key.LOGIN_PASS,        "  Password : ",                    "  Пароль: ",                       "  Құпия сөз: ");
        put(Key.LOGIN_FAILED,      "  Login failed: ",                 "  Ошибка входа: ",                 "  Кіру сәтсіз: ");
        put(Key.LOGGED_OUT,        "  Logged out.\n",                  "  Вы вышли.\n",                    "  Шығылды.\n");

        put(Key.MAIN_LOGIN,        "│  1. Login                            │", "│  1. Войти                            │", "│  1. Кіру                             │");
        put(Key.MAIN_EXIT,         "│  0. Exit                             │", "│  0. Выход                            │", "│  0. Шығу                             │");

        put(Key.OPT_VIEW_COURSES,    "║  1. View all courses             ║", "║  1. Все курсы                    ║", "║  1. Барлық курстар               ║");
        put(Key.OPT_REGISTER_COURSE, "║  2. Register for a course        ║", "║  2. Записаться на курс           ║", "║  2. Курсқа тіркелу               ║");
        put(Key.OPT_DROP_COURSE,     "║  3. Drop a course                ║", "║  3. Отказаться от курса          ║", "║  3. Курстан шығу                 ║");
        put(Key.OPT_VIEW_MARKS,      "║  4. View my marks                ║", "║  4. Мои оценки                   ║", "║  4. Менің бағаларым              ║");
        put(Key.OPT_VIEW_TRANSCRIPT, "║  5. View transcript              ║", "║  5. Транскрипт                   ║", "║  5. Транскрипт                   ║");
        put(Key.OPT_TEACHER_INFO,    "║  6. View teacher info            ║", "║  6. Инфо о преподавателе         ║", "║  6. Оқытушы туралы               ║");
        put(Key.OPT_RATE_TEACHER,    "║  7. Rate a teacher               ║", "║  7. Оценить преподавателя        ║", "║  7. Оқытушыға баға беру          ║");
        put(Key.OPT_CHANGE_LANG,     "║  L. Change language              ║", "║  L. Сменить язык                 ║", "║  L. Тілді өзгерту                ║");

        put(Key.ENTER_COURSE_ID,  "  Enter Course ID: ",              "  Введите ID курса: ",             "  Курс ID енгізіңіз: ");
        put(Key.ENTER_SCORE,      "  Score (1-5): ",                  "  Оценка (1-5): ",                 "  Балл (1-5): ");
        put(Key.YOUR_COURSES,     "  Your courses:",                  "  Ваши курсы:",                    "  Сіздің курстарыңыз:");
        put(Key.RATING_PROMPT,    "  Teacher ID (e.g. t001): ",       "  ID преподавателя: ",             "  Оқытушы ID: ");

        put(Key.OPT_VIEW_SUPERVISOR, "║  4. View supervisor              ║", "║  4. Научный руководитель         ║", "║  4. Ғылыми жетекші               ║");
        put(Key.OPT_MY_PAPERS,       "║  5. View my research papers      ║", "║  5. Мои научные статьи           ║", "║  5. Менің мақалаларым            ║");
        put(Key.OPT_H_INDEX,         "║  6. My h-index                   ║", "║  6. Мой h-индекс                 ║", "║  6. Менің h-индексім             ║");
        put(Key.NO_SUPERVISOR,       "  No supervisor assigned.",      "  Руководитель не назначен.",      "  Жетекші тағайындалмаған.");
        put(Key.NO_PAPERS,           "  No papers yet.",               "  Статей пока нет.",               "  Мақалалар жоқ.");
        put(Key.H_INDEX_LABEL,       "  Your h-index: ",              "  Ваш h-индекс: ",                 "  Сіздің h-индексіңіз: ");

        put(Key.OPT_MY_COURSES,   "║  1. View my courses              ║", "║  1. Мои курсы                    ║", "║  1. Менің курстарым              ║");
        put(Key.OPT_ENROLLED,     "║  2. View enrolled students       ║", "║  2. Записанные студенты          ║", "║  2. Тіркелген студенттер         ║");
        put(Key.OPT_PUT_MARK,     "║  3. Put mark for student         ║", "║  3. Поставить оценку             ║", "║  3. Баға қою                     ║");
        put(Key.OPT_MARK_REPORT,  "║  4. Generate mark report         ║", "║  4. Отчёт по оценкам             ║", "║  4. Бағалар есебі                ║");
        put(Key.OPT_COMPLAINT,    "║  5. Send complaint to dean       ║", "║  5. Жалоба декану                ║", "║  5. Деканға шағым жіберу         ║");
        put(Key.OPT_RESEARCH_PAPERS,"║  6. My research papers           ║","║  6. Мои статьи                   ║","║  6. Менің мақалаларым            ║");
        put(Key.OPT_SEND_MSG,     "║  8. Send message                 ║", "║  8. Отправить сообщение          ║", "║  8. Хабар жіберу                 ║");
        put(Key.OPT_VIEW_MSG,     "║  9. View messages                ║", "║  9. Входящие сообщения           ║", "║  9. Хабарларды көру              ║");
        put(Key.STUDENT_ID,       "  Student ID: ",                   "  ID студента: ",                  "  Студент ID: ");
        put(Key.COURSE_ID,        "  Course ID:  ",                   "  ID курса: ",                     "  Курс ID: ");
        put(Key.ATTEST1,          "  Attestation 1 (0-30): ",         "  Аттестация 1 (0-30): ",          "  Аттестация 1 (0-30): ");
        put(Key.ATTEST2,          "  Attestation 2 (0-30): ",         "  Аттестация 2 (0-30): ",          "  Аттестация 2 (0-30): ");
        put(Key.FINAL_EXAM,       "  Final exam  (0-40): ",           "  Финальный экзамен (0-40): ",     "  Финалдық емтихан (0-40): ");
        put(Key.COMPLAINT_TEXT,   "  Complaint text: ",               "  Текст жалобы: ",                 "  Шағым мәтіні: ");
        put(Key.URGENCY_PROMPT,   "  Urgency: 1=LOW  2=MEDIUM  3=HIGH","  Срочность: 1=НИЗ  2=СРЕДН  3=ВЫСОК","  Маңыздылық: 1=ТӨМ  2=ОРТА  3=ЖОҒАРЫ");
        put(Key.SORT_PROMPT,      "  Sort by: 1=Citations  2=Date  3=Pages","  Сорт: 1=Цитаты  2=Дата  3=Стр","  Сұрыптау: 1=Цит  2=Күн  3=Бет");
        put(Key.RECIPIENT_ID,     "  Recipient ID: ",                 "  ID получателя: ",               "  Алушы ID: ");
        put(Key.MESSAGE_PROMPT,   "  Message: ",                      "  Сообщение: ",                   "  Хабар: ");
        put(Key.NOT_EMPLOYEE,     "  Recipient is not an employee.",  "  Получатель не является сотрудником.", "  Алушы қызметкер емес.");
        put(Key.COMPLAINT_SENT,   "  Complaint sent to Dean.",        "  Жалоба отправлена декану.",      "  Шағым деканға жіберілді.");

        put(Key.OPT_ALL_USERS,    "║  1. View all users               ║", "║  1. Все пользователи             ║", "║  1. Барлық пайдаланушылар        ║");
        put(Key.OPT_LOGS,         "║  2. View system logs             ║", "║  2. Системные логи               ║", "║  2. Жүйе журналы                 ║");
        put(Key.OPT_ADD_STUDENT,  "║  3. Add user (Student)           ║", "║  3. Добавить студента            ║", "║  3. Студент қосу                 ║");
        put(Key.OPT_REMOVE_USER,  "║  4. Remove user by ID            ║", "║  4. Удалить пользователя         ║", "║  4. Пайдаланушыны жою            ║");
        put(Key.ENTER_ID,         "  ID: ",                           "  ID: ",                           "  ID: ");
        put(Key.ENTER_FNAME,      "  First name: ",                   "  Имя: ",                          "  Аты: ");
        put(Key.ENTER_LNAME,      "  Last name: ",                    "  Фамилия: ",                      "  Тегі: ");
        put(Key.ENTER_PASS,       "  Password: ",                     "  Пароль: ",                       "  Құпия сөз: ");
        put(Key.ENTER_YEAR,       "  Year: ",                         "  Курс: ",                         "  Курс: ");
        put(Key.ENTER_MAJOR,      "  Major: ",                        "  Специальность: ",                "  Мамандық: ");
        put(Key.STUDENT_ADDED,    "  Student added.",                 "  Студент добавлен.",              "  Студент қосылды.");
        put(Key.USER_REMOVED,     "  Removed: ",                      "  Удалён: ",                       "  Жойылды: ");
        put(Key.INVALID_YEAR,     "  Invalid year.",                  "  Неверный курс.",                 "  Қате курс.");
        put(Key.ID_EXISTS,        "  User with this ID already exists.","  Пользователь с таким ID уже существует.","  Бұл ID бар пайдаланушы бар.");

        put(Key.OPT_ASSIGN_TEACHER,  "║  1. Assign teacher to course     ║","║  1. Назначить препод. на курс    ║","║  1. Оқытушыны курсқа тағайындау  ║");
        put(Key.OPT_APPROVE_REG,     "║  2. Approve student registration ║","║  2. Одобрить запись студента     ║","║  2. Студент тіркелуін бекіту     ║");
        put(Key.OPT_ACADEMIC_REPORT, "║  3. Academic report (by GPA)     ║","║  3. Академический отчёт (GPA)    ║","║  3. Академиялық есеп (GPA)       ║");
        put(Key.OPT_STUDENTS_ALPHA,  "║  4. Students alphabetically      ║","║  4. Студенты по алфавиту         ║","║  4. Студенттер (алфавит)         ║");
        put(Key.OPT_CREATE_NEWS,     "║  5. Create news                  ║","║  5. Создать новость              ║","║  5. Жаңалық жасау                ║");
        put(Key.OPT_VIEW_REQUESTS,   "║  6. View support requests        ║","║  6. Заявки в техподдержку        ║","║  6. Техқолдау өтінімдері         ║");
        put(Key.OPT_ADD_COURSE,      "║  8. Add new course               ║","║  8. Добавить курс                ║","║  8. Жаңа курс қосу               ║");
        put(Key.OPT_ADD_TEACHER,     "║  9. Add new teacher              ║","║  9. Добавить преподавателя       ║","║  9. Жаңа оқытушы қосу            ║");
        put(Key.TEACHER_ID,       "  Teacher ID: ",                   "  ID преподавателя: ",             "  Оқытушы ID: ");
        put(Key.NEWS_TITLE,       "  Title:   ",                      "  Заголовок: ",                    "  Тақырып: ");
        put(Key.NEWS_CONTENT,     "  Content: ",                      "  Содержание: ",                   "  Мазмұн: ");
        put(Key.NEWS_TOPIC,       "  Topic (e.g. Research/Academic): ","  Тема (напр. Research/Academic): ","  Тақырып (Research/Academic): ");
        put(Key.COURSE_NAME,      "  Course name:  ",                 "  Название курса: ",               "  Курс атауы: ");
        put(Key.CREDITS,          "  Credits:      ",                 "  Кредиты: ",                      "  Кредиттер: ");
        put(Key.COURSE_TYPE_PROMPT,"  Type: 1=MAJOR  2=MINOR  3=FREE_ELECTIVE","  Тип: 1=MAJOR  2=MINOR  3=FREE_ELECTIVE","  Түрі: 1=MAJOR  2=MINOR  3=FREE_ELECTIVE");
        put(Key.YEAR_PROMPT,      "  For year (1-4): ",               "  Для курса (1-4): ",              "  Курс үшін (1-4): ");
        put(Key.MAJOR_PROMPT,     "  For major:    ",                 "  Для специальности: ",            "  Мамандық үшін: ");
        put(Key.COURSE_ADDED,     "  Course added: ",                 "  Курс добавлен: ",                "  Курс қосылды: ");
        put(Key.COURSE_EXISTS,    "  Course with this ID already exists.","  Курс с таким ID уже существует.","  Бұл ID курс бар.");
        put(Key.INVALID_CREDITS,  "  Invalid credits.",               "  Неверное кол-во кредитов.",      "  Қате кредит саны.");
        put(Key.SALARY_PROMPT,    "  Salary:             ",           "  Зарплата: ",                     "  Жалақы: ");
        put(Key.DEPT_PROMPT,      "  Department:         ",           "  Кафедра: ",                      "  Кафедра: ");
        put(Key.POSITION_PROMPT,  "  Position: 1=TUTOR  2=LECTOR  3=SENIOR_LECTOR  4=PROFESSOR","  Должность: 1=TUTOR  2=LECTOR  3=SENIOR_LECTOR  4=PROFESSOR","  Лауазым: 1=TUTOR  2=LECTOR  3=SENIOR_LECTOR  4=PROFESSOR");
        put(Key.TEACHER_ADDED,    "  Teacher added: ",                "  Преподаватель добавлен: ",       "  Оқытушы қосылды: ");
        put(Key.INVALID_SALARY,   "  Invalid salary.",                "  Неверная зарплата.",             "  Қате жалақы.");
        put(Key.STUDENTS_ALPHA_HEADER, "  Students (alphabetical):",  "  Студенты (по алфавиту):",        "  Студенттер (алфавит бойынша):");

        put(Key.OPT_NEW_REQUESTS, "║  1. View new requests            ║","║  1. Новые заявки                 ║","║  1. Жаңа өтінімдер               ║");
        put(Key.OPT_ACCEPT,       "║  2. Accept request               ║","║  2. Принять заявку               ║","║  2. Өтінімді қабылдау            ║");
        put(Key.OPT_REJECT,       "║  3. Reject request               ║","║  3. Отклонить заявку             ║","║  3. Өтінімді қабылдамау          ║");
        put(Key.OPT_MARK_DONE,    "║  4. Mark request as Done         ║","║  4. Отметить как выполнено       ║","║  4. Орындалды деп белгілеу       ║");
        put(Key.OPT_ALL_REQUESTS, "║  5. View all requests            ║","║  5. Все заявки                   ║","║  5. Барлық өтінімдер             ║");
        put(Key.ALL_REQUESTS,     "  All requests:",                  "  Все заявки:",                    "  Барлық өтінімдер:");
        put(Key.REQUEST_NUM,      "  Request number: ",               "  Номер заявки: ",                 "  Өтінім нөмірі: ");
        put(Key.INVALID_IDX,      "  Invalid number.",                "  Неверный номер.",                "  Қате нөмір.");
    }

    private static void put(Key key, String en, String ru, String kz) {
        T.put(key, new String[]{en, ru, kz});
    }

    public static String get(Key key) {
        String[] arr = T.get(key);
        if (arr == null) return "??";
        return switch (current) {
            case EN -> arr[0];
            case RU -> arr[1];
            case KZ -> arr[2];
        };
    }

    /** Shortcut: M.get(Key) */
    public static String m(Key key) { return get(key); }
}