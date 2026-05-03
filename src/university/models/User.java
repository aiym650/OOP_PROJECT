package university.models;

import university.enums.Language;
import university.interfaces.JournalObserver;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable, JournalObserver {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String firstName;
    protected String lastName;
    protected String password;     
    protected Language language;    

    public User(String id, String firstName, String lastName, String password, Language language) {
        this.id        = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.password  = password;
        this.language  = language;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public void logout() {
        System.out.println(getFullName() + " logged out.");
    }
    public abstract String getInfo();

    @Override
    public void onNewPaperPublished(String journalName, String paperTitle) {
        System.out.printf("[Notification → %s] New paper in '%s': \"%s\"%n",
                getFullName(), journalName, paperTitle);
    }

    public String getId()                      { return id; }
    public String getFirstName()               { return firstName; }
    public String getLastName()                { return lastName; }
    public String getFullName()                { return firstName + " " + lastName; }
    public Language getLanguage()              { return language; }
    public void setLanguage(Language language) { this.language = language; }
    public void setPassword(String password)   { this.password = password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', lang=%s}", id, getFullName(), language);
    }
}