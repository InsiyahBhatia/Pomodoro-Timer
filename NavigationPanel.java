import javax.swing.*;
import java.awt.*;

public class NavigationPanel extends JPanel {
    public interface NavListener {
        void onNav(String target);
    }

    public NavigationPanel(NavListener listener) {
        // Updated to 1 row, 6 columns for the new Chat tab
        setLayout(new GridLayout(1, 6));
        setBackground(DesignSystem.SURFACE_CONTAINER);
        setPreferredSize(new Dimension(500, 70));

        add(createNavButton("Timer", "TIMER", listener));
        add(createNavButton("Garden", "GARDEN", listener));
        add(createNavButton("Stats", "STATS", listener));
        add(createNavButton("Tasks", "TASKS", listener));
        add(createNavButton("Chat", "CHAT", listener)); // New Chat Tab
        add(createNavButton("Settings", "SETTINGS", listener));
    }

    private JButton createNavButton(String label, String target, NavListener listener) {
        JButton btn = new JButton(label);
        btn.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(10f)); // Reduced font size to fit 6 items
        btn.setForeground(DesignSystem.ON_SURFACE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> listener.onNav(target));
        return btn;
    }
}
