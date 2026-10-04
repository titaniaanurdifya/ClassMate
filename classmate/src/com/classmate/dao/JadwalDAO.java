package com.classmate.dao;

import com.classmate.db.DatabaseHelper;
import com.classmate.model.Jadwal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {

    public List<Jadwal> getAll() {
        List<Jadwal> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal ORDER BY CASE hari " +
                     "WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 " +
                     "WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 " +
                     "WHEN 'Sunday' THEN 7 ELSE 8 END, jam_mulai";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("JadwalDAO.getAll error: " + e.getMessage());
        }
        return list;
    }

    public List<Jadwal> getByHari(String hari) {
        List<Jadwal> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal WHERE hari = ? ORDER BY jam_mulai";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hari);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("JadwalDAO.getByHari error: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Jadwal j) {
        String sql = "INSERT INTO jadwal (id_mahasiswa, mata_kuliah, hari, jam_mulai, jam_selesai, ruangan, dosen_pengajar) VALUES (1,?,?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, j.getMataKuliah());
            ps.setString(2, j.getHari());
            ps.setString(3, j.getJamMulai());
            ps.setString(4, j.getJamSelesai());
            ps.setString(5, j.getRuangan());
            ps.setString(6, j.getDosenPengajar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("JadwalDAO.insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Jadwal j) {
        String sql = "UPDATE jadwal SET mata_kuliah=?, hari=?, jam_mulai=?, jam_selesai=?, ruangan=?, dosen_pengajar=? WHERE id_jadwal=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, j.getMataKuliah());
            ps.setString(2, j.getHari());
            ps.setString(3, j.getJamMulai());
            ps.setString(4, j.getJamSelesai());
            ps.setString(5, j.getRuangan());
            ps.setString(6, j.getDosenPengajar());
            ps.setInt(7, j.getIdJadwal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("JadwalDAO.update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM jadwal WHERE id_jadwal=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("JadwalDAO.delete error: " + e.getMessage());
            return false;
        }
    }

    private Jadwal mapRow(ResultSet rs) throws SQLException {
        return new Jadwal(
            rs.getInt("id_jadwal"),
            rs.getInt("id_mahasiswa"),
            rs.getString("mata_kuliah"),
            rs.getString("hari"),
            rs.getString("jam_mulai"),
            rs.getString("jam_selesai"),
            rs.getString("ruangan"),
            rs.getString("dosen_pengajar")
        );
    }
}
