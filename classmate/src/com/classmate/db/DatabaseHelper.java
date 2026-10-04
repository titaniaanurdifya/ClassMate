package com.classmate.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:classmate.db";
    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {}

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Mahasiswa (student) table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS mahasiswa (
                    id_mahasiswa INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama TEXT NOT NULL,
                    nim TEXT,
                    email TEXT,
                    password TEXT
                )
            """);

            // Jadwal (schedule) table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS jadwal (
                    id_jadwal INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_mahasiswa INTEGER DEFAULT 1,
                    mata_kuliah TEXT NOT NULL,
                    hari TEXT NOT NULL,
                    jam_mulai TEXT NOT NULL,
                    jam_selesai TEXT NOT NULL,
                    ruangan TEXT,
                    dosen_pengajar TEXT,
                    FOREIGN KEY(id_mahasiswa) REFERENCES mahasiswa(id_mahasiswa)
                )
            """);

            // Tugas (assignment) table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tugas (
                    id_tugas INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_mahasiswa INTEGER DEFAULT 1,
                    id_jadwal INTEGER,
                    nama_tugas TEXT NOT NULL,
                    deskripsi TEXT,
                    deadline TEXT,
                    status TEXT DEFAULT 'Pending',
                    FOREIGN KEY(id_mahasiswa) REFERENCES mahasiswa(id_mahasiswa),
                    FOREIGN KEY(id_jadwal) REFERENCES jadwal(id_jadwal)
                )
            """);

            // Dosen (lecturer) table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS dosen (
                    id_dosen INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama_dosen TEXT NOT NULL,
                    email TEXT,
                    no_hp TEXT,
                    mata_kuliah TEXT
                )
            """);

            // Reminder table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reminder (
                    id_reminder INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_tugas INTEGER,
                    pesan TEXT,
                    waktu_reminder TEXT,
                    status_reminder TEXT DEFAULT 'Active',
                    FOREIGN KEY(id_tugas) REFERENCES tugas(id_tugas)
                )
            """);

            // Seed default student profile (id=1)
            stmt.execute("""
                INSERT OR IGNORE INTO mahasiswa (id_mahasiswa, nama, nim, email)
                VALUES (1, 'Student', '000000', 'student@classmate.app')
            """);

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}
