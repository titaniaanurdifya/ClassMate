package com.classmate.ui;

import com.classmate.dao.JadwalDAO;
import com.classmate.dao.TugasDAO;
import com.classmate.model.Jadwal;
import com.classmate.model.Tugas;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DashboardController {
    private final JadwalDAO jadwalDAO = new JadwalDAO();
    private final TugasDAO tugasDAO   = new TugasDAO();

    private VBox view;
    private final MainApp app;

    // Refreshable references
    private Label nextClassCourse;
    private Label nextClassDosen;
    private Label nextClassRoom;
    private Label nextClassTime;
    private VBox  deadlineList;
    private Label deadlineCountBadge;
    private Label pendingCountLabel;
    private Label totalCountLabel;
    private Button detailBtn;
    private Button addTaskBtn;

    public DashboardController(MainApp app) {
        this.app = app;
        buildView();
    }

    private void buildView() {
        view = new VBox(20);
        view.setPadding(new Insets(32, 32, 32, 32));
        view.getStyleClass().add("content-area");

        // ---- Top bar ----
        HBox topBar = buildTopBar();

        // ---- Row 1: Next Class (wide yellow) + Deadline (pink) ----
        HBox row1 = new HBox(16);
        row1.setFillHeight(true);

        VBox nextClassCard = buildNextClassCard();
        HBox.setHgrow(nextClassCard, Priority.ALWAYS);

        VBox deadlineCard = buildDeadlineCard();
        deadlineCard.setPrefWidth(280);
        deadlineCard.setMinWidth(260);
        deadlineCard.setMaxWidth(310);

        row1.getChildren().addAll(nextClassCard, deadlineCard);

        // ---- Row 2: Progress (blue) + Dosen (white) + Catatan Cepat (green) ----
        HBox row2 = new HBox(16);
        row2.setFillHeight(true);
        row2.setPrefHeight(160);

        VBox progressCard = buildProgressCard();
        progressCard.setPrefWidth(260);
        progressCard.setCursor(javafx.scene.Cursor.HAND);
        progressCard.setOnMouseClicked(e -> app.showTugas());

        VBox dosenCard = buildDosenCard();
        HBox.setHgrow(dosenCard, Priority.ALWAYS);
        dosenCard.setCursor(javafx.scene.Cursor.HAND);
        dosenCard.setOnMouseClicked(e -> app.showDosen());

        VBox noteCard = buildNoteCard();
        HBox.setHgrow(noteCard, Priority.ALWAYS);
        noteCard.setCursor(javafx.scene.Cursor.HAND);
        noteCard.setOnMouseClicked(e -> app.showReminder());

        row2.getChildren().addAll(progressCard, dosenCard, noteCard);

        view.getChildren().addAll(topBar, row1, row2);
    }

    // ======================== TOP BAR ========================
    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);

        VBox textBox = new VBox(2);
        Label greeting = new Label("ClassMate 🎓");
        greeting.getStyleClass().add("top-bar-greeting");
        Label sub = new Label("Semangat kuliahnya hari ini! ✨");
        sub.getStyleClass().add("top-bar-sub");
        textBox.getChildren().addAll(greeting, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Date box
        LocalDate today = LocalDate.now();
        Locale idLocale = new Locale("id", "ID");
        String dayName  = today.getDayOfWeek().getDisplayName(TextStyle.FULL, idLocale).toUpperCase();
        String dateStr  = today.format(DateTimeFormatter.ofPattern("d MMM yyyy", idLocale));

        VBox dateBox = new VBox(2);
        dateBox.setAlignment(Pos.CENTER_RIGHT);
        dateBox.getStyleClass().add("top-bar-date-box");

        Label dayLabel  = new Label(dayName);
        dayLabel.getStyleClass().add("top-bar-day");
        Label dateLabel = new Label(dateStr);
        dateLabel.getStyleClass().add("top-bar-date");

        dateBox.getChildren().addAll(dayLabel, dateLabel);

        bar.getChildren().addAll(textBox, spacer, dateBox);
        return bar;
    }

    // ======================== NEXT CLASS CARD (Yellow) ========================
    private VBox buildNextClassCard() {
        VBox card = new VBox();
        card.getStyleClass().add("card-yellow");
        card.setPadding(new Insets(24, 24, 20, 24));
        card.setMinHeight(200);

        // Badge
        Label badge = new Label("KELAS BERIKUTNYA");
        badge.getStyleClass().add("dash-next-badge");

        // Course title
        nextClassCourse = new Label("—");
        nextClassCourse.getStyleClass().add("dash-course-title");
        nextClassCourse.setWrapText(true);

        // Dosen
        nextClassDosen = new Label("");
        nextClassDosen.getStyleClass().add("dash-course-sub");

        // Decorative circle (top-right)
        Region topDecoBox = new Region();
        HBox.setHgrow(topDecoBox, Priority.ALWAYS);

        HBox badgeRow = new HBox(topDecoBox, badge);
        badgeRow.setAlignment(Pos.CENTER_RIGHT);
        // Actually put badge top-left
        HBox badgeRowLeft = new HBox();
        badgeRowLeft.getChildren().add(badge);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Bottom row: tags + button
        nextClassRoom = new Label("📍 —");
        nextClassRoom.getStyleClass().add("dash-meta-tag");

        nextClassTime = new Label("🕐 —");
        nextClassTime.getStyleClass().add("dash-meta-tag");

        detailBtn = new Button("Detail");
        detailBtn.getStyleClass().add("dash-detail-btn");
        detailBtn.setOnAction(e -> app.showJadwal());

        Region spacerH = new Region();
        HBox.setHgrow(spacerH, Priority.ALWAYS);

        HBox bottomRow = new HBox(10, nextClassRoom, nextClassTime, spacerH, detailBtn);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(badgeRowLeft, nextClassCourse, nextClassDosen, spacer, bottomRow);
        return card;
    }

    // ======================== DEADLINE CARD (Pink) ========================
    private VBox buildDeadlineCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card-pink");
        card.setPadding(new Insets(22, 20, 20, 20));
        card.setAlignment(Pos.TOP_LEFT);

        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Deadline ⏳");
        title.getStyleClass().add("dash-deadline-title");
        titleRow.getChildren().add(title);

        // List area
        deadlineList = new VBox(8);
        deadlineList.setAlignment(Pos.TOP_LEFT);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        addTaskBtn = new Button("＋ TUGAS BARU");
        addTaskBtn.getStyleClass().add("dash-add-task-btn");
        addTaskBtn.setOnAction(e -> app.showTugasAndAdd());
        VBox.setMargin(addTaskBtn, new Insets(8, 0, 0, 0));

        card.getChildren().addAll(titleRow, deadlineList, spacer, addTaskBtn);
        return card;
    }

    // ======================== PROGRESS CARD (Blue) ========================
    private VBox buildProgressCard() {
        VBox card = new VBox(10);
        card.getStyleClass().add("card-blue");
        card.setPadding(new Insets(22, 20, 20, 20));
        card.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Progress Tugas");
        title.getStyleClass().add("dash-stat-card-title");

        pendingCountLabel = new Label("0%");
        pendingCountLabel.getStyleClass().add("dash-stat-card-value");

        totalCountLabel = new Label("0 / 0 tugas selesai");
        totalCountLabel.getStyleClass().add("dash-stat-card-sub");

        card.getChildren().addAll(title, pendingCountLabel, totalCountLabel);
        return card;
    }

    // ======================== DOSEN CARD (White) ========================
    private VBox buildDosenCard() {
        VBox card = new VBox(8);
        card.getStyleClass().add("card-white");
        card.setPadding(new Insets(22, 20, 20, 20));
        card.setAlignment(Pos.CENTER_LEFT);

        // Avatar circle
        StackPane avatar = new StackPane();
        Circle circle = new Circle(24, Color.web("#dbeafe"));
        Label avatarLabel = new Label("D");
        avatarLabel.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill:#2563eb;");
        avatar.getChildren().addAll(circle, avatarLabel);

        VBox textBox = new VBox(2);
        Label name = new Label("Dosen");
        name.setStyle("-fx-font-size:15px; -fx-font-weight:bold; -fx-text-fill:#1e1b2e;");
        Label sub = new Label("DAFTAR KONTAK");
        sub.setStyle("-fx-font-size:10px; -fx-text-fill:#9ca3af; -fx-font-weight:bold;");
        textBox.getChildren().addAll(name, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("→");
        arrow.setStyle("-fx-font-size:18px; -fx-text-fill:#9ca3af;");

        HBox row = new HBox(12, avatar, textBox, spacer, arrow);
        row.setAlignment(Pos.CENTER_LEFT);
        card.getChildren().add(row);
        return card;
    }

    // ======================== NOTE CARD (Green) ========================
    private VBox buildNoteCard() {
        VBox card = new VBox(8);
        card.getStyleClass().add("card-green");
        card.setPadding(new Insets(22, 20, 20, 20));
        card.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("📋 Catatan Cepat");
        title.getStyleClass().add("dash-note-title");

        Label hint = new Label("Ada ide apa hari ini?");
        hint.getStyleClass().add("dash-note-hint");

        card.getChildren().addAll(title, hint);
        return card;
    }

    // ======================== REFRESH ========================
    public void refresh() {
        LocalDate today   = LocalDate.now();
        Locale idLocale   = new Locale("id", "ID");
        String todayName  = today.getDayOfWeek().getDisplayName(TextStyle.FULL, idLocale);

        // Next class today
        List<Jadwal> todayClasses = jadwalDAO.getByHari(todayName);
        if (todayClasses.isEmpty()) {
            nextClassCourse.setText("Tidak ada kelas hari ini 🎉");
            nextClassDosen.setText("");
            nextClassRoom.setText("📍 —");
            nextClassTime.setText("🕐 —");
        } else {
            Jadwal j = todayClasses.get(0);
            nextClassCourse.setText(j.getMataKuliah());
            nextClassDosen.setText("Dosen: " + j.getDosenPengajar());
            nextClassRoom.setText("📍 " + (j.getRuangan() != null && !j.getRuangan().isEmpty() ? j.getRuangan() : "—"));
            nextClassTime.setText("🕐 " + j.getJamMulai());
        }

        // Deadlines (upcoming 7 days)
        List<Tugas> upcoming = tugasDAO.getUpcomingDeadlines(7);
        deadlineList.getChildren().clear();
        if (upcoming.isEmpty()) {
            Label emoji  = new Label("☕");
            emoji.setStyle("-fx-font-size:28px;");
            emoji.setAlignment(Pos.CENTER);

            Label msg = new Label("Hooray! Tugas aman.\nWaktunya santai.");
            msg.getStyleClass().add("dash-no-deadline");
            msg.setWrapText(true);
            msg.setAlignment(Pos.CENTER);

            VBox center = new VBox(6, emoji, msg);
            center.setAlignment(Pos.CENTER);
            center.setPadding(new Insets(10, 0, 6, 0));
            deadlineList.getChildren().add(center);
        } else {
            for (Tugas t : upcoming) {
                HBox row = new HBox(8);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(4, 8, 4, 8));
                row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius:8;");

                Label dot = new Label("•");
                dot.setStyle("-fx-text-fill:#be185d; -fx-font-size:16px;");

                VBox info = new VBox(1);
                Label tName = new Label(t.getNamaTugas());
                tName.setStyle("-fx-font-size:12px; -fx-font-weight:bold; -fx-text-fill:#9d174d;");
                Label tDate = new Label(t.getDeadline());
                tDate.setStyle("-fx-font-size:10px; -fx-text-fill:#be185d;");
                info.getChildren().addAll(tName, tDate);

                row.getChildren().addAll(dot, info);
                deadlineList.getChildren().add(row);
            }
        }

        // Progress
        int total     = tugasDAO.getAll().size();
        int completed = (int) tugasDAO.getAll().stream().filter(t -> "Selesai".equals(t.getStatus())).count();
        int pct       = total == 0 ? 0 : (int) Math.round((double) completed / total * 100);

        pendingCountLabel.setText(pct + "%");
        totalCountLabel.setText(completed + " / " + total + " tugas selesai");
    }

    public VBox getView() { return view; }
}
