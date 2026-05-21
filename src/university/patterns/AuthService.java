package university.patterns;

import university.exceptions.AuthenticationException;
import university.models.User;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static volatile AuthService instance;

    private final Map<String, User> userRegistry = new HashMap<>();
    private User currentUser;

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new AuthService();
                }
            }
        }
        return instance;
    }

    public void registerUser(User user) {
        userRegistry.put(user.getId(), user);
    }

    public void updateUser(User user) {
        userRegistry.put(user.getId(), user);
    }

    public User login(String userId, String password) throws AuthenticationException {
        User user = userRegistry.get(userId);
        if (user == null || !user.login(password)) {
            throw new AuthenticationException(userId);
        }
        currentUser = user;
        System.out.println("[Auth] Logged in: " + user.getFullName()
                + " (" + user.getClass().getSimpleName() + ")");
        return user;
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }

    public User getCurrentUser()  { return currentUser; }
    public boolean isLoggedIn()   { return currentUser != null; }
    public int getUserCount()     { return userRegistry.size(); }

	
}