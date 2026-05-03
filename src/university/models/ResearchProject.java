package university.models;

import university.interfaces.Researcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String topic;
    private final List<Researcher>    participants = new ArrayList<>();
    private final List<ResearchPaper> papers       = new ArrayList<>();

    public ResearchProject(String topic) {
        this.topic = topic;
    }

    public void addParticipant(Researcher researcher) {
        if (!participants.contains(researcher)) {
            participants.add(researcher);
            researcher.addToProject(this);
            if (researcher instanceof User u)
                System.out.printf("[Project '%s'] Added participant: %s%n", topic, u.getFullName());
        }
    }

    public void removeParticipant(Researcher researcher) {
        participants.remove(researcher);
    }

    public void addPaper(ResearchPaper paper) { papers.add(paper); }

    public String getTopic()                         { return topic; }
    public List<Researcher> getParticipants()        { return Collections.unmodifiableList(participants); }
    public List<ResearchPaper> getPapers()           { return Collections.unmodifiableList(papers); }

    @Override
    public String toString() {
        return String.format("ResearchProject{topic='%s', participants=%d, papers=%d}",
                topic, participants.size(), papers.size());
    }
}