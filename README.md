# 🏉 Kabaddi Arena — Personal Performance Scout



An Android application built to digitize Kabaddi player performance tracking for local "Matti" (mud) tournament players who currently have no way to document their stats.

---

 📱 App Screenshots

> Splash | Match Setup | Live Logger | Performance Card | Stats

---

 🎯 Problem Statement

Millions of Kabaddi players competing in local tournaments have no system to track their performance. Players don't know their raid success rate or tackle efficiency. Talented rural players go unnoticed by Pro-Kabaddi scouts simply because they have no documented proof of skill.

---

 ✅ Features

| Feature | Description |
|---|---|
| 🏃 Live Logger | One-tap buttons for Empty Raid, Touch Point, Bonus Point, Super Tackle, Tackle Success |
| ↩ Undo | Instantly reverse accidental taps during fast-paced matches |
| 📊 Success % | Real-time Raid Success % and Tackle Success % calculation |
| 🃏 Performance Card | Shareable image card with total points, percentages and badge |
| 🗺️ Player Heatmap | Court visual showing raid and tackle zones |
| 📈 Point Graph | Match-wise line chart showing scoring trends over time |
| 🎥 Video Link | Attach a Best Raid YouTube link to any match |
| 💾 Room DB | All match history stored locally — works offline |
| 📤 Share | Export Performance Card as image to WhatsApp, Instagram, etc |

---

 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| IDE | Android Studio |
| Database | Room DB (SQLite) |
| Architecture | ViewModel + LiveData |
| Charts | MPAndroidChart |
| UI | XML Layouts + Custom Canvas (Heatmap) |
| Image Export | FileProvider + Android Share Sheet |
| Async | Kotlin Coroutines |

---

 🚀 How to Run

1. Clone the repo
   git clone https://github.com/Rayif18/KabaddiArena.git
2. Open in Android Studio
3. Wait for Gradle sync
4. Run on emulator or physical device (Android 7.0+)

---

 📋 Success Criteria Met

- ✅ Live Logger allows undoing an accidental tap
- ✅ Performance Card is shareable as an image
- ✅ UI is fast and handles rapid pace of a Kabaddi match

---

 🌍 Impact Goals

- **Rural Talent Visibility** — Shareable stats card gives village players digital proof of skill
- **Data-Driven Discipline** — Players can see their own numbers and train with purpose
- **Cultural Continuity** — Modernizing India's indigenous sport for the next generation

---

 👨‍💻 Developer

Built as part of MindMatrix VTU Internship Program
 Android App Development using GenAI
