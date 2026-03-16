package esports.model;

public class Match {
    public enum Status { UPCOMING, ONGOING, FINISHED }
    public enum Round  { SEMIFINAL, FINAL }

    private String id;
    private String team1Id;
    private String team2Id;
    private int    score1;
    private int    score2;
    private Status status;
    private String winnerId;
    private Round  round;
    private int    matchNumber;

    public Match(String id, String team1Id, String team2Id,
                 int score1, int score2, String statusStr,
                 String winnerId, String roundStr, int matchNumber) {
        this.id          = id;
        this.team1Id     = team1Id;
        this.team2Id     = team2Id;
        this.score1      = score1;
        this.score2      = score2;
        this.status      = Status.valueOf(statusStr);
        this.winnerId    = winnerId;
        this.round       = Round.valueOf(roundStr);
        this.matchNumber = matchNumber;
    }

    public String getId()           { return id; }
    public String getTeam1Id()      { return team1Id; }
    public String getTeam2Id()      { return team2Id; }
    public int    getScore1()       { return score1; }
    public int    getScore2()       { return score2; }
    public Status getStatus()       { return status; }
    public String getWinnerId()     { return winnerId; }
    public Round  getRound()        { return round; }
    public int    getMatchNumber()  { return matchNumber; }

    public void setScore1(int s)        { this.score1 = s; }
    public void setScore2(int s)        { this.score2 = s; }
    public void setStatus(Status st)    { this.status = st; }
    public void setWinnerId(String wid) { this.winnerId = wid; }

    public boolean isFinished()  { return status == Status.FINISHED; }
    public boolean isUpcoming()  { return status == Status.UPCOMING; }

    public String getScoreDisplay() {
        if (status == Status.UPCOMING) return "vs";
        return score1 + " - " + score2;
    }

    public String getRoundLabel() {
        return round == Round.SEMIFINAL ? "Semifinal #" + matchNumber : "Grand Final";
    }
}
