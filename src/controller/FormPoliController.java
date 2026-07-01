package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Poli;
import service.PoliService;
import util.AlertUtil;
import util.ValidationUtil;

public class FormPoliController {

    @FXML
    private TextField txtId,
            txtNamaPoli,
            txtLokasi;

    @FXML
    private TextArea txtDeskripsi;

    private PoliService poliService = new PoliService();
    private boolean modeEdit = false;

    public void setModeTambah() {
        modeEdit = false;
    }

    public void setModeEdit(Poli p) {
        modeEdit = true;
        txtId.setText(String.valueOf(p.getIdPoli()));
        txtNamaPoli.setText(p.getNamaPoli());
        txtLokasi.setText(p.getLokasi());
        txtDeskripsi.setText(p.getDeskripsi());
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(txtNamaPoli, "Nama poli wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtLokasi, "Lokasi wajib diisi")) {
                return;
            }

            Poli p = new Poli(
                    modeEdit ? Integer.parseInt(txtId.getText()) : 0,
                    txtNamaPoli.getText(),
                    txtDeskripsi.getText(),
                    txtLokasi.getText()
            );

            poliService.simpan(p, modeEdit);
            AlertUtil.success(modeEdit ? "Data poli berhasil diupdate" : "Data poli berhasil disimpan");
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
        Stage stage = (Stage) txtNamaPoli.getScene().getWindow();
        stage.close();
    }
}
