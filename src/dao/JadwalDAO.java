package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Dokter;
import model.Jadwal;
import model.Poli;

import java.sql.*;

public class JadwalDAO {
    Connection conn = DBConnection.connect();

    private static final String SELECT_JOIN = """
            SELECT j.*,
                   d.id_dokter, d.nama AS nama_dokter, d.spesialis, d.no_hp, d.alamat,
                   p.id_poli, p.nama_poli, p.deskripsi, p.lokasi
            FROM jadwal j
            JOIN dokter d ON j.id_dokter = d.id_dokter
            JOIN poli p ON j.id_poli = p.id_poli
            """;

    private Jadwal mapRow(ResultSet rs) throws SQLException {
        Dokter dokter = new Dokter();
        dokter.setIdDokter(rs.getInt("id_dokter"));
        dokter.setNama(rs.getString("nama_dokter"));
        dokter.setSpesialis(rs.getString("spesialis"));
        dokter.setNoHP(rs.getString("no_hp"));
        dokter.setAlamat(rs.getString("alamat"));

        Poli poli = new Poli();
        poli.setIdPoli(rs.getInt("id_poli"));
        poli.setNamaPoli(rs.getString("nama_poli"));
        poli.setDeskripsi(rs.getString("deskripsi"));
        poli.setLokasi(rs.getString("lokasi"));

        return new Jadwal(
                rs.getInt("id_jadwal"),
                dokter,
                poli,
                rs.getString("hari"),
                rs.getString("jam_mulai"),
                rs.getString("jam_selesai")
        );
    }

    // LOAD DATA
    public ObservableList<Jadwal> getAllJadwal() {
        ObservableList<Jadwal> list = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(SELECT_JOIN);
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertJadwal(Jadwal j) {
        try {
            String sql = "INSERT INTO jadwal(id_dokter, id_poli, hari, jam_mulai, jam_selesai) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, j.getDokter().getIdDokter());
            ps.setInt(2, j.getPoli().getIdPoli());
            ps.setString(3, j.getHari());
            ps.setString(4, j.getJamMulai());
            ps.setString(5, j.getJamSelesai());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateJadwal(Jadwal j) {
        try {
            String sql = "UPDATE jadwal SET id_dokter=?, id_poli=?, hari=?, jam_mulai=?, jam_selesai=? WHERE id_jadwal=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, j.getDokter().getIdDokter());
            ps.setInt(2, j.getPoli().getIdPoli());
            ps.setString(3, j.getHari());
            ps.setString(4, j.getJamMulai());
            ps.setString(5, j.getJamSelesai());
            ps.setInt(6, j.getIdJadwal());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteJadwal(int id) throws SQLException {
        String sql = "DELETE FROM jadwal WHERE id_jadwal=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // SEARCH
    public ObservableList<Jadwal> searchJadwal(String keyword) {
        ObservableList<Jadwal> list = FXCollections.observableArrayList();
        try {
            String sql = SELECT_JOIN + " WHERE d.nama LIKE ? OR p.nama_poli LIKE ? OR j.hari LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchStr = "%" + keyword + "%";
            ps.setString(1, searchStr);
            ps.setString(2, searchStr);
            ps.setString(3, searchStr);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
