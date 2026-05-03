package university.models;

import university.enums.Format;
import university.interfaces.Researcher;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {

    private static final long serialVersionUID = 1L;

    private String title;
    private List<Researcher> authors;
    private int citations;
    private String journal;
    private int pages;
    private LocalDate date;
    private String doi;

    public ResearchPaper(String title, String journal, int pages, LocalDate date, String doi) {
        this.title   = title;
        this.journal = journal;
        this.pages   = pages;
        this.date    = date;
        this.doi     = doi;
        this.authors = new ArrayList<>();
        this.citations = 0;
    }

    public String getCitation(Format format) {
        String authorStr = buildAuthorString();
        if (format == Format.BIBTEX) {
            return String.format(
                "@article{%s%d,%n  title={%s},%n  author={%s},%n  journal={%s},%n  year={%d},%n  pages={%d},%n  doi={%s}%n}",
                title.replaceAll("\\s+", "").toLowerCase().substring(0, Math.min(8, title.length())),
                date.getYear(), title, authorStr, journal, date.getYear(), pages, doi);
        }
     
        return String.format("%s (%d). \"%s\". %s, pp. %d. DOI: %s",
                authorStr, date.getYear(), title, journal, pages, doi);
    }

    private String buildAuthorString() {
        if (authors.isEmpty()) return "Unknown";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i) instanceof User u) {
                sb.append(u.getLastName()).append(" ").append(u.getFirstName().charAt(0)).append(".");
            }
            if (i < authors.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations);
    }

    public static final Comparator<ResearchPaper> BY_TITLE =
            Comparator.comparing(p -> p.title.toLowerCase());

    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDate).reversed();

    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    public String getTitle()                  { return title; }
    public List<Researcher> getAuthors()      { return authors; }
    public int getCitations()                 { return citations; }
    public String getJournal()                { return journal; }
    public int getPages()                     { return pages; }
    public LocalDate getDate()                { return date; }
    public String getDoi()                    { return doi; }
    public int getLength()                    { return pages; }

    public void addAuthor(Researcher r)       { authors.add(r); }
    public void setCitations(int c)           { this.citations = c; }
    public void addCitation()                 { this.citations++; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper p)) return false;
        return Objects.equals(doi, p.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }

    @Override
    public String toString() {
        return String.format("ResearchPaper{\"%s\", journal='%s', citations=%d, date=%s}",
                title, journal, citations, date);
    }
}