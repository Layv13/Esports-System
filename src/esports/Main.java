package esports;

import esports.view.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.UIManager.put("OptionPane.messageFont", 
        new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    javax.swing.UIManager.put("OptionPane.buttonFont", 
        new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    javax.swing.UIManager.put("OptionPane.messageForeground", 
        new java.awt.Color(241, 245, 249));
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // Global scroll-bar colors
            UIManager.put("ScrollBar.thumb",       new javax.swing.plaf.ColorUIResource(55, 65, 81));
            UIManager.put("ScrollBar.track",       new javax.swing.plaf.ColorUIResource(17, 24, 39));
            UIManager.put("ScrollBar.background",  new javax.swing.plaf.ColorUIResource(17, 24, 39));
            UIManager.put("SplitPane.background",  new javax.swing.plaf.ColorUIResource(9, 11, 19));
            UIManager.put("SplitPaneDivider.background",
                          new javax.swing.plaf.ColorUIResource(30, 38, 60));
            UIManager.put("OptionPane.background", new javax.swing.plaf.ColorUIResource(20, 26, 44));
            UIManager.put("Panel.background",      new javax.swing.plaf.ColorUIResource(20, 26, 44));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
