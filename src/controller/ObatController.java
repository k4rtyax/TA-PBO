package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Obat;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import util.SessionUtil;
import service.ObatService;

public class ObatController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<Obat> tableObat;

    @FXML
    private TableColumn<Obat, Integer> colId;

    @FXML
    private TableColumn<Obat, String> colNama;

    @FXML
    private TableColumn<Obat, Integer> colStok;

    @FXML
    private TableColumn<Obat, Double> colHarga;

    ObservableList<Obat> list = FXCollections.observableArrayList();

    private ObatService obatService = new ObatService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idObat"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaObat"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));

        loadData();
        tableObat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_obat.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Obat", 450, 350);
            FormObatController controller = loader.getController();
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
            Obat obat = tableObat.getSelectionModel().getSelectedItem();
            if (obat == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_obat.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Obat", 450, 350);
            FormObatController controller = loader.getController();
            controller.setModeEdit(obat);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tableObat.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadData() {
        list.clear();
        list = obatService.getAll();
        tableObat.setItems(list);
    }

    @FXML
    public void handleHapus() {
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            Obat obat = tableObat.getSelectionModel().getSelectedItem();
            if (obat == null) {
                AlertUtil.warning("Pilih data obat dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data obat?");
            if (!confirm) {
                return;
            }
            obatService.delete(obat.getIdObat());
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
        list = obatService.search(txtCari.getText());
        tableObat.setItems(list);
    }
}
