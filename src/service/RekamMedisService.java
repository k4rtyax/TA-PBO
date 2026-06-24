package service;

import dao.RekamMedisDAO;
import javafx.collections.ObservableList;
import model.RekamMedis;

public class RekamMedisService {
    private RekamMedisDAO dao = new RekamMedisDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<RekamMedis> getAll() {
        return dao.getAllRekam();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<RekamMedis> search(String keyword) {
        return dao.searchRekam(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID rekam medis tidak valid");
        }
        dao.deleteRekam(id);
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(RekamMedis rm, boolean modeEdit) throws Exception {
        if (rm.getPemeriksaan() == null) {
            throw new Exception("Pemeriksaan wajib dipilih");
        }
        if (rm.getTanggal() == null) {
            throw new Exception("Tanggal wajib diisi");
        }
        if (rm.getRingkasan() == null || rm.getRingkasan().trim().isEmpty()) {
            throw new Exception("Ringkasan wajib diisi");
        }

        if (modeEdit) {
            dao.updateRekam(rm);
        } else {
            dao.insertRekam(rm);
        }
    }
}
