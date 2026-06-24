package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.RekamMedis;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import service.RekamMedisService;
import java.util.Date;

public class RekamMedisController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<RekamMedis> tableRekam;

    @FXML
    private TableColumn<RekamMedis, Integer> colId;

    @FXML
    private TableColumn<RekamMedis, Date> colTanggal;

    @FXML
    private TableColumn<RekamMedis, String> colPasien;

    @FXML
    private TableColumn<RekamMedis, String> colDokter;

    @FXML
    private TableColumn<RekamMedis, String> colDiagnosa;

    @FXML
    private TableColumn<RekamMedis, String> colRingkasan;

    ObservableList<RekamMedis> list = FXCollections.observableArrayList();

    private RekamMedisService rekamMedisService = new RekamMedisService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idRekam"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colDiagnosa.setCellValueFactory(new PropertyValueFactory<>("diagnosa"));
        colRingkasan.setCellValueFactory(new PropertyValueFactory<>("ringkasan"));

        loadData();
        tableRekam.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_rekam_medis.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Rekam Medis", 800, 420);
            FormRekamMedisController controller = loader.getController();
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
        try {
            RekamMedis rekam = tableRekam.getSelectionModel().getSelectedItem();
            if (rekam == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_rekam_medis.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Rekam Medis", 800, 420);
            FormRekamMedisController controller = loader.getController();
            controller.setModeEdit(rekam);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tableRekam.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadData() {
        list.clear();
        list = rekamMedisService.getAll();
        tableRekam.setItems(list);
    }

    @FXML
    public void handleHapus() {
        try {
            RekamMedis rekam = tableRekam.getSelectionModel().getSelectedItem();
            if (rekam == null) {
                AlertUtil.warning("Pilih data rekam medis dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data rekam medis?");
            if (!confirm) {
                return;
            }
            rekamMedisService.delete(rekam.getIdRekam());
            AlertUtil.success("Data berhasil dihapus");
            loadData();
        } catch (Exception e) {
            AlertUtil.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCari() {
        list.clear();
        list = rekamMedisService.search(txtCari.getText());
        tableRekam.setItems(list);
    }
}
