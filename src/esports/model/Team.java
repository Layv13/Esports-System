package esports.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String id;
    private String name;
    private String managerId;
    private List<Player> players;
    private boolean registered;

    public Team(String id, String name, String managerId) {
        this.id         = id;
        this.name       = name;
        this.managerId  = managerId;
        this.players    = new ArrayList<>();
        this.registered = false;
    }

    public String       getId()         { return id; }
    public String       getName()       { return name; }
    public String       getManagerId()  { return managerId; }
    public List<Player> getPlayers()    { return players; }
    public boolean      isRegistered()  { return registered; }

    public void setName(String n)           { this.name = n; }
    public void setRegistered(boolean r)    { this.registered = r; }

    public void addPlayer(Player p)         { players.add(p); }

    public boolean removePlayer(String playerId) {
        return players.removeIf(p -> p.getId().equals(playerId));
    }

    public Player getPlayerById(String pid) {
        return players.stream().filter(p -> p.getId().equals(pid)).findFirst().orElse(null);
    }

    public int getPlayerCount()             { return players.size(); }
    public boolean isFull()                 { return players.size() >= 6; }

    @Override
    public String toString() { return name; }
}
