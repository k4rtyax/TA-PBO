package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Pendaftaran;
import model.Pemeriksaan;
import service.PendaftaranService;
import service.PemeriksaanService;
import util.AlertUtil;
import util.SessionUtil;
import util.ValidationUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FormPemeriksaanController {

    @FXML
    private TextField txtId,
            txtDiagnosa,
            txtTekanan,
            txtGula,
            txtSuhu,
            txtBerat,
            txtHasilPrediksi,
            txtTingkatResiko;

    @FXML
    private DatePicker dpTanggal;

    @FXML
    private ComboBox<Pendaftaran> cbPendaftaran;

    @FXML
    private TextArea txtCatatan;

    private PemeriksaanService pemeriksaanService = new PemeriksaanService();
    private PendaftaranService pendaftaranService = new PendaftaranService();
    private boolean modeEdit = false;

    @FXML
    public void initialize() {
        try {
            ObservableList<Pendaftaran> all = pendaftaranService.getAll();
            if (SessionUtil.isDokter()) {
                Integer idDokter = SessionUtil.getCurrentDoctorId();
                ObservableList<Pendaftaran> milikSendiri = FXCollections.observableArrayList();
                for (Pendaftaran pd : all) {
                    if (pd.getDokter() != null && idDokter != null
                            && pd.getDokter().getIdDokter() == idDokter) {
                        milikSendiri.add(pd);
                    }
                }
                cbPendaftaran.setItems(milikSendiri);
            } else {
                cbPendaftaran.setItems(all);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeTambah() {
        modeEdit = false;
        dpTanggal.setValue(LocalDate.now());
    }

    public void setModeEdit(Pemeriksaan pm) {
        modeEdit = true;
        txtId.setText(String.valueOf(pm.getIdPeriksa()));
        
        if (pm.getTanggalPeriksa() != null) {
            dpTanggal.setValue(pm.getTanggalPeriksa().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        if (pm.getPendaftaran() != null) {
            for (Pendaftaran pd : cbPendaftaran.getItems()) {
                if (pd.getIdDaftar() == pm.getPendaftaran().getIdDaftar()) {
                    cbPendaftaran.setValue(pd);
                    break;
                }
            }
        }

        txtDiagnosa.setText(pm.getDiagnosa());
        txtTekanan.setText(String.valueOf(pm.getTekananDarah()));
        txtGula.setText(String.valueOf(pm.getGulaDarah()));
        txtSuhu.setText(String.valueOf(pm.getSuhu()));
        txtBerat.setText(String.valueOf(pm.getBeratBadan()));
        txtHasilPrediksi.setText(pm.getHasilPrediksi());
        txtTingkatResiko.setText(pm.getTingkatResiko());
        txtCatatan.setText(pm.getCatatan());
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(cbPendaftaran, "Pendaftaran wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(dpTanggal, "Tanggal pemeriksaan wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtDiagnosa, "Diagnosa wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtTekanan, "Tekanan darah wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtGula, "Gula darah wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtSuhu, "Suhu tubuh wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtBerat, "Berat badan wajib diisi")) {
                return;
            }

            LocalDate localDate = dpTanggal.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Pemeriksaan pm = new Pemeriksaan();
            pm.setIdPeriksa(modeEdit ? Integer.parseInt(txtId.getText()) : 0);
            pm.setPendaftaran(cbPendaftaran.getValue());
            pm.setTanggalPeriksa(date);
            pm.setDiagnosa(txtDiagnosa.getText());
            pm.setTekananDarah(Double.parseDouble(txtTekanan.getText()));
            pm.setGulaDarah(Double.parseDouble(txtGula.getText()));
            pm.setSuhu(Double.parseDouble(txtSuhu.getText()));
            pm.setBeratBadan(Double.parseDouble(txtBerat.getText()));
            pm.setCatatan(txtCatatan.getText());
            pm.setHasilPrediksi(txtHasilPrediksi.getText());
            pm.setTingkatResiko(txtTingkatResiko.getText());

            pemeriksaanService.simpan(pm, modeEdit);
            AlertUtil.success(modeEdit ? "Data pemeriksaan berhasil diupdate" : "Data pemeriksaan berhasil disimpan");
            closeWindow();
        } catch (NumberFormatException e) {
            AlertUtil.error("Tekanan darah, Gula darah, Suhu, dan Berat badan harus berupa angka!");
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
