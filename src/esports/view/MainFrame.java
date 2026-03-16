package esports.view;

import esports.model.Session;
import esports.view.admin.AdminDashboard;
import esports.view.manager.ManagerDashboard;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final String CARD_LOGIN   = "LOGIN";
    private static final String CARD_ADMIN   = "ADMIN";
    private static final String CARD_MANAGER = "MANAGER";

    private final CardLayout   cardLayout   = new CardLayout();
    private final JPanel       rootPanel    = new JPanel(cardLayout);
    private final LoginPanel   loginPanel;
    private AdminDashboard     adminDash;
    private ManagerDashboard   managerDash;

    public MainFrame() {
        super("NEXUS CUP 2025 — Esports Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIConstants.FRAME_W, UIConstants.FRAME_H);
        setMinimumSize(new Dimension(1000, 620));
        setLocationRelativeTo(null);

        rootPanel.setBackground(UIConstants.BG_DARK);
        loginPanel = new LoginPanel(this);
        rootPanel.add(loginPanel, CARD_LOGIN);

        add(rootPanel);
        cardLayout.show(rootPanel, CARD_LOGIN);
    }

    public void showLogin() {
        cardLayout.show(rootPanel, CARD_LOGIN);
        loginPanel.reset();
    }

    public void showAdminDashboard() {
        if (adminDash == null) {
            adminDash = new AdminDashboard(this);
            rootPanel.add(adminDash, CARD_ADMIN);
        } else {
            adminDash.refresh();
        }
        cardLayout.show(rootPanel, CARD_ADMIN);
    }

    public void showManagerDashboard() {
        if (managerDash == null) {
            managerDash = new ManagerDashboard(this);
            rootPanel.add(managerDash, CARD_MANAGER);
        } else {
            managerDash.refresh();
        }
        cardLayout.show(rootPanel, CARD_MANAGER);
    }

    /** Re-instantiate dashboards so fresh data is shown after logout/re-login. */
    public void resetDashboards() {
        if (adminDash   != null) { rootPanel.remove(adminDash);   adminDash   = null; }
        if (managerDash != null) { rootPanel.remove(managerDash); managerDash = null; }
        rootPanel.revalidate();
        rootPanel.repaint();
    }
}
