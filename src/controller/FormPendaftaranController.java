package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Dokter;
import model.Pasien;
import model.Pendaftaran;
import service.DokterService;
import service.PasienService;
import service.PendaftaranService;
import util.AlertUtil;
import util.ValidationUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FormPendaftaranController {

    @FXML
    private TextField txtId;

    @FXML
    private DatePicker dpTanggal;

    @FXML
    private ComboBox<Pasien> cbPasien;

    @FXML
    private ComboBox<Dokter> cbDokter;

    @FXML
    private TextArea txtKeluhan;

    private PendaftaranService pendaftaranService = new PendaftaranService();
    private PasienService pasienService = new PasienService();
    private DokterService dokterService = new DokterService();
    private boolean modeEdit = false;

    @FXML
    public void initialize() {
        try {
            cbPasien.setItems(pasienService.getAll());
            cbDokter.setItems(dokterService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeTambah() {
        modeEdit = false;
        dpTanggal.setValue(LocalDate.now());
    }

    public void setModeEdit(Pendaftaran p) {
        modeEdit = true;
        txtId.setText(String.valueOf(p.getIdDaftar()));
        
        if (p.getTanggal() != null) {
            dpTanggal.setValue(p.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        if (p.getPasien() != null) {
            for (Pasien pasien : cbPasien.getItems()) {
                if (pasien.getIdPasien() == p.getPasien().getIdPasien()) {
                    cbPasien.setValue(pasien);
                    break;
                }
            }
        }
        
        if (p.getDokter() != null) {
            for (Dokter dokter : cbDokter.getItems()) {
                if (dokter.getIdDokter() == p.getDokter().getIdDokter()) {
                    cbDokter.setValue(dokter);
                    break;
                }
            }
        }
        
        txtKeluhan.setText(p.getKeluhan());
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(dpTanggal, "Tanggal periksa wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(cbPasien, "Pasien wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(cbDokter, "Dokter wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtKeluhan, "Keluhan wajib diisi")) {
                return;
            }

            LocalDate localDate = dpTanggal.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Pendaftaran p = new Pendaftaran(
                    modeEdit ? Integer.parseInt(txtId.getText()) : 0,
                    date,
                    txtKeluhan.getText(),
                    cbPasien.getValue(),
                    cbDokter.getValue()
            );

            pendaftaranService.simpan(p, modeEdit);
            AlertUtil.success(modeEdit ? "Data pendaftaran berhasil diupdate" : "Data pendaftaran berhasil disimpan");
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
