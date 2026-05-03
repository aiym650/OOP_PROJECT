package university.models;

import university.interfaces.JournalObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversityJournal implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<ResearchPaper>   papers      = new ArrayList<>();
    private final List<JournalObserver> subscribers = new ArrayList<>();

    public UniversityJournal(String name) { this.name = name; }

    public void subscribe(JournalObserver observer) {
        if (!subscribers.contains(observer)) {
            subscribers.add(observer);
            System.out.printf("[Journal '%s'] New subscriber%n", name);
        }
    }

    public void unsubscribe(JournalObserver observer) {
        subscribers.remove(observer);
    }

    public void publishResearchPaper(ResearchPaper paper) {
        papers.add(paper);
        notifySubscribers(paper.getTitle());
    }

    private void notifySubscribers(String paperTitle) {
        subscribers.forEach(obs -> obs.onNewPaperPublished(name, paperTitle));
    }

    public String getName()                        { return name; }
    public List<ResearchPaper> getPapers()         { return Collections.unmodifiableList(papers); }
    public List<JournalObserver> getSubscribers()  { return Collections.unmodifiableList(subscribers); }

    @Override
    public String toString() {
        return String.format("Journal{'%s', papers=%d, subscribers=%d}",
                name, papers.size(), subscribers.size());
    }
}