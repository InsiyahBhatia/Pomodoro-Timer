import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class GardenPanel extends JPanel implements AppController.StateListener {
    private JPanel gridPanel;
    private JLabel countLabel;
    private JLabel lifetimeLabel;
    private Random random = new Random();
    private List<Point2D.Double> raindrops = new ArrayList<>();
    private Timer rainTimer;

    public GardenPanel(AppController controller) {
        controller.addListener(this);
        setLayout(new BorderLayout());
        setBackground(DesignSystem.BACKGROUND);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Aha! Farm Garden");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(32f));
        title.setForeground(DesignSystem.TOMATO_RED);
        
        countLabel = new JLabel("Current Session Harvest: 0");
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabel.setFont(DesignSystem.QUICKSAND_MEDIUM);
        countLabel.setForeground(DesignSystem.ON_SURFACE);
        
        lifetimeLabel = new JLabel("Lifetime Total Harvest: " + controller.getTotalLifetimeTomatoes());
        lifetimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lifetimeLabel.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(12f));
        lifetimeLabel.setForeground(DesignSystem.LEAF_GREEN);

        header.add(title);
        header.add(countLabel);
        header.add(lifetimeLabel);
        add(header, BorderLayout.NORTH);

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                DesignSystem.enableAntialiasing(g2d);
                g2d.setColor(DesignSystem.SOIL_BROWN);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Rain
                g2d.setColor(new Color(173, 216, 230, 150));
                for (Point2D.Double drop : raindrops) {
                    g2d.drawLine((int)drop.x, (int)drop.y, (int)drop.x, (int)drop.y + 10);
                }
                g2d.dispose();
            }
        };
        gridPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 40));
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        rainTimer = new Timer(50, e -> {
            for (Point2D.Double drop : raindrops) {
                drop.y += 15;
                if (drop.y > getHeight()) {
                    drop.y = -10;
                    drop.x = random.nextInt(Math.max(1, getWidth()));
                }
            }
            if (raindrops.size() < 50) raindrops.add(new Point2D.Double(random.nextInt(Math.max(1, getWidth())), random.nextInt(getHeight())));
            repaint();
        });
        rainTimer.start();
        updateGarden(controller.getTomatoesGrown(), controller.getTotalLifetimeTomatoes());
    }

    private void updateGarden(int sessionCount, int lifetimeCount) {
        gridPanel.removeAll();
        for (int i = 0; i < sessionCount; i++) {
            JLabel tomato = new JLabel(new ImageIcon() {
                @Override
                public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    DesignSystem.enableAntialiasing(g2d);
                    g2d.setColor(DesignSystem.TOMATO_RED);
                    g2d.fillOval(x, y, 60, 60);
                    g2d.setColor(new Color(255, 255, 255, 100));
                    g2d.fillOval(x + 15, y + 10, 20, 12);
                    g2d.setColor(DesignSystem.LEAF_GREEN);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawLine(x + 30, y + 5, x + 30, y - 5);
                    g2d.dispose();
                }
                @Override public int getIconWidth() { return 60; }
                @Override public int getIconHeight() { return 65; }
            });
            gridPanel.add(tomato);
        }
        countLabel.setText("Current Session Harvest: " + sessionCount);
        lifetimeLabel.setText("Lifetime Total Harvest: " + lifetimeCount);
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    @Override public void onTimerTick(int secondsRemaining) {}
    @Override public void onStateChanged(boolean isBreak, int session, boolean isPaused) {}
    @Override public void onProgressUpdated(int xp, int level) {}
    @Override public void onSkinChanged(String skin) {}
    @Override public void onStatsUpdated() {}
    @Override public void onTaskChanged(String task, List<String> fullList) {}
    @Override public void onSoundscapeChanged(String sound) {}
    @Override public void onAICoachMessage(String message) {}
    @Override public void onGardenUpdated(int sessionCount, int lifetimeCount) { updateGarden(sessionCount, lifetimeCount); }
}
