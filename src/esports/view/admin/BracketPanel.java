package esports.view.admin;

import esports.controller.AdminController;
import esports.model.*;
import esports.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BracketPanel extends JPanel {

    private final AdminController ctrl = new AdminController();

    public BracketPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        buildUI();
    }

    private void buildUI() {
        JLabel title = UIHelper.titleLabel("Tournament Bracket — NEXUS CUP 2025");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        BracketCanvas canvas = new BracketCanvas();
        canvas.setBackground(UIConstants.BG_DARK);
        add(canvas, BorderLayout.CENTER);
    }

    public void refresh() {
        repaint();
    }

    // ── Inner canvas ──────────────────────────────────────────────────────
    class BracketCanvas extends JPanel {

        BracketCanvas() {
            setBackground(UIConstants.BG_DARK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Tournament t   = ctrl.getTournament();
            Match sf1      = t.getSemifinal1();
            Match sf2      = t.getSemifinal2();
            Match fin      = t.getFinal();

            int w = getWidth();
            int h = getHeight();

            // Layout constants
            int boxW  = 220;
            int boxH  = 52;
            int lx    = 60;         
            int rx    = w - 60 - boxW; 
            int mx    = lx + boxW + (rx - lx - boxW) / 2 - 1; 

            int sf1Y1 = h / 2 - 180; 
            int sf1Y2 = sf1Y1 + 100; 
            int sf2Y1 = h / 2 + 50;  
            int sf2Y2 = sf2Y1 + 100; 
            int finY  = (sf1Y1 + sf1Y2 + boxH + sf2Y1 + sf2Y2 + boxH) / 4 - boxH / 2;

            // -- Draw section labels --
            g2.setFont(UIConstants.FONT_SMALL);
            g2.setColor(UIConstants.TEXT_MUTED);
            g2.drawString("SEMIFINALS", lx, sf1Y1 - 16);
            g2.drawString("GRAND FINAL", rx, finY - 16);

            // Draw SF1
            if (sf1 != null) drawMatch(g2, sf1, lx, sf1Y1, sf1Y2, boxW, boxH, true);
            // Draw SF2
            if (sf2 != null) drawMatch(g2, sf2, lx, sf2Y1, sf2Y2, boxW, boxH, true);
            // Draw Final
            if (fin != null) drawFinal(g2, fin, rx, finY, boxW, boxH);

            // -- Connector lines --
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(UIConstants.BORDER);

            if (sf1 != null) {
                int sf1MidY = sf1Y1 + boxH / 2 + 50; 
                
                g2.drawLine(lx + boxW, sf1MidY, mx, sf1MidY);
              
                g2.drawLine(mx, sf1MidY, mx, finY + boxH / 2);
                
                g2.drawLine(mx, finY + boxH / 2, rx, finY + boxH / 2);
            }
            if (sf2 != null) {
                int sf2MidY = sf2Y1 + boxH / 2 + 50;
                g2.drawLine(lx + boxW, sf2MidY, mx, sf2MidY);
                g2.drawLine(mx, sf2MidY, mx, finY + boxH / 2);
            }

            g2.dispose();
        }

        private void drawMatch(Graphics2D g2, Match m,
                               int x, int y1, int y2,
                               int bw, int bh, boolean showScore) {
            Team t1 = ctrl.getTeamById(m.getTeam1Id());
            Team t2 = ctrl.getTeamById(m.getTeam2Id());
            String n1 = t1 != null ? t1.getName() : "TBD";
            String n2 = t2 != null ? t2.getName() : "TBD";

            boolean t1wins = m.isFinished() && m.getTeam1Id().equals(m.getWinnerId());
            boolean t2wins = m.isFinished() && m.getTeam2Id().equals(m.getWinnerId());

            // Team 1 box
            drawTeamBox(g2, x, y1, bw, bh, n1, m.getScore1(), t1wins, m.isFinished());
            // Team 2 box
            drawTeamBox(g2, x, y2, bw, bh, n2, m.getScore2(), t2wins, m.isFinished());

            // Score badge between boxes
            if (showScore && m.isFinished()) {
                int midY = (y1 + bh + y2) / 2 - 10;
                g2.setFont(UIConstants.FONT_SMALL);
                g2.setColor(UIConstants.TEXT_MUTED);
                String scoreStr = m.getScore1() + " — " + m.getScore2();
                FontMetrics fm = g2.getFontMetrics();
                int sw = fm.stringWidth(scoreStr);
                g2.drawString(scoreStr, x + bw / 2 - sw / 2, midY);
            }
        }

        private void drawTeamBox(Graphics2D g2, int x, int y, int bw, int bh,
                                  String name, int score, boolean winner, boolean finished) {
            Color bgColor   = winner ? new Color(16, 185, 129, 30)
                            : finished ? new Color(239, 68, 68, 15)
                            : UIConstants.BG_CARD;
            Color borderCol = winner ? UIConstants.SUCCESS
                            : finished ? new Color(239, 68, 68, 80)
                            : UIConstants.BORDER;

            // Background
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(x, y, bw, bh, 10, 10));

            // Border
            g2.setColor(borderCol);
            g2.setStroke(new BasicStroke(winner ? 2f : 1f));
            g2.draw(new RoundRectangle2D.Float(x, y, bw, bh, 10, 10));

            // Team name
            g2.setFont(UIConstants.FONT_NAV);
            g2.setColor(winner ? UIConstants.SUCCESS : UIConstants.TEXT_PRIMARY);
            g2.drawString(name, x + 14, y + bh / 2 + 5);

            // Score
            if (finished) {
                String sc = String.valueOf(score);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(winner ? UIConstants.SUCCESS : UIConstants.DANGER);
                g2.drawString(sc, x + bw - fm.stringWidth(sc) - 14, y + bh / 2 + 6);
            }

            // Winner crown
            if (winner) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                g2.drawString("", x + bw - 34, y + bh / 2 + 6);
            }
        }

        private void drawFinal(Graphics2D g2, Match m, int x, int y, int bw, int bh) {
             Tournament t   = ctrl.getTournament();
    Match sf1      = t.getSemifinal1();
    Match sf2      = t.getSemifinal2();

    String n1, n2;

    // Only show real finalist names if semis are FINISHED
    if (sf1 != null && sf1.isFinished() && sf1.getWinnerId() != null) {
        Team winner1 = ctrl.getTeamById(sf1.getWinnerId());
        n1 = winner1 != null ? winner1.getName() : "TBD";
    } else {
        n1 = "TBD — SF1 Winner";
    }

    if (sf2 != null && sf2.isFinished() && sf2.getWinnerId() != null) {
        Team winner2 = ctrl.getTeamById(sf2.getWinnerId());
        n2 = winner2 != null ? winner2.getName() : "TBD";
    } else {
        n2 = "TBD — SF2 Winner";
    }

    // Outer glow frame
    g2.setColor(new Color(99, 102, 241, 25));
    g2.fill(new RoundRectangle2D.Float(x - 6, y - 6, bw + 12, bh * 2 + 56, 14, 14));
    g2.setColor(UIConstants.ACCENT);
    g2.setStroke(new BasicStroke(2f));
    g2.draw(new RoundRectangle2D.Float(x - 6, y - 6, bw + 12, bh * 2 + 56, 14, 14));

    // Grand Final accent bar
    g2.setColor(UIConstants.ACCENT);
    g2.fillRoundRect(x, y - 30, bw, 24, 6, 6);
    g2.setFont(UIConstants.FONT_NAV);
    g2.setColor(Color.WHITE);
    FontMetrics fm = g2.getFontMetrics();
    String gf = "★  GRAND FINAL  ★";
    g2.drawString(gf, x + (bw - fm.stringWidth(gf)) / 2, y - 30 + 17);

    if (m.isFinished()) {
        // Show actual scores with winner
        drawTeamBox(g2, x, y, bw, bh, n1, m.getScore1(),
            m.getTeam1Id().equals(m.getWinnerId()), true);
        drawTeamBox(g2, x, y + bh + 20, bw, bh, n2, m.getScore2(),
            m.getTeam2Id().equals(m.getWinnerId()), true);

        // Champion banner
        Team champ = ctrl.getTeamById(m.getWinnerId());
        if (champ != null) {
            g2.setColor(UIConstants.GOLD);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String champStr = "🏆 CHAMPION: " + champ.getName();
            fm = g2.getFontMetrics();
            g2.drawString(champStr,
                x + (bw - fm.stringWidth(champStr)) / 2,
                y + bh * 2 + 40);
        }

    } else {
        // Show upcoming — with real names OR TBD
        drawUpcomingTeam(g2, n1, x, y,          bw, bh);
        drawUpcomingTeam(g2, n2, x, y + bh + 20, bw, bh);

        // Show different message depending on state
        g2.setFont(UIConstants.FONT_NAV);
        fm = g2.getFontMetrics();

        if ("TBD — SF1 Winner".equals(n1) || "TBD — SF2 Winner".equals(n2)) {
            // Semis not done yet
            g2.setColor(UIConstants.TEXT_MUTED);
            String waiting = "Awaiting semifinal results...";
            g2.drawString(waiting,
                x + (bw - fm.stringWidth(waiting)) / 2,
                y + bh * 2 + 42);
        } else {
            // Both semis done, final is ready
            g2.setColor(UIConstants.WARNING);
            String up = "⏳ UPCOMING MATCH";
            g2.drawString(up,
                x + (bw - fm.stringWidth(up)) / 2,
                y + bh * 2 + 42);
        }
        }
        }
        private void drawUpcomingTeam(Graphics2D g2, String name, int x, int y, int bw, int bh) {
            g2.setColor(UIConstants.BG_CARD2);
            g2.fill(new RoundRectangle2D.Float(x, y, bw, bh, 10, 10));
            g2.setColor(UIConstants.BORDER);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x, y, bw, bh, 10, 10));

            g2.setFont(UIConstants.FONT_NAV);
            g2.setColor(UIConstants.TEXT_PRIMARY);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(name, x + (bw - fm.stringWidth(name)) / 2, y + bh / 2 + 5);
        }
    }
}
