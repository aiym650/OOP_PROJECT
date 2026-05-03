package university.patterns;

import university.enums.*;
import university.models.*;

import java.util.Map;

public class UserFactory {

    private UserFactory() {}

    public static User create(Map<String, String> p) {
        String type = p.getOrDefault("type", "").toUpperCase();
        String id    = p.get("id");
        String first = p.get("firstName");
        String last  = p.get("lastName");
        String pass  = p.get("password");
        Language lang = Language.valueOf(p.getOrDefault("language", "EN"));

        return switch (type) {
            case "STUDENT" -> new Student(id, first, last, pass, lang,
                    Integer.parseInt(p.getOrDefault("year", "1")),
                    p.getOrDefault("major", "CS"));

            case "GRADUATE" -> new GraduateStudent(id, first, last, pass, lang,
                    Integer.parseInt(p.getOrDefault("year", "1")),
                    p.getOrDefault("major", "CS"),
                    DegreeType.valueOf(p.getOrDefault("degree", "MASTER")));

            case "TEACHER" -> new Teacher(id, first, last, pass, lang,
                    Double.parseDouble(p.getOrDefault("salary", "100000")),
                    p.getOrDefault("department", "IT"),
                    TeacherPosition.valueOf(p.getOrDefault("position", "LECTOR")));

            case "MANAGER" -> new Manager(id, first, last, pass, lang,
                    Double.parseDouble(p.getOrDefault("salary", "150000")),
                    p.getOrDefault("department", "Management"),
                    ManagerType.valueOf(p.getOrDefault("managerType", "OR")));

            case "ADMIN" -> new Admin(id, first, last, pass, lang,
                    Double.parseDouble(p.getOrDefault("salary", "200000")),
                    p.getOrDefault("department", "IT"));

            case "TECHSUPPORT" -> new TechSupportSpecialist(id, first, last, pass, lang,
                    Double.parseDouble(p.getOrDefault("salary", "80000")),
                    p.getOrDefault("department", "IT Support"));

            default -> throw new IllegalArgumentException("Unknown user type: " + type);
        };
    }
}