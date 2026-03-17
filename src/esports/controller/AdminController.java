package esports.controller;

import esports.model.*;
import java.util.List;

public class AdminController {
    private final DataStore dataStore = DataStore.getInstance();

    public String registerManager(String username, String password, String fullName) {
    if (username == null || username.trim().isEmpty()) return "Username is required.";
    if (password == null || password.trim().isEmpty()) return "Password is required.";
    if (fullName == null || fullName.trim().isEmpty()) return "Full name is required.";
    if (dataStore.findUserByUsername(username.trim()) != null) 
        return "Username already exists.";

    String id = "M" + dataStore.generateId();
    // Hash the password before saving
    String hashed = PasswordUtil.hash(password.trim());
    Manager manager = new Manager(id, username.trim(), hashed, fullName.trim());
    dataStore.addUser(manager);
    return null;
}
    public String resetManagerPassword(String username, String newPassword) {
    if (newPassword == null || newPassword.trim().isEmpty()) 
        return "New password cannot be empty.";
    if (newPassword.trim().length() < 6) 
        return "Password must be at least 6 characters.";

    User u = dataStore.findUserByUsername(username);
    if (u == null) return "User not found.";
    if ("ADMIN".equals(u.getRole())) return "Cannot reset admin password here.";

    String hashed = PasswordUtil.hash(newPassword.trim());
    
    String sql = "UPDATE users SET password = ? WHERE username = ?";
    try {
        java.sql.PreparedStatement ps = 
            DatabaseConnection.getInstance().getConnection()
            .prepareStatement(sql);
        ps.setString(1, hashed);
        ps.setString(2, username);
        ps.executeUpdate();
        u.setPassword(hashed); // update in memory too
        return null; // success
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
        return "Database error: " + e.getMessage();
    }
}
public String changeOwnPassword(String currentPassword, String newPassword) {
    User u = esports.model.Session.getInstance().getCurrentUser();
    if (u == null) return "Not logged in.";
    if (!PasswordUtil.verify(currentPassword, u.getPassword()))
        return "Current password is incorrect.";
    if (newPassword == null || newPassword.trim().length() < 6)
        return "New password must be at least 6 characters.";

    String hashed = PasswordUtil.hash(newPassword.trim());
    String sql = "UPDATE users SET password = ? WHERE username = ?";
    try {
        java.sql.PreparedStatement ps =
            DatabaseConnection.getInstance().getConnection()
            .prepareStatement(sql);
        ps.setString(1, hashed);
        ps.setString(2, u.getUsername());
        ps.executeUpdate();
        u.setPassword(hashed);
        return null;
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
        return "Database error: " + e.getMessage();
    }
}
    public String removeManager(String username) {
        User u = dataStore.findUserByUsername(username);
        if (u == null || !(u instanceof Manager)) return "Manager not found.";
        Manager m = (Manager) u;
        if (m.hasTeam()) {
            dataStore.removeTeamFromMatches(m.getTeamId());
            dataStore.removeTeam(m.getTeamId());
        }
        dataStore.removeUser(username);
        return null;
    }

    public List<Manager>  getManagers()         { return dataStore.getManagers(); }
    public List<Team>     getTeams()             { return dataStore.getTeams(); }
    public Tournament     getTournament()        { return dataStore.getTournament(); }
    public Team           getTeamById(String id) { return dataStore.findTeamById(id); }

   
    public String setFinalResult(int score1, int score2, String winnerId) {
        Match fin = dataStore.getTournament().getFinal();
        if (fin == null) return "Final match not found.";
        fin.setScore1(score1);
        fin.setScore2(score2);
        fin.setWinnerId(winnerId);
        fin.setStatus(Match.Status.FINISHED);
        dataStore.updateMatch(fin);
        return null;
    }
}
