package university.models;

import university.enums.*;
import university.exceptions.SupervisorRequirementException;
import university.interfaces.Researcher;

import java.util.*;

public class GraduateStudent extends Student implements Researcher {

    private static final long serialVersionUID = 1L;

    private DegreeType degree;
    private Teacher supervisor;
    private final List<ResearchPaper>   thesisPapers = new ArrayList<>();
    private final List<ResearchProject> projects     = new ArrayList<>();

    public GraduateStudent(String id, String firstName, String lastName,
                           String password, Language language,
                           int year, String major, DegreeType degree) {
        super(id, firstName, lastName, password, language, year, major);
        this.degree = degree;
    }

    public void setSupervisor(Teacher teacher) throws SupervisorRequirementException {
        teacher.validateSupervisorQualification();
        this.supervisor = teacher;
        System.out.printf("[Supervisor] %s assigned to %s%n",
                teacher.getFullName(), getFullName());
    }

    public Teacher getSupervisor() { return supervisor; }

    public void addThesisPaper(ResearchPaper paper) {
        thesisPapers.add(paper);
        addPaper(paper);
    }

    @Override
    public int calculateHIndex() {
        List<Integer> sorted = thesisPapers.stream()
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
        thesisPapers.stream()
                .sorted(comparator)
                .forEach(p -> System.out.println(p.getCitation(Format.PLAIN_TEXT)));
    }

    @Override
    public List<ResearchProject> getProjects() { return Collections.unmodifiableList(projects); }

    @Override
    public List<ResearchPaper> getPapers()     { return Collections.unmodifiableList(thesisPapers); }

    @Override
    public void addPaper(ResearchPaper paper)  { if (!thesisPapers.contains(paper)) thesisPapers.add(paper); }

    @Override
    public void addToProject(ResearchProject p){ projects.add(p); }

    @Override
    public String getInfo() {
        return String.format("GradStudent{%s, degree=%s, supervisor=%s, papers=%d, h=%d}",
                getFullName(), degree,
                supervisor != null ? supervisor.getFullName() : "none",
                thesisPapers.size(), calculateHIndex());
    }

    public DegreeType getDegree() { return degree; }
}