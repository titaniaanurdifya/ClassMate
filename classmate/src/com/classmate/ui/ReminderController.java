package com.classmate.ui;

import com.classmate.dao.ReminderDAO;
import com.classmate.model.Reminder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

public class ReminderController {

    private final ReminderDAO dao = new ReminderDAO();
    private VBox view;
    private TableView<Reminder> table;
    private ObservableList<Reminder> data;

    public ReminderController() {
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

        Label title = new Label("Pengingat");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button refreshBtn = new Button("🔄  Buat Otomatis");
        refreshBtn.getStyleClass().add("btn-secondary");
        refreshBtn.setOnAction(e -> { dao.autoGenerateReminders(7); refresh(); });

        header.getChildren().addAll(title, spacer, refreshBtn);

        // Info label
        Label info = new Label("Pengingat dibuat otomatis untuk tugas yang mendekati batas waktu (7 hari). Klik 'Buat Otomatis' untuk memperbarui.");
        info.getStyleClass().add("info-label");
        info.setWrapText(true);

        // Table
        table = new TableView<>();
        table.getStyleClass().add("data-table");
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Reminder, Integer> colId    = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idReminder"));
        colId.setPrefWidth(50);

        TableColumn<Reminder, String> colMsg    = col("Pesan", "pesan", 300);
        TableColumn<Reminder, String> colTime   = col("Tanggal Pengingat", "waktuReminder", 140);
        TableColumn<Reminder, String> colStatus = col("Status", "statusReminder", 100);

        TableColumn<Reminder, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(150);
        colAction.setCellFactory(tc -> new TableCell<>() {
            private final Button dismissBtn = new Button("Dismiss");
            private final Button deleteBtn  = new Button("Delete");
            {
                dismissBtn.setText("Selesaikan");
                deleteBtn.setText("Hapus");
                dismissBtn.setOnAction(e -> {
                    Reminder r = getTableView().getItems().get(getIndex());
                    dao.markDismissed(r.getIdReminder());
                    refresh();
                });
                deleteBtn.setOnAction(e -> {
                    Reminder r = getTableView().getItems().get(getIndex());
                    dao.delete(r.getIdReminder());
                    refresh();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Reminder r = getTableView().getItems().get(getIndex());
                HBox box = new HBox(6);
                if ("Aktif".equals(r.getStatusReminder())) box.getChildren().add(dismissBtn);
                box.getChildren().add(deleteBtn);
                setGraphic(box);
            }
        });

        // Row factory: dim dismissed reminders
        table.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Reminder item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("row-dismissed");
                if (!empty && item != null && "Selesai".equals(item.getStatusReminder())) {
                    getStyleClass().add("row-dismissed");
                }
            }
        });

        table.getColumns().addAll(colId, colMsg, colTime, colStatus, colAction);
        data = FXCollections.observableArrayList();
        table.setItems(data);

        view.getChildren().addAll(header, info, table);
    }

    public void refresh() {
        dao.autoGenerateReminders(7); // always auto-check on open
        data.setAll(dao.getAll());
    }

    private <T> TableColumn<Reminder, T> col(String label, String property, int width) {
        TableColumn<Reminder, T> col = new TableColumn<>(label);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }

    public VBox getView() { return view; }
}
