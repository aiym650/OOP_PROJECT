package university.exceptions;

public class CreditLimitExceededException extends Exception {
    private final int attempted;
    private final int limit;

    public CreditLimitExceededException(int attempted, int limit) {
        super(String.format("Credit limit exceeded: attempted %d, max allowed %d.", attempted, limit));
        this.attempted = attempted;
        this.limit = limit;
    }

    public int getAttempted() { return attempted; }
    public int getLimit()     { return limit; }
}