package service;

import dao.DokterDAO;
import javafx.collections.ObservableList;
import model.Dokter;

public class DokterService {
    private DokterDAO dao = new DokterDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<Dokter> getAll() {
        return dao.getAllDokter();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<Dokter> search(String keyword) {
        return dao.searchDokter(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID dokter tidak valid");
        }
        dao.deleteDokter(id);
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(Dokter dokter, boolean modeEdit) throws Exception {
        // VALIDASI
        if (dokter.getNama() == null || dokter.getNama().trim().isEmpty()) {
            throw new Exception("Nama dokter wajib diisi");
        }
        if (dokter.getSpesialis() == null || dokter.getSpesialis().trim().isEmpty()) {
            throw new Exception("Spesialis wajib diisi");
        }
        if (dokter.getNoHP() == null || dokter.getNoHP().trim().isEmpty()) {
            throw new Exception("Nomor HP wajib diisi");
        }
        if (dokter.getAlamat() == null || dokter.getAlamat().trim().isEmpty()) {
            throw new Exception("Alamat wajib diisi");
        }

        // INSERT / UPDATE
        if (modeEdit) {
            dao.updateDokter(dokter);
        } else {
            dao.insertDokter(dokter);
        }
    }
}
