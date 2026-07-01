package service;

import dao.ResepObatDAO;
import javafx.collections.ObservableList;
import model.ResepObat;

public class ResepObatService {
    private ResepObatDAO dao = new ResepObatDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<ResepObat> getAll() {
        return dao.getAllResepObat();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<ResepObat> search(String keyword) {
        return dao.searchResepObat(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID resep tidak valid");
        }
        try {
            dao.deleteResepObat(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Tidak bisa menghapus resep ini", e);
        }
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(ResepObat r, boolean modeEdit) throws Exception {
        if (r.getPemeriksaan() == null) {
            throw new Exception("Pemeriksaan wajib dipilih");
        }
        if (r.getObat() == null) {
            throw new Exception("Obat wajib dipilih");
        }
        if (r.getJumlah() <= 0) {
            throw new Exception("Jumlah wajib lebih dari 0");
        }
        if (r.getDosis() == null || r.getDosis().trim().isEmpty()) {
            throw new Exception("Dosis wajib diisi");
        }

        if (modeEdit) {
            dao.updateResepObat(r);
        } else {
            dao.insertResepObat(r);
        }
    }
}
