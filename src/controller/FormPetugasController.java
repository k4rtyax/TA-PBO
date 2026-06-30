package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Dokter;
import model.User;
import service.DokterService;
import service.UserService;
import util.AlertUtil;
import util.ValidationUtil;

public class FormPetugasController {

    @FXML
    private TextField txtId,
            txtNama,
            txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private ComboBox<Dokter> cbDokter;

    private UserService userService = new UserService();
    private DokterService dokterService = new DokterService();
    private boolean modeEdit = false;

    @FXML
    public void initialize() {
        cbRole.getItems().addAll("Admin", "Petugas", "Dokter");
        
        try {
            cbDokter.setItems(dokterService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Disable cbDokter if selected role is not Dokter
        cbRole.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Dokter".equals(newVal)) {
                cbDokter.setDisable(false);
            } else {
                cbDokter.setValue(null);
                cbDokter.setDisable(true);
            }
        });
    }

    public void setModeTambah() {
        modeEdit = false;
        cbRole.setValue("Petugas");
    }

    public void setModeEdit(User u) {
        modeEdit = true;
        txtId.setText(String.valueOf(u.getIdUser()));
        txtNama.setText(u.getNama());
        txtUsername.setText(u.getUsername());
        txtPassword.setText("");
        txtPassword.setPromptText("Kosongkan jika tidak ingin mengubah password");

        if (u.getIdRole() == 1) {
            cbRole.setValue("Admin");
        } else if (u.getIdRole() == 3) {
            cbRole.setValue("Dokter");
        } else {
            cbRole.setValue("Petugas");
        }

        if (u.getIdDokter() != null) {
            for (Dokter d : cbDokter.getItems()) {
                if (d.getIdDokter() == u.getIdDokter()) {
                    cbDokter.setValue(d);
                    break;
                }
            }
        }
    }

    @FXML
    public void handleSimpan() {
        try {
            if (ValidationUtil.isEmpty(txtNama, "Nama wajib diisi")) {
                return;
            }
            if (ValidationUtil.isEmpty(txtUsername, "Username wajib diisi")) {
                return;
            }
            if (!modeEdit && ValidationUtil.isEmpty(txtPassword, "Password wajib diisi")) {
                return;
            }
            if (cbRole.getValue() == null) {
                AlertUtil.warning("Role wajib dipilih");
                return;
            }

            int idRole = 2; // Default Petugas
            String selectedRole = cbRole.getValue();
            if ("Admin".equals(selectedRole)) {
                idRole = 1;
            } else if ("Dokter".equals(selectedRole)) {
                idRole = 3;
            }

            Integer idDokter = null;
            if (idRole == 3) {
                if (cbDokter.getValue() == null) {
                    AlertUtil.warning("Dokter terkait wajib dipilih jika role adalah Dokter");
                    return;
                }
                idDokter = cbDokter.getValue().getIdDokter();
            }

            User u = new User(
                    modeEdit ? Integer.parseInt(txtId.getText()) : 0,
                    txtNama.getText(),
                    txtUsername.getText(),
                    txtPassword.getText(),
                    idRole,
                    idDokter
            );

            userService.simpan(u, modeEdit);
            AlertUtil.success(modeEdit ? "Data petugas berhasil diupdate" : "Data petugas berhasil disimpan");
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
