package ui;

import javax.swing.*;
import java.awt.*;

public class ButtonStyleUtil {

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 102, 204));
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }


    public static void styleSuccessButton(JButton button) {
        button.setBackground(new Color(40, 167, 69));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static void styleDangerButton(JButton button) {
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static void styleInfoButton(JButton button) {
        button.setBackground(new Color(23, 162, 184));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static void styleMediumButton(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 35));
    }

    public static void addHoverEffect(JButton button) {
        Color originalBackground = button.getBackground();
        Color originalForeground = button.getForeground();

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBackground.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBackground);
            }
        });
    }
}