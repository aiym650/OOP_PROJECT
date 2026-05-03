package university.exceptions;
public class MaxFailsReachedException extends Exception {
    public MaxFailsReachedException() {
        super("Student has reached the maximum number of course failures (3).");
    }
}