package esports.controller;

import esports.model.*;
import java.util.List;

public class AdminController {
    private final DataStore dataStore = DataStore.getInstance();

    public String registerManager(String username, String password, String fullName) {
        if (username == null || username.trim().isEmpty()) return "Username is required.";
        if (password == null || password.trim().isEmpty()) return "Password is required.";
        if (fullName == null || fullName.trim().isEmpty()) return "Full name is required.";
        if (dataStore.findUserByUsername(username.trim()) != null) return "Username already exists.";

        String id = "M" + dataStore.generateId();
        Manager manager = new Manager(id, username.trim(), password.trim(), fullName.trim());
        dataStore.addUser(manager);
        return null;
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

    /** Set and persist the final match result to MySQL. */
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
