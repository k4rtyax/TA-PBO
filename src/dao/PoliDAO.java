package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Poli;
import java.sql.*;

public class PoliDAO {
    Connection conn = DBConnection.connect();

    // LOAD DATA
    public ObservableList<Poli> getAllPoli() {
        ObservableList<Poli> list = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM poli");
            while (rs.next()) {
                list.add(new Poli(
                        rs.getInt("id_poli"),
                        rs.getString("nama_poli"),
                        rs.getString("deskripsi"),
                        rs.getString("lokasi")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertPoli(Poli p) {
        try {
            String sql = "INSERT INTO poli(nama_poli, deskripsi, lokasi) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNamaPoli());
            ps.setString(2, p.getDeskripsi());
            ps.setString(3, p.getLokasi());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updatePoli(Poli p) {
        try {
            String sql = "UPDATE poli SET nama_poli=?, deskripsi=?, lokasi=? WHERE id_poli=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNamaPoli());
            ps.setString(2, p.getDeskripsi());
            ps.setString(3, p.getLokasi());
            ps.setInt(4, p.getIdPoli());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePoli(int id) throws SQLException {
        String sql = "DELETE FROM poli WHERE id_poli=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // SEARCH
    public ObservableList<Poli> searchPoli(String keyword) {
        ObservableList<Poli> list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM poli WHERE nama_poli LIKE ? OR lokasi LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Poli(
                        rs.getInt("id_poli"),
                        rs.getString("nama_poli"),
                        rs.getString("deskripsi"),
                        rs.getString("lokasi")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
