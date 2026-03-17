package esports.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {

    public interface NavListener { void onNavigate(String key); }

    private final List<JButton> navButtons = new ArrayList<>();
    private JButton activeButton;

    public SidebarPanel(String logoText, NavListener listener,
                        String[][] items,  // [label, key]
                        Runnable onLogout) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIConstants.BG_SIDEBAR);
        setPreferredSize(new Dimension(UIConstants.SIDEBAR_W, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.BORDER));

        // ── Logo ──────────────────────────────────────────────────────────
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        logoPanel.setBackground(UIConstants.BG_SIDEBAR);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel logo = new JLabel(" " + logoText);
        logo.setFont(UIConstants.FONT_LOGO);
        logo.setForeground(UIConstants.ACCENT);
        logoPanel.add(logo);
        add(logoPanel);

        add(UIHelper.darkSeparator());
        add(Box.createRigidArea(new Dimension(0, 12)));

        // ── Nav buttons ───────────────────────────────────────────────────
        for (String[] item : items) {
            String label = item[0];
            String key   = item[1];
            JButton btn  = makeNavBtn(label);
            btn.addActionListener(e -> {
                setActive(btn);
                listener.onNavigate(key);
            });
            navButtons.add(btn);
            add(btn);
            add(Box.createRigidArea(new Dimension(0, 3)));
        }

        add(Box.createVerticalGlue());
        add(UIHelper.darkSeparator());

        // ── Logout ────────────────────────────────────────────────────────
        JButton logoutBtn = makeNavBtn("  Logout");
        logoutBtn.setForeground(UIConstants.DANGER);
        logoutBtn.addActionListener(e -> onLogout.run());
        add(logoutBtn);
        add(Box.createRigidArea(new Dimension(0, 16)));

        // Activate first by default
        if (!navButtons.isEmpty()) setActive(navButtons.get(0));
    }

    private JButton makeNavBtn(String label) {
        JButton btn = new JButton(label);
        btn.setFont(UIConstants.FONT_NAV);
        btn.setForeground(UIConstants.TEXT_SECOND);
        btn.setBackground(UIConstants.BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(UIConstants.SIDEBAR_W, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) btn.setBackground(UIConstants.BG_CARD);
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) btn.setBackground(UIConstants.BG_SIDEBAR);
            }
        });
        return btn;
    }

    public void setActive(JButton btn) {
        if (activeButton != null) {
            activeButton.setBackground(UIConstants.BG_SIDEBAR);
            activeButton.setForeground(UIConstants.TEXT_SECOND);
        }
        activeButton = btn;
        btn.setBackground(new Color(99, 102, 241, 40));
        btn.setForeground(UIConstants.ACCENT_LIGHT);
    }

    public void setActiveByIndex(int i) {
        if (i >= 0 && i < navButtons.size()) setActive(navButtons.get(i));
    }
}
