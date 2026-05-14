import javax.swing.*;
import java.awt.*;

public class TaskPanel extends JPanel implements AppController.StateListener {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;

    public TaskPanel(AppController controller) {
        controller.addListener(this);
        setLayout(new BorderLayout());
        setBackground(DesignSystem.BACKGROUND);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));
        
        JLabel title = new JLabel("My Focus Tasks");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(DesignSystem.QUICKSAND_BOLD.deriveFont(32f));
        title.setForeground(DesignSystem.TOMATO_RED);
        
        header.add(title);
        add(header, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        for (String t : controller.getTaskList()) listModel.addElement(t);
        
        taskList = new JList<>(listModel);
        taskList.setFont(DesignSystem.QUICKSAND_MEDIUM.deriveFont(16f));
        taskList.setBackground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(taskList);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        controls.setOpaque(false);

        TamatarButton addBtn = new TamatarButton("ADD", DesignSystem.LEAF_GREEN, DesignSystem.LEAF_GREEN_DARK);
        addBtn.setPreferredSize(new Dimension(90, 45));
        addBtn.addActionListener(e -> {
            String t = JOptionPane.showInputDialog(this, "New Task:");
            if (t != null && !t.trim().isEmpty()) controller.addTask(t);
        });

        TamatarButton aiBtn = new TamatarButton("AI BRAIN", DesignSystem.SUNNY_YELLOW, DesignSystem.SUNNY_YELLOW.darker());
        aiBtn.setPreferredSize(new Dimension(100, 45));
        aiBtn.addActionListener(e -> {
            String selected = taskList.getSelectedValue();
            if (selected != null) controller.aiBreakdownTask(selected);
            else JOptionPane.showMessageDialog(this, "Select a task first!");
        });

        TamatarButton setBtn = new TamatarButton("SET ACTIVE", DesignSystem.TOMATO_RED, DesignSystem.TOMATO_RED_DARK);
        setBtn.setPreferredSize(new Dimension(110, 45));
        setBtn.addActionListener(e -> {
            String selected = taskList.getSelectedValue();
            if (selected != null) controller.setActiveTask(selected);
        });

        TamatarButton delBtn = new TamatarButton("DELETE", Color.GRAY, Color.DARK_GRAY);
        delBtn.setPreferredSize(new Dimension(100, 45));
        delBtn.addActionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx != -1) controller.removeTask(idx);
        });

        controls.add(addBtn);
        controls.add(aiBtn);
        controls.add(setBtn);
        controls.add(delBtn);
        add(controls, BorderLayout.SOUTH);
    }

    @Override public void onTimerTick(int secondsRemaining) {}
    @Override public void onStateChanged(boolean isBreak, int session, boolean isPaused) {}
    @Override public void onGardenUpdated(int sessionCount, int lifetimeCount) {}
    @Override public void onProgressUpdated(int xp, int level) {}
    @Override public void onSkinChanged(String skin) {}
    @Override public void onStatsUpdated() {}
    @Override public void onSoundscapeChanged(String sound) {}
    @Override public void onAICoachMessage(String message) {}
    @Override public void onTaskChanged(String activeTask, java.util.List<String> fullList) {
        listModel.clear();
        for (String t : fullList) listModel.addElement(t);
    }
}
