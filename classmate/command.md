# Project Documentation  
# ClassMate — College Schedule Management Application

---

# 1. Application Description

ClassMate is a Windows-based desktop application designed to help students manage their academic activities efficiently. The application functions as a personal academic organizer that allows users to manage class schedules, assignments, deadline reminders, and lecturer contacts in one simple platform.

The application is developed using:

- Programming Language: Java
- Database: SQLite
- Platform: Windows Desktop

The application works offline and stores all data locally using SQLite database.

---

# 2. Application Objectives

The main objectives of this application are:

- Help students organize class schedules
- Help students remember assignment deadlines
- Store lecturer contact information neatly
- Reduce the risk of missing tasks or classes
- Provide a lightweight and user-friendly desktop application

---

# 3. Target Users

Target users of this application include:

- College students
- Students who need academic schedule management
- Users who want a simple academic organizer

---

# 4. Platform and Technology

## Platform
- Windows Desktop

## Programming Language
- Java

## Database
- SQLite


## Java GUI Libraries
- JavaFX

## SQLite Driver
- sqlite-jdbc

---

# 5. Application Concept

The application works as a personal academic organizer.

Users can:
- Add class schedules
- Manage assignments
- View deadlines
- Save lecturer contacts
- Receive task reminders

All data is stored locally in SQLite database.

---

# 6. Main Features

---

## 6.1 Class Schedule Management

This feature is used to manage class schedules.

### Functions:
- Add schedule
- Edit schedule
- Delete schedule
- View schedules

### Stored Data:
- Course name
- Day
- Start time
- End time
- Classroom
- Lecturer name

### Output:
- Daily and weekly schedule list

---

## 6.2 Assignment Management

This feature is used to manage assignments.

### Functions:
- Add assignment
- Edit assignment
- Delete assignment
- Mark assignment as completed

### Stored Data:
- Assignment title
- Course name
- Assignment description
- Deadline
- Assignment status

### Output:
- Active assignments list
- Completed assignments history

---

## 6.3 Deadline Reminder

The reminder feature helps users remember assignment deadlines and class schedules.

### Functions:
- Display automatic reminders
- Show deadline notifications
- Remind upcoming class schedules

### Output:
- Reminder popups
- Deadline warning notifications

---

## 6.4 Lecturer Contact Management

This feature stores lecturer information.

### Functions:
- Add lecturer contact
- Edit lecturer contact
- Delete lecturer contact

### Stored Data:
- Lecturer name
- Phone number
- Email
- Course taught

### Output:
- Lecturer contact list

---

# 7. Application Menu Structure

```text
ClassMate
│
├── Dashboard
│
├── Class Schedule
│   ├── Add Schedule
│   ├── Edit Schedule
│   ├── Delete Schedule
│   └── View Schedule
│
├── Assignments
│   ├── Add Assignment
│   ├── Edit Assignment
│   ├── Delete Assignment
│   └── Assignment Status
│
├── Reminders
│   ├── Deadline Reminder
│   └── Schedule Reminder
│
├── Lecturer Contacts
│   ├── Add Contact
│   ├── Edit Contact
│   ├── Delete Contact
│   └── Contact List
│
└── Logout / Exit
```

---

# 8. System Workflow

## User Flow

1. User opens the application
2. Dashboard is displayed
3. User adds class schedules
4. User adds assignments
5. System stores data into SQLite database
6. System monitors deadlines
7. Reminder notifications appear before deadlines
8. User can access lecturer contacts anytime

---

# 9. User Interface Design

## UI Concept

The application design focuses on:
- Minimalist appearance
- Modern interface
- Clean layout
- Easy navigation

## Main Pages

### Dashboard
Displays:
- Today's schedules
- Upcoming deadlines
- Total active assignments

### Schedule Page
Displays class schedules in table format.

### Assignment Page
Displays assignment list and completion status.

### Reminder Page
Displays upcoming reminders and deadlines.

### Lecturer Contact Page
Displays lecturer contact information.

---

# 10. SQLite Database Structure

The application uses a local SQLite database.

Database name:
classmate.db

---

# 11. Database Tables

## Student Table

Stores user information.

Columns:
- id_mahasiswa
- nama
- nim
- email
- password

---

## Schedule Table

Stores class schedule information.

Columns:
- id_jadwal
- mata_kuliah
- hari
- jam_mulai
- jam_selesai
- ruangan
- dosen_pengajar

---

## Assignment Table

Stores assignment information.

Columns:
- id_tugas
- nama_tugas
- deskripsi
- deadline
- status

---

## Lecturer Table

Stores lecturer contact information.

Columns:
- id_dosen
- nama_dosen
- email
- no_hp
- mata_kuliah

---

## Reminder Table

Stores reminder information.

Columns:
- id_reminder
- pesan
- waktu_reminder
- status_reminder

---

# 12. System Requirements

## Software Requirements
- Java JDK 17+
- SQLite
- VS Code / IntelliJ IDEA
- SQLite JDBC Driver

## Minimum Hardware Requirements
- 4 GB RAM
- 500 MB Storage
- Windows 10/11

---

# 13. Future Development

Possible future improvements:

- Multi-user login
- Cloud synchronization
- Academic calendar integration
- PDF export feature
- Dark mode
- Sound reminders
- Campus email integration

---

# 14. Conclusion

ClassMate is a Java and SQLite-based desktop application designed to help students manage academic activities in a more organized way.

The application provides several main features:
- Schedule management
- Assignment management
- Deadline reminders
- Lecturer contact management

With offline support and a simple interface, ClassMate can function as a personal academic assistant for students.