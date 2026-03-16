package esports.model;

public class Session {
    private static Session instance;
    private User currentUser;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void login(User user)    { this.currentUser = user; }
    public void logout()            { this.currentUser = null; }
    public User getCurrentUser()    { return currentUser; }
    public boolean isLoggedIn()     { return currentUser != null; }
    public boolean isAdmin()        { return isLoggedIn() && "ADMIN".equals(currentUser.getRole()); }
    public boolean isManager()      { return isLoggedIn() && "MANAGER".equals(currentUser.getRole()); }

    public Manager getManagerUser() {
        if (isManager()) return (Manager) currentUser;
        return null;
    }
}
