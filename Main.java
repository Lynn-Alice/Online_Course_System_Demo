import ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            UIManager.put("Button.background", java.awt.Color.WHITE);
            UIManager.put("Button.foreground", new java.awt.Color(0, 102, 204));
            UIManager.put("Button.font", new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}