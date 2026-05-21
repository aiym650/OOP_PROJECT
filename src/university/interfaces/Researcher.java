package university.interfaces;

import university.models.ResearchPaper;
import university.models.ResearchProject;

import java.util.Comparator;
import java.util.List;

public interface Researcher {

    int calculateHIndex();

    void printPapers(Comparator<ResearchPaper> comparator);

    List<ResearchProject> getProjects();

    List<ResearchPaper> getPapers();

    void addPaper(ResearchPaper paper);

    void addToProject(ResearchProject project);
}