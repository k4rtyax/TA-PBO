package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Obat;
import model.Pemeriksaan;
import model.ResepObat;
import service.ObatService;
import service.PemeriksaanService;
import service.ResepObatService;
import util.AlertUtil;
import util.SessionUtil;
import util.ValidationUtil;

public class FormResepObatController {

    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<Pemeriksaan> cbPemeriksaan;

    @FXML
    private ComboBox<Obat> cbObat;

    @FXML
    private TextField txtJumlah;

    @FXML
    private TextField txtDosis;

    @FXML
    private TextArea txtKeterangan;

    private ResepObatService resepObatService = new ResepObatService();
    private PemeriksaanService pemeriksaanService = new PemeriksaanService();
    private ObatService obatService = new ObatService();
    private boolean modeEdit = false;

    @FXML
    public void initialize() {
        try {
            ObservableList<Pemeriksaan> all = pemeriksaanService.getAll();
            if (SessionUtil.isDokter()) {
                Integer idDokter = SessionUtil.getCurrentDoctorId();
                ObservableList<Pemeriksaan> milikSendiri = FXCollections.observableArrayList();
                for (Pemeriksaan pm : all) {
                    if (pm.getPendaftaran() != null && pm.getPendaftaran().getDokter() != null
                            && idDokter != null
                            && pm.getPendaftaran().getDokter().getIdDokter() == idDokter) {
                        milikSendiri.add(pm);
                    }
                }
                cbPemeriksaan.setItems(milikSendiri);
            } else {
                cbPemeriksaan.setItems(all);
            }

            cbObat.setItems(obatService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeTambah() {
        modeEdit = false;
    }

    public void setModeEdit(ResepObat r) {
        modeEdit = true;
        txtId.setText(String.valueOf(r.getIdResep()));

        if (r.getPemeriksaan() != null) {
            for (Pemeriksaan pm : cbPemeriksaan.getItems()) {
                if (pm.getIdPeriksa() == r.getPemeriksaan().getIdPeriksa()) {
                    cbPemeriksaan.setValue(pm);
                    break;
                }
            }
        }

        if (r.getObat() != null) {
            for (Obat obat : cbObat.getItems()) {
                if (obat.getIdObat() == r.getObat().getIdObat()) {
                    cbObat.setValue(obat);
                    break;
                }
            }
        }

        txtJumlah.setText(String.valueOf(r.getJumlah()));
        txtDosis.setText(r.getDosis());
        txtKeterangan.setText(r.getKeterangan());
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(cbPemeriksaan, "Pemeriksaan wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(cbObat, "Obat wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtJumlah, "Jumlah wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtDosis, "Dosis wajib diisi")) {
                return;
            }

            ResepObat r = new ResepObat();
            r.setIdResep(modeEdit ? Integer.parseInt(txtId.getText()) : 0);
            r.setPemeriksaan(cbPemeriksaan.getValue());
            r.setObat(cbObat.getValue());
            r.setJumlah(Integer.parseInt(txtJumlah.getText()));
            r.setDosis(txtDosis.getText());
            r.setKeterangan(txtKeterangan.getText());

            resepObatService.simpan(r, modeEdit);
            AlertUtil.success(modeEdit ? "Resep obat berhasil diupdate" : "Resep obat berhasil disimpan");
            closeWindow();
        } catch (NumberFormatException e) {
            AlertUtil.error("Jumlah harus berupa angka!");
        } catch (Exception e) {
            AlertUtil.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBatal() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }
}
