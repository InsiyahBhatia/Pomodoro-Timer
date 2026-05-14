import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChatPanel extends JPanel implements AppController.StateListener {
    private AppController controller;
    private JTextArea chatArea;
    private JTextField inputField;

    public ChatPanel(AppController controller) {
        this.controller = controller;
        controller.addListener(this);
        setLayout(new BorderLayout());
        setBackground(DesignSystem.BACKGROUND);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Aha! Chat Garden");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(32f));
        title.setForeground(DesignSystem.TOMATO_RED);
        header.add(title);
        add(header, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(14f));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(Color.WHITE);
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        inputField = new JTextField();
        inputField.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(14f));
        inputField.addActionListener(e -> sendMessage());
        
        TamatarButton sendBtn = new TamatarButton("SEND", DesignSystem.TOMATO_RED, DesignSystem.TOMATO_RED_DARK);
        sendBtn.setPreferredSize(new Dimension(80, 40));
        sendBtn.addActionListener(e -> sendMessage());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        chatArea.setText("Aha! Tamatar: Welcome to the garden! How can I help you grow today?\n\n");
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            chatArea.append("You: " + msg + "\n");
            inputField.setText("");
            chatArea.append("Aha! Tamatar: Thinking...\n");
            controller.chatWithAI(msg);
        }
    }

    @Override public void onAICoachMessage(String message) {
        // Remove "Thinking..." and add actual response
        String text = chatArea.getText();
        int lastIdx = text.lastIndexOf("Aha! Tamatar: Thinking...");
        if (lastIdx != -1) {
            chatArea.setText(text.substring(0, lastIdx));
        }
        chatArea.append("Aha! Tamatar: " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    @Override public void onTimerTick(int secondsRemaining) {}
    @Override public void onStateChanged(boolean isBreak, int session, boolean isPaused) {}
    @Override public void onGardenUpdated(int sessionCount, int lifetimeCount) {}
    @Override public void onProgressUpdated(int xp, int level) {}
    @Override public void onSkinChanged(String skin) {}
    @Override public void onStatsUpdated() {}
    @Override public void onTaskChanged(String task, List<String> fullList) {}
    @Override public void onSoundscapeChanged(String sound) {}
}
