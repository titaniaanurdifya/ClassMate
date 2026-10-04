package com.classmate.dao;

import com.classmate.db.DatabaseHelper;
import com.classmate.model.Tugas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TugasDAO {

    public List<Tugas> getAll() {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT * FROM tugas ORDER BY deadline ASC";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("TugasDAO.getAll error: " + e.getMessage());
        }
        return list;
    }

    public List<Tugas> getPending() {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT * FROM tugas WHERE status='Pending' ORDER BY deadline ASC";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("TugasDAO.getPending error: " + e.getMessage());
        }
        return list;
    }

    public List<Tugas> getUpcomingDeadlines(int daysAhead) {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT * FROM tugas WHERE status='Pending' AND deadline BETWEEN date('now') AND date('now', '+' || ? || ' days') ORDER BY deadline ASC";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, daysAhead);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("TugasDAO.getUpcomingDeadlines error: " + e.getMessage());
        }
        return list;
    }

    public int countPending() {
        String sql = "SELECT COUNT(*) FROM tugas WHERE status='Pending'";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("TugasDAO.countPending error: " + e.getMessage());
        }
        return 0;
    }

    public boolean insert(Tugas t) {
        String sql = "INSERT INTO tugas (id_mahasiswa, id_jadwal, nama_tugas, deskripsi, deadline, status) VALUES (1,?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, t.getIdJadwal() == 0 ? null : t.getIdJadwal());
            ps.setString(2, t.getNamaTugas());
            ps.setString(3, t.getDeskripsi());
            ps.setString(4, t.getDeadline());
            ps.setString(5, t.getStatus() == null ? "Pending" : t.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TugasDAO.insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Tugas t) {
        String sql = "UPDATE tugas SET nama_tugas=?, deskripsi=?, deadline=?, status=?, id_jadwal=? WHERE id_tugas=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNamaTugas());
            ps.setString(2, t.getDeskripsi());
            ps.setString(3, t.getDeadline());
            ps.setString(4, t.getStatus());
            ps.setObject(5, t.getIdJadwal() == 0 ? null : t.getIdJadwal());
            ps.setInt(6, t.getIdTugas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TugasDAO.update error: " + e.getMessage());
            return false;
        }
    }

    public boolean markComplete(int id) {
        String sql = "UPDATE tugas SET status='Completed' WHERE id_tugas=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TugasDAO.markComplete error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM tugas WHERE id_tugas=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TugasDAO.delete error: " + e.getMessage());
            return false;
        }
    }

    private Tugas mapRow(ResultSet rs) throws SQLException {
        return new Tugas(
            rs.getInt("id_tugas"),
            rs.getInt("id_mahasiswa"),
            rs.getInt("id_jadwal"),
            rs.getString("nama_tugas"),
            rs.getString("deskripsi"),
            rs.getString("deadline"),
            rs.getString("status")
        );
    }
}
