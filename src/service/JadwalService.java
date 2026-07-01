package service;

import dao.JadwalDAO;
import javafx.collections.ObservableList;
import model.Jadwal;

public class JadwalService {
    private JadwalDAO dao = new JadwalDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<Jadwal> getAll() {
        return dao.getAllJadwal();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<Jadwal> search(String keyword) {
        return dao.searchJadwal(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID jadwal tidak valid");
        }
        try {
            dao.deleteJadwal(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Tidak bisa menghapus jadwal ini", e);
        }
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(Jadwal j, boolean modeEdit) throws Exception {
        // VALIDASI
        if (j.getDokter() == null) {
            throw new Exception("Dokter wajib dipilih");
        }
        if (j.getPoli() == null) {
            throw new Exception("Poli wajib dipilih");
        }
        if (j.getHari() == null || j.getHari().trim().isEmpty()) {
            throw new Exception("Hari wajib dipilih");
        }
        if (j.getJamMulai() == null || j.getJamMulai().trim().isEmpty()) {
            throw new Exception("Jam mulai wajib diisi");
        }
        if (j.getJamSelesai() == null || j.getJamSelesai().trim().isEmpty()) {
            throw new Exception("Jam selesai wajib diisi");
        }

        // INSERT / UPDATE
        if (modeEdit) {
            dao.updateJadwal(j);
        } else {
            dao.insertJadwal(j);
        }
    }
}
