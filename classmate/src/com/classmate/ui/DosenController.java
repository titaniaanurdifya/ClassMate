package com.classmate.ui;

import com.classmate.dao.DosenDAO;
import com.classmate.model.Dosen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.Optional;

public class DosenController {

    private final DosenDAO dao = new DosenDAO();
    private VBox view;
    private TableView<Dosen> table;
    private ObservableList<Dosen> data;

    public DosenController() {
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

        Label title = new Label("Daftar Dosen");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("＋  Tambah Dosen");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> showForm(null));
        header.getChildren().addAll(title, spacer, addBtn);

        // Table
        table = new TableView<>();
        table.getStyleClass().add("data-table");
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Dosen, String> colName    = col("Nama", "namaDosen", 180);
        TableColumn<Dosen, String> colEmail   = col("Email", "email", 200);
        TableColumn<Dosen, String> colPhone   = col("Telepon", "noHp", 130);
        TableColumn<Dosen, String> colCourse  = col("Mata Kuliah", "mataKuliah", 180);

        TableColumn<Dosen, Void> colAction = new TableColumn<>("Aksi");
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

        table.getColumns().addAll(colName, colEmail, colPhone, colCourse, colAction);
        data = FXCollections.observableArrayList();
        table.setItems(data);

        view.getChildren().addAll(header, table);
    }

    public void refresh() {
        data.setAll(dao.getAll());
    }

    private void showForm(Dosen existing) {
        Dialog<Dosen> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Tambah Dosen" : "Ubah Dosen");
        dialog.getDialogPane().getStylesheets().add(
            getClass().getResource("/com/classmate/styles/app.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(12);
        grid.setPadding(new Insets(20));

        TextField tfName   = styledField("Nama lengkap");
        TextField tfEmail  = styledField("email@universitas.ac.id");
        TextField tfPhone  = styledField("misal: 0812-xxxx-xxxx");
        TextField tfCourse = styledField("Mata kuliah yang diampu");

        if (existing != null) {
            tfName.setText(existing.getNamaDosen());
            tfEmail.setText(existing.getEmail());
            tfPhone.setText(existing.getNoHp());
            tfCourse.setText(existing.getMataKuliah());
        }

        grid.addRow(0, formLabel("Nama *"), tfName);
        grid.addRow(1, formLabel("Email"), tfEmail);
        grid.addRow(2, formLabel("Telepon"), tfPhone);
        grid.addRow(3, formLabel("Mata Kuliah"), tfCourse);

        for (int i = 0; i < 4; i++) GridPane.setHgrow(grid.getChildren().get(i * 2 + 1), Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);
        ButtonType saveBtn = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Batal");

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                Dosen d = existing == null ? new Dosen() : existing;
                d.setNamaDosen(tfName.getText().trim());
                d.setEmail(tfEmail.getText().trim());
                d.setNoHp(tfPhone.getText().trim());
                d.setMataKuliah(tfCourse.getText().trim());
                return d;
            }
            return null;
        });

        Optional<Dosen> result = dialog.showAndWait();
        result.ifPresent(d -> {
            if (d.getNamaDosen().isEmpty()) { new Alert(Alert.AlertType.WARNING, "Nama wajib diisi.").showAndWait(); return; }
            if (existing == null) dao.insert(d);
            else dao.update(d);
            refresh();
        });
    }

    private void deleteItem(Dosen d) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus dosen \"" + d.getNamaDosen() + "\"?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi Hapus");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Ya");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText("Tidak");
        alert.getDialogPane().getStylesheets().add(
            getClass().getResource("/com/classmate/styles/app.css").toExternalForm()
        );
        alert.showAndWait().ifPresent(btn -> { if (btn == ButtonType.YES) { dao.delete(d.getIdDosen()); refresh(); } });
    }

    private <T> TableColumn<Dosen, T> col(String label, String property, int width) {
        TableColumn<Dosen, T> col = new TableColumn<>(label);
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
