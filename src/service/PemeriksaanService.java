package service;

import dao.PemeriksaanDAO;
import dao.PasienDAO;
import javafx.collections.ObservableList;
import model.Pemeriksaan;
import model.Pasien;

public class PemeriksaanService {
    private PemeriksaanDAO dao = new PemeriksaanDAO();
    private PasienDAO pasienDao = new PasienDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<Pemeriksaan> getAll() {
        return dao.getAllPemeriksaan();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<Pemeriksaan> search(String keyword) {
        return dao.searchPemeriksaan(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID pemeriksaan tidak valid");
        }
        dao.deletePemeriksaan(id);
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(Pemeriksaan pm, boolean modeEdit) throws Exception {
        // VALIDASI
        if (pm.getPendaftaran() == null) {
            throw new Exception("Pendaftaran wajib dipilih");
        }
        if (pm.getTanggalPeriksa() == null) {
            throw new Exception("Tanggal pemeriksaan wajib diisi");
        }
        if (pm.getDiagnosa() == null || pm.getDiagnosa().trim().isEmpty()) {
            throw new Exception("Diagnosa wajib diisi");
        }
        if (pm.getTekananDarah() <= 0) {
            throw new Exception("Tekanan darah harus lebih dari 0");
        }
        if (pm.getGulaDarah() <= 0) {
            throw new Exception("Gula darah harus lebih dari 0");
        }
        if (pm.getSuhu() <= 0) {
            throw new Exception("Suhu tubuh harus lebih dari 0");
        }
        if (pm.getBeratBadan() <= 0) {
            throw new Exception("Berat badan harus lebih dari 0");
        }

        // SAVE
        if (modeEdit) {
            dao.updatePemeriksaan(pm);
        } else {
            dao.insertPemeriksaan(pm);
        }

        // UPDATE PASIEN'S MEASUREMENTS
        try {
            Pasien pasien = pm.getPendaftaran().getPasien();
            if (pasien != null) {
                // Fetch latest full details of the patient first if needed, or update directly
                // Let's load the patient list and find our patient
                ObservableList<Pasien> allPasien = pasienDao.getAllPasien();
                for (Pasien p : allPasien) {
                    if (p.getIdPasien() == pasien.getIdPasien()) {
                        p.setTekananDarah(pm.getTekananDarah());
                        p.setGulaDarah(pm.getGulaDarah());
                        pasienDao.updatePasien(p);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
