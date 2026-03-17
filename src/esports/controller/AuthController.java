package esports.controller;

import esports.model.DataStore;
import esports.model.PasswordUtil;
import esports.model.Session;
import esports.model.User;

public class AuthController {
    private final DataStore dataStore = DataStore.getInstance();
    private final Session   session   = Session.getInstance();

  public boolean login(String username, String password) {
    if (username == null || username.trim().isEmpty()) return false;
    if (password == null || password.isEmpty())        return false;
    User user = dataStore.findUserByUsername(username.trim());
    if (user != null && PasswordUtil.verify(password, user.getPassword())) {
        session.login(user);
        return true;
    }
    return false;
}

    public void logout() {
        session.logout();
    }

    public String getCurrentRole() {
        User u = session.getCurrentUser();
        return u != null ? u.getRole() : null;
    }
}
