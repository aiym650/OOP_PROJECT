package university.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final User sender;
    private final User receiver;
    private final String content;
    private final LocalDate date;
    private boolean isRead;

    public Message(User sender, User receiver, String content) {
        this.sender   = sender;
        this.receiver = receiver;
        this.content  = content;
        this.date     = LocalDate.now();
        this.isRead   = false;
    }

    public void markAsRead()        { isRead = true; }
    public User getSender()         { return sender; }
    public User getReceiver()       { return receiver; }
    public String getContent()      { return content; }
    public LocalDate getDate()      { return date; }
    public boolean isRead()         { return isRead; }

    @Override
    public String toString() {
        return String.format("Message{from=%s, to=%s, date=%s, read=%b, content='%s'}",
                sender.getFullName(), receiver.getFullName(), date, isRead, content);
    }
}