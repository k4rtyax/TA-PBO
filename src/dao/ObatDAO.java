package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Obat;
import java.sql.*;

public class ObatDAO {
    Connection conn = DBConnection.connect();

    // LOAD DATA
    public ObservableList<Obat> getAllObat() {
        ObservableList<Obat> list = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM obat");
            while (rs.next()) {
                list.add(new Obat(
                        rs.getInt("id_obat"),
                        rs.getString("nama_obat"),
                        rs.getInt("stok"),
                        rs.getDouble("harga")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertObat(Obat o) {
        try {
            String sql = "INSERT INTO obat(nama_obat, stok, harga) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, o.getNamaObat());
            ps.setInt(2, o.getStok());
            ps.setDouble(3, o.getHarga());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateObat(Obat o) {
        try {
            String sql = "UPDATE obat SET nama_obat=?, stok=?, harga=? WHERE id_obat=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, o.getNamaObat());
            ps.setInt(2, o.getStok());
            ps.setDouble(3, o.getHarga());
            ps.setInt(4, o.getIdObat());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteObat(int id) {
        try {
            String sql = "DELETE FROM obat WHERE id_obat=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    public ObservableList<Obat> searchObat(String keyword) {
        ObservableList<Obat> list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM obat WHERE nama_obat LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Obat(
                        rs.getInt("id_obat"),
                        rs.getString("nama_obat"),
                        rs.getInt("stok"),
                        rs.getDouble("harga")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
