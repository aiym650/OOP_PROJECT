package university.patterns;

import university.models.Student;

import java.util.Comparator;
import java.util.List;

public interface SortStrategy<T> {
    List<T> sort(List<T> items);
}

class SortByGpa implements SortStrategy<Student> {
    @Override
    public List<Student> sort(List<Student> items) {
        return items.stream().sorted().toList(); 
    }
}

class SortByName implements SortStrategy<Student> {
    @Override
    public List<Student> sort(List<Student> items) {
        return items.stream()
                .sorted(Comparator.comparing(s -> s.getLastName() + s.getFirstName()))
                .toList();
    }
}

class SortByCredits implements SortStrategy<Student> {
    @Override
    public List<Student> sort(List<Student> items) {
        return items.stream()
                .sorted(Comparator.comparingInt(Student::getTotalCredits).reversed())
                .toList();
    }
}
class StudentSortFactory {
    public static SortStrategy<Student> get(String type) {
        return switch (type.toUpperCase()) {
            case "GPA"     -> new SortByGpa();
            case "NAME"    -> new SortByName();
            case "CREDITS" -> new SortByCredits();
            default -> throw new IllegalArgumentException("Unknown sort strategy: " + type);
        };
    }
}