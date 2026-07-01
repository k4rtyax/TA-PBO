package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Jadwal;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import util.SessionUtil;
import service.JadwalService;

public class JadwalController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<Jadwal> tableJadwal;

    @FXML
    private TableColumn<Jadwal, Integer> colId;

    @FXML
    private TableColumn<Jadwal, String> colDokter;

    @FXML
    private TableColumn<Jadwal, String> colPoli;

    @FXML
    private TableColumn<Jadwal, String> colHari;

    @FXML
    private TableColumn<Jadwal, String> colJamMulai;

    @FXML
    private TableColumn<Jadwal, String> colJamSelesai;

    ObservableList<Jadwal> list = FXCollections.observableArrayList();

    private JadwalService jadwalService = new JadwalService();

    @FXML
    public void initialize() {
        // BIND TABLE COLUMN
        colId.setCellValueFactory(new PropertyValueFactory<>("idJadwal"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colPoli.setCellValueFactory(new PropertyValueFactory<>("namaPoli"));
        colHari.setCellValueFactory(new PropertyValueFactory<>("hari"));
        colJamMulai.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        colJamSelesai.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));

        // LOAD DATA MYSQL
        loadData();
        tableJadwal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        if (!SessionUtil.requireRole("Admin")) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_jadwal.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Jadwal", 500, 450);
            FormJadwalController controller = loader.getController();
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
        if (!SessionUtil.requireRole("Admin")) return;
        try {
            Jadwal jadwal = tableJadwal.getSelectionModel().getSelectedItem();
            if (jadwal == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_jadwal.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Jadwal", 500, 450);
            FormJadwalController controller = loader.getController();
            controller.setModeEdit(jadwal);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tableJadwal.getScene().getWindow();
        stage.close();
    }

    // LOAD TABLE
    @FXML
    public void loadData() {
        list.clear();
        list = jadwalService.getAll();
        tableJadwal.setItems(list);
    }

    @FXML
    public void handleHapus() {
        if (!SessionUtil.requireRole("Admin")) return;
        try {
            Jadwal jadwal = tableJadwal.getSelectionModel().getSelectedItem();
            if (jadwal == null) {
                AlertUtil.warning("Pilih data jadwal dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data jadwal?");
            if (!confirm) {
                return;
            }
            jadwalService.delete(jadwal.getIdJadwal());
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
        list = jadwalService.search(txtCari.getText());
        tableJadwal.setItems(list);
    }
}
