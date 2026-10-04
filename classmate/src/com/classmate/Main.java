package com.classmate;

import com.classmate.db.DatabaseHelper;
import com.classmate.ui.MainApp;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database on startup
        DatabaseHelper.getInstance().initializeDatabase();

        // Launch main window
        MainApp mainApp = new MainApp();
        mainApp.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
