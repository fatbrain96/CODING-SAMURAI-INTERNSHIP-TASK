package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class QuizApplication {
    public static void main(String[] args) {
      try {
    // Set system look and feel
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
} catch (Exception e) {
    e.printStackTrace();
}

        SwingUtilities.invokeLater(() -> {
            new QuizGUI().setVisible(true);
        });
    }
}
