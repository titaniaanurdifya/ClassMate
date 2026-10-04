package com.classmate.dao;

import com.classmate.db.DatabaseHelper;
import com.classmate.model.Dosen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenDAO {

    public List<Dosen> getAll() {
        List<Dosen> list = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY nama_dosen ASC";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("DosenDAO.getAll error: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Dosen d) {
        String sql = "INSERT INTO dosen (nama_dosen, email, no_hp, mata_kuliah) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNamaDosen());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getNoHp());
            ps.setString(4, d.getMataKuliah());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DosenDAO.insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Dosen d) {
        String sql = "UPDATE dosen SET nama_dosen=?, email=?, no_hp=?, mata_kuliah=? WHERE id_dosen=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNamaDosen());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getNoHp());
            ps.setString(4, d.getMataKuliah());
            ps.setInt(5, d.getIdDosen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DosenDAO.update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM dosen WHERE id_dosen=?";
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DosenDAO.delete error: " + e.getMessage());
            return false;
        }
    }

    private Dosen mapRow(ResultSet rs) throws SQLException {
        return new Dosen(
            rs.getInt("id_dosen"),
            rs.getString("nama_dosen"),
            rs.getString("email"),
            rs.getString("no_hp"),
            rs.getString("mata_kuliah")
        );
    }
}
