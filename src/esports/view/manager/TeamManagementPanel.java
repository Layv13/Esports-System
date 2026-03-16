package esports.view.manager;

import esports.controller.ManagerController;
import esports.model.*;
import esports.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeamManagementPanel extends JPanel {

    private static final String[] ROLES = {
        "Top", "Jungle", "Mid", "Bot Carry", "Support", "Sixth Man"
    };

    private final ManagerController ctrl;
    private final ManagerDashboard  parent;

    private JPanel         teamInfoPanel;
    private JTable         playersTable;
    private DefaultTableModel playersModel;
    private JLabel         lblStatus;
    private JLabel         teamNameLabel;
    private JLabel         teamStatusLabel;

    public TeamManagementPanel(ManagerDashboard parent) {
        this.parent = parent;
        this.ctrl   = new ManagerController();
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel title = UIHelper.titleLabel("My Team");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel body = UIHelper.darkPanel(new GridLayout(1, 2, 20, 0));

        // ── Left: team info + actions ─────────────────────────────────────
        JPanel leftCard = UIHelper.cardPanel(new BorderLayout(0, 16));

        teamNameLabel   = new JLabel("—");
        teamNameLabel.setFont(UIConstants.FONT_LARGE);
        teamNameLabel.setForeground(UIConstants.ACCENT_LIGHT);

        teamStatusLabel = new JLabel("No team created");
        teamStatusLabel.setFont(UIConstants.FONT_BODY);
        teamStatusLabel.setForeground(UIConstants.TEXT_SECOND);

        JPanel infoTop = UIHelper.darkPanel(new GridLayout(2, 1, 0, 4));
        infoTop.setBackground(UIConstants.BG_CARD);
        infoTop.add(teamNameLabel);
        infoTop.add(teamStatusLabel);
        leftCard.add(infoTop, BorderLayout.NORTH);

        // Create team form
        JPanel createForm = UIHelper.darkPanel(new GridBagLayout());
        createForm.setBackground(UIConstants.BG_CARD);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL; c.gridx = 0;

        JLabel lCreate = UIHelper.sectionLabel("CREATE TEAM");
        lCreate.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        c.gridy = 0; createForm.add(lCreate, c);

        JTextField tfTeamName = UIHelper.styledField(18);
        tfTeamName.setPreferredSize(new Dimension(0, 38));
        c.gridy = 1; c.insets = new Insets(0,0,8,0);
        createForm.add(tfTeamName, c);

        JButton btnCreate = UIHelper.successButton("Create Team");
        c.gridy = 2; c.insets = new Insets(0,0,0,0);
        createForm.add(btnCreate, c);
        leftCard.add(createForm, BorderLayout.CENTER);

        // Add player form
        JPanel addForm = UIHelper.darkPanel(new GridBagLayout());
        addForm.setBackground(UIConstants.BG_CARD);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL; c2.gridx = 0;

        JLabel lAdd = UIHelper.sectionLabel("ADD PLAYER");
        lAdd.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        c2.gridy = 0; addForm.add(lAdd, c2);

        JTextField tfName = UIHelper.styledField(18);
        tfName.setPreferredSize(new Dimension(0, 36));
        JTextField tfIgn  = UIHelper.styledField(18);
        tfIgn.setPreferredSize(new Dimension(0, 36));
        JComboBox<String> cbRole = UIHelper.styledCombo(ROLES);

        c2.gridy = 1; c2.insets = new Insets(2,0,2,0); addForm.add(placeholder("Player Name"), c2);
        c2.gridy = 2; addForm.add(tfName, c2);
        c2.gridy = 3; addForm.add(placeholder("In-Game Name (IGN)"), c2);
        c2.gridy = 4; addForm.add(tfIgn, c2);
        c2.gridy = 5; addForm.add(placeholder("Role"), c2);
        c2.gridy = 6; addForm.add(cbRole, c2);

        JButton btnAdd = UIHelper.primaryButton("Add Player");
        c2.gridy = 7; c2.insets = new Insets(10,0,0,0); addForm.add(btnAdd, c2);
        leftCard.add(addForm, BorderLayout.SOUTH);

        body.add(leftCard);

        // ── Right: player roster + actions ───────────────────────────────
        JPanel rightCard = UIHelper.cardPanel(new BorderLayout(0, 12));
        JLabel rTitle = UIHelper.sectionLabel("PLAYER ROSTER (5 MAIN + 1 SIXTH MAN)");
        rTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        rightCard.add(rTitle, BorderLayout.NORTH);

        String[] cols = {"#", "Player Name", "IGN", "Role"};
        playersModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        playersTable = new JTable(playersModel);
        UIHelper.styleTable(playersTable);
        playersTable.getColumnModel().getColumn(0).setMaxWidth(40);
        rightCard.add(UIHelper.styledScrollPane(playersTable), BorderLayout.CENTER);

        // Bottom action buttons
        JPanel btnRow = UIHelper.darkPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        btnRow.setBackground(UIConstants.BG_CARD);
        JButton btnRemove   = UIHelper.dangerButton("Remove Player");
        JButton btnPullOut  = UIHelper.dangerButton("Pull Out Team");
        btnPullOut.setBackground(new Color(127, 29, 29));
        btnRow.add(btnRemove);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(btnPullOut);
        rightCard.add(btnRow, BorderLayout.SOUTH);

        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UIConstants.FONT_SMALL);
        rightCard.add(lblStatus, BorderLayout.NORTH);

        body.add(rightCard);
        add(body, BorderLayout.CENTER);

        // ── Action listeners ──────────────────────────────────────────────
        btnCreate.addActionListener(e -> {
            String err = ctrl.createTeam(tfTeamName.getText());
            if (err == null) {
                setStatus("Team created!", UIConstants.SUCCESS);
                tfTeamName.setText("");
                refresh();
            } else {
                setStatus(err, UIConstants.DANGER);
            }
        });

        btnAdd.addActionListener(e -> {
            String err = ctrl.addPlayer(
                tfName.getText(), tfIgn.getText(),
                (String) cbRole.getSelectedItem()
            );
            if (err == null) {
                setStatus("Player added.", UIConstants.SUCCESS);
                tfName.setText(""); tfIgn.setText("");
                refresh();
            } else {
                setStatus(err, UIConstants.DANGER);
            }
        });

        btnRemove.addActionListener(e -> {
            int row = playersTable.getSelectedRow();
            if (row < 0) { setStatus("Select a player to remove.", UIConstants.WARNING); return; }
            Team team = ctrl.getCurrentTeam();
            if (team == null) return;
            Player p = team.getPlayers().get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Remove player " + p.getName() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String err = ctrl.removePlayer(p.getId());
                if (err == null) { setStatus("Player removed.", UIConstants.SUCCESS); refresh(); }
                else             { setStatus(err, UIConstants.DANGER); }
            }
        });

        btnPullOut.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to PULL OUT your team from the tournament?\nThis action cannot be undone.",
                "Confirm Pull Out", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                String err = ctrl.pullOutTeam();
                if (err == null) {
                    setStatus("Team pulled out successfully.", UIConstants.WARNING);
                    refresh();
                } else {
                    setStatus(err, UIConstants.DANGER);
                }
            }
        });
    }

    private JLabel placeholder(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_SMALL);
        l.setForeground(UIConstants.TEXT_MUTED);
        return l;
    }

    private void setStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }

    public void refresh() {
        playersModel.setRowCount(0);
        Team team = ctrl.getCurrentTeam();
        if (team != null) {
            teamNameLabel.setText("🎮 " + team.getName());
            teamStatusLabel.setText(team.isRegistered() ? "✅ Registered for tournament" : "⚠ Not registered");
            teamStatusLabel.setForeground(team.isRegistered() ? UIConstants.SUCCESS : UIConstants.WARNING);
            int num = 1;
            for (Player p : team.getPlayers()) {
                String roleTag = "Sixth Man".equals(p.getRole()) ? "⭐ " + p.getRole() : p.getRole();
                playersModel.addRow(new Object[]{ num++, p.getName(), p.getIgn(), roleTag });
            }
        } else {
            teamNameLabel.setText("No Team");
            teamStatusLabel.setText("Create a team below to get started");
            teamStatusLabel.setForeground(UIConstants.TEXT_SECOND);
        }
    }
}
