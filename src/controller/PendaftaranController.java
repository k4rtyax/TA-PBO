package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Pendaftaran;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import util.SessionUtil;
import service.PendaftaranService;
import java.util.Date;

public class PendaftaranController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<Pendaftaran> tablePendaftaran;

    @FXML
    private TableColumn<Pendaftaran, Integer> colId;

    @FXML
    private TableColumn<Pendaftaran, Date> colTanggal;

    @FXML
    private TableColumn<Pendaftaran, String> colPasien;

    @FXML
    private TableColumn<Pendaftaran, String> colDokter;

    @FXML
    private TableColumn<Pendaftaran, String> colKeluhan;

    ObservableList<Pendaftaran> list = FXCollections.observableArrayList();

    private PendaftaranService pendaftaranService = new PendaftaranService();

    @FXML
    public void initialize() {
        // BIND TABLE COLUMN
        colId.setCellValueFactory(new PropertyValueFactory<>("idDaftar"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));

        // LOAD DATA MYSQL
        loadData();
        tablePendaftaran.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        if (!SessionUtil.requireRole("Admin", "Petugas")) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_pendaftaran.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Pendaftaran", 800, 420);
            FormPendaftaranController controller = loader.getController();
            controller.setModeTambah();
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEdit() {
        if (!SessionUtil.requireRole("Admin", "Petugas")) return;
        try {
            Pendaftaran pendaftaran = tablePendaftaran.getSelectionModel().getSelectedItem();
            if (pendaftaran == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_pendaftaran.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Pendaftaran", 800, 420);
            FormPendaftaranController controller = loader.getController();
            controller.setModeEdit(pendaftaran);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tablePendaftaran.getScene().getWindow();
        stage.close();
    }

    // LOAD TABLE
    @FXML
    public void loadData() {
        list.clear();
        list = pendaftaranService.getAll();
        tablePendaftaran.setItems(list);
    }

    @FXML
    public void handleHapus() {
        if (!SessionUtil.requireRole("Admin", "Petugas")) return;
        try {
            Pendaftaran pendaftaran = tablePendaftaran.getSelectionModel().getSelectedItem();
            if (pendaftaran == null) {
                AlertUtil.warning("Pilih data pendaftaran dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data pendaftaran?");
            if (!confirm) {
                return;
            }
            pendaftaranService.delete(pendaftaran.getIdDaftar());
            AlertUtil.success("Data berhasil dihapus");
            loadData();
        } catch (Exception e) {
            AlertUtil.error(e.getMessage());
            e.printStackTrace();
        }
    }

    // SEARCH
    @FXML
    public void handleCari() {
        list.clear();
        list = pendaftaranService.search(txtCari.getText());
        tablePendaftaran.setItems(list);
    }
}
