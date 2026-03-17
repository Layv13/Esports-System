/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package esports.view.admin;

/**
 *
 * @author raz3d
 */
public class MatchesPanel extends javax.swing.JPanel {
public void refresh() {
    esports.controller.AdminController ctrl = new esports.controller.AdminController();
    javax.swing.table.DefaultTableModel fm =
        (javax.swing.table.DefaultTableModel) tblFinished.getModel();
    javax.swing.table.DefaultTableModel um =
        (javax.swing.table.DefaultTableModel) tblUpcoming.getModel();
    fm.setRowCount(0);
    um.setRowCount(0);
    for (esports.model.Match m : ctrl.getTournament().getMatches()) {
        esports.model.Team t1 = ctrl.getTeamById(m.getTeam1Id());
        esports.model.Team t2 = ctrl.getTeamById(m.getTeam2Id());
        String n1 = t1 != null ? t1.getName() : "TBD";
        String n2 = t2 != null ? t2.getName() : "TBD";
        if (m.isFinished()) {
            esports.model.Team w = ctrl.getTeamById(m.getWinnerId());
            fm.addRow(new Object[]{
                m.getRoundLabel(), n1,
                m.getScore1() + " — " + m.getScore2(),
                n2, w != null ? " " + w.getName() : "?"
            });
        } else {
            um.addRow(new Object[]{m.getRoundLabel(), n1, "VS", n2, "⏳ UPCOMING"});
        }
    }
}
    private void setSemifinalResult(String matchId) {
    esports.controller.AdminController ctrl = 
        new esports.controller.AdminController();
    esports.model.Tournament t = ctrl.getTournament();

    // Find the match
    esports.model.Match m = t.findMatchById(matchId);
    if (m == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Match not found. Generate bracket first.",
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Get team names from DB
    esports.model.Team t1 = ctrl.getTeamById(m.getTeam1Id());
    esports.model.Team t2 = ctrl.getTeamById(m.getTeam2Id());
    String n1 = t1 != null ? t1.getName() : "Team 1";
    String n2 = t2 != null ? t2.getName() : "Team 2";

    // Ask for scores
    String s1 = javax.swing.JOptionPane.showInputDialog(this,
        "Enter score for " + n1 + ":", "Set Result",
        javax.swing.JOptionPane.QUESTION_MESSAGE);
    if (s1 == null) return;

    String s2 = javax.swing.JOptionPane.showInputDialog(this,
        "Enter score for " + n2 + ":", "Set Result",
        javax.swing.JOptionPane.QUESTION_MESSAGE);
    if (s2 == null) return;

    try {
        int score1 = Integer.parseInt(s1.trim());
        int score2 = Integer.parseInt(s2.trim());

        if (score1 == score2) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Scores cannot be equal. There must be a winner.",
                "Invalid Score", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Determine winner
        String winnerId = score1 > score2 
            ? m.getTeam1Id() 
            : m.getTeam2Id();
        String winnerName = score1 > score2 ? n1 : n2;

        // Save to DB
        m.setScore1(score1);
        m.setScore2(score2);
        m.setStatus(esports.model.Match.Status.FINISHED);
        m.setWinnerId(winnerId);
        esports.model.DataStore.getInstance().updateMatch(m);

        // Now update the Final match to use the actual winners
        updateFinalWithWinners(ctrl);

        javax.swing.JOptionPane.showMessageDialog(this,
            "Result saved!\n" +
            n1 + " " + score1 + " — " + score2 + " " + n2 + "\n" +
            "Winner: " + winnerName,
            "Result Saved",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        refresh();

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Please enter valid numbers.",
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    private void updateFinalWithWinners(esports.controller.AdminController ctrl) {
    esports.model.Tournament t = ctrl.getTournament();

    esports.model.Match sf1 = t.findMatchById("SF1");
    esports.model.Match sf2 = t.findMatchById("SF2");
    esports.model.Match fin = t.findMatchById("F1");

    if (sf1 == null || sf2 == null || fin == null) return;

    // Only update final if both semis are finished
    if (!sf1.isFinished() || !sf2.isFinished()) return;

    // Get the two winners
    String winner1 = sf1.getWinnerId();  // SF1 winner
    String winner2 = sf2.getWinnerId();  // SF2 winner

    if (winner1 == null || winner2 == null) return;

    // Update Final match teams to be the actual winners
    try {
        java.sql.Connection conn = esports.model.DatabaseConnection
            .getInstance().getConnection();

        java.sql.PreparedStatement ps = conn.prepareStatement(
            "UPDATE matches SET team1_id = ?, team2_id = ?, " +
            "status = 'UPCOMING', score1 = 0, score2 = 0, " +
            "winner_id = NULL WHERE id = 'F1'");
        ps.setString(1, winner1);
        ps.setString(2, winner2);
        ps.executeUpdate();

    } catch (java.sql.SQLException e) {
        e.printStackTrace();
    }
}
    public MatchesPanel() {
        initComponents();
        for (javax.swing.JTable t : new javax.swing.JTable[]{tblFinished, tblUpcoming}) {
    t.getTableHeader().setBackground(new java.awt.Color(14, 18, 30));
    t.getTableHeader().setForeground(new java.awt.Color(148, 163, 184));
    t.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 13));
    t.setSelectionBackground(new java.awt.Color(99, 102, 241, 60));
}
scrollFinished.getViewport().setBackground(new java.awt.Color(20, 26, 44));
scrollUpcoming.getViewport().setBackground(new java.awt.Color(20, 26, 44));
refresh();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPageTitle = new javax.swing.JLabel();
        pnlFinished = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scrollFinished = new javax.swing.JScrollPane();
        tblFinished = new javax.swing.JTable();
        pnlUpcoming = new javax.swing.JPanel();
        scrollUpcoming = new javax.swing.JScrollPane();
        tblUpcoming = new javax.swing.JTable();
        lblUpcomingTitle = new javax.swing.JLabel();
        btnSetFinalResult = new javax.swing.JButton();
        btnResetTournament = new javax.swing.JButton();
        btnGenerateBracket = new javax.swing.JButton();
        btnSetSF2 = new javax.swing.JButton();
        btnSetSF1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(9, 11, 19));

        lblPageTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblPageTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblPageTitle.setText("Match Results & Schedule");

        pnlFinished.setBackground(new java.awt.Color(20, 25, 44));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("COMPLETED MATCHES");

        tblFinished.setBackground(new java.awt.Color(20, 25, 44));
        tblFinished.setForeground(new java.awt.Color(255, 255, 255));
        tblFinished.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Round", "Team 1", "Score", "Team 2", "Winner"
            }
        ));
        tblFinished.setGridColor(new java.awt.Color(30, 38, 60));
        tblFinished.setRowHeight(38);
        scrollFinished.setViewportView(tblFinished);

        javax.swing.GroupLayout pnlFinishedLayout = new javax.swing.GroupLayout(pnlFinished);
        pnlFinished.setLayout(pnlFinishedLayout);
        pnlFinishedLayout.setHorizontalGroup(
            pnlFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFinishedLayout.createSequentialGroup()
                .addGroup(pnlFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFinishedLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1))
                    .addGroup(pnlFinishedLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(scrollFinished, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        pnlFinishedLayout.setVerticalGroup(
            pnlFinishedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFinishedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollFinished, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlUpcoming.setBackground(new java.awt.Color(20, 25, 44));

        tblUpcoming.setBackground(new java.awt.Color(20, 25, 44));
        tblUpcoming.setForeground(new java.awt.Color(255, 255, 255));
        tblUpcoming.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Round", "Team 1", "vs", "Team 2", "Status"
            }
        ));
        scrollUpcoming.setViewportView(tblUpcoming);

        lblUpcomingTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUpcomingTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblUpcomingTitle.setText("UPCOMING MATCHES");

        javax.swing.GroupLayout pnlUpcomingLayout = new javax.swing.GroupLayout(pnlUpcoming);
        pnlUpcoming.setLayout(pnlUpcomingLayout);
        pnlUpcomingLayout.setHorizontalGroup(
            pnlUpcomingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUpcomingLayout.createSequentialGroup()
                .addGroup(pnlUpcomingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlUpcomingLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(scrollUpcoming, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlUpcomingLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblUpcomingTitle)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlUpcomingLayout.setVerticalGroup(
            pnlUpcomingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlUpcomingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUpcomingTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollUpcoming, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnSetFinalResult.setBackground(new java.awt.Color(127, 29, 29));
        btnSetFinalResult.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSetFinalResult.setForeground(new java.awt.Color(255, 255, 255));
        btnSetFinalResult.setText("Set Final Result");
        btnSetFinalResult.setBorderPainted(false);
        btnSetFinalResult.addActionListener(this::btnSetFinalResultActionPerformed);

        btnResetTournament.setBackground(new java.awt.Color(127, 29, 29));
        btnResetTournament.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnResetTournament.setForeground(new java.awt.Color(255, 255, 255));
        btnResetTournament.setText("Reset Tournament");
        btnResetTournament.setBorderPainted(false);
        btnResetTournament.addActionListener(this::btnResetTournamentActionPerformed);

        btnGenerateBracket.setBackground(new java.awt.Color(127, 29, 29));
        btnGenerateBracket.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGenerateBracket.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateBracket.setText("Generate Bracket");
        btnGenerateBracket.setBorderPainted(false);
        btnGenerateBracket.addActionListener(this::btnGenerateBracketActionPerformed);

        btnSetSF2.setBackground(new java.awt.Color(127, 29, 29));
        btnSetSF2.setForeground(new java.awt.Color(255, 255, 255));
        btnSetSF2.setText("Set SF2 Result");
        btnSetSF2.setBorderPainted(false);
        btnSetSF2.setFocusPainted(false);
        btnSetSF2.addActionListener(this::btnSetSF2ActionPerformed);

        btnSetSF1.setBackground(new java.awt.Color(127, 29, 29));
        btnSetSF1.setForeground(new java.awt.Color(255, 255, 255));
        btnSetSF1.setText("Set SF1 Result");
        btnSetSF1.setBorderPainted(false);
        btnSetSF1.setFocusPainted(false);
        btnSetSF1.addActionListener(this::btnSetSF1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPageTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlFinished, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlUpcoming, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnSetSF2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSetFinalResult, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnSetSF1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(109, 109, 109)
                        .addComponent(btnResetTournament)
                        .addGap(80, 80, 80)
                        .addComponent(btnGenerateBracket)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lblPageTitle)
                .addGap(46, 46, 46)
                .addComponent(pnlFinished, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlUpcoming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnResetTournament)
                            .addComponent(btnGenerateBracket))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(btnSetSF1)
                        .addGap(18, 18, 18)
                        .addComponent(btnSetSF2)
                        .addGap(18, 18, 18)
                        .addComponent(btnSetFinalResult)
                        .addGap(24, 24, 24))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSetFinalResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetFinalResultActionPerformed
        esports.controller.AdminController ctrl = 
        new esports.controller.AdminController();
    esports.model.Tournament t = ctrl.getTournament();
    esports.model.Match fin = t.getFinal();

    if (fin == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Final match not found. Generate bracket first.",
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Read ACTUAL team names from DB — not hardcoded
    esports.model.Team t1 = ctrl.getTeamById(fin.getTeam1Id());
    esports.model.Team t2 = ctrl.getTeamById(fin.getTeam2Id());

    if (t1 == null || t2 == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Finalists not determined yet.\n" +
            "Please set both semifinal results first.",
            "Not Ready", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    String n1 = t1.getName();
    String n2 = t2.getName();

    String s1 = javax.swing.JOptionPane.showInputDialog(this,
        "Enter score for " + n1 + ":", "Grand Final Result",
        javax.swing.JOptionPane.QUESTION_MESSAGE);
    if (s1 == null) return;

    String s2 = javax.swing.JOptionPane.showInputDialog(this,
        "Enter score for " + n2 + ":", "Grand Final Result",
        javax.swing.JOptionPane.QUESTION_MESSAGE);
    if (s2 == null) return;

    try {
        int score1 = Integer.parseInt(s1.trim());
        int score2 = Integer.parseInt(s2.trim());

        if (score1 == score2) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Scores cannot be equal. There must be a champion.",
                "Invalid Score", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String winnerId   = score1 > score2 ? fin.getTeam1Id() : fin.getTeam2Id();
        String winnerName = score1 > score2 ? n1 : n2;

        String err = ctrl.setFinalResult(score1, score2, winnerId);

        if (err == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Grand Final result saved!\n\n" +
                n1 + " " + score1 + " — " + score2 + " " + n2 + "\n\n" +
                " CHAMPION: " + winnerName,
                "Champion Crowned",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            refresh();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error: " + err, "Failed",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Please enter valid numbers.",
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnSetFinalResultActionPerformed

    private void btnResetTournamentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTournamentActionPerformed
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
        "This will DELETE all teams, players and match results.\n" +
        "The tournament will be completely reset.\n\n" +
        "Are you absolutely sure?",
        "Reset Tournament",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.WARNING_MESSAGE);

    if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

    // Second confirmation
    int confirm2 = javax.swing.JOptionPane.showConfirmDialog(this,
        "This cannot be undone. Continue?",
        "Final Warning",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.ERROR_MESSAGE);

    if (confirm2 != javax.swing.JOptionPane.YES_OPTION) return;

    try {
        java.sql.Connection conn = esports.model.DatabaseConnection
            .getInstance().getConnection();

        // Delete everything in correct order (foreign keys)
        conn.createStatement().executeUpdate("DELETE FROM matches");
        conn.createStatement().executeUpdate("DELETE FROM players");
        conn.createStatement().executeUpdate("DELETE FROM teams");
        conn.createStatement().executeUpdate(
            "UPDATE users SET role = role WHERE role = 'MANAGER'");

        // Reset manager team references (in memory session)
        for (esports.model.Manager m : 
                new esports.controller.AdminController().getManagers()) {
            m.setTeamId(null);
        }

        javax.swing.JOptionPane.showMessageDialog(this,
            "Tournament reset! You can now register new teams.\n" +
            "Use Register Manager to add managers,\n" +
            "then they log in and create their teams.",
            "Reset Complete",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        refresh();

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Error resetting: " + e.getMessage(),
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnResetTournamentActionPerformed

    private void btnGenerateBracketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateBracketActionPerformed
         esports.controller.AdminController ctrl = 
        new esports.controller.AdminController();
    java.util.List<esports.model.Team> teams = ctrl.getTeams();

    // Need exactly 4 teams
    if (teams.size() < 4) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Need exactly 4 registered teams to generate bracket.\n" +
            "Currently have: " + teams.size() + " team(s).",
            "Not Enough Teams",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
        "Generate bracket with these teams?\n\n" +
        "SF1: " + teams.get(0).getName() + " vs " + teams.get(1).getName() + "\n" +
        "SF2: " + teams.get(2).getName() + " vs " + teams.get(3).getName() + "\n" +
        "Final: Winners of SF1 vs SF2",
        "Generate Bracket",
        javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

    try {
        java.sql.Connection conn = esports.model.DatabaseConnection
            .getInstance().getConnection();

        // Clear old matches first
        conn.createStatement().executeUpdate("DELETE FROM matches");

        // Get tournament ID
        java.sql.ResultSet rs = conn.createStatement()
            .executeQuery("SELECT id FROM tournaments LIMIT 1");
        if (!rs.next()) {
            // Create tournament if none exists
            conn.createStatement().executeUpdate(
                "INSERT INTO tournaments (name) VALUES ('NEXUS CUP 2025')");
            rs = conn.createStatement()
                .executeQuery("SELECT id FROM tournaments LIMIT 1");
            rs.next();
        }
        int tId = rs.getInt("id");

        String t1 = teams.get(0).getId();
        String t2 = teams.get(1).getId();
        String t3 = teams.get(2).getId();
        String t4 = teams.get(3).getId();

        // Insert SF1, SF2, Final
        java.sql.PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO matches (id, tournament_id, team1_id, team2_id, " +
            "score1, score2, status, winner_id, round, match_number) " +
            "VALUES (?,?,?,?,0,0,'UPCOMING',NULL,?,?)");

        ps.setString(1, "SF1"); ps.setInt(2, tId);
        ps.setString(3, t1);   ps.setString(4, t2);
        ps.setString(5, "SEMIFINAL"); ps.setInt(6, 1);
        ps.executeUpdate();

        ps.setString(1, "SF2"); ps.setInt(2, tId);
        ps.setString(3, t3);   ps.setString(4, t4);
        ps.setString(5, "SEMIFINAL"); ps.setInt(6, 2);
        ps.executeUpdate();

      java.sql.PreparedStatement psFinal = conn.prepareStatement(
    "INSERT INTO matches " +
    "(id, tournament_id, team1_id, team2_id, score1, score2, status, winner_id, round, match_number) " +
    "VALUES ('F1', ?, ?, ?, 0, 0, 'UPCOMING', NULL, 'FINAL', 3)");
psFinal.setInt(1, tId);
psFinal.setString(2, t1);  // temp — will be replaced when SF1 finishes
psFinal.setString(3, t3);  // temp — will be replaced when SF2 finishes
psFinal.executeUpdate();

        javax.swing.JOptionPane.showMessageDialog(this,
            "Bracket generated!\n\n" +
            "SF1: " + teams.get(0).getName() + " vs " + teams.get(1).getName() + "\n" +
            "SF2: " + teams.get(2).getName() + " vs " + teams.get(3).getName() + "\n" +
            "Final: TBD",
            "Success",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);

        refresh();

    } catch (java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Error generating bracket: " + e.getMessage(),
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnGenerateBracketActionPerformed

    private void btnSetSF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetSF1ActionPerformed
        setSemifinalResult("SF1");
    }//GEN-LAST:event_btnSetSF1ActionPerformed

    private void btnSetSF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetSF2ActionPerformed
        setSemifinalResult("SF2");
    }//GEN-LAST:event_btnSetSF2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerateBracket;
    private javax.swing.JButton btnResetTournament;
    private javax.swing.JButton btnSetFinalResult;
    private javax.swing.JButton btnSetSF1;
    private javax.swing.JButton btnSetSF2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblPageTitle;
    private javax.swing.JLabel lblUpcomingTitle;
    private javax.swing.JPanel pnlFinished;
    private javax.swing.JPanel pnlUpcoming;
    private javax.swing.JScrollPane scrollFinished;
    private javax.swing.JScrollPane scrollUpcoming;
    private javax.swing.JTable tblFinished;
    private javax.swing.JTable tblUpcoming;
    // End of variables declaration//GEN-END:variables
}
