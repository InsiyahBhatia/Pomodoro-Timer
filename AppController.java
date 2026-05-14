import javax.swing.*;
import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;
import java.net.http.*;
import java.net.*;

public class AppController {
    private int pomodoroMinutes = 25;
    private int breakMinutes = 5;
    private int totalSessions = 4;
    private int currentSession = 1;
    
    // Stats
    private int tomatoesGrown = 0;
    private int totalLifetimeTomatoes = 0;
    private int xp = 0;
    private int level = 1;
    private String selectedSkin = "DEFAULT";
    
    // AI Features - Updated for 2026 Model Availability
    private String geminiApiKey = "YOUR_API_KEY_HERE";
    private String[] modelAttempts = {
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=", // Still trying 1.5 as fallback
        "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=",    // 2026 Standard
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite:generateContent?key=", // 2026 Flagship
        "https://generativelanguage.googleapis.com/v1/models/gemini-flash-latest:generateContent?key="  // 2026 Alias
    };

    // Tasks
    private List<String> taskList = new ArrayList<>();
    private String activeTask = "Growing Greatness!";
    
    // Soundscapes
    private String selectedSoundscape = "NONE";
    private Clip ambientClip;

    // Daily Analytics
    private Map<String, Integer> dailyStats = new LinkedHashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private boolean isBreak = false;
    private boolean isPaused = false;
    private int remainingSeconds;
    private javax.swing.Timer timer;

    private static final String STATS_FILE = "aha_tamatar_stats.txt";
    private static final String ENV_FILE = ".env";

    public interface StateListener {
        void onTimerTick(int secondsRemaining);
        void onStateChanged(boolean isBreak, int session, boolean isPaused);
        void onGardenUpdated(int sessionCount, int lifetimeCount);
        void onProgressUpdated(int xp, int level);
        void onSkinChanged(String skin);
        void onStatsUpdated();
        void onTaskChanged(String activeTask, List<String> fullList);
        void onSoundscapeChanged(String sound);
        void onAICoachMessage(String message);
    }

    private List<StateListener> listeners = new ArrayList<>();

    public AppController() {
        remainingSeconds = pomodoroMinutes * 60;
        loadEnv();
        loadStats();
        ensureTodayExists();
    }

    public void addListener(StateListener l) { listeners.add(l); }

    // --- AI LOGIC ---

    public void askAICoach() {
        chatWithAI("Give me a one-sentence pun-filled productivity tip.");
    }

    public void chatWithAI(String userMessage) {
        if (geminiApiKey.equals("YOUR_API_KEY_HERE") || geminiApiKey.isEmpty()) {
            String msg = "Aha! I need your real API Key in the .env file!";
            for (StateListener l : listeners) l.onAICoachMessage(msg);
            return;
        }

        String prompt = "You are 'Aha Tamatar', a friendly farm-themed focus coach. " +
                        "The user just said: '" + userMessage + "'. " +
                        "Reply in exactly one short, pun-filled sentence. Keep it encouraging.";
        
        attemptAIRequest(prompt, 0, message -> {
            for (StateListener l : listeners) l.onAICoachMessage(message);
        });
    }

    public void aiBreakdownTask(String task) {
        if (geminiApiKey.equals("YOUR_API_KEY_HERE") || geminiApiKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please add your Gemini API Key to the .env file!");
            return;
        }

        String prompt = "Break down this task into 3 small steps: " + task + 
                        ". Format: Step 1 | Step 2 | Step 3. Only return the steps separated by |.";
        
        attemptAIRequest(prompt, 0, response -> {
            String[] subtasks = response.split("\\|");
            for (String st : subtasks) {
                if (!st.trim().isEmpty()) addTask(st.trim());
            }
        });
    }

    private void attemptAIRequest(String prompt, int attemptIndex, java.util.function.Consumer<String> callback) {
        if (attemptIndex >= modelAttempts.length) {
            callback.accept("Aha! All AI tunnels are blocked. Check your API key or regional access!");
            return;
        }

        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .build();
                
                String url = modelAttempts[attemptIndex] + geminiApiKey;
                String escapedPrompt = prompt.replace("\"", "\\\"");
                String json = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                
                if (response.statusCode() == 404 || response.statusCode() == 400) {
                    // Fail over to next model
                    System.err.println("Attempt " + attemptIndex + " (URL: " + url.substring(0, 50) + "...) failed. Retrying...");
                    attemptAIRequest(prompt, attemptIndex + 1, callback);
                    return;
                }

                if (response.statusCode() != 200) {
                    callback.accept("Aha! The AI garden is having issues (Status " + response.statusCode() + ")");
                    return;
                }

                int start = body.indexOf("\"text\": \"");
                if (start != -1) {
                    start += 9;
                    int end = body.indexOf("\"", start);
                    if (end != -1) {
                        String result = body.substring(start, end)
                            .replace("\\n", " ")
                            .replace("\\\"", "\"")
                            .replace("\\", "");
                        callback.accept(result);
                        return;
                    }
                }
                callback.accept("Aha! The AI gave a weird answer. Try again!");
            } catch (Exception e) {
                attemptAIRequest(prompt, attemptIndex + 1, callback);
            }
        }).start();
    }

    private void loadEnv() {
        File file = new File(ENV_FILE);
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("#") || line.isEmpty()) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2 && parts[0].trim().equals("GEMINI_API_KEY")) {
                    this.geminiApiKey = parts[1].trim();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- POMODORO LOGIC ---

    public void startTimer() {
        if (timer != null && timer.isRunning()) return;
        isPaused = false;
        notifyStateChange();
        playAmbient();
        timer = new javax.swing.Timer(1000, e -> {
            remainingSeconds--;
            for (StateListener l : listeners) l.onTimerTick(remainingSeconds);
            if (remainingSeconds <= 0) toggleState();
        });
        timer.start();
    }

    public void pauseTimer() { if (timer != null) { timer.stop(); isPaused = true; stopAmbient(); notifyStateChange(); } }
    public void stopTimer() { if (timer != null) timer.stop(); isPaused = false; stopAmbient(); notifyStateChange(); }
    public void resetTimer() {
        stopTimer();
        isBreak = false;
        currentSession = 1;
        remainingSeconds = pomodoroMinutes * 60;
        notifyStateChange();
        for (StateListener l : listeners) l.onTimerTick(remainingSeconds);
    }

    private void toggleState() {
        stopTimer();
        if (!isBreak) {
            tomatoesGrown++;
            totalLifetimeTomatoes++;
            xp += 100;
            String today = LocalDate.now().format(DATE_FORMATTER);
            dailyStats.put(today, dailyStats.getOrDefault(today, 0) + 1);
            checkLevelUp();
            saveStats();
            DesignSystem.playSound("Aaha Tamatar Bada Mazedar.wav");
            for (StateListener l : listeners) {
                l.onGardenUpdated(tomatoesGrown, totalLifetimeTomatoes);
                l.onProgressUpdated(xp, level);
                l.onStatsUpdated();
            }
            remainingSeconds = breakMinutes * 60;
            isBreak = true;
        } else {
            if (currentSession < totalSessions) {
                currentSession++;
                remainingSeconds = pomodoroMinutes * 60;
                isBreak = false;
            } else {
                JOptionPane.showMessageDialog(null, "Aha! You grew a whole garden!");
                resetTimer();
                return;
            }
        }
        notifyStateChange();
        startTimer();
    }

    private void notifyStateChange() { for (StateListener l : listeners) l.onStateChanged(isBreak, currentSession, isPaused); }
    private void checkLevelUp() {
        int nextLevelXP = level * 500;
        if (xp >= nextLevelXP) {
            level++;
            JOptionPane.showMessageDialog(null, "LEVEL UP! You are now a Level " + level + " Gardener!");
            saveStats();
        }
    }
    private void ensureTodayExists() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        if (!dailyStats.containsKey(today)) dailyStats.put(today, 0);
    }
    public void setSoundscape(String sound) {
        this.selectedSoundscape = sound;
        if (isRunning() && !isPaused) { stopAmbient(); playAmbient(); }
        saveStats();
        for (StateListener l : listeners) l.onSoundscapeChanged(sound);
    }
    private void playAmbient() {
        if (selectedSoundscape.equals("NONE")) return;
        try {
            File soundFile = new File(selectedSoundscape + ".wav");
            if (!soundFile.exists()) return;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            ambientClip = AudioSystem.getClip();
            ambientClip.open(audioStream);
            ambientClip.loop(Clip.LOOP_CONTINUOUSLY);
            ambientClip.start();
        } catch (Exception e) {}
    }
    private void stopAmbient() { if (ambientClip != null && ambientClip.isRunning()) { ambientClip.stop(); ambientClip.close(); } }
    public void addTask(String task) {
        taskList.add(task);
        saveStats();
        for (StateListener l : listeners) l.onTaskChanged(activeTask, taskList);
    }
    public void removeTask(int index) {
        if (index >= 0 && index < taskList.size()) {
            taskList.remove(index);
            saveStats();
            for (StateListener l : listeners) l.onTaskChanged(activeTask, taskList);
        }
    }
    public void setActiveTask(String task) {
        this.activeTask = task;
        saveStats();
        for (StateListener l : listeners) l.onTaskChanged(activeTask, taskList);
    }
    private void saveStats() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STATS_FILE))) {
            writer.println("lifetimeTomatoes=" + totalLifetimeTomatoes);
            writer.println("xp=" + xp);
            writer.println("level=" + level);
            writer.println("selectedSkin=" + selectedSkin);
            writer.println("activeTask=" + activeTask);
            writer.println("selectedSoundscape=" + selectedSoundscape);
            writer.println("pomodoroMinutes=" + pomodoroMinutes);
            writer.println("breakMinutes=" + breakMinutes);
            StringBuilder tasks = new StringBuilder();
            for (String t : taskList) tasks.append(t).append("|");
            writer.println("taskList=" + tasks.toString());
            StringBuilder ds = new StringBuilder();
            for (Map.Entry<String, Integer> entry : dailyStats.entrySet()) ds.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            writer.println("dailyStats=" + ds.toString());
        } catch (IOException e) {}
    }
    private void loadStats() {
        File file = new File(STATS_FILE);
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length < 2) continue;
                switch (parts[0]) {
                    case "lifetimeTomatoes": totalLifetimeTomatoes = Integer.parseInt(parts[1]); break;
                    case "xp": xp = Integer.parseInt(parts[1]); break;
                    case "level": level = Integer.parseInt(parts[1]); break;
                    case "selectedSkin": selectedSkin = parts[1]; break;
                    case "activeTask": activeTask = parts[1]; break;
                    case "selectedSoundscape": selectedSoundscape = parts[1]; break;
                    case "pomodoroMinutes": pomodoroMinutes = Integer.parseInt(parts[1]); break;
                    case "breakMinutes": breakMinutes = Integer.parseInt(parts[1]); break;
                    case "taskList":
                        String[] ts = parts[1].split("\\|");
                        taskList.clear();
                        for (String t : ts) if (!t.isEmpty()) taskList.add(t);
                        break;
                    case "dailyStats":
                        String[] days = parts[1].split(",");
                        for (String d : days) {
                            String[] dp = d.split(":");
                            if (dp.length == 2) dailyStats.put(dp[0], Integer.parseInt(dp[1]));
                        }
                        break;
                }
            }
            remainingSeconds = pomodoroMinutes * 60;
        } catch (Exception e) {}
    }
    public String getActiveTask() { return activeTask; }
    public List<String> getTaskList() { return taskList; }
    public String getSelectedSoundscape() { return selectedSoundscape; }
    public Map<String, Integer> getDailyStats() { return dailyStats; }
    public String getSelectedSkin() { return selectedSkin; }
    public int getXP() { return xp; }
    public int getLevel() { return level; }
    public int getXPForNextLevel() { return level * 500; }
    public int getRemainingSeconds() { return remainingSeconds; }
    public boolean isBreak() { return isBreak; }
    public boolean isPaused() { return isPaused; }
    public boolean isRunning() { return timer != null && timer.isRunning(); }
    public int getCurrentSession() { return currentSession; }
    public int getTotalSessions() { return totalSessions; }
    public int getTomatoesGrown() { return tomatoesGrown; }
    public int getTotalLifetimeTomatoes() { return totalLifetimeTomatoes; }
    public void setSkin(String skin) { this.selectedSkin = skin; saveStats(); for (StateListener l : listeners) l.onSkinChanged(skin); }
    public void setConfig(int pom, int brk, int sess) { this.pomodoroMinutes = pom; this.breakMinutes = brk; this.totalSessions = sess; saveStats(); resetTimer(); }
}
