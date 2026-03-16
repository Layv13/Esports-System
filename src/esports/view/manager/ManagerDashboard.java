package esports.view.manager;

import esports.model.Session;
import esports.view.*;

import javax.swing.*;
import java.awt.*;

public class ManagerDashboard extends JPanel {

    private static final String CARD_OVERVIEW = "OVERVIEW";
    private static final String CARD_TEAM     = "TEAM";
    private static final String CARD_RESULTS  = "RESULTS";

    private final MainFrame     mainFrame;
    private final CardLayout    contentLayout = new CardLayout();
    private final JPanel        contentPanel  = new JPanel(contentLayout);

    private ManagerOverviewPanel overviewPanel;
    private TeamManagementPanel  teamPanel;
    private ManagerMatchesPanel  resultsPanel;
    private JLabel               headerTitle;

    public ManagerDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);

        String[][] navItems = {
            {"🏠  Overview",       CARD_OVERVIEW},
            {"🎮  My Team",        CARD_TEAM},
            {"📋  Match Results",  CARD_RESULTS}
        };
        SidebarPanel sidebar = new SidebarPanel(
            "MANAGER", key -> navigate(key), navItems,
            () -> { Session.getInstance().logout(); mainFrame.showLogin(); }
        );
        add(sidebar, BorderLayout.WEST);

        contentPanel.setBackground(UIConstants.BG_DARK);
        overviewPanel = new ManagerOverviewPanel();
        teamPanel     = new TeamManagementPanel(this);
        resultsPanel  = new ManagerMatchesPanel();

        contentPanel.add(overviewPanel, CARD_OVERVIEW);
        contentPanel.add(teamPanel,     CARD_TEAM);
        contentPanel.add(resultsPanel,  CARD_RESULTS);
        add(contentPanel, BorderLayout.CENTER);

        navigate(CARD_OVERVIEW);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.BG_HEADER);
        header.setPreferredSize(new Dimension(0, UIConstants.HEADER_H));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER),
            BorderFactory.createEmptyBorder(0, 24, 0, 24)
        ));

        headerTitle = new JLabel("Overview");
        headerTitle.setFont(UIConstants.FONT_LARGE);
        headerTitle.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(headerTitle, BorderLayout.WEST);

        String name = Session.getInstance().getCurrentUser().getFullName();
        JLabel user = new JLabel("🧑‍💼 " + name + "  [Team Manager]");
        user.setFont(UIConstants.FONT_BODY);
        user.setForeground(UIConstants.TEXT_SECOND);
        header.add(user, BorderLayout.EAST);
        return header;
    }

    public void navigate(String card) {
        contentLayout.show(contentPanel, card);
        switch (card) {
            case CARD_OVERVIEW: headerTitle.setText("Overview");        overviewPanel.refresh(); break;
            case CARD_TEAM:     headerTitle.setText("My Team");         teamPanel.refresh();     break;
            case CARD_RESULTS:  headerTitle.setText("Match Results");   resultsPanel.refresh();  break;
        }
    }

    public void refresh() {
        overviewPanel.refresh();
        teamPanel.refresh();
        resultsPanel.refresh();
    }
}
