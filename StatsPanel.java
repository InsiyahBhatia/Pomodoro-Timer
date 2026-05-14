import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatsPanel extends JPanel implements AppController.StateListener {
    private AppController controller;
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEE");

    public StatsPanel(AppController controller) {
        this.controller = controller;
        controller.addListener(this);
        setBackground(DesignSystem.BACKGROUND);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        DesignSystem.enableAntialiasing(g2d);
        int w = getWidth();
        int h = getHeight();
        int margin = 60;
        
        g2d.setColor(DesignSystem.TOMATO_RED);
        g2d.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(28f));
        g2d.drawString("Harvest Trends", margin, margin);
        
        int chartW = w - 2 * margin;
        int chartH = h - 3 * margin;
        int baseY = h - 2 * margin;
        
        g2d.setColor(DesignSystem.OUTLINE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(margin, baseY, margin + chartW, baseY);
        g2d.drawLine(margin, baseY, margin, margin + 40);
        
        Map<String, Integer> stats = controller.getDailyStats();
        List<String> last7Days = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) last7Days.add(today.minusDays(i).toString());
        
        int maxVal = 5;
        for (Integer val : stats.values()) if (val > maxVal) maxVal = val;
        
        int barGap = 20;
        int barW = (chartW - (6 * barGap)) / 7;

        for (int i = 0; i < 7; i++) {
            String dateKey = last7Days.get(i);
            int count = stats.getOrDefault(dateKey, 0);
            int barH = (int)((double)count / maxVal * (chartH - 40));
            int x = margin + i * (barW + barGap) + barGap/2;
            int y = baseY - barH;

            g2d.setColor(DesignSystem.TOMATO_RED);
            g2d.fillRect(x, y, barW, barH);
            
            g2d.setColor(DesignSystem.ON_SURFACE);
            g2d.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(12f));
            String dayName = LocalDate.parse(dateKey).format(DAY_FORMATTER);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(dayName, x + (barW - fm.stringWidth(dayName))/2, baseY + 25);
            
            if (count > 0) {
                String cStr = String.valueOf(count);
                g2d.drawString(cStr, x + (barW - fm.stringWidth(cStr))/2, y - 10);
            }
        }
        g2d.dispose();
    }

    @Override public void onTimerTick(int secondsRemaining) {}
    @Override public void onStateChanged(boolean isBreak, int session, boolean isPaused) {}
    @Override public void onGardenUpdated(int sessionCount, int lifetimeCount) {}
    @Override public void onProgressUpdated(int xp, int level) {}
    @Override public void onSkinChanged(String skin) {}
    @Override public void onStatsUpdated() { repaint(); }
    @Override public void onTaskChanged(String task, List<String> fullList) {}
    @Override public void onSoundscapeChanged(String sound) {}
    @Override public void onAICoachMessage(String message) {}
}
