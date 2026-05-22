package university.exceptions;

public class NonResearcherException extends Exception {
    public NonResearcherException(String name) {
        super("'" + name + "' is not a Researcher and cannot join a ResearchProject.");
    }
}