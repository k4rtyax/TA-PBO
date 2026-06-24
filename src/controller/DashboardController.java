package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Pasien;
import service.PasienService;
import util.SceneUtil;
import database.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardController {
    @FXML
    private VBox sidebar;
    @FXML
    private Label logoTitle;
    @FXML
    private Button btnDashboard, btnPasien, btnDokter, btnPetugas, btnObat;
    private boolean collapsed = false;
    @FXML
    private Label lblMaster, lblTransaksi, lblLaporan;
    @FXML
    private Button btnPendaftaran, btnPemeriksaan, btnRekam, btnPrediksi;

    // DASHBOARD DYNAMIC ELEMENTS
    @FXML
    private Label lblTotalPasien;
    @FXML
    private Label lblTotalRekam;
    @FXML
    private Label lblTotalPrediksi;
    @FXML
    private Label lblTotalObat;

    @FXML
    private TableView<Pasien> tableDashboard;
    @FXML
    private TableColumn<Pasien, Integer> colId;
    @FXML
    private TableColumn<Pasien, String> colNama;
    @FXML
    private TableColumn<Pasien, Integer> colUmur;
    @FXML
    private TableColumn<Pasien, String> colGender;
    @FXML
    private TableColumn<Pasien, String> colAlamat;

    private PasienService pasienService = new PasienService();

    @FXML
    public void initialize() {
        // BIND TABLE COLUMNS
        colId.setCellValueFactory(new PropertyValueFactory<>("idPasien"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colUmur.setCellValueFactory(new PropertyValueFactory<>("umur"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        // LOAD STATISTICS AND TABLE DATA
        loadDashboardData();
    }

    public void loadDashboardData() {
        try {
            // Load Table Data
            tableDashboard.setItems(pasienService.getAll());
            tableDashboard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            // Load Statistics Counts
            Connection conn = DBConnection.connect();
            if (conn != null) {
                Statement st = conn.createStatement();
                
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM pasien");
                if (rs.next()) {
                    lblTotalPasien.setText(String.valueOf(rs.getInt(1)));
                }
                
                rs = st.executeQuery("SELECT COUNT(*) FROM rekam_medis");
                if (rs.next()) {
                    lblTotalRekam.setText(String.valueOf(rs.getInt(1)));
                }
                
                rs = st.executeQuery("SELECT COUNT(*) FROM prediksi");
                if (rs.next()) {
                    lblTotalPrediksi.setText(String.valueOf(rs.getInt(1)));
                }
                
                rs = st.executeQuery("SELECT COUNT(*) FROM obat");
                if (rs.next()) {
                    lblTotalObat.setText(String.valueOf(rs.getInt(1)));
                }
                
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleSidebar() {
        if (!collapsed) {
            sidebar.setPrefWidth(80);
            logoTitle.setVisible(false);
            lblMaster.setVisible(false);
            lblTransaksi.setVisible(false);
            lblLaporan.setVisible(false);
            btnDashboard.setText("🏠");
            btnPasien.setText("👨");
            btnDokter.setText("🩺");
            btnPetugas.setText("👩");
            btnObat.setText("💊");
            btnPendaftaran.setText("📝");
            btnPemeriksaan.setText("🩻");
            btnRekam.setText("📋");
            btnPrediksi.setText("🧠");
            collapsed = true;
        } else {
            sidebar.setPrefWidth(240);
            logoTitle.setVisible(true);
            lblMaster.setVisible(true);
            lblTransaksi.setVisible(true);
            lblLaporan.setVisible(true);
            logoTitle.setText("SMART CLINIC");
            btnDashboard.setText("🏠 Dashboard");
            btnPasien.setText("👨‍⚕ Pasien");
            btnDokter.setText("🩺 Dokter");
            btnPetugas.setText("👩‍💼 Petugas");
            btnObat.setText("💊 Obat");
            btnPendaftaran.setText("📝 Pendaftaran");
            btnPemeriksaan.setText("🩻 Pemeriksaan");
            btnRekam.setText("📋 Rekam Medis");
            btnPrediksi.setText("🧠 Prediksi ML");
            collapsed = false;
        }
    }

    @FXML
    private void openPasien() {
        SceneUtil.openMaximizedWindow("/view/pasien.fxml", "Data Pasien");
    }

    @FXML
    private void openDokter() {
        SceneUtil.openMaximizedWindow("/view/dokter.fxml", "Data Dokter");
    }

    @FXML
    private void openPetugas() {
        SceneUtil.openMaximizedWindow("/view/petugas.fxml", "Data Petugas");
    }

    @FXML
    private void openObat() {
        SceneUtil.openMaximizedWindow("/view/obat.fxml", "Data Obat");
    }

    @FXML
    private void openPendaftaran() {
        SceneUtil.openMaximizedWindow("/view/pendaftaran.fxml", "Pendaftaran Periksa");
    }

    @FXML
    private void openPemeriksaan() {
        SceneUtil.openMaximizedWindow("/view/pemeriksaan.fxml", "Pemeriksaan Dokter");
    }

    @FXML
    private void openRekam() {
        SceneUtil.openMaximizedWindow("/view/rekam_medis.fxml", "Rekam Medis");
    }

    @FXML
    private void openPrediksi() {
        SceneUtil.openMaximizedWindow("/view/prediksi.fxml", "Prediksi Risiko Diabetes");
    }
}