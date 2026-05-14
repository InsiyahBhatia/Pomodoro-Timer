import javax.swing.*;
import java.awt.*;

public class RhymePanel extends JPanel {
    public RhymePanel() {
        setLayout(new BorderLayout());
        setBackground(DesignSystem.BACKGROUND);

        JLabel title = new JLabel("Aha Tamatar Rhyme", SwingConstants.CENTER);
        title.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(24f));
        title.setForeground(DesignSystem.TOMATO_RED);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        String lyrics = "<html><center><div style='font-family: Arial; font-size: 16pt; line-height: 2.0; color: #1B1C17;'>" +
                "Aha tamatar bada mazedar!<br/>" +
                "Wah tamatar bada mazedar!<br/>" +
                "<br/>" +
                "<b>Ek din isko chuhe ne khaya!</b><br/>" +
                "Billi ko bhi maar bhagaya!<br/>" +
                "<br/>" +
                "Aha tamatar bada mazedar!<br/>" +
                "Wah tamatar bada mazedar!" +
                "</div></center></html>";

        JLabel lyricsLabel = new JLabel(lyrics, SwingConstants.CENTER);
        add(lyricsLabel, BorderLayout.CENTER);
        
        JLabel footer = new JLabel("Grow strong like a healthy vine!", SwingConstants.CENTER);
        footer.setFont(DesignSystem.QUICKSAND_MEDIUM);
        footer.setForeground(DesignSystem.LEAF_GREEN);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        add(footer, BorderLayout.SOUTH);
    }
}
