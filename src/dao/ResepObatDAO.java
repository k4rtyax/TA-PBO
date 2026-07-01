package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.sql.*;

public class ResepObatDAO {
    Connection conn = DBConnection.connect();

    // GET ALL
    public ObservableList<ResepObat> getAllResepObat() {
        ObservableList<ResepObat> list = FXCollections.observableArrayList();
        String sql = """
                SELECT r.*,
                       pm.diagnosa, pm.tekanan_darah, pm.gula_darah, pm.suhu, pm.berat_badan, pm.catatan, pm.hasil_prediksi, pm.tingkat_resiko, pm.tanggal_periksa,
                       pd.id_daftar, pd.tanggal AS tanggal_daftar, pd.keluhan, pd.id_pasien, pd.id_dokter,
                       ps.nama AS nama_pasien,
                       d.nama AS nama_dokter,
                       o.nama_obat, o.stok, o.harga
                FROM resep_obat r
                JOIN pemeriksaan pm ON r.id_periksa = pm.id_periksa
                JOIN pendaftaran pd ON pm.id_daftar = pd.id_daftar
                JOIN pasien ps ON pd.id_pasien = ps.id_pasien
                JOIN dokter d ON pd.id_dokter = d.id_dokter
                JOIN obat o ON r.id_obat = o.id_obat
                """;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(buildResepObat(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertResepObat(ResepObat r) {
        String sql = "INSERT INTO resep_obat(id_periksa, id_obat, jumlah, dosis, keterangan) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, r.getPemeriksaan().getIdPeriksa());
            ps.setInt(2, r.getObat().getIdObat());
            ps.setInt(3, r.getJumlah());
            ps.setString(4, r.getDosis());
            ps.setString(5, r.getKeterangan());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateResepObat(ResepObat r) {
        String sql = "UPDATE resep_obat SET id_periksa=?, id_obat=?, jumlah=?, dosis=?, keterangan=? WHERE id_resep=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, r.getPemeriksaan().getIdPeriksa());
            ps.setInt(2, r.getObat().getIdObat());
            ps.setInt(3, r.getJumlah());
            ps.setString(4, r.getDosis());
            ps.setString(5, r.getKeterangan());
            ps.setInt(6, r.getIdResep());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteResepObat(int id) throws SQLException {
        String sql = "DELETE FROM resep_obat WHERE id_resep=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // SEARCH
    public ObservableList<ResepObat> searchResepObat(String keyword) {
        ObservableList<ResepObat> list = FXCollections.observableArrayList();
        String sql = """
                SELECT r.*,
                       pm.diagnosa, pm.tekanan_darah, pm.gula_darah, pm.suhu, pm.berat_badan, pm.catatan, pm.hasil_prediksi, pm.tingkat_resiko, pm.tanggal_periksa,
                       pd.id_daftar, pd.tanggal AS tanggal_daftar, pd.keluhan, pd.id_pasien, pd.id_dokter,
                       ps.nama AS nama_pasien,
                       d.nama AS nama_dokter,
                       o.nama_obat, o.stok, o.harga
                FROM resep_obat r
                JOIN pemeriksaan pm ON r.id_periksa = pm.id_periksa
                JOIN pendaftaran pd ON pm.id_daftar = pd.id_daftar
                JOIN pasien ps ON pd.id_pasien = ps.id_pasien
                JOIN dokter d ON pd.id_dokter = d.id_dokter
                JOIN obat o ON r.id_obat = o.id_obat
                WHERE o.nama_obat LIKE ? OR r.dosis LIKE ?
                """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchStr = "%" + keyword + "%";
            ps.setString(1, searchStr);
            ps.setString(2, searchStr);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildResepObat(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // HELPER - BUILD OBJECT FROM RESULTSET
    private ResepObat buildResepObat(ResultSet rs) throws SQLException {
        Pasien pasien = new Pasien();
        pasien.setIdPasien(rs.getInt("id_pasien"));
        pasien.setNama(rs.getString("nama_pasien"));

        Dokter dokter = new Dokter();
        dokter.setIdDokter(rs.getInt("id_dokter"));
        dokter.setNama(rs.getString("nama_dokter"));

        Pendaftaran pd = new Pendaftaran(
                rs.getInt("id_daftar"),
                rs.getDate("tanggal_daftar"),
                rs.getString("keluhan"),
                pasien,
                dokter
        );

        Pemeriksaan pm = new Pemeriksaan();
        pm.setIdPeriksa(rs.getInt("id_periksa"));
        pm.setPendaftaran(pd);
        pm.setTanggalPeriksa(rs.getDate("tanggal_periksa"));
        pm.setDiagnosa(rs.getString("diagnosa"));
        pm.setTekananDarah(rs.getDouble("tekanan_darah"));
        pm.setGulaDarah(rs.getDouble("gula_darah"));
        pm.setSuhu(rs.getDouble("suhu"));
        pm.setBeratBadan(rs.getDouble("berat_badan"));
        pm.setCatatan(rs.getString("catatan"));
        pm.setHasilPrediksi(rs.getString("hasil_prediksi"));
        pm.setTingkatResiko(rs.getString("tingkat_resiko"));

        Obat obat = new Obat();
        obat.setIdObat(rs.getInt("id_obat"));
        obat.setNamaObat(rs.getString("nama_obat"));
        obat.setStok(rs.getInt("stok"));
        obat.setHarga(rs.getDouble("harga"));

        ResepObat resep = new ResepObat();
        resep.setIdResep(rs.getInt("id_resep"));
        resep.setPemeriksaan(pm);
        resep.setObat(obat);
        resep.setJumlah(rs.getInt("jumlah"));
        resep.setDosis(rs.getString("dosis"));
        resep.setKeterangan(rs.getString("keterangan"));

        return resep;
    }
}
