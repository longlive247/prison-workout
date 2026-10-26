# Prison Workout 🏋️‍♂️🃏

A high-intensity, minimalist Calisthenics & Bodyweight workout app for Android powered by the classic **52-Card Deck** training method. Build functional strength anywhere with zero equipment!

---

## 🌟 Key Features

- 🃏 **52-Card Deck Method**: Suit and rank combinations determine card values (2–10 = value, J/Q/K = 10, Ace = 1) seamlessly paired with a fixed exercise rotation: **Push-ups ➔ Pull-ups ➔ Dips**.
- 🤸 **Guided Warm-up Routine**: A 4-step pre-workout mobility routine (Arm Circles, Jumping Jacks, Torso Twists, Wrist Circles) with instructions and graphics to prepare your joints and muscles.
- ⏱️ **Rest Timer with Controls**: Customizable countdown rest periods between cards (`30s`, `45s`, `60s`, `90s`, or `Disabled`) featuring `+15s`/`-15s` controls, vibration signals, and a skip option.
- ⏱️ **Live Workout Timer**: Real-time elapsed duration tracking during workouts.
- ✏️ **Custom Rep Logging & Early Finish**: Log actual completed reps per card if you need to scale down, or end workouts early at any time.
- 📊 **Persistent History & Analytics**: Automatic session logging saved to local storage with progress comparisons (+X reps, +Y cards) against your previous workout.
- 🌐 **Multilingual Support**: Fully localized in **English (Default)** and **German** with in-app language switching and persistent locale preferences.
- 🎨 **High-Contrast Material UI**: Built with Google Material Components, ensuring clear visibility on all screen types.

---

## 📸 Application Flow

1. **Main Menu (`MenuActivity`)**: Quick view of your last workout summary, language selector (`EN`/`DE`), and rest timer configuration.
2. **Warm-up Screen (`WarmupActivity`)**: Guided 4-step mobility warmup before starting the deck.
3. **Workout Screen (`MainActivity`)**: Card viewer, live timer, exercise display, custom rep input field, and rest timer overlay.
4. **Stats & History (`StatsActivity`)**: Workout completion metrics, direct comparison to previous workouts, and historical log of past sessions.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 1.9
- **UI Framework**: Android XML Layouts, ViewBinding, Material Components
- **Build System**: Android Gradle Plugin 8.x (Target SDK 34, Min SDK 24)
- **Data Persistence**: `SharedPreferences` + `org.json` serialization
- **Localization**: Android Resource Bundles (`values/strings.xml`, `values-de/strings.xml`) with dynamic `Context` wrapper (`LocaleHelper`)

---

## 📂 Project Structure

```
app/src/main/java/com/example/deckworkout/
├── BaseActivity.kt          # Base activity handling locale configuration context
├── Card.kt                  # Data models for Card, Suit, Rank, and Exercise
├── LocaleHelper.kt          # Preferences manager for in-app language switching
├── MainActivity.kt          # Workout execution screen with card deck & rest overlay
├── MenuActivity.kt          # Home menu screen with settings & history launcher
├── RestTimerHelper.kt       # Preferences manager for rest timer duration
├── StatsActivity.kt         # Workout summary, comparison, & history log screen
├── WarmupActivity.kt        # Guided warmup routine screen
├── WorkoutHistoryManager.kt # JSON persistence engine for workout history
├── WorkoutManager.kt        # Engine managing deck generation, shuffling & exercises
└── WorkoutSession.kt        # Data model & JSON serializer for workout sessions
```

---

## 🚀 Building & Running

### Prerequisites
- Android Studio (2023.1+ or newer)
- Android SDK (API Level 34)
- JDK 17

### Build Commands

To build the debug APK via terminal:
```bash
./gradlew :app:assembleDebug
```

The compiled APK will be available at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.
