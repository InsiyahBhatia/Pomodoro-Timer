import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class TimerPanel extends JPanel implements AppController.StateListener {
    private AppController controller;
    private JLabel timeLabel;
    private JLabel sessionLabel;
    private JLabel taskLabel;
    private JLabel coachLabel;
    private TamatarButton startButton;
    private int secondsLeft;
    private double bounceScale = 1.0;
    private double bouncePhase = 0;
    private Timer animTimer;

    public TimerPanel(AppController controller) {
        this.controller = controller;
        this.secondsLeft = controller.getRemainingSeconds();
        controller.addListener(this);
        setLayout(new BorderLayout());
        setBackground(DesignSystem.BACKGROUND);

        // --- Top Info Section ---
        JPanel topInfo = new JPanel();
        topInfo.setLayout(new BoxLayout(topInfo, BoxLayout.Y_AXIS));
        topInfo.setOpaque(false);
        topInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        JLabel title = new JLabel("Aha Tamatar!", SwingConstants.CENTER);
        title.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(28f));
        title.setForeground(DesignSystem.TOMATO_RED);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        coachLabel = new JLabel("<html><center>\"Aha! Ready to grow?\"</center></html>", SwingConstants.CENTER);
        coachLabel.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(12f));
        coachLabel.setForeground(DesignSystem.LEAF_GREEN);
        coachLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        coachLabel.setPreferredSize(new Dimension(400, 45));
        
        JLabel levelLabel = new JLabel("LEVEL " + controller.getLevel() + " GARDENER");
        levelLabel.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(13f));
        levelLabel.setForeground(DesignSystem.SUNNY_YELLOW);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sessionLabel = new JLabel("Session 1/4");
        sessionLabel.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(22f));
        sessionLabel.setForeground(DesignSystem.ON_SURFACE);
        sessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        taskLabel = new JLabel("Target: " + controller.getActiveTask());
        taskLabel.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(15f));
        taskLabel.setForeground(DesignSystem.LEAF_GREEN);
        taskLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topInfo.add(title);
        topInfo.add(Box.createVerticalStrut(5));
        topInfo.add(coachLabel);
        topInfo.add(Box.createVerticalStrut(10));
        topInfo.add(levelLabel);
        topInfo.add(sessionLabel);
        topInfo.add(taskLabel);
        add(topInfo, BorderLayout.NORTH);

        // --- Center Mascot Area ---
        JPanel centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                DesignSystem.enableAntialiasing(g2d);
                
                int baseSize = (int)(Math.min(getWidth(), getHeight()) * 0.78); 
                int size = (int)(baseSize * bounceScale);
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                g2d.setColor(new Color(0, 0, 0, 25));
                g2d.fillOval(x + 10, y + 10, size, size);

                Color baseColor = DesignSystem.TOMATO_RED;
                String skin = controller.getSelectedSkin();
                if (skin.equals("NINJA")) baseColor = new Color(40, 40, 40);
                if (skin.equals("GOLDEN")) baseColor = DesignSystem.SUNNY_YELLOW;

                g2d.setColor(baseColor);
                g2d.fillOval(x, y, size, size);
                
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fill(new Ellipse2D.Double(x + size/4, y + size/8, size/2, size/4));
                
                g2d.setColor(DesignSystem.SUNNY_YELLOW);
                g2d.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                double progress = 1.0 - ((double)secondsLeft / (25 * 60)); 
                g2d.drawArc(x - 15, y - 15, size + 30, size + 30, 90, -(int)(progress * 360));
                g2d.dispose();
            }
        };
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        timeLabel = new JLabel("25:00", SwingConstants.CENTER);
        timeLabel.setFont(DesignSystem.DISPLAY_TIMER.deriveFont(72f)); 
        timeLabel.setForeground(Color.WHITE);
        centerPanel.add(timeLabel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
        controls.setOpaque(false);
        
        TamatarButton resetBtn = new TamatarButton("RESET", new Color(110, 90, 90), new Color(90, 70, 70));
        resetBtn.setPreferredSize(new Dimension(110, 45));
        startButton = new TamatarButton("START", DesignSystem.TOMATO_RED, DesignSystem.TOMATO_RED_DARK);
        startButton.setPreferredSize(new Dimension(130, 55));
        TamatarButton pauseBtn = new TamatarButton("PAUSE", DesignSystem.SUNNY_YELLOW, DesignSystem.SUNNY_YELLOW.darker());
        pauseBtn.setPreferredSize(new Dimension(110, 45));

        resetBtn.addActionListener(e -> controller.resetTimer());
        startButton.addActionListener(e -> controller.startTimer());
        pauseBtn.addActionListener(e -> controller.pauseTimer());

        controls.add(resetBtn);
        controls.add(startButton);
        controls.add(pauseBtn);
        add(controls, BorderLayout.SOUTH);

        animTimer = new Timer(50, e -> {
            if (controller.isRunning()) {
                bouncePhase += 0.2;
                bounceScale = 1.0 + Math.sin(bouncePhase) * 0.02;
                repaint();
            } else { bounceScale = 1.0; repaint(); }
        });
        animTimer.start();
    }

    @Override public void onProgressUpdated(int xp, int level) { repaint(); }
    @Override public void onSkinChanged(String skin) { repaint(); }
    @Override public void onStatsUpdated() { repaint(); }
    @Override public void onTaskChanged(String activeTask, java.util.List<String> fullList) { taskLabel.setText("Target: " + activeTask); }
    @Override public void onSoundscapeChanged(String sound) {}
    @Override public void onAICoachMessage(String message) { coachLabel.setText("<html><center>\"" + message + "\"</center></html>"); }
    @Override public void onTimerTick(int secondsRemaining) {
        this.secondsLeft = secondsRemaining;
        int mins = secondsRemaining / 60;
        int secs = secondsRemaining % 60;
        timeLabel.setText(String.format("%02d:%02d", mins, secs));
    }
    @Override public void onStateChanged(boolean isBreak, int session, boolean isPaused) {
        sessionLabel.setText(isBreak ? "Break Time!" : "Session " + session + "/4");
        if (isPaused) startButton.setText("RESUME");
        else if (controller.isRunning()) startButton.setText("WORKING");
        else startButton.setText(isBreak ? "REST" : "START");
    }
    @Override public void onGardenUpdated(int sessionCount, int lifetimeCount) {}
}
