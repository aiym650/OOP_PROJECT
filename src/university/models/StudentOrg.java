package university.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private Student head;
    private final List<Student> members = new ArrayList<>();

    public StudentOrg(String name, Student head) {
        this.name = name;
        this.head = head;
        members.add(head);
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            System.out.printf("[Org] %s joined %s%n", student.getFullName(), name);
        }
    }

    public void removeMember(Student student) {
        members.remove(student);
        System.out.printf("[Org] %s left %s%n", student.getFullName(), name);
    }

    public Student getHead()             { return head; }
    public void setHead(Student head)    { this.head = head; }
    public String getName()              { return name; }
    public List<Student> getMembers()    { return Collections.unmodifiableList(members); }

    @Override
    public String toString() {
        return String.format("StudentOrg{name='%s', head=%s, members=%d}",
                name, head.getFullName(), members.size());
    }
}