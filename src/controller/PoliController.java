package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Poli;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import util.SessionUtil;
import service.PoliService;

public class PoliController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<Poli> tablePoli;

    @FXML
    private TableColumn<Poli, Integer> colId;

    @FXML
    private TableColumn<Poli, String> colNama;

    @FXML
    private TableColumn<Poli, String> colDeskripsi;

    @FXML
    private TableColumn<Poli, String> colLokasi;

    ObservableList<Poli> list = FXCollections.observableArrayList();

    private PoliService poliService = new PoliService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPoli"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaPoli"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));

        loadData();
        tablePoli.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        if (!SessionUtil.requireRole("Admin")) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_poli.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Poli", 450, 400);
            FormPoliController controller = loader.getController();
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
            Poli poli = tablePoli.getSelectionModel().getSelectedItem();
            if (poli == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_poli.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Poli", 450, 400);
            FormPoliController controller = loader.getController();
            controller.setModeEdit(poli);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tablePoli.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadData() {
        list.clear();
        list = poliService.getAll();
        tablePoli.setItems(list);
    }

    @FXML
    public void handleHapus() {
        if (!SessionUtil.requireRole("Admin")) return;
        try {
            Poli poli = tablePoli.getSelectionModel().getSelectedItem();
            if (poli == null) {
                AlertUtil.warning("Pilih data poli dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data poli?");
            if (!confirm) {
                return;
            }
            poliService.delete(poli.getIdPoli());
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
        list = poliService.search(txtCari.getText());
        tablePoli.setItems(list);
    }
}
