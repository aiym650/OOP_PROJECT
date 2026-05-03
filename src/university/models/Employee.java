package university.models;

import university.enums.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Employee extends User {

    private static final long serialVersionUID = 1L;

    protected double salary;
    protected String department;
    private final List<Message> inbox = new ArrayList<>();

    public Employee(String id, String firstName, String lastName,
                    String password, Language language,
                    double salary, String department) {
        super(id, firstName, lastName, password, language);
        this.salary     = salary;
        this.department = department;
    }

    public void sendMessage(User receiver, String content) {
        Message msg = new Message(this, receiver, content);
        if (receiver instanceof Employee) {
            ((Employee) receiver).receiveMessage(msg);
        }
        System.out.printf("[Message] %s → %s: \"%s\"%n",
                getFullName(), receiver.getFullName(), content);
    }
    public void receiveMessage(Message msg) {
        inbox.add(msg);
    }
    public void viewMessages() {
        if (inbox.isEmpty()) {
            System.out.println("Inbox is empty.");
            return;
        }
        System.out.println("=== Inbox of " + getFullName() + " ===");
        inbox.forEach(System.out::println);
    }

    public double getSalary()                  { return salary; }
    public void setSalary(double salary)       { this.salary = salary; }
    public String getDepartment()              { return department; }
    public void setDepartment(String dept)     { this.department = dept; }
    public List<Message> getInbox()            { return Collections.unmodifiableList(inbox); }
}