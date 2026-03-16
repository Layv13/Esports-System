package esports.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStore — replaces the old in-memory store with live MySQL queries.
 * Every method hits the database; no data is cached in Java.
 */
public class DataStore {

    private static DataStore instance;

    private DataStore() {}

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // ================================================================
    //  ID GENERATION
    // ================================================================
    public String generateId() {
        return String.valueOf(System.currentTimeMillis() % 100000);
    }

    // ================================================================
    //  USERS
    // ================================================================
    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean addUser(User user) {
        if (findUserByUsername(user.getUsername()) != null) return false;
        String sql = "INSERT INTO users (id, username, password, full_name, role) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean removeUser(String username) {
        String sql = "DELETE FROM users WHERE username = ? AND role != 'ADMIN'";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Manager> getManagers() {
        List<Manager> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'MANAGER'";
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Manager m = new Manager(
                    rs.getString("id"), rs.getString("username"),
                    rs.getString("password"), rs.getString("full_name")
                );
                Team t = findTeamByManagerId(m.getId());
                if (t != null) m.setTeamId(t.getId());
                list.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        if ("ADMIN".equals(role)) {
            return new Admin(rs.getString("id"), rs.getString("username"),
                             rs.getString("password"), rs.getString("full_name"));
        } else {
            Manager m = new Manager(rs.getString("id"), rs.getString("username"),
                                    rs.getString("password"), rs.getString("full_name"));
            Team t = findTeamByManagerId(m.getId());
            if (t != null) m.setTeamId(t.getId());
            return m;
        }
    }

    // ================================================================
    //  TEAMS
    // ================================================================
    public List<Team> getTeams() {
        List<Team> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM teams")) {
            while (rs.next()) {
                Team t = mapTeam(rs);
                t.getPlayers().addAll(getPlayersByTeam(t.getId()));
                list.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Team findTeamById(String id) {
        if (id == null || id.isEmpty()) return null;
        String sql = "SELECT * FROM teams WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Team t = mapTeam(rs);
                t.getPlayers().addAll(getPlayersByTeam(t.getId()));
                return t;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Team findTeamByManagerId(String managerId) {
        String sql = "SELECT * FROM teams WHERE manager_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, managerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Team t = mapTeam(rs);
                t.getPlayers().addAll(getPlayersByTeam(t.getId()));
                return t;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void addTeam(Team team) {
        String sql = "INSERT INTO teams (id, name, manager_id, registered) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, team.getId());
            ps.setString(2, team.getName());
            ps.setString(3, team.getManagerId());
            ps.setBoolean(4, team.isRegistered());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean removeTeam(String id) {
        String sql = "DELETE FROM teams WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Team mapTeam(ResultSet rs) throws SQLException {
        Team t = new Team(rs.getString("id"), rs.getString("name"), rs.getString("manager_id"));
        t.setRegistered(rs.getBoolean("registered"));
        return t;
    }

    // ================================================================
    //  PLAYERS
    // ================================================================
    public List<Player> getPlayersByTeam(String teamId) {
        List<Player> list = new ArrayList<>();
        String sql = "SELECT * FROM players WHERE team_id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, teamId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Player(rs.getString("id"), rs.getString("name"),
                                    rs.getString("ign"), rs.getString("role"),
                                    rs.getString("team_id")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void addPlayer(Player p) {
        String sql = "INSERT INTO players (id, name, ign, role, team_id) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getIgn());
            ps.setString(4, p.getRole());
            ps.setString(5, p.getTeamId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean removePlayer(String playerId) {
        String sql = "DELETE FROM players WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, playerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ================================================================
    //  TOURNAMENT
    // ================================================================
    public Tournament getTournament() {
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tournaments LIMIT 1")) {
            if (rs.next()) {
                Tournament t = new Tournament(rs.getString("name"));
                int tId = rs.getInt("id");
                for (Match m : getMatchesByTournament(tId)) {
                    t.addMatch(m);
                }
                for (Match m : t.getMatches()) {
                    t.addTeamId(m.getTeam1Id());
                    t.addTeamId(m.getTeam2Id());
                }
                return t;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new Tournament("NEXUS CUP 2025");
    }

    private List<Match> getMatchesByTournament(int tournamentId) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT * FROM matches WHERE tournament_id = ? ORDER BY match_number";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Match(
                    rs.getString("id"),
                    rs.getString("team1_id"),
                    rs.getString("team2_id"),
                    rs.getInt("score1"),
                    rs.getInt("score2"),
                    rs.getString("status"),
                    rs.getString("winner_id"),
                    rs.getString("round"),
                    rs.getInt("match_number")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void updateMatch(Match m) {
        String sql = "UPDATE matches SET score1=?, score2=?, status=?, winner_id=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, m.getScore1());
            ps.setInt(2, m.getScore2());
            ps.setString(3, m.getStatus().name());
            ps.setString(4, m.getWinnerId());
            ps.setString(5, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void removeTeamFromMatches(String teamId) {
        String sql = "UPDATE matches SET status='UPCOMING', winner_id=NULL, score1=0, score2=0 " +
                     "WHERE team1_id=? OR team2_id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, teamId);
            ps.setString(2, teamId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
