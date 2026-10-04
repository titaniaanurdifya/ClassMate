package com.classmate.dao;

import com.classmate.db.DatabaseHelper;
import com.classmate.model.Reminder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAO {

    public List<Reminder> getAll() {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT * FROM reminder ORDER BY waktu_reminder ASC";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ReminderDAO.getAll error: " + e.getMessage());
        }
        return list;
    }

    public List<Reminder> getActive() {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT r.*, t.nama_tugas FROM reminder r LEFT JOIN tugas t ON r.id_tugas=t.id_tugas WHERE r.status_reminder='Active' ORDER BY r.waktu_reminder ASC";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ReminderDAO.getActive error: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Reminder r) {
        String sql = "INSERT INTO reminder (id_tugas, pesan, waktu_reminder, status_reminder) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdTugas());
            ps.setString(2, r.getPesan());
            ps.setString(3, r.getWaktuReminder());
            ps.setString(4, r.getStatusReminder() == null ? "Active" : r.getStatusReminder());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReminderDAO.insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean markDismissed(int id) {
        String sql = "UPDATE reminder SET status_reminder='Dismissed' WHERE id_reminder=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReminderDAO.markDismissed error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reminder WHERE id_reminder=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReminderDAO.delete error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Auto-generates reminders for pending assignments with deadlines within daysAhead days.
     * Skips if a reminder for that task already exists.
     */
    public void autoGenerateReminders(int daysAhead) {
        String sql = """
            INSERT OR IGNORE INTO reminder (id_tugas, pesan, waktu_reminder, status_reminder)
            SELECT t.id_tugas,
                   'Deadline approaching: ' || t.nama_tugas || ' due on ' || t.deadline,
                   t.deadline,
                   'Active'
            FROM tugas t
            WHERE t.status = 'Pending'
              AND t.deadline BETWEEN date('now') AND date('now', '+' || ? || ' days')
              AND t.id_tugas NOT IN (SELECT id_tugas FROM reminder WHERE status_reminder='Active')
        """;
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, daysAhead);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ReminderDAO.autoGenerate error: " + e.getMessage());
        }
    }

    private Reminder mapRow(ResultSet rs) throws SQLException {
        return new Reminder(
            rs.getInt("id_reminder"),
            rs.getInt("id_tugas"),
            rs.getString("pesan"),
            rs.getString("waktu_reminder"),
            rs.getString("status_reminder")
        );
    }
}
