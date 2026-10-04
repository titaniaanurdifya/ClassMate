package com.classmate.ui;

import com.classmate.dao.JadwalDAO;
import com.classmate.model.Jadwal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JadwalController {

    private final JadwalDAO dao = new JadwalDAO();
    private VBox view;
    private TableView<Jadwal> table;
    private ObservableList<Jadwal> data;

    private static final List<String> DAYS = Arrays.asList(
        "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
    );

    public JadwalController() {
        buildView();
    }

    @SuppressWarnings("unchecked")
    private void buildView() {
        view = new VBox(20);
        view.setPadding(new Insets(32));
        view.getStyleClass().add("content-area");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Jadwal Kuliah");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("＋  Tambah Jadwal");
        addBtn.getStyleClass().addAll("btn-primary");
        addBtn.setOnAction(e -> showForm(null));

        header.getChildren().addAll(title, spacer, addBtn);

        // Table
        table = new TableView<>();
        table.getStyleClass().add("data-table");
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Jadwal, String> colCourse = col("Mata Kuliah", "mataKuliah", 200);
        TableColumn<Jadwal, String> colDay    = col("Hari", "hari", 100);
        TableColumn<Jadwal, String> colStart  = col("Mulai", "jamMulai", 80);
        TableColumn<Jadwal, String> colEnd    = col("Selesai", "jamSelesai", 80);
        TableColumn<Jadwal, String> colRoom   = col("Ruangan", "ruangan", 100);
        TableColumn<Jadwal, String> colLect   = col("Dosen", "dosenPengajar", 160);

        TableColumn<Jadwal, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(130);
        colAction.setCellFactory(tc -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn  = new Button("Delete");
            {
                editBtn.setText("Ubah");
                delBtn.setText("Hapus");
                editBtn.setOnAction(e -> showForm(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> deleteItem(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(6, editBtn, delBtn));
            }
        });

        table.getColumns().addAll(colCourse, colDay, colStart, colEnd, colRoom, colLect, colAction);
        data = FXCollections.observableArrayList();
        table.setItems(data);

        view.getChildren().addAll(header, table);
    }

    private <T> TableColumn<Jadwal, T> col(String label, String property, int width) {
        TableColumn<Jadwal, T> col = new TableColumn<>(label);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }

    public void refresh() {
        data.setAll(dao.getAll());
    }

    private void showForm(Jadwal existing) {
        Dialog<Jadwal> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Tambah Jadwal" : "Ubah Jadwal");
        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/com/classmate/styles/app.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        TextField tfCourse = styledField("misal: Kalkulus");
        ComboBox<String> cbDay = new ComboBox<>(FXCollections.observableArrayList(DAYS));
        cbDay.getStyleClass().add("styled-combo");
        cbDay.setMaxWidth(Double.MAX_VALUE);
        TextField tfStart  = styledField("HH:MM");
        TextField tfEnd    = styledField("HH:MM");
        TextField tfRoom   = styledField("misal: R.204");
        TextField tfLect   = styledField("Nama dosen");

        if (existing != null) {
            tfCourse.setText(existing.getMataKuliah());
            cbDay.setValue(existing.getHari());
            tfStart.setText(existing.getJamMulai());
            tfEnd.setText(existing.getJamSelesai());
            tfRoom.setText(existing.getRuangan());
            tfLect.setText(existing.getDosenPengajar());
        }

        grid.addRow(0, label("Nama Matkul *"), tfCourse);
        grid.addRow(1, label("Hari *"), cbDay);
        grid.addRow(2, label("Jam Mulai *"), tfStart);
        grid.addRow(3, label("Jam Selesai *"), tfEnd);
        grid.addRow(4, label("Ruangan"), tfRoom);
        grid.addRow(5, label("Dosen"), tfLect);

        GridPane.setHgrow(tfCourse, Priority.ALWAYS);
        GridPane.setHgrow(cbDay, Priority.ALWAYS);
        GridPane.setHgrow(tfStart, Priority.ALWAYS);
        GridPane.setHgrow(tfEnd, Priority.ALWAYS);
        GridPane.setHgrow(tfRoom, Priority.ALWAYS);
        GridPane.setHgrow(tfLect, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);
        ButtonType saveBtn = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Batal");

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                Jadwal j = existing == null ? new Jadwal() : existing;
                j.setMataKuliah(tfCourse.getText().trim());
                j.setHari(cbDay.getValue() == null ? "" : cbDay.getValue());
                j.setJamMulai(tfStart.getText().trim());
                j.setJamSelesai(tfEnd.getText().trim());
                j.setRuangan(tfRoom.getText().trim());
                j.setDosenPengajar(tfLect.getText().trim());
                return j;
            }
            return null;
        });

        Optional<Jadwal> result = dialog.showAndWait();
        result.ifPresent(j -> {
            if (j.getMataKuliah().isEmpty() || j.getHari().isEmpty()) {
                showAlert("Nama matkul dan hari wajib diisi.");
                return;
            }
            if (existing == null) dao.insert(j);
            else dao.update(j);
            refresh();
        });
    }

    private void deleteItem(Jadwal j) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus jadwal untuk \"" + j.getMataKuliah() + "\"?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi Hapus");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Ya");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText("Tidak");
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/com/classmate/styles/app.css").toExternalForm()
        );
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) { dao.delete(j.getIdJadwal()); refresh(); }
        });
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("styled-field");
        return tf;
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("form-label");
        return l;
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    public VBox getView() { return view; }
}
