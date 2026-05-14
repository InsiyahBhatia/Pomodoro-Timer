import javax.swing.*;
import java.awt.*;

public class PomodoroApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private AppController controller;

    public PomodoroApp() {
        setTitle("Aha Tamatar!");
        setSize(500, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(DesignSystem.BACKGROUND);

        controller = new AppController();
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        // Screens
        mainContainer.add(new TimerPanel(controller), "TIMER");
        mainContainer.add(new GardenPanel(controller), "GARDEN");
        mainContainer.add(new StatsPanel(controller), "STATS");
        mainContainer.add(new TaskPanel(controller), "TASKS");
        mainContainer.add(new ChatPanel(controller), "CHAT"); // New Chat Panel
        mainContainer.add(new SettingsPanel(controller), "SETTINGS");

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);
        add(new NavigationPanel(target -> cardLayout.show(mainContainer, target)), BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PomodoroApp().setVisible(true);
        });
    }
}
