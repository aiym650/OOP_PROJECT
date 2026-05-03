package university.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private String topic;
    private boolean pinned;
    private final LocalDate date;
    private final List<String> comments = new ArrayList<>();
    private final User author;

    public News(String title, String content, String topic, User author) {
        this.title   = title;
        this.content = content;
        this.topic   = topic;
        this.author  = author;
        this.date    = LocalDate.now();
        this.pinned  = false;
    }

    public void addComment(String comment) { comments.add(comment); }
    public void pin()   { pinned = true; }
    public void unpin() { pinned = false; }

    public String getTitle()               { return title; }
    public String getContent()             { return content; }
    public boolean isPinned()              { return pinned; }
    public List<String> getComments()      { return Collections.unmodifiableList(comments); }
    public User getAuthor()                { return author; }
    public LocalDate getDate()             { return date; }

    @Override
    public String toString() {
        return String.format("News{[%s] '%s' by %s | pinned=%b | comments=%d}",
                topic, title, author.getFullName(), pinned, comments.size());
    }
}