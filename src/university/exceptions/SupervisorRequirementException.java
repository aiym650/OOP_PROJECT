package university.exceptions;

public class SupervisorRequirementException extends Exception {
    public SupervisorRequirementException(String supervisorName, int hIndex) {
        super(String.format(
            "Teacher '%s' cannot be a supervisor: h-index is %d (minimum required: 3).",
            supervisorName, hIndex));
    }
}