package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.sql.*;

public class PemeriksaanDAO {
    Connection conn = DBConnection.connect();

    // GET ALL
    public ObservableList<Pemeriksaan> getAllPemeriksaan() {
        ObservableList<Pemeriksaan> list = FXCollections.observableArrayList();
        String sql = """
                SELECT pm.*,
                       pd.tanggal AS tanggal_daftar,
                       pd.keluhan,
                       pd.id_pasien,
                       pd.id_dokter,
                       ps.nama AS nama_pasien,
                       d.nama AS nama_dokter
                FROM pemeriksaan pm
                JOIN pendaftaran pd ON pm.id_daftar = pd.id_daftar
                JOIN pasien ps ON pd.id_pasien = ps.id_pasien
                JOIN dokter d ON pd.id_dokter = d.id_dokter
                """;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
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

                list.add(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertPemeriksaan(Pemeriksaan pm) {
        String sql = """
                INSERT INTO pemeriksaan
                (id_daftar, tanggal_periksa, diagnosa, tekanan_darah, gula_darah, suhu, berat_badan, catatan, hasil_prediksi, tingkat_resiko)
                VALUES(?,?,?,?,?,?,?,?,?,?)
                """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pm.getPendaftaran().getIdDaftar());
            ps.setDate(2, new java.sql.Date(pm.getTanggalPeriksa().getTime()));
            ps.setString(3, pm.getDiagnosa());
            ps.setDouble(4, pm.getTekananDarah());
            ps.setDouble(5, pm.getGulaDarah());
            ps.setDouble(6, pm.getSuhu());
            ps.setDouble(7, pm.getBeratBadan());
            ps.setString(8, pm.getCatatan());
            ps.setString(9, pm.getHasilPrediksi());
            ps.setString(10, pm.getTingkatResiko());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updatePemeriksaan(Pemeriksaan pm) {
        String sql = """
                UPDATE pemeriksaan
                SET id_daftar=?, tanggal_periksa=?, diagnosa=?, tekanan_darah=?, gula_darah=?, suhu=?, berat_badan=?, catatan=?, hasil_prediksi=?, tingkat_resiko=?
                WHERE id_periksa=?
                """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pm.getPendaftaran().getIdDaftar());
            ps.setDate(2, new java.sql.Date(pm.getTanggalPeriksa().getTime()));
            ps.setString(3, pm.getDiagnosa());
            ps.setDouble(4, pm.getTekananDarah());
            ps.setDouble(5, pm.getGulaDarah());
            ps.setDouble(6, pm.getSuhu());
            ps.setDouble(7, pm.getBeratBadan());
            ps.setString(8, pm.getCatatan());
            ps.setString(9, pm.getHasilPrediksi());
            ps.setString(10, pm.getTingkatResiko());
            ps.setInt(11, pm.getIdPeriksa());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePemeriksaan(int id) {
        try {
            String sql = "DELETE FROM pemeriksaan WHERE id_periksa=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    public ObservableList<Pemeriksaan> searchPemeriksaan(String keyword) {
        ObservableList<Pemeriksaan> list = FXCollections.observableArrayList();
        String sql = """
                SELECT pm.*,
                       pd.tanggal AS tanggal_daftar,
                       pd.keluhan,
                       pd.id_pasien,
                       pd.id_dokter,
                       ps.nama AS nama_pasien,
                       d.nama AS nama_dokter
                FROM pemeriksaan pm
                JOIN pendaftaran pd ON pm.id_daftar = pd.id_daftar
                JOIN pasien ps ON pd.id_pasien = ps.id_pasien
                JOIN dokter d ON pd.id_dokter = d.id_dokter
                WHERE ps.nama LIKE ? OR d.nama LIKE ? OR pm.diagnosa LIKE ?
                """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchStr = "%" + keyword + "%";
            ps.setString(1, searchStr);
            ps.setString(2, searchStr);
            ps.setString(3, searchStr);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

                list.add(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
