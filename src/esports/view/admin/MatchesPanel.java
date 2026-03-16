package esports.view.admin;

import esports.controller.AdminController;
import esports.model.*;
import esports.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class MatchesPanel extends JPanel {

    private final AdminController ctrl = new AdminController();
    private DefaultTableModel finishedModel;
    private DefaultTableModel upcomingModel;

    public MatchesPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel title = UIHelper.titleLabel("Match Results & Schedule");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel body = UIHelper.darkPanel(new GridLayout(2, 1, 0, 20));

        // ── Finished matches ──────────────────────────────────────────────
        JPanel finCard = UIHelper.cardPanel(new BorderLayout(0, 12));
        JLabel finTitle = UIHelper.sectionLabel("✅  COMPLETED MATCHES");
        finTitle.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        finCard.add(finTitle, BorderLayout.NORTH);

        String[] finCols = {"Round", "Team 1", "Score", "Team 2", "Winner"};
        finishedModel = new DefaultTableModel(finCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable finTable = new JTable(finishedModel);
        UIHelper.styleTable(finTable);
        styleScoreColumn(finTable, 2);
        finCard.add(UIHelper.styledScrollPane(finTable), BorderLayout.CENTER);
        body.add(finCard);

        // ── Upcoming matches ──────────────────────────────────────────────
        JPanel upCard = UIHelper.cardPanel(new BorderLayout(0, 12));
        JLabel upTitle = UIHelper.sectionLabel("⏳  UPCOMING MATCHES");
        upTitle.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        upCard.add(upTitle, BorderLayout.NORTH);

        String[] upCols = {"Round", "Team 1", "vs", "Team 2", "Status"};
        upcomingModel = new DefaultTableModel(upCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable upTable = new JTable(upcomingModel);
        UIHelper.styleTable(upTable);
        styleStatusColumn(upTable, 4);
        upCard.add(UIHelper.styledScrollPane(upTable), BorderLayout.CENTER);
        body.add(upCard);

        add(body, BorderLayout.CENTER);
        refresh();
    }

    private void styleScoreColumn(JTable table, int col) {
        table.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground(UIConstants.SUCCESS);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setBackground(sel ? new Color(99,102,241,60)
                              : row % 2 == 0 ? UIConstants.BG_CARD : UIConstants.BG_ROW_ALT);
                return this;
            }
        });
    }

    private void styleStatusColumn(JTable table, int col) {
        table.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground(UIConstants.WARNING);
                setFont(UIConstants.FONT_NAV);
                setBackground(sel ? new Color(99,102,241,60)
                              : row % 2 == 0 ? UIConstants.BG_CARD : UIConstants.BG_ROW_ALT);
                return this;
            }
        });
    }

    public void refresh() {
        Tournament t = ctrl.getTournament();
        finishedModel.setRowCount(0);
        upcomingModel.setRowCount(0);

        for (Match m : t.getMatches()) {
            Team t1 = ctrl.getTeamById(m.getTeam1Id());
            Team t2 = ctrl.getTeamById(m.getTeam2Id());
            String n1 = t1 != null ? t1.getName() : "TBD";
            String n2 = t2 != null ? t2.getName() : "TBD";

            if (m.isFinished()) {
                Team winner = ctrl.getTeamById(m.getWinnerId());
                String wName = winner != null ? "🏆 " + winner.getName() : "?";
                finishedModel.addRow(new Object[]{
                    m.getRoundLabel(), n1,
                    m.getScore1() + " — " + m.getScore2(),
                    n2, wName
                });
            } else {
                upcomingModel.addRow(new Object[]{
                    m.getRoundLabel(), n1, "VS", n2, "⏳ UPCOMING"
                });
            }
        }
    }
}
