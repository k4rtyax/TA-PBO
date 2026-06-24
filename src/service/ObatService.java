package service;

import dao.ObatDAO;
import javafx.collections.ObservableList;
import model.Obat;

public class ObatService {
    private ObatDAO dao = new ObatDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<Obat> getAll() {
        return dao.getAllObat();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<Obat> search(String keyword) {
        return dao.searchObat(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID obat tidak valid");
        }
        dao.deleteObat(id);
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(Obat obat, boolean modeEdit) throws Exception {
        if (obat.getNamaObat() == null || obat.getNamaObat().trim().isEmpty()) {
            throw new Exception("Nama obat wajib diisi");
        }
        if (obat.getStok() < 0) {
            throw new Exception("Stok tidak boleh negatif");
        }
        if (obat.getHarga() <= 0) {
            throw new Exception("Harga harus lebih dari 0");
        }

        if (modeEdit) {
            dao.updateObat(obat);
        } else {
            dao.insertObat(obat);
        }
    }
}
