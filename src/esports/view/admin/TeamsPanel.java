package esports.view.admin;

import esports.controller.AdminController;
import esports.model.*;
import esports.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeamsPanel extends JPanel {

    private final AdminController    ctrl = new AdminController();
    private JTable  teamsTable;
    private DefaultTableModel teamsModel;
    private JTable  playersTable;
    private DefaultTableModel playersModel;
    private JLabel  teamInfoLabel;

    public TeamsPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel title = UIHelper.titleLabel("All Teams");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBackground(UIConstants.BG_DARK);
        split.setBorder(null);
        split.setDividerSize(8);
        split.setDividerLocation(380);
        split.setResizeWeight(0.35);

        // ── Left: teams list ──────────────────────────────────────────────
        JPanel leftCard = UIHelper.cardPanel(new BorderLayout(0, 10));
        JLabel leftTitle = UIHelper.sectionLabel("REGISTERED TEAMS");
        leftTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        leftCard.add(leftTitle, BorderLayout.NORTH);

        String[] teamCols = {"Team Name", "Manager", "Players"};
        teamsModel = new DefaultTableModel(teamCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        teamsTable = new JTable(teamsModel);
        UIHelper.styleTable(teamsTable);
        teamsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showTeamPlayers();
        });
        leftCard.add(UIHelper.styledScrollPane(teamsTable), BorderLayout.CENTER);
        split.setLeftComponent(leftCard);

        // ── Right: players list ───────────────────────────────────────────
        JPanel rightCard = UIHelper.cardPanel(new BorderLayout(0, 10));
        teamInfoLabel = UIHelper.sectionLabel("SELECT A TEAM TO VIEW ROSTER");
        teamInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        rightCard.add(teamInfoLabel, BorderLayout.NORTH);

        String[] playerCols = {"#", "Player Name", "IGN", "Role"};
        playersModel = new DefaultTableModel(playerCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        playersTable = new JTable(playersModel);
        UIHelper.styleTable(playersTable);
        playersTable.getColumnModel().getColumn(0).setMaxWidth(40);
        rightCard.add(UIHelper.styledScrollPane(playersTable), BorderLayout.CENTER);
        split.setRightComponent(rightCard);

        add(split, BorderLayout.CENTER);
        refresh();
    }

    private void showTeamPlayers() {
        int row = teamsTable.getSelectedRow();
        playersModel.setRowCount(0);
        if (row < 0) return;

        String teamName = (String) teamsModel.getValueAt(row, 0);
        List<Team> teams = ctrl.getTeams();
        for (Team t : teams) {
            if (t.getName().equals(teamName)) {
                teamInfoLabel.setText("ROSTER: " + t.getName().toUpperCase());
                int num = 1;
                for (Player p : t.getPlayers()) {
                    String roleTag = "Sixth Man".equals(p.getRole()) ? "⭐ " + p.getRole() : p.getRole();
                    playersModel.addRow(new Object[]{ num++, p.getName(), p.getIgn(), roleTag });
                }
                break;
            }
        }
    }

    public void refresh() {
        teamsModel.setRowCount(0);
        for (Team t : ctrl.getTeams()) {
            // Find manager name
            String mName = "—";
            for (Manager m : ctrl.getManagers()) {
                if (t.getId().equals(m.getTeamId())) { mName = m.getFullName(); break; }
            }
            teamsModel.addRow(new Object[]{ t.getName(), mName, t.getPlayerCount() + "/6" });
        }
        playersModel.setRowCount(0);
        teamInfoLabel.setText("SELECT A TEAM TO VIEW ROSTER");
    }
}
