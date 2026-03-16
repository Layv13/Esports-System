package esports.view.manager;

import esports.controller.ManagerController;
import esports.model.*;
import esports.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagerOverviewPanel extends JPanel {

    private final ManagerController ctrl = new ManagerController();
    private JPanel statsRow;
    private JPanel matchSection;

    public ManagerOverviewPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel title = UIHelper.titleLabel("Manager Dashboard");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel body = UIHelper.darkPanel(new BorderLayout(0, 20));
        statsRow     = UIHelper.darkPanel(new GridLayout(1, 3, 16, 0));
        matchSection = UIHelper.darkPanel(new BorderLayout(0, 12));
        body.add(statsRow,     BorderLayout.NORTH);
        body.add(matchSection, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        statsRow.removeAll();
        matchSection.removeAll();

        Team team = ctrl.getCurrentTeam();
        int playerCount = (team != null) ? team.getPlayerCount() : 0;

        // Stat cards
        statsRow.add(statCard("My Team",
            team != null ? team.getName() : "No Team", UIConstants.ACCENT, "🎮"));
        statsRow.add(statCard("Players",
            playerCount + " / 6", UIConstants.SUCCESS, "👥"));
        statsRow.add(statCard("Status",
            team != null && team.isRegistered() ? "Registered" : "Not Registered",
            team != null && team.isRegistered() ? UIConstants.SUCCESS : UIConstants.WARNING, "📋"));

        // Match info
        JLabel sec = UIHelper.sectionLabel("YOUR TOURNAMENT MATCHES");
        sec.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        matchSection.add(sec, BorderLayout.NORTH);

        JPanel matchCards = UIHelper.darkPanel(new GridLayout(1, 3, 16, 0));
        for (Match m : ctrl.getTournamentMatches()) {
            matchCards.add(buildMatchCard(m, team));
        }
        matchSection.add(matchCards, BorderLayout.CENTER);

        statsRow.revalidate();     statsRow.repaint();
        matchSection.revalidate(); matchSection.repaint();
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
        valLabel.setFont(UIConstants.FONT_LARGE);
        valLabel.setForeground(accent);
        p.add(valLabel, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMatchCard(Match m, Team myTeam) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(UIConstants.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(0,0,6,0);

        // Round
        JLabel round = new JLabel(m.getRoundLabel().toUpperCase());
        round.setFont(UIConstants.FONT_SMALL);
        round.setForeground(UIConstants.ACCENT_LIGHT);
        p.add(round, gc);

        // Teams
        Team t1 = ctrl.getTeamById(m.getTeam1Id());
        Team t2 = ctrl.getTeamById(m.getTeam2Id());
        String n1 = t1 != null ? t1.getName() : "TBD";
        String n2 = t2 != null ? t2.getName() : "TBD";

        gc.gridy = 1;
        JLabel vs = new JLabel("<html><b>" + n1 + "</b> vs <b>" + n2 + "</b></html>");
        vs.setFont(UIConstants.FONT_BODY);
        vs.setForeground(UIConstants.TEXT_PRIMARY);
        p.add(vs, gc);

        gc.gridy = 2; gc.insets = new Insets(8,0,0,0);
        if (m.isFinished()) {
            boolean myTeamWon = myTeam != null && myTeam.getId().equals(m.getWinnerId());
            boolean myTeamInMatch = myTeam != null &&
                (myTeam.getId().equals(m.getTeam1Id()) || myTeam.getId().equals(m.getTeam2Id()));

            Color c = myTeamInMatch ? (myTeamWon ? UIConstants.SUCCESS : UIConstants.DANGER)
                                     : UIConstants.TEXT_SECOND;
            JLabel score = new JLabel(m.getScore1() + " — " + m.getScore2());
            score.setFont(UIConstants.FONT_TITLE);
            score.setForeground(c);
            p.add(score, gc);
        } else {
            JLabel up = new JLabel("⏳ UPCOMING");
            up.setFont(UIConstants.FONT_TITLE);
            up.setForeground(UIConstants.WARNING);
            p.add(up, gc);
        }
        return p;
    }
}
