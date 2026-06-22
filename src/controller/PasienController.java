package controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Pasien;
import util.AlertUtil;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import util.SceneUtil;
import service.PasienService;

public class PasienController {

    @FXML
    private TextField txtId,
            txtCari;

    @FXML
    private TableView<Pasien> tablePasien;

    @FXML
    private TableColumn<Pasien,Integer> colId;

    @FXML
    private TableColumn<Pasien,String> colNama;

    @FXML
    private TableColumn<Pasien,Integer> colUmur;

    @FXML
    private TableColumn<Pasien,String> colGender;

    @FXML
    private TableColumn<Pasien,String> colAlamat;

    @FXML
    private TableColumn<Pasien,String> colHP;

    @FXML
    private TableColumn<Pasien,Double> colTekanan;

    @FXML
    private TableColumn<Pasien,Double> colGula;

    ObservableList<Pasien> list =
            FXCollections.observableArrayList();

    private PasienService pasienService = new PasienService();
    @FXML
    public void handleTambah() {
        try {
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/form_pasien.fxml"));
            Stage stage =SceneUtil.createModal(loader,"Tambah Pasien",800,420);
            FormPasienController controller =loader.getController();
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
            Pasien pasien =tablePasien.getSelectionModel().getSelectedItem();
            if (pasien == null) {
                AlertUtil.warning("Pilih data terlebih dahulu");
                return;
            }
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/view/form_pasien.fxml"));
            Stage stage =SceneUtil.createModal(loader,"Edit Pasien",800,420);
            FormPasienController controller = loader.getController();
            controller.setModeEdit(pasien);
            stage.showAndWait();
            loadData();
        } catch (Exception e) {
            AlertUtil.error("Gagal membuka form edit");
            e.printStackTrace();
        }
    }
     @FXML
        public void initialize(){
        
        // BIND TABLE COLUMN
        
        colId.setCellValueFactory(new PropertyValueFactory<>("idPasien"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colUmur.setCellValueFactory(new PropertyValueFactory<>("umur"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colHP.setCellValueFactory(new PropertyValueFactory<>("noHP"));
        colTekanan.setCellValueFactory(new PropertyValueFactory<>("tekananDarah"));
        colGula.setCellValueFactory(new PropertyValueFactory<>("gulaDarah"));
        // LOAD DATA MYSQL
        loadData();
        tablePasien.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        klikTable();
        }
    @FXML
    public void handleClose(){
        
        Stage stage =
                (Stage) tablePasien
                .getScene()
                .getWindow();

        stage.close();
    }
    // LOAD TABLE
    @FXML
    public void loadData(){
        list.clear();
        //list = pasienDAO.getAllPasien();
        list = pasienService.getAll();
        tablePasien.setItems(list);
    }

    @FXML
    public void handleHapus(){
        try{
            Pasien pasien =tablePasien.getSelectionModel().getSelectedItem();
            if(pasien == null){
                AlertUtil.warning("Pilih data pasien dahulu!");
                return;
            }
            boolean confirm =AlertUtil.confirm("Yakin ingin menghapus data?");
            if(!confirm){
                return;
            }
            pasienService.delete(pasien.getIdPasien());
            AlertUtil.success("Data berhasil dihapus");
            loadData();
        }catch(Exception e){
            AlertUtil.error(e.getMessage());
            e.printStackTrace();
        }
    }

    // SEARCH
    @FXML
    public void handleCari(){

        list.clear();
        //list = pasienDAO.searchPasien(txtCari.getText()
        list = pasienService.search(txtCari.getText()
        );
        tablePasien.setItems(list);
    }

    private void klikTable(){

 }
}