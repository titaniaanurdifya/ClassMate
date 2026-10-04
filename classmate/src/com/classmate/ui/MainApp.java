package com.classmate.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp {

    private BorderPane root;
    private StackPane contentArea;
    private Button activeBtn;

    // Panel instances (lazy loaded)
    private DashboardController dashboardCtrl;
    private JadwalController jadwalCtrl;
    private TugasController tugasCtrl;
    private ReminderController reminderCtrl;
    private DosenController dosenCtrl;

    public void start(Stage stage) {
        showSplashScreen(stage);
    }

    private void showSplashScreen(Stage mainStage) {
        Stage splashStage = new Stage();
        splashStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        ImageView splashLogo = new ImageView(new Image(getClass().getResourceAsStream("/com/classmate/images/logo.png")));
        splashLogo.setFitHeight(300);
        splashLogo.setPreserveRatio(true);

        VBox layout = new VBox(splashLogo);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white; -fx-padding: 40; -fx-background-radius: 20;");
        layout.setEffect(new javafx.scene.effect.DropShadow(20, Color.BLACK));

        Scene splashScene = new Scene(layout, Color.TRANSPARENT);
        splashStage.setScene(splashScene);
        splashStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        splashStage.show();

        // Pause for 3 seconds then show main app
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(e -> {
            splashStage.close();
            initializeMainApp(mainStage);
        });
        pause.play();
    }

    private void initializeMainApp(Stage stage) {
        root = new BorderPane();
        root.setLeft(buildSidebar());
        contentArea = new StackPane();
        root.setCenter(contentArea);

        // Load CSS
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/com/classmate/styles/app.css").toExternalForm());

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/classmate/images/logo.png")));
        stage.setTitle("ClassMate — Penyelenggara Akademik");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();

        // Show dashboard by default
        showDashboard();
    }

    private Button btnDashboard, btnJadwal, btnTugas, btnReminder, btnDosen;

    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("sidebar");

        // Logo header
        VBox header = new VBox(4);
        header.setPadding(new Insets(28, 20, 24, 20));
        header.getStyleClass().add("sidebar-header");

        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/com/classmate/images/logo.png")));
        logoView.setFitHeight(50);
        logoView.setPreserveRatio(true);

        Label tagline = new Label("Penyelenggara Akademik");
        tagline.getStyleClass().add("sidebar-tagline");

        header.getChildren().addAll(logoView, tagline);

        // Nav buttons
        VBox nav = new VBox(4);
        nav.setPadding(new Insets(8, 12, 8, 12));
        nav.setFillWidth(true);

        btnDashboard  = navButton("🏠", "Beranda");
        btnJadwal     = navButton("📅", "Jadwal Kuliah");
        btnTugas      = navButton("📝", "Daftar Tugas");
        btnReminder   = navButton("🔔", "Pengingat");
        btnDosen      = navButton("👤", "Daftar Dosen");

        btnDashboard.setOnAction(e -> showDashboard());
        btnJadwal.setOnAction(e -> showJadwal());
        btnTugas.setOnAction(e -> showTugas());
        btnReminder.setOnAction(e -> showReminder());
        btnDosen.setOnAction(e -> showDosen());

        nav.getChildren().addAll(btnDashboard, btnJadwal, btnTugas, btnReminder, btnDosen);

        // Spacer + footer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label version = new Label("v1.0  •  ClassMate");
        version.getStyleClass().add("sidebar-footer");
        VBox footer = new VBox(version);
        footer.setPadding(new Insets(12, 20, 20, 20));

        sidebar.getChildren().addAll(header, nav, spacer, footer);

        // Set initial active
        setActive(btnDashboard);

        return sidebar;
    }

    private Button navButton(String icon, String label) {
        Button btn = new Button(icon + "  " + label);
        btn.getStyleClass().add("nav-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    private void setActive(Button btn) {
        if (activeBtn != null) {
            activeBtn.getStyleClass().remove("nav-btn-active");
        }
        btn.getStyleClass().add("nav-btn-active");
        activeBtn = btn;
    }

    public void showDashboard() {
        setActive(btnDashboard);
        if (dashboardCtrl == null) dashboardCtrl = new DashboardController(this);
        contentArea.getChildren().setAll(dashboardCtrl.getView());
        dashboardCtrl.refresh();
    }

    public void showJadwal() {
        setActive(btnJadwal);
        if (jadwalCtrl == null) jadwalCtrl = new JadwalController();
        contentArea.getChildren().setAll(jadwalCtrl.getView());
        jadwalCtrl.refresh();
    }

    public void showTugas() {
        setActive(btnTugas);
        if (tugasCtrl == null) tugasCtrl = new TugasController();
        contentArea.getChildren().setAll(tugasCtrl.getView());
        tugasCtrl.refresh();
    }

    public void showTugasAndAdd() {
        showTugas();
        tugasCtrl.showForm(null);
    }

    public void showReminder() {
        setActive(btnReminder);
        if (reminderCtrl == null) reminderCtrl = new ReminderController();
        contentArea.getChildren().setAll(reminderCtrl.getView());
        reminderCtrl.refresh();
    }

    public void showDosen() {
        setActive(btnDosen);
        if (dosenCtrl == null) dosenCtrl = new DosenController();
        contentArea.getChildren().setAll(dosenCtrl.getView());
        dosenCtrl.refresh();
    }
}
