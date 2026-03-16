package esports.model;

public class Player {
    private String id;
    private String name;
    private String ign;   // In-Game Name
    private String role;  // Top, Jungle, Mid, Bot Carry, Support, Sixth Man
    private String teamId;

    public Player(String id, String name, String ign, String role, String teamId) {
        this.id     = id;
        this.name   = name;
        this.ign    = ign;
        this.role   = role;
        this.teamId = teamId;
    }

    public String getId()     { return id; }
    public String getName()   { return name; }
    public String getIgn()    { return ign; }
    public String getRole()   { return role; }
    public String getTeamId() { return teamId; }

    public void setName(String name)   { this.name = name; }
    public void setIgn(String ign)     { this.ign = ign; }
    public void setRole(String role)   { this.role = role; }

    @Override
    public String toString() { return name + " [" + ign + "] - " + role; }
}
