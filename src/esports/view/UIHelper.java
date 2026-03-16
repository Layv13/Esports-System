package esports.view;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

public class UIHelper {

    // ── Buttons ────────────────────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(UIConstants.ACCENT);
        btn.setForeground(UIConstants.TEXT_PRIMARY);
        btn.setFont(UIConstants.FONT_NAV);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 38));
        addHover(btn, UIConstants.ACCENT, UIConstants.ACCENT_HOVER);
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(UIConstants.DANGER);
        addHover(btn, UIConstants.DANGER, new Color(220, 38, 38));
        return btn;
    }

    public static JButton successButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(UIConstants.SUCCESS);
        addHover(btn, UIConstants.SUCCESS, new Color(5, 150, 105));
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(UIConstants.BG_CARD2);
        btn.setForeground(UIConstants.TEXT_SECOND);
        btn.setFont(UIConstants.FONT_BODY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHover(btn, UIConstants.BG_CARD2, UIConstants.BG_SIDEBAR);
        return btn;
    }

    private static void addHover(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(normal); }
        });
    }

    // ── Text fields ────────────────────────────────────────────────────────
    public static JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        styleInput(f);
        return f;
    }

    public static JPasswordField styledPasswordField(int cols) {
        JPasswordField f = new JPasswordField(cols);
        styleInput(f);
        return f;
    }

    private static void styleInput(JTextComponent f) {
        f.setBackground(UIConstants.BG_INPUT);
        f.setForeground(UIConstants.TEXT_PRIMARY);
        f.setCaretColor(UIConstants.TEXT_PRIMARY);
        f.setFont(UIConstants.FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    // ── Labels ─────────────────────────────────────────────────────────────
    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_XLARGE);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        return l;
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_TITLE);
        l.setForeground(UIConstants.TEXT_SECOND);
        return l;
    }

    public static JLabel badge(String text, Color bg) {
        JLabel l = new JLabel(" " + text + " ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(UIConstants.FONT_SMALL);
        l.setForeground(Color.WHITE);
        l.setOpaque(false);
        return l;
    }

    // ── Tables ─────────────────────────────────────────────────────────────
    public static void styleTable(JTable table) {
        table.setBackground(UIConstants.BG_CARD);
        table.setForeground(UIConstants.TEXT_PRIMARY);
        table.setFont(UIConstants.FONT_BODY);
        table.setRowHeight(38);
        table.setGridColor(UIConstants.BORDER);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(99, 102, 241, 60));
        table.setSelectionForeground(UIConstants.TEXT_PRIMARY);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(UIConstants.BG_SIDEBAR);
        header.setForeground(UIConstants.TEXT_SECOND);
        header.setFont(UIConstants.FONT_NAV);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER));
        header.setReorderingAllowed(false);

        // Alternating row renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBackground(isSelected ? new Color(99, 102, 241, 60)
                              : row % 2 == 0 ? UIConstants.BG_CARD : UIConstants.BG_ROW_ALT);
                setForeground(UIConstants.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setFont(UIConstants.FONT_BODY);
                return this;
            }
        });
    }

    public static JScrollPane styledScrollPane(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(UIConstants.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        sp.getViewport().setBackground(UIConstants.BG_CARD);
        sp.getVerticalScrollBar().setBackground(UIConstants.BG_CARD);
        return sp;
    }

    // ── Panels ─────────────────────────────────────────────────────────────
    public static JPanel cardPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(UIConstants.BG_CARD);
        p.setBorder(UIConstants.cardBorder());
        return p;
    }

    public static JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(UIConstants.BG_DARK);
        return p;
    }

    // ── Combobox ───────────────────────────────────────────────────────────
    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(UIConstants.BG_INPUT);
        cb.setForeground(UIConstants.TEXT_PRIMARY);
        cb.setFont(UIConstants.FONT_BODY);
        return cb;
    }

    // ── Separator ──────────────────────────────────────────────────────────
    public static JSeparator darkSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.BORDER);
        sep.setBackground(UIConstants.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
}
