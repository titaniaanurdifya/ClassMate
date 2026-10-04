# Data Flow Diagram (DFD) – ClassMate Application

## 1. Context Diagram

```mermaid
flowchart LR

    A[Student]
    B((ClassMate System))

    A -->|Input Course Schedule| B
    A -->|Input Assignments| B
    A -->|Input Lecturer Contacts| B
    A -->|Reminder Request| B

    B -->|Schedule Information| A
    B -->|Assignment Information| A
    B -->|Deadline Notifications| A
    B -->|Lecturer Contact Data| A
```

---

# 2. DFD Level 1

```mermaid
flowchart LR

    %% External Entity
    A[Student]

    %% Processes
    P1((1.0 Manage Course Schedule))
    P2((2.0 Manage Assignments))
    P3((3.0 Manage Lecturer Contacts))
    P4((4.0 Reminder & Notifications))

    %% Data Stores
    D1[(Schedule Data)]
    D2[(Assignment Data)]
    D3[(Lecturer Data)]
    D4[(Reminder Data)]

    %% Schedule Flow
    A -->|Add/Edit Schedule| P1
    P1 -->|Save Schedule| D1
    D1 -->|Schedule Data| P1
    P1 -->|Display Schedule| A

    %% Assignment Flow
    A -->|Add/Edit Assignments| P2
    P2 -->|Save Assignments| D2
    D2 -->|Assignment Data| P2
    P2 -->|Assignment Information| A

    %% Lecturer Contact Flow
    A -->|Add/Edit Contacts| P3
    P3 -->|Save Contacts| D3
    D3 -->|Lecturer Data| P3
    P3 -->|Contact Information| A

    %% Reminder Flow
    D1 -->|Course Schedule| P4
    D2 -->|Assignment Deadlines| P4
    P4 -->|Save Reminders| D4
    P4 -->|Deadline Notifications| A
```

---

# DFD Explanation

## External Entity

### Student
The main user of the application who inputs and receives information from the system.

---

## Processes

### 1.0 Manage Course Schedule
Handles course schedule data:
- Add schedule
- Edit schedule
- Delete schedule
- View schedule

### 2.0 Manage Assignments
Handles assignment data:
- Add assignments
- Edit assignments
- Mark assignments as completed
- Delete assignments

### 3.0 Manage Lecturer Contacts
Handles lecturer information:
- Save contacts
- Edit contacts
- Delete contacts

### 4.0 Reminder & Notifications
Sends reminders for:
- Assignment deadlines
- Class schedules
- Important notifications

---

## Data Stores

### Schedule Data
Stores all student course schedules.

### Assignment Data
Stores all assignment data and deadlines.

### Lecturer Data
Stores lecturer contact information.

### Reminder Data
Stores reminder and notification data.