package esports.model;

public class Manager extends User {
    private String teamId;

    public Manager(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, "MANAGER");
        this.teamId = null;
    }

    public String getTeamId()          { return teamId; }
    public void   setTeamId(String id) { this.teamId = id; }
    public boolean hasTeam()           { return teamId != null && !teamId.isEmpty(); }
}
