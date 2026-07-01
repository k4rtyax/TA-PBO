package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Dokter;
import model.Jadwal;
import model.Poli;
import service.DokterService;
import service.JadwalService;
import service.PoliService;
import util.AlertUtil;
import util.ValidationUtil;

public class FormJadwalController {

    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<Dokter> cbDokter;

    @FXML
    private ComboBox<Poli> cbPoli;

    @FXML
    private ComboBox<String> cbHari;

    @FXML
    private TextField txtJamMulai;

    @FXML
    private TextField txtJamSelesai;

    private JadwalService jadwalService = new JadwalService();
    private DokterService dokterService = new DokterService();
    private PoliService poliService = new PoliService();
    private boolean modeEdit = false;

    @FXML
    public void initialize() {
        cbHari.getItems().addAll("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu");

        try {
            cbDokter.setItems(dokterService.getAll());
            cbPoli.setItems(poliService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeTambah() {
        modeEdit = false;
    }

    public void setModeEdit(Jadwal j) {
        modeEdit = true;
        txtId.setText(String.valueOf(j.getIdJadwal()));

        if (j.getDokter() != null) {
            for (Dokter d : cbDokter.getItems()) {
                if (d.getIdDokter() == j.getDokter().getIdDokter()) {
                    cbDokter.setValue(d);
                    break;
                }
            }
        }

        if (j.getPoli() != null) {
            for (Poli p : cbPoli.getItems()) {
                if (p.getIdPoli() == j.getPoli().getIdPoli()) {
                    cbPoli.setValue(p);
                    break;
                }
            }
        }

        cbHari.setValue(j.getHari());
        txtJamMulai.setText(j.getJamMulai());
        txtJamSelesai.setText(j.getJamSelesai());
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(cbDokter, "Dokter wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(cbPoli, "Poli wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(cbHari, "Hari wajib dipilih")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtJamMulai, "Jam mulai wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtJamSelesai, "Jam selesai wajib diisi")) {
                return;
            }

            if (!txtJamMulai.getText().trim().matches("\\d{2}:\\d{2}")
                    || !txtJamSelesai.getText().trim().matches("\\d{2}:\\d{2}")) {
                AlertUtil.warning("Format jam harus HH:MM, contoh 08:00");
                return;
            }

            Jadwal j = new Jadwal(
                    modeEdit ? Integer.parseInt(txtId.getText()) : 0,
                    cbDokter.getValue(),
                    cbPoli.getValue(),
                    cbHari.getValue(),
                    txtJamMulai.getText().trim(),
                    txtJamSelesai.getText().trim()
            );

            jadwalService.simpan(j, modeEdit);
            AlertUtil.success(modeEdit ? "Data jadwal berhasil diupdate" : "Data jadwal berhasil disimpan");
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
        Stage stage = (Stage) txtJamMulai.getScene().getWindow();
        stage.close();
    }
}
