import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    public SettingsPanel(AppController controller) {
        setLayout(new GridBagLayout());
        setBackground(DesignSystem.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Settings & Audio", SwingConstants.CENTER);
        title.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(24f));
        title.setForeground(DesignSystem.TOMATO_RED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // --- Mascot Skin Selection ---
        gbc.gridy++; gbc.gridwidth = 1;
        add(new JLabel("Mascot Skin:"), gbc);
        String[] skins = {"DEFAULT", "NINJA (Lvl 2)", "GOLDEN (Lvl 3)"};
        JComboBox<String> skinBox = new JComboBox<>(skins);
        skinBox.setSelectedItem(controller.getSelectedSkin());
        gbc.gridx = 1;
        add(skinBox, gbc);

        // --- Soundscape Selection ---
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Ambient Sound:"), gbc);
        String[] sounds = {"NONE", "Rain", "Forest", "Waves"};
        JComboBox<String> soundBox = new JComboBox<>(sounds);
        soundBox.setSelectedItem(controller.getSelectedSoundscape());
        gbc.gridx = 1;
        add(soundBox, gbc);

        // --- Timer Config ---
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Focus (min):"), gbc);
        JTextField pomField = new JTextField("25", 5);
        gbc.gridx = 1;
        add(pomField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Break (min):"), gbc);
        JTextField breakField = new JTextField("5", 5);
        gbc.gridx = 1;
        add(breakField, gbc);

        // Save Button
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        TamatarButton saveBtn = new TamatarButton("SAVE CHANGES", DesignSystem.LEAF_GREEN, DesignSystem.LEAF_GREEN_DARK);
        saveBtn.addActionListener(e -> {
            try {
                String selected = (String) skinBox.getSelectedItem();
                if (selected.startsWith("NINJA") && controller.getLevel() < 2) {
                    JOptionPane.showMessageDialog(this, "Unlock Ninja skin at Level 2!");
                    return;
                }
                if (selected.startsWith("GOLDEN") && controller.getLevel() < 3) {
                    JOptionPane.showMessageDialog(this, "Unlock Golden skin at Level 3!");
                    return;
                }
                controller.setSkin(selected.split(" ")[0]);
                controller.setSoundscape((String) soundBox.getSelectedItem());
                controller.setConfig(Integer.parseInt(pomField.getText()), Integer.parseInt(breakField.getText()), 4);
                JOptionPane.showMessageDialog(this, "Settings Saved!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid numbers!"); }
        });
        add(saveBtn, gbc);

        gbc.gridy++;
        JLabel xpLabel = new JLabel("Current XP: " + controller.getXP() + " / " + controller.getXPForNextLevel());
        xpLabel.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(10f));
        add(xpLabel, gbc);
    }
}
