package university.models;

import university.interfaces.JournalObserver;
import university.interfaces.Researcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;  
import java.util.List;

public class UniversityJournal implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<ResearchPaper>   papers      = new ArrayList<>();
    private final List<JournalObserver> subscribers = new ArrayList<>();
    private final List<News> autoNews = new ArrayList<>();
    private Manager newsManager;
    
    public void setNewsManager(Manager manager) { this.newsManager = manager; }
    
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

        if (newsManager != null) {
            // News about the new paper
            String title   = "New paper published in " + name;
            String content = "\"" + paper.getTitle() + "\" has been published in " + name;
            newsManager.createNews(title, content, "Research");

            // Auto-news: top cited researcher in this journal
            papers.stream()
                .flatMap(p -> p.getAuthors().stream())
                .distinct()
                .max(Comparator.comparingInt(Researcher::calculateHIndex))
                .ifPresent(top -> {
                    if (top instanceof User u) {
                        String topTitle   = "Top cited researcher in " + name + ": " + u.getFullName();
                        String topContent = u.getFullName() + " leads with h-index="
                                + top.calculateHIndex() + " in journal '" + name + "'.";
                        newsManager.createNews(topTitle, topContent, "Research");
                    }
                });
        }
    }
    
 
    public static void printTopCitedResearcher(List<? extends Researcher> researchers) {
        researchers.stream()
            .max(Comparator.comparingInt(Researcher::calculateHIndex))
            .ifPresent(r -> {
                if (r instanceof User u)
                    System.out.println("[Top Researcher] " + u.getFullName()
                            + " | h-index=" + r.calculateHIndex());
            });
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