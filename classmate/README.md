# ClassMate — Academic Organizer
> A Java + JavaFX + SQLite desktop application for college students

---

## ✅ Requirements

| Tool | Version | Status |
|---|---|---|
| JDK | 25.0.3 | ✅ Installed at `C:\Program Files\Java\jdk-25.0.3` |
| JavaFX SDK | 25.0.3 | ✅ Installed at `C:\Program Files\JavaFX\javafx-sdk-25.0.3` |
| sqlite-jdbc | Latest | ⬇️ **You need to download this** |

---

## ⬇️ Step 1 — Download sqlite-jdbc

1. Go to: https://github.com/xerial/sqlite-jdbc/releases
2. Download the latest `sqlite-jdbc-x.x.x.jar`
3. Rename it to `sqlite-jdbc.jar`
4. Place it in the `lib\` folder:

```
classmate\
└── lib\
    └── sqlite-jdbc.jar   ← here
```

---

## 🚀 Step 2 — Run the App

Double-click `run.bat` in the project root, OR run in PowerShell:

```powershell
.\run.bat
```

This will:
1. Compile all Java source files
2. Copy resources (CSS, FXML)
3. Launch ClassMate

The SQLite database (`classmate.db`) is created automatically on first run in the project root.

---

## 📁 Project Structure

```
classmate/
├── src/
│   └── com/classmate/
│       ├── Main.java              ← App entry point
│       ├── db/
│       │   └── DatabaseHelper.java
│       ├── model/
│       │   ├── Jadwal.java
│       │   ├── Tugas.java
│       │   ├── Dosen.java
│       │   └── Reminder.java
│       ├── dao/
│       │   ├── JadwalDAO.java
│       │   ├── TugasDAO.java
│       │   ├── DosenDAO.java
│       │   └── ReminderDAO.java
│       └── ui/
│           ├── MainApp.java
│           ├── DashboardController.java
│           ├── JadwalController.java
│           ├── TugasController.java
│           ├── ReminderController.java
│           └── DosenController.java
├── resources/
│   └── com/classmate/styles/
│       └── app.css               ← UI theme
├── lib/
│   └── sqlite-jdbc.jar           ← add this
├── out/                          ← auto-created on build
├── classmate.db                  ← auto-created on first run
├── run.bat                       ← build & run script
└── README.md
```

---

## 🎨 Features

| Module | Features |
|---|---|
| **Dashboard** | Today's classes, upcoming deadlines (≤3 days), active assignment count |
| **Class Schedule** | Add / Edit / Delete schedules (course, day, time, room, lecturer) |
| **Assignments** | Add / Edit / Delete / Mark Complete — color-coded (🔴 overdue, 🟢 done) |
| **Reminders** | Auto-generated for assignments due in ≤7 days, dismiss / delete |
| **Lecturers** | Add / Edit / Delete lecturer contacts |

---

## ⚠️ Notes

- `PATH_TO_FX` is set as a **user environment variable** — the `run.bat` script uses the hardcoded path directly, so it works regardless of whether it's a user or system variable.
- All data is stored locally in `classmate.db` (SQLite). No internet connection needed.
- No login screen — single user app (student ID = 1).
