package university.interfaces;

import university.models.ResearchPaper;
import university.models.ResearchProject;
import university.enums.Format;

import java.util.List;

public interface Researcher {

    int calculateHIndex();

    void printPapers(Format format);

    List<ResearchProject> getProjects();

    List<ResearchPaper> getPapers();

    void addPaper(ResearchPaper paper);

    void addToProject(ResearchProject project);
}