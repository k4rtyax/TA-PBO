package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Dokter;
import java.sql.*;

public class DokterDAO {
    Connection conn = DBConnection.connect();

    // LOAD DATA
    public ObservableList<Dokter> getAllDokter() {
        ObservableList<Dokter> list = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM dokter");
            while (rs.next()) {
                list.add(new Dokter(
                        rs.getInt("id_dokter"),
                        rs.getString("nama"),
                        rs.getString("spesialis"),
                        rs.getString("no_hp"),
                        rs.getString("alamat")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertDokter(Dokter d) {
        try {
            String sql = "INSERT INTO dokter(nama, spesialis, no_hp, alamat) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, d.getNama());
            ps.setString(2, d.getSpesialis());
            ps.setString(3, d.getNoHP());
            ps.setString(4, d.getAlamat());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateDokter(Dokter d) {
        try {
            String sql = "UPDATE dokter SET nama=?, spesialis=?, no_hp=?, alamat=? WHERE id_dokter=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, d.getNama());
            ps.setString(2, d.getSpesialis());
            ps.setString(3, d.getNoHP());
            ps.setString(4, d.getAlamat());
            ps.setInt(5, d.getIdDokter());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteDokter(int id) throws SQLException {
        String sql = "DELETE FROM dokter WHERE id_dokter=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // SEARCH
    public ObservableList<Dokter> searchDokter(String keyword) {
        ObservableList<Dokter> list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM dokter WHERE nama LIKE ? OR spesialis LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Dokter(
                        rs.getInt("id_dokter"),
                        rs.getString("nama"),
                        rs.getString("spesialis"),
                        rs.getString("no_hp"),
                        rs.getString("alamat")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
