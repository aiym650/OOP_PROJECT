package university.models;

import university.enums.Language;
import university.patterns.UserFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Admin extends Employee {

    private static final long serialVersionUID = 1L;

    private final List<String> logFile = new ArrayList<>();

    public Admin(String id, String firstName, String lastName,
                 String password, Language language,
                 double salary, String department) {
        super(id, firstName, lastName, password, language, salary, department);
    }

  
    public User createAccount(Map<String, String> params) {
        User user = UserFactory.create(params);
        writeLog("Created account: " + user.getId() + " (" + user.getClass().getSimpleName() + ")");
        return user;
    }

    public void addUser(User user, List<User> userList) {
        userList.add(user);
        writeLog("Added user: " + user.getId());
    }

    public void removeUser(User user, List<User> userList) {
        userList.remove(user);
        writeLog("Removed user: " + user.getId());
    }

    public void updateUser(User user, String field, String value) {
     
        writeLog(String.format("Updated user %s: %s = %s", user.getId(), field, value));
    }

 
    public void writeLog(String entry) {
        String stamped = "[" + LocalDateTime.now() + "] " + entry;
        logFile.add(stamped);
    }

    public void viewLogs() {
        System.out.println("=== System Log ===");
        logFile.forEach(System.out::println);
    }

    public List<String> getLogFile() { return Collections.unmodifiableList(logFile); }

    @Override
    public String getInfo() {
        return String.format("Admin{%s, logs=%d}", getFullName(), logFile.size());
    }
}