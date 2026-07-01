package service;

import dao.PoliDAO;
import javafx.collections.ObservableList;
import model.Poli;

public class PoliService {
    private PoliDAO dao = new PoliDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<Poli> getAll() {
        return dao.getAllPoli();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<Poli> search(String keyword) {
        return dao.searchPoli(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID poli tidak valid");
        }
        try {
            dao.deletePoli(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Tidak bisa menghapus poli karena masih memiliki data terkait (jadwal)", e);
        }
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(Poli poli, boolean modeEdit) throws Exception {
        // VALIDASI
        if (poli.getNamaPoli() == null || poli.getNamaPoli().trim().isEmpty()) {
            throw new Exception("Nama poli wajib diisi");
        }
        if (poli.getLokasi() == null || poli.getLokasi().trim().isEmpty()) {
            throw new Exception("Lokasi wajib diisi");
        }

        // INSERT / UPDATE
        if (modeEdit) {
            dao.updatePoli(poli);
        } else {
            dao.insertPoli(poli);
        }
    }
}
