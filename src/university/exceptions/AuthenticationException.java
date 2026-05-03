package university.exceptions;
public class AuthenticationException extends Exception {
    public AuthenticationException(String login) {
        super("Authentication failed for login: " + login);
    }
}