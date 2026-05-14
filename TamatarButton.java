import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TamatarButton extends JButton {
    private Color baseColor;
    private Color hoverColor;
    private boolean isHovered = false;
    private float alpha = 1.0f;

    public TamatarButton(String text, Color base, Color hover) {
        super(text);
        this.baseColor = base;
        this.hoverColor = hover;
        
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(16f));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { 
                isHovered = true; 
                repaint(); 
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                isHovered = false; 
                repaint(); 
            }
            @Override
            public void mousePressed(MouseEvent e) {
                alpha = 0.7f;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                alpha = 1.0f;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        DesignSystem.enableAntialiasing(g2d);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int w = getWidth();
        int h = getHeight();

        // Drop Shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRoundRect(3, 4, w - 6, h - 6, DesignSystem.ROUNDING, DesignSystem.ROUNDING);

        // Gradient Background
        Color color1 = isHovered ? hoverColor : baseColor;
        Color color2 = color1.darker();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, w - 3, h - 3, DesignSystem.ROUNDING, DesignSystem.ROUNDING);

        // Subtle Inner Glow
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(1, 1, w - 5, h - 5, DesignSystem.ROUNDING, DesignSystem.ROUNDING);

        // Text with Shadow
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(getText())) / 2;
        int y = (h + fm.getAscent()) / 2 - 4;

        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawString(getText(), x + 1, y + 1);
        g2d.setColor(Color.WHITE);
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }
}
