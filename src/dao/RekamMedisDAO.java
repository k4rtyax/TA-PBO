package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.sql.*;

public class RekamMedisDAO {
    Connection conn = DBConnection.connect();

    // GET ALL
    public ObservableList<RekamMedis> getAllRekam() {
        ObservableList<RekamMedis> list = FXCollections.observableArrayList();
        String sql = """
                SELECT rm.*,
                       pm.diagnosa, pm.tekanan_darah, pm.gula_darah, pm.suhu, pm.berat_badan, pm.catatan, pm.hasil_prediksi, pm.tingkat_resiko, pm.tanggal_periksa,
                       pd.id_daftar, pd.tanggal AS tanggal_daftar, pd.keluhan, pd.id_pasien, pd.id_dokter,
                       ps.nama AS nama_pasien,
                       d.nama AS nama_dokter
                FROM rekam_medis rm
                JOIN pemeriksaan pm ON rm.id_periksa = pm.id_periksa
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

                RekamMedis rm = new RekamMedis();
                rm.setIdRekam(rs.getInt("id_rekam"));
                rm.setPemeriksaan(pm);
                rm.setTanggal(rs.getDate("tanggal"));
                rm.setRingkasan(rs.getString("ringkasan"));

                list.add(rm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertRekam(RekamMedis rm) {
        String sql = "INSERT INTO rekam_medis(id_periksa, tanggal, ringkasan) VALUES(?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rm.getPemeriksaan().getIdPeriksa());
            ps.setDate(2, new java.sql.Date(rm.getTanggal().getTime()));
            ps.setString(3, rm.getRingkasan());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateRekam(RekamMedis rm) {
        String sql = "UPDATE rekam_medis SET id_periksa=?, tanggal=?, ringkasan=? WHERE id_rekam=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rm.getPemeriksaan().getIdPeriksa());
            ps.setDate(2, new java.sql.Date(rm.getTanggal().getTime()));
            ps.setString(3, rm.getRingkasan());
            ps.setInt(4, rm.getIdRekam());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteRekam(int id) {
        try {
            String sql = "DELETE FROM rekam_medis WHERE id_rekam=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    public ObservableList<RekamMedis> searchRekam(String keyword) {
        ObservableList<RekamMedis> list = FXCollections.observableArrayList();
        String sql = """
                SELECT rm.*,
                       pm.diagnosa, pm.tekanan_darah, pm.gula_darah, pm.suhu, pm.berat_badan, pm.catatan, pm.hasil_prediksi, pm.tingkat_resiko, pm.tanggal_periksa,
                       pd.id_daftar, pd.tanggal AS tanggal_daftar, pd.keluhan, pd.id_pasien, pd.id_dokter,
                       ps.nama AS nama_pasien,
                       d.nama AS nama_dokter
                FROM rekam_medis rm
                JOIN pemeriksaan pm ON rm.id_periksa = pm.id_periksa
                JOIN pendaftaran pd ON pm.id_daftar = pd.id_daftar
                JOIN pasien ps ON pd.id_pasien = ps.id_pasien
                JOIN dokter d ON pd.id_dokter = d.id_dokter
                WHERE ps.nama LIKE ? OR d.nama LIKE ? OR rm.ringkasan LIKE ?
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

                RekamMedis rm = new RekamMedis();
                rm.setIdRekam(rs.getInt("id_rekam"));
                rm.setPemeriksaan(pm);
                rm.setTanggal(rs.getDate("tanggal"));
                rm.setRingkasan(rs.getString("ringkasan"));

                list.add(rm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
