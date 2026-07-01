package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ResepObat;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import util.SessionUtil;
import service.ResepObatService;

public class ResepObatController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<ResepObat> tableResep;

    @FXML
    private TableColumn<ResepObat, Integer> colId;

    @FXML
    private TableColumn<ResepObat, String> colPasien;

    @FXML
    private TableColumn<ResepObat, String> colObat;

    @FXML
    private TableColumn<ResepObat, Integer> colJumlah;

    @FXML
    private TableColumn<ResepObat, String> colDosis;

    @FXML
    private TableColumn<ResepObat, String> colKeterangan;

    ObservableList<ResepObat> list = FXCollections.observableArrayList();

    private ResepObatService resepObatService = new ResepObatService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idResep"));
        colPasien.setCellValueFactory(cellData -> {
            ResepObat r = cellData.getValue();
            String namaPasien = (r.getPemeriksaan() != null) ? r.getPemeriksaan().getNamaPasien() : "";
            return new javafx.beans.property.SimpleStringProperty(namaPasien);
        });
        colObat.setCellValueFactory(cellData -> {
            ResepObat r = cellData.getValue();
            String namaObat = (r.getObat() != null) ? r.getObat().getNamaObat() : "";
            return new javafx.beans.property.SimpleStringProperty(namaObat);
        });
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colDosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        loadData();
        tableResep.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_resep_obat.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Resep Obat", 500, 450);
            FormResepObatController controller = loader.getController();
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
            ResepObat resep = tableResep.getSelectionModel().getSelectedItem();
            if (resep == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_resep_obat.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Resep Obat", 500, 450);
            FormResepObatController controller = loader.getController();
            controller.setModeEdit(resep);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tableResep.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadData() {
        list.clear();
        list = resepObatService.getAll();
        tableResep.setItems(list);
    }

    @FXML
    public void handleHapus() {
        if (!SessionUtil.requireRole("Admin", "Dokter")) return;
        try {
            ResepObat resep = tableResep.getSelectionModel().getSelectedItem();
            if (resep == null) {
                AlertUtil.warning("Pilih data resep obat dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data resep obat?");
            if (!confirm) {
                return;
            }
            resepObatService.delete(resep.getIdResep());
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
        list = resepObatService.search(txtCari.getText());
        tableResep.setItems(list);
    }
}
