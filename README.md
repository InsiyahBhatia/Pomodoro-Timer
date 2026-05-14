# Aha Tamatar!
### The Gamified AI Pomodoro Garden

Aha Tamatar is a professional-grade Pomodoro application built with **Java Swing**, designed to turn focus into a harvest. Featuring a classic "Aha" aesthetic, AI-powered productivity coaching, and a persistent garden ecosystem.

##  Key Features
- **Classic Aesthetic**: A high-contrast, clean UI with a professional "Aha" color palette and Arial typography.
- **AI Chat Garden**: A dedicated tab to talk to your **AI Coach** (powered by Google Gemini 2.5/3.1). Get pun-filled productivity tips and task breakdowns.
- **Tomato Garden**: Earn tomatoes for every focused session and grow your lifetime harvest.
- **Progressive Leveling**: Gain XP and level up from a "Seedling" to a "Master Gardener."
- **Focus Trends**: Track your productivity over the last 7 days with built-in analytics.
- **Custom Soundscapes**: Focus to the sound of Rain, Forests, or Waves.

##  Tech Stack
- **Language**: Java 17+
- **Framework**: Java Swing (Custom `paintComponent` rendering)
- **AI Engine**: Google Gemini API (v1beta/v1)
- **Persistence**: Local file-based I/O for stats and settings.

##  Setup & Security
To enable the AI Chatbot:
1. Create a `.env` file in the root directory.
2. Add your API key: `GEMINI_API_KEY=your_key_here`.
3. The `.env` file is automatically ignored by Git to keep your key safe.

## How to Run
```powershell
javac --release 17 *.java
java PomodoroApp
```

