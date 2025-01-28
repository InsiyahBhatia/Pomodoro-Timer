import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Main extends JFrame {

    private JLabel timeLabel;
    private JButton startButton, stopButton, resetButton;
    private JTextField pomodoroField, breakField, sessionsField;
    private JLabel statsLabel;
    private JProgressBar progressBar;

    private Timer timer;
    private int totalSeconds;
    private int pomodoroMinutes = 25;
    private int breakMinutes = 5;
    private int totalSessions = 4;
    private int currentSession = 0;
    private boolean isBreak = false;

    private int sessionsCompleted = 0;
    private int totalWorkSeconds = 0;
    private int totalBreakSeconds = 0;

    public Main() {
        setTitle("Pomodoro Timer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon("Pomodoro\\logo.png");
        setIconImage(image.getImage());

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(48, 63, 159));
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel pomodoroLabel = new JLabel("Pomodoro (min):");
        pomodoroLabel.setForeground(Color.WHITE);
        pomodoroLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(pomodoroLabel, gbc);

        pomodoroField = new JTextField("25", 5);
        pomodoroField.setFont(new Font("Roboto", Font.PLAIN, 16));
        gbc.gridx = 1;
        inputPanel.add(pomodoroField, gbc);

        JLabel breakLabel = new JLabel("Break (min):");
        breakLabel.setForeground(Color.WHITE);
        breakLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        gbc.gridx = 2;
        inputPanel.add(breakLabel, gbc);

        breakField = new JTextField("5", 5);
        breakField.setFont(new Font("Roboto", Font.PLAIN, 16));
        gbc.gridx = 3;
        inputPanel.add(breakField, gbc);

        JLabel sessionsLabel = new JLabel("Sessions:");
        sessionsLabel.setForeground(Color.WHITE);
        sessionsLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        gbc.gridx = 4;
        inputPanel.add(sessionsLabel, gbc);

        sessionsField = new JTextField("4", 5);
        sessionsField.setFont(new Font("Roboto", Font.PLAIN, 16));
        gbc.gridx = 5;
        inputPanel.add(sessionsField, gbc);

        add(inputPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(63, 81, 181); 
                Color color2 = new Color(38, 50, 56); 
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        centerPanel.setLayout(new BorderLayout());

        timeLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Roboto", Font.BOLD, 120));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(new Color(0, 31, 60)); 
        timeLabel.setBorder(new LineBorder(new Color(63, 81, 181), 5));
        centerPanel.add(timeLabel, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setForeground(new Color(63, 81, 181));
        progressBar.setBackground(new Color(0, 31, 60));
        centerPanel.add(progressBar, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(48, 63, 159));
        bottomPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(48, 63, 159));
        buttonPanel.setLayout(new FlowLayout());

        startButton = new JButton("Start");
        styleButton(startButton, new Color(63, 81, 181)); 
        startButton.addActionListener(new StartButtonListener());

        stopButton = new JButton("Stop");
        styleButton(stopButton, new Color(63, 81, 181)); 
        stopButton.addActionListener(new StopButtonListener());

        resetButton = new JButton("Reset");
        styleButton(resetButton, new Color(63, 81, 181)); 
        resetButton.addActionListener(new ResetButtonListener());

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        statsLabel = new JLabel("Sessions Completed: 0 | Total Work: 0 min | Total Break: 0 min", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        statsLabel.setForeground(Color.WHITE);
        bottomPanel.add(statsLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(63, 81, 181), 2));
        button.setPreferredSize(new Dimension(120, 40));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer != null && timer.isRunning()) {
                return;
            }

            try {
                pomodoroMinutes = Integer.parseInt(pomodoroField.getText());
                breakMinutes = Integer.parseInt(breakField.getText());
                totalSessions = Integer.parseInt(sessionsField.getText());

                if (pomodoroMinutes <= 0 || breakMinutes <= 0 || totalSessions <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Main.this, "Please enter valid positive numbers for all fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isBreak) {
                totalSeconds = breakMinutes * 60;
            } else {
                totalSeconds = pomodoroMinutes * 60;
            }

            progressBar.setMaximum(isBreak ? breakMinutes * 60 : pomodoroMinutes * 60);
            progressBar.setValue(0);

            timer = new Timer(1000, new TimerListener());
            timer.start();
        }
    }

    private class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer != null) {
                timer.stop();
            }
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer != null) {
                timer.stop();
            }
            currentSession = 0;
            sessionsCompleted = 0;
            totalWorkSeconds = 0;
            totalBreakSeconds = 0;
            isBreak = false;
            timeLabel.setText("00:00");
            progressBar.setValue(0);
            statsLabel.setText("Sessions Completed: 0 | Total Work: 0 min | Total Break: 0 min");
        }
    }

   public class TimerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        totalSeconds--;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        progressBar.setValue(progressBar.getMaximum() - totalSeconds);

        if (totalSeconds <= 0) {

            playAlarmSound();

            if (isBreak) {
                totalBreakSeconds += breakMinutes;
                sessionsCompleted++;
                statsLabel.setText("Sessions Completed: " + sessionsCompleted + " | Total Work: " + totalWorkSeconds / 60 + " min | Total Break: " + totalBreakSeconds / 60 + " min");
                if (currentSession < totalSessions) {
                    currentSession++;
                } else {
                    JOptionPane.showMessageDialog(Main.this, "All sessions completed!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                totalWorkSeconds += pomodoroMinutes;
            }

            isBreak = !isBreak;
            timer.stop();
            startButton.doClick();
        }
    }


    private void playAlarmSound() {
        try {
            File soundFile = new File("Pomodoro\\alarm.wav"); 
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            JOptionPane.showMessageDialog(Main.this, "Session complete! Click OK to continue.", "Pomodoro Timer", JOptionPane.INFORMATION_MESSAGE);
            
            clip.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        new Main();
    }
}
}
