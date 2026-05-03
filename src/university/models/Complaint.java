package university.models;

import university.enums.UrgencyLevel;

import java.io.Serializable;
import java.time.LocalDate;

public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    private final User complainant;
    private final String description;
    private final UrgencyLevel urgency;
    private final LocalDate date;
    private boolean resolved;

    public Complaint(User complainant, String description, UrgencyLevel urgency) {
        this.complainant = complainant;
        this.description = description;
        this.urgency     = urgency;
        this.date        = LocalDate.now();
        this.resolved    = false;
    }

    public void resolve()                  { resolved = true; }
    public User getComplainant()           { return complainant; }
    public String getDescription()         { return description; }
    public UrgencyLevel getUrgency()       { return urgency; }
    public boolean isResolved()            { return resolved; }

    @Override
    public String toString() {
        return String.format("Complaint{from=%s, urgency=%s, resolved=%b, date=%s}",
                complainant.getFullName(), urgency, resolved, date);
    }
}