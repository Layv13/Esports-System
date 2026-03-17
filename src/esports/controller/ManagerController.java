package esports.controller;

import esports.model.*;
import java.util.List;

public class ManagerController {
    private final DataStore dataStore = DataStore.getInstance();
    private final Session   session   = Session.getInstance();

    public Manager getCurrentManager() { return session.getManagerUser(); }

    public Team getCurrentTeam() {
        Manager m = getCurrentManager();
        if (m == null) return null;
        // Always re-fetch from DB so data is fresh
        return dataStore.findTeamByManagerId(m.getId());
    }

    public boolean hasTeam() {
        return getCurrentTeam() != null;
    }

    /** Create a team for the current manager (persists to MySQL). */
    public String createTeam(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) return "Team name is required.";
        Manager m = getCurrentManager();
        if (m == null)   return "Not logged in as manager.";
        if (hasTeam())   return "You already have a team.";

        String id   = "T" + dataStore.generateId();
        Team   team = new Team(id, teamName.trim(), m.getId());
        team.setRegistered(true);
        dataStore.addTeam(team);
        m.setTeamId(id);
        return null;
    }
    public String changeOwnPassword(String currentPassword, String newPassword) {
    User u = session.getCurrentUser();
    if (u == null) return "Not logged in.";
    if (!esports.model.PasswordUtil.verify(currentPassword, u.getPassword()))
        return "Current password is incorrect.";
    if (newPassword == null || newPassword.trim().length() < 6)
        return "New password must be at least 6 characters.";

    String hashed = esports.model.PasswordUtil.hash(newPassword.trim());
    String sql = "UPDATE users SET password = ? WHERE username = ?";
    try {
        java.sql.PreparedStatement ps =
            esports.model.DatabaseConnection.getInstance().getConnection()
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

    /** Add a player to the current manager's team (persists to MySQL). */
    public String addPlayer(String name, String ign, String role) {
        if (name == null || name.trim().isEmpty()) return "Player name is required.";
        if (ign  == null || ign.trim().isEmpty())  return "IGN is required.";
        if (role == null || role.trim().isEmpty())  return "Role is required.";

        Team team = getCurrentTeam();
        if (team == null)  return "No team found. Create a team first.";
        if (team.isFull()) return "Team is full (max 6 players: 5 main + 1 sixth man).";

        String pid = "P" + dataStore.generateId();
        Player p   = new Player(pid, name.trim(), ign.trim(), role.trim(), team.getId());
        dataStore.addPlayer(p);
        return null;
    }

    /** Remove a player by their ID (persists to MySQL). */
    public String removePlayer(String playerId) {
        if (!dataStore.removePlayer(playerId)) return "Player not found.";
        return null;
    }

    /** Manager withdraws their team from the tournament. */
    public String pullOutTeam() {
        Team team = getCurrentTeam();
        if (team == null) return "You don't have a team to pull out.";

        dataStore.removeTeamFromMatches(team.getId());
        dataStore.removeTeam(team.getId());

        Manager m = getCurrentManager();
        if (m != null) m.setTeamId(null);
        return null;
    }

    public List<Match> getTournamentMatches() {
        return dataStore.getTournament().getMatches();
    }

    public List<Team>  getAllTeams()             { return dataStore.getTeams(); }
    public Team        getTeamById(String id)    { return dataStore.findTeamById(id); }
    public Tournament  getTournament()           { return dataStore.getTournament(); }
}
