package esports.view.admin;

import esports.controller.AdminController;
import esports.model.*;
import esports.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminOverviewPanel extends JPanel {

    private final AdminController ctrl = new AdminController();
    private JPanel statsRow;
    private JPanel recentPanel;

    public AdminOverviewPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel title = UIHelper.titleLabel("Tournament Dashboard");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel center = UIHelper.darkPanel(new BorderLayout(0, 20));

        // Stat cards
        statsRow = UIHelper.darkPanel(new GridLayout(1, 4, 16, 0));
        center.add(statsRow, BorderLayout.NORTH);

        // Recent activity
        recentPanel = UIHelper.darkPanel(new BorderLayout(0, 12));
        center.add(recentPanel, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        statsRow.removeAll();
        statsRow.add(statCard("Teams Registered",
            String.valueOf(ctrl.getTeams().size()), UIConstants.ACCENT, "👥"));
        statsRow.add(statCard("Managers",
            String.valueOf(ctrl.getManagers().size()), UIConstants.SUCCESS, "🧑‍💼"));
        statsRow.add(statCard("Matches Finished",
            String.valueOf(ctrl.getTournament().getFinishedMatches().size()), UIConstants.WARNING, "✅"));
        statsRow.add(statCard("Upcoming Matches",
            String.valueOf(ctrl.getTournament().getUpcomingMatches().size()), UIConstants.DANGER, "⏳"));

        recentPanel.removeAll();
        JLabel sec = UIHelper.sectionLabel("TOURNAMENT STATUS");
        sec.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        recentPanel.add(sec, BorderLayout.NORTH);

        JPanel matchCards = UIHelper.darkPanel(new GridLayout(1, 3, 16, 0));
        Tournament t = ctrl.getTournament();
        for (Match m : t.getMatches()) {
            matchCards.add(matchCard(m));
        }
        recentPanel.add(matchCards, BorderLayout.CENTER);

        statsRow.revalidate(); statsRow.repaint();
        recentPanel.revalidate(); recentPanel.repaint();
    }

    private JPanel statCard(String label, String value, Color accent, String icon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIConstants.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon + "  " + label);
        iconLabel.setFont(UIConstants.FONT_SMALL);
        iconLabel.setForeground(UIConstants.TEXT_SECOND);
        p.add(iconLabel, BorderLayout.NORTH);

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valLabel.setForeground(accent);
        p.add(valLabel, BorderLayout.CENTER);

        return p;
    }

    private JPanel matchCard(Match m) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(UIConstants.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill  = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(0,0,6,0);

        // Round label
        JLabel round = new JLabel(m.getRoundLabel().toUpperCase());
        round.setFont(UIConstants.FONT_SMALL);
        round.setForeground(UIConstants.ACCENT_LIGHT);
        p.add(round, gc);

        gc.gridy = 1;
        Team t1 = ctrl.getTeamById(m.getTeam1Id());
        Team t2 = ctrl.getTeamById(m.getTeam2Id());
        String t1name = t1 != null ? t1.getName() : "TBD";
        String t2name = t2 != null ? t2.getName() : "TBD";

        JLabel vs = new JLabel("<html><b>" + t1name + "</b> vs <b>" + t2name + "</b></html>");
        vs.setFont(UIConstants.FONT_BODY);
        vs.setForeground(UIConstants.TEXT_PRIMARY);
        p.add(vs, gc);

        gc.gridy = 2; gc.insets = new Insets(8,0,0,0);
        Color statusColor = m.isFinished() ? UIConstants.SUCCESS : UIConstants.WARNING;
        String statusText = m.isFinished()
            ? "Final Score: " + m.getScore1() + " - " + m.getScore2()
            : "⏳ UPCOMING";
        JLabel score = new JLabel(statusText);
        score.setFont(UIConstants.FONT_TITLE);
        score.setForeground(statusColor);
        p.add(score, gc);

        return p;
    }
}
