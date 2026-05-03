package university.models;

import university.enums.RequestStatus;
import university.enums.UrgencyLevel;

import java.io.Serializable;
import java.time.LocalDate;

public class SupportRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String description;
    private final User requester;
    private RequestStatus status;
    private final UrgencyLevel urgency;
    private final LocalDate date;

    public SupportRequest(String description, User requester, UrgencyLevel urgency) {
        this.description = description;
        this.requester   = requester;
        this.urgency     = urgency;
        this.status      = RequestStatus.NEW;
        this.date        = LocalDate.now();
    }

    public RequestStatus getStatus()           { return status; }
    public void setStatus(RequestStatus status){ this.status = status; }
    public User getRequester()                 { return requester; }
    public String getDescription()             { return description; }
    public UrgencyLevel getUrgency()           { return urgency; }

    @Override
    public String toString() {
        return String.format("SupportRequest{from=%s, urgency=%s, status=%s, date=%s, desc='%s'}",
                requester.getFullName(), urgency, status, date, description);
    }
}