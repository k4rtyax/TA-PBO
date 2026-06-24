package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import service.UserService;

public class PetugasController {

    @FXML
    private TextField txtCari;

    @FXML
    private TableView<User> tableUser;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colNama;

    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private TableColumn<User, String> colDokter;

    ObservableList<User> list = FXCollections.observableArrayList();

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        colDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));

        loadData();
        tableUser.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleTambah() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_petugas.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Tambah Petugas", 800, 420);
            FormPetugasController controller = loader.getController();
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
            User user = tableUser.getSelectionModel().getSelectedItem();
            if (user == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_petugas.fxml"));
            Stage stage = SceneUtil.createModal(loader, "Edit Petugas", 800, 420);
            FormPetugasController controller = loader.getController();
            controller.setModeEdit(user);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) tableUser.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadData() {
        list.clear();
        list = userService.getAll();
        tableUser.setItems(list);
    }

    @FXML
    public void handleHapus() {
        try {
            User user = tableUser.getSelectionModel().getSelectedItem();
            if (user == null) {
                AlertUtil.warning("Pilih data petugas dahulu!");
                return;
            }
            boolean confirm = AlertUtil.confirm("Yakin ingin menghapus data petugas?");
            if (!confirm) {
                return;
            }
            userService.delete(user.getIdUser());
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
        list = userService.search(txtCari.getText());
        tableUser.setItems(list);
    }
}
