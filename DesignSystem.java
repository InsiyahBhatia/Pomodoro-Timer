import java.awt.*;
import javax.sound.sampled.*;
import java.io.File;

public class DesignSystem {
    // --- Classic Aha! Palette (From Screenshot) ---
    public static final Color TOMATO_RED = new Color(211, 47, 47);        
    public static final Color TOMATO_RED_DARK = new Color(183, 28, 28);
    public static final Color SUNNY_YELLOW = new Color(192, 160, 0);      
    public static final Color LEAF_GREEN = new Color(0, 100, 0);          
    public static final Color LEAF_GREEN_DARK = new Color(0, 80, 0);
    public static final Color SOIL_BROWN = new Color(93, 64, 55);         // Added back for Garden
    
    public static final Color BACKGROUND = new Color(248, 245, 233);      
    public static final Color SURFACE = new Color(255, 255, 255, 200);    // Added back for Task/Stats
    public static final Color SURFACE_CONTAINER = new Color(240, 238, 225); 
    public static final Color ON_SURFACE = new Color(40, 40, 40);         
    public static final Color OUTLINE = new Color(150, 150, 150);
    
    // --- Classic Clean Typography ---
    public static final Font QUICKSAND_BOLD = new Font("Arial", Font.BOLD, 22);
    public static final Font QUICKSAND_MEDIUM = new Font("Arial", Font.PLAIN, 16);
    public static final Font DISPLAY_TIMER = new Font("Arial", Font.BOLD, 88);

    public static final int ROUNDING = 25; // Added back for Buttons

    public static void enableAntialiasing(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public static void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            if (file.exists()) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
            }
        } catch (Exception e) {}
    }
}
