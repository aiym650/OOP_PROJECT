package university.models;

import university.enums.Format;
import university.enums.Language;
import university.interfaces.Researcher;

import java.util.*;

public class ResearchEmployee extends Employee implements Researcher {

    private static final long serialVersionUID = 1L;

    private final List<ResearchPaper>   papers   = new ArrayList<>();
    private final List<ResearchProject> projects = new ArrayList<>();

    public ResearchEmployee(String id, String firstName, String lastName,
                            String password, Language language,
                            double salary, String department) {
        super(id, firstName, lastName, password, language, salary, department);
    }

    @Override
    public int calculateHIndex() {
        List<Integer> sorted = papers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();
        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        papers.stream().sorted(comparator).forEach(System.out::println);
    }

    @Override public List<ResearchProject> getProjects() { return Collections.unmodifiableList(projects); }
    @Override public List<ResearchPaper>   getPapers()   { return Collections.unmodifiableList(papers); }
    @Override public void addPaper(ResearchPaper p)      { papers.add(p); }
    @Override public void addToProject(ResearchProject p){ projects.add(p); }

    @Override
    public String getInfo() {
        return String.format("ResearchEmployee{%s, dept=%s, papers=%d, h=%d}",
                getFullName(), getDepartment(), papers.size(), calculateHIndex());
    }
}