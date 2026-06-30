package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Pemeriksaan;
import model.RekamMedis;
import service.PemeriksaanService;
import service.RekamMedisService;
import util.AlertUtil;
import util.SessionUtil;
import util.ValidationUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FormRekamMedisController {

    @FXML
    private TextField txtId;

    @FXML
    private DatePicker dpTanggal;

    @FXML
    private ComboBox<Pemeriksaan> cbPemeriksaan;

    @FXML
    private TextArea txtRingkasan;

    private RekamMedisService rekamMedisService = new RekamMedisService();
    private PemeriksaanService pemeriksaanService = new PemeriksaanService();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeTambah() {
        modeEdit = false;
        dpTanggal.setValue(LocalDate.now());
    }

    public void setModeEdit(RekamMedis rm) {
        modeEdit = true;
        txtId.setText(String.valueOf(rm.getIdRekam()));
        
        if (rm.getTanggal() != null) {
            dpTanggal.setValue(rm.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        if (rm.getPemeriksaan() != null) {
            for (Pemeriksaan pm : cbPemeriksaan.getItems()) {
                if (pm.getIdPeriksa() == rm.getPemeriksaan().getIdPeriksa()) {
                    cbPemeriksaan.setValue(pm);
                    break;
                }
            }
        }
        
        txtRingkasan.setText(rm.getRingkasan());
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(cbPemeriksaan, "Pemeriksaan wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(dpTanggal, "Tanggal wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtRingkasan, "Ringkasan wajib diisi")) {
                return;
            }

            LocalDate localDate = dpTanggal.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            RekamMedis rm = new RekamMedis();
            rm.setIdRekam(modeEdit ? Integer.parseInt(txtId.getText()) : 0);
            rm.setPemeriksaan(cbPemeriksaan.getValue());
            rm.setTanggal(date);
            rm.setRingkasan(txtRingkasan.getText());

            rekamMedisService.simpan(rm, modeEdit);
            AlertUtil.success(modeEdit ? "Rekam medis berhasil diupdate" : "Rekam medis berhasil disimpan");
            closeWindow();
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
