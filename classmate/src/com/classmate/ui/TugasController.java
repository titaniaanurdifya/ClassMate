package com.classmate.ui;

import com.classmate.dao.JadwalDAO;
import com.classmate.dao.TugasDAO;
import com.classmate.model.Jadwal;
import com.classmate.model.Tugas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TugasController {

    private final TugasDAO tugasDAO = new TugasDAO();
    private final JadwalDAO jadwalDAO = new JadwalDAO();

    private VBox view;
    private TableView<Tugas> table;
    private ObservableList<Tugas> data;
    private ToggleButton btnAll, btnPending, btnDone;

    public TugasController() {
        buildView();
    }

    @SuppressWarnings("unchecked")
    private void buildView() {
        view = new VBox(20);
        view.setPadding(new Insets(32));
        view.getStyleClass().add("content-area");

        // Header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Daftar Tugas");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("＋  Tambah Tugas");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> showForm(null));
        header.getChildren().addAll(title, spacer, addBtn);

        // Filter tabs
        HBox tabs = new HBox(8);
        btnAll     = filterTab("Semua");
        btnPending = filterTab("Belum Selesai");
        btnDone    = filterTab("Selesai");

        btnAll.setSelected(true);
        btnAll.getStyleClass().add("tab-active");

        btnAll.setOnAction(e -> applyFilter("All"));
        btnPending.setOnAction(e -> applyFilter("Pending"));
        btnDone.setOnAction(e -> applyFilter("Completed"));
        tabs.getChildren().addAll(btnAll, btnPending, btnDone);

        // Table
        table = new TableView<>();
        table.getStyleClass().add("data-table");
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Color row factory: overdue = red tint, completed = green tint
        table.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Tugas item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("row-overdue", "row-completed");
                if (!empty && item != null) {
                    if ("Selesai".equals(item.getStatus())) {
                        getStyleClass().add("row-completed");
                    } else if (item.getDeadline() != null && !item.getDeadline().isEmpty()) {
                        try {
                            LocalDate dl = LocalDate.parse(item.getDeadline());
                            if (dl.isBefore(LocalDate.now())) getStyleClass().add("row-overdue");
                        } catch (Exception ignored) {}
                    }
                }
            }
        });

        TableColumn<Tugas, String> colName     = col("Tugas", "namaTugas", 200);
        TableColumn<Tugas, String> colDesc     = col("Deskripsi", "deskripsi", 200);
        TableColumn<Tugas, String> colDeadline = col("Batas Waktu", "deadline", 110);
        TableColumn<Tugas, String> colStatus   = col("Status", "status", 100);

        TableColumn<Tugas, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(200);
        colAction.setCellFactory(tc -> new TableCell<>() {
            private final Button editBtn     = new Button("Ubah");
            private final Button completeBtn = new Button("✓ Selesai");
            private final Button delBtn      = new Button("Hapus");
            {
                editBtn.getStyleClass().add("btn-edit");
                completeBtn.getStyleClass().add("btn-success");
                delBtn.getStyleClass().add("btn-delete");
                editBtn.setOnAction(e -> showForm(getTableView().getItems().get(getIndex())));
                completeBtn.setOnAction(e -> markComplete(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> deleteItem(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Tugas t = getTableView().getItems().get(getIndex());
                HBox box = new HBox(6, editBtn);
                if (!"Selesai".equals(t.getStatus())) box.getChildren().add(completeBtn);
                box.getChildren().add(delBtn);
                setGraphic(box);
            }
        });

        table.getColumns().addAll(colName, colDesc, colDeadline, colStatus, colAction);
        data = FXCollections.observableArrayList();
        table.setItems(data);

        view.getChildren().addAll(header, tabs, table);
    }

    private ToggleButton filterTab(String label) {
        ToggleButton btn = new ToggleButton(label);
        btn.getStyleClass().add("tab-btn");
        return btn;
    }

    private void applyFilter(String filter) {
        btnAll.getStyleClass().remove("tab-active");
        btnPending.getStyleClass().remove("tab-active");
        btnDone.getStyleClass().remove("tab-active");

        List<Tugas> all = tugasDAO.getAll();
        switch (filter) {
            case "Belum Selesai" -> { btnPending.getStyleClass().add("tab-active"); data.setAll(all.stream().filter(t -> "Belum Selesai".equals(t.getStatus())).collect(Collectors.toList())); }
            case "Selesai"   -> { btnDone.getStyleClass().add("tab-active");    data.setAll(all.stream().filter(t -> "Selesai".equals(t.getStatus())).collect(Collectors.toList())); }
            default          -> { btnAll.getStyleClass().add("tab-active");     data.setAll(all); }
        }
    }

    public void refresh() {
        data.setAll(tugasDAO.getAll());
    }

    public void showForm(Tugas existing) {
        Dialog<Tugas> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Tambah Tugas" : "Ubah Tugas");
        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/com/classmate/styles/app.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12);
        grid.setPadding(new Insets(20));

        TextField tfName = styledField("Judul tugas");
        TextArea  taDesc = new TextArea();
        taDesc.setPromptText("Deskripsi (opsional)");
        taDesc.setPrefRowCount(3);
        taDesc.getStyleClass().add("styled-field");

        DatePicker dpDeadline = new DatePicker();
        dpDeadline.getStyleClass().add("styled-field");

        ComboBox<String> cbStatus = new ComboBox<>(FXCollections.observableArrayList("Belum Selesai", "Selesai"));
        cbStatus.getStyleClass().add("styled-combo");
        cbStatus.setMaxWidth(Double.MAX_VALUE);

        // Load schedules for linking
        List<Jadwal> schedules = jadwalDAO.getAll();
        ComboBox<Jadwal> cbJadwal = new ComboBox<>(FXCollections.observableArrayList(schedules));
        cbJadwal.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Jadwal j) { return j == null ? "Tidak ada" : j.getMataKuliah() + " (" + j.getHari() + ")"; }
            @Override public Jadwal fromString(String s) { return null; }
        });
        cbJadwal.getStyleClass().add("styled-combo");
        cbJadwal.setMaxWidth(Double.MAX_VALUE);

        if (existing != null) {
            tfName.setText(existing.getNamaTugas());
            taDesc.setText(existing.getDeskripsi());
            cbStatus.setValue(existing.getStatus());
            if (existing.getDeadline() != null && !existing.getDeadline().isEmpty()) {
                try { dpDeadline.setValue(LocalDate.parse(existing.getDeadline())); } catch (Exception ignored) {}
            }
            schedules.stream().filter(j -> j.getIdJadwal() == existing.getIdJadwal()).findFirst().ifPresent(cbJadwal::setValue);
        } else {
            cbStatus.setValue("Belum Selesai");
        }

        grid.addRow(0, formLabel("Judul *"), tfName);
        grid.addRow(1, formLabel("Deskripsi"), taDesc);
        grid.addRow(2, formLabel("Batas Waktu"), dpDeadline);
        grid.addRow(3, formLabel("Status"), cbStatus);
        grid.addRow(4, formLabel("Hubungkan ke Jadwal"), cbJadwal);

        for (int i = 0; i < 5; i++) {
            GridPane.setHgrow(grid.getChildren().get(i * 2 + 1), Priority.ALWAYS);
        }

        dialog.getDialogPane().setContent(grid);
        ButtonType saveBtn = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Batal");

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                Tugas t = existing == null ? new Tugas() : existing;
                t.setNamaTugas(tfName.getText().trim());
                t.setDeskripsi(taDesc.getText().trim());
                t.setDeadline(dpDeadline.getValue() != null ? dpDeadline.getValue().toString() : "");
                t.setStatus(cbStatus.getValue());
                t.setIdJadwal(cbJadwal.getValue() != null ? cbJadwal.getValue().getIdJadwal() : 0);
                return t;
            }
            return null;
        });

        Optional<Tugas> result = dialog.showAndWait();
        result.ifPresent(t -> {
            if (t.getNamaTugas().isEmpty()) { new Alert(Alert.AlertType.WARNING, "Judul wajib diisi.").showAndWait(); return; }
            if (existing == null) tugasDAO.insert(t);
            else tugasDAO.update(t);
            refresh();
        });
    }

    private void markComplete(Tugas t) {
        tugasDAO.markComplete(t.getIdTugas());
        refresh();
    }

    private void deleteItem(Tugas t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus \"" + t.getNamaTugas() + "\"?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi Hapus");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Ya");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText("Tidak");
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/com/classmate/styles/app.css").toExternalForm()
        );
        alert.showAndWait().ifPresent(btn -> { if (btn == ButtonType.YES) { tugasDAO.delete(t.getIdTugas()); refresh(); } });
    }

    private <T> TableColumn<Tugas, T> col(String label, String property, int width) {
        TableColumn<Tugas, T> col = new TableColumn<>(label);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("styled-field");
        return tf;
    }

    private Label formLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("form-label");
        return l;
    }

    public VBox getView() { return view; }
}
