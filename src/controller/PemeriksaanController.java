package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Pemeriksaan;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import util.SessionUtil;
import service.PemeriksaanService;
import java.util.Date;

public class PemeriksaanController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<Pemeriksaan> tablePemeriksaan;

    @FXML
    private TableColumn<Pemeriksaan, Integer> colId;

    @FXML
    private TableColumn<Pemeriksaan, Date> colTanggal;

    @FXML
    private TableColumn<Pemeriksaan, String> colPasien;

    @FXML
    private TableColumn<Pemeriksaan, String> colDokter;

    @FXML
    private TableColumn<Pemeriksaan, String> colDiagnosa;

    @FXML
    private TableColumn<Pemeriksaan, Double> colTekanan;

    @FXML
    private TableColumn<Pemeriksaan, Double> colGula;

    @FXML
    private TableColumn<Pemeriksaan, Double> colSuhu;

    @FXML
    private TableColumn<Pemeriksaan, Double> colBerat;

    @FXML
    private TableColumn<Pemeriksaan, String> colHasilPrediksi;

    @FXML
    private TableColumn<Pemeriksaan, String> colResiko;

    ObservableList<Pemeriksaan> list = FXCollections.observableArrayList();

    private PemeriksaanService pemeriksaanService = new PemeriksaanService();

    @FXML
    public void initialize() {
        // BIND TABLE COLUMN
        colId.setCellValueFactory(new PropertyValueFactory<>("idPeriksa"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPeriksa"));
        colPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colDiagnosa.setCellValueFactory(new PropertyValueFactory<>("diagnosa"));
        colTekanan.setCellValueFactory(new PropertyValueFactory<>("tekananDarah"));
        colGula.setCellValueFactory(new PropertyValueFactory<>("gulaDarah"));
        colSuhu.setCellValueFactory(new PropertyValueFactory<>("suhu"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("beratBadan"));
        colHasilPrediksi.setCellValueFactory(new PropertyValueFactory<>("hasilPrediksi"));
        colResiko.setCellValueFactory(new PropertyValueFactory<>("tingkatResiko"));

        // LOAD DATA MYSQL
        loadData();
        tablePemeriksaan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_pemeriksaan.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Pemeriksaan", 800, 550);
            FormPemeriksaanController controller = loader.getController();
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
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            Pemeriksaan pemeriksaan = tablePemeriksaan.getSelectionModel().getSelectedItem();
            if (pemeriksaan == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_pemeriksaan.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Pemeriksaan", 800, 550);
            FormPemeriksaanController controller = loader.getController();
            controller.setModeEdit(pemeriksaan);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tablePemeriksaan.getScene().getWindow();
        stage.close();
    }

    // LOAD TABLE
    @FXML
    public void loadData() {
        list.clear();
        list = pemeriksaanService.getAll();
        tablePemeriksaan.setItems(list);
    }

    @FXML
    public void handleHapus() {
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            Pemeriksaan pemeriksaan = tablePemeriksaan.getSelectionModel().getSelectedItem();
            if (pemeriksaan == null) {
                AlertUtil.warning("Pilih data pemeriksaan dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data pemeriksaan?");
            if (!confirm) {
                return;
            }
            pemeriksaanService.delete(pemeriksaan.getIdPeriksa());
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
        list = pemeriksaanService.search(txtCari.getText());
        tablePemeriksaan.setItems(list);
    }
}
