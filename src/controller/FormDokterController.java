package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Dokter;
import service.DokterService;
import util.AlertUtil;
import util.ValidationUtil;

public class FormDokterController {

    @FXML
    private TextField txtId,
            txtNama,
            txtSpesialis,
            txtHP,
            txtAlamat;

    private DokterService dokterService = new DokterService();
    private boolean modeEdit = false;

    @FXML
    public void initialize() {
    }

    public void setModeTambah() {
        modeEdit = false;
    }

    public void setModeEdit(Dokter d) {
        modeEdit = true;
        txtId.setText(String.valueOf(d.getIdDokter()));
        txtNama.setText(d.getNama());
        txtSpesialis.setText(d.getSpesialis());
        txtHP.setText(d.getNoHP());
        txtAlamat.setText(d.getAlamat());
    }

    @FXML
    public void handleSimpan() {
        try {
            // =====================
            // VALIDASI
            // =====================
            if (ValidationUtil.isEmpty(txtNama, "Nama dokter wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtSpesialis, "Spesialis wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtHP, "Nomor HP wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtAlamat, "Alamat wajib diisi")) {
                return;
            }

            // =====================
            // OBJECT
            // =====================
            Dokter d = new Dokter(
                    modeEdit ? Integer.parseInt(txtId.getText()) : 0,
                    txtNama.getText(),
                    txtSpesialis.getText(),
                    txtHP.getText(),
                    txtAlamat.getText()
            );

            // =====================
            // SERVICE
            // =====================
            dokterService.simpan(d, modeEdit);
            AlertUtil.success(modeEdit ? "Data berhasil diupdate" : "Data berhasil disimpan");
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
        Stage stage = (Stage) txtNama.getScene().getWindow();
        stage.close();
    }
}
