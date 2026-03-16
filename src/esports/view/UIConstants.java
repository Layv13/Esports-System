package esports.view;

import java.awt.*;

public class UIConstants {
    // ── Colours ────────────────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(9,  11, 19);
    public static final Color BG_SIDEBAR   = new Color(14, 18, 30);
    public static final Color BG_CARD      = new Color(20, 26, 44);
    public static final Color BG_CARD2     = new Color(26, 33, 55);
    public static final Color BG_HEADER    = new Color(14, 18, 30);
    public static final Color BG_INPUT     = new Color(12, 16, 28);
    public static final Color BG_ROW_ALT   = new Color(17, 22, 38);

    public static final Color ACCENT       = new Color( 99, 102, 241); // indigo-500
    public static final Color ACCENT_HOVER = new Color( 79,  70, 229); // indigo-600
    public static final Color ACCENT_LIGHT = new Color(165, 180, 252); // indigo-300
    public static final Color SUCCESS      = new Color( 16, 185, 129); // emerald-500
    public static final Color DANGER       = new Color(239,  68,  68); // red-500
    public static final Color WARNING      = new Color(245, 158,  11); // amber-500
    public static final Color GOLD         = new Color(251, 191,  36); // gold

    public static final Color TEXT_PRIMARY  = new Color(241, 245, 249);
    public static final Color TEXT_SECOND   = new Color(148, 163, 184);
    public static final Color TEXT_MUTED    = new Color( 71,  85, 105);
    public static final Color BORDER        = new Color( 30,  38,  60);

    // ── Fonts ──────────────────────────────────────────────────────────────
    public static final Font FONT_LOGO   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_XLARGE = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font FONT_LARGE  = new Font("Segoe UI", Font.BOLD,  18);
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_NAV    = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_MONO   = new Font("Consolas", Font.PLAIN, 13);

    // ── Dimensions ─────────────────────────────────────────────────────────
    public static final int SIDEBAR_W    = 215;
    public static final int HEADER_H     = 60;
    public static final int FRAME_W      = 1200;
    public static final int FRAME_H      = 720;

    // ── Helpers ────────────────────────────────────────────────────────────
    public static javax.swing.border.Border cardBorder() {
        return javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }
    public static javax.swing.border.Border padBorder(int v, int h) {
        return javax.swing.BorderFactory.createEmptyBorder(v, h, v, h);
    }
}
