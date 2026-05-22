package university.exceptions;

public class MaxFailsReachedException extends Exception {
    public MaxFailsReachedException(String studentName, int failCount) {
        super("Student '" + studentName + "' has reached " + failCount
                + " course failures (max allowed: 3). Academic suspension required.");
    }
}