package esports.model;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String name;
    private List<Match>  matches;
    private List<String> teamIds;

    public Tournament(String name) {
        this.name    = name;
        this.matches = new ArrayList<>();
        this.teamIds = new ArrayList<>();
    }

    public String       getName()    { return name; }
    public List<Match>  getMatches() { return matches; }
    public List<String> getTeamIds() { return teamIds; }

    public void addMatch(Match m)   { matches.add(m); }
    public void addTeamId(String id){ if (!teamIds.contains(id)) teamIds.add(id); }
    public void removeTeamId(String id) { teamIds.remove(id); }

    public Match findMatchById(String id) {
        return matches.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Match> getFinishedMatches() {
        List<Match> result = new ArrayList<>();
        for (Match m : matches) if (m.isFinished()) result.add(m);
        return result;
    }

    public List<Match> getUpcomingMatches() {
        List<Match> result = new ArrayList<>();
        for (Match m : matches) if (m.isUpcoming()) result.add(m);
        return result;
    }

    public Match getSemifinal1() {
        return matches.stream()
            .filter(m -> m.getRound() == Match.Round.SEMIFINAL && m.getMatchNumber() == 1)
            .findFirst().orElse(null);
    }

    public Match getSemifinal2() {
        return matches.stream()
            .filter(m -> m.getRound() == Match.Round.SEMIFINAL && m.getMatchNumber() == 2)
            .findFirst().orElse(null);
    }

    public Match getFinal() {
        return matches.stream()
            .filter(m -> m.getRound() == Match.Round.FINAL)
            .findFirst().orElse(null);
    }
}
