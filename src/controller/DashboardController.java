package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Pasien;
import model.User;
import service.PasienService;
import util.SceneUtil;
import util.SessionUtil;
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
    @FXML
    private Button btnPoli, btnJadwal, btnResepObat;

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
    private Label lblUserInfo;

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
            setSidebarButtonsContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            collapsed = true;
        } else {
            sidebar.setPrefWidth(240);
            logoTitle.setVisible(true);
            lblMaster.setVisible(true);
            lblTransaksi.setVisible(true);
            lblLaporan.setVisible(true);
            logoTitle.setText("SMART CLINIC");
            setSidebarButtonsContentDisplay(ContentDisplay.LEFT);
            collapsed = false;
        }
    }

    private void setSidebarButtonsContentDisplay(ContentDisplay mode) {
        btnDashboard.setContentDisplay(mode);
        btnPasien.setContentDisplay(mode);
        btnDokter.setContentDisplay(mode);
        btnPetugas.setContentDisplay(mode);
        btnObat.setContentDisplay(mode);
        btnPoli.setContentDisplay(mode);
        btnJadwal.setContentDisplay(mode);
        btnPendaftaran.setContentDisplay(mode);
        btnPemeriksaan.setContentDisplay(mode);
        btnResepObat.setContentDisplay(mode);
        btnRekam.setContentDisplay(mode);
        btnPrediksi.setContentDisplay(mode);
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

    @FXML
    private void openPoli() {
        SceneUtil.openMaximizedWindow("/view/poli.fxml", "Data Poli");
    }

    @FXML
    private void openJadwal() {
        SceneUtil.openMaximizedWindow("/view/jadwal.fxml", "Jadwal Dokter");
    }

    @FXML
    private void openResepObat() {
        SceneUtil.openMaximizedWindow("/view/resep_obat.fxml", "Resep Obat");
    }

    // ==============================================
    // ROLE-BASED ACCESS CONTROL
    // ==============================================
    /**
     * Dipanggil oleh LoginController setelah login berhasil.
     * Menyembunyikan / menampilkan menu sesuai role.
     *
     * Role ID:
     *   1 = Admin    -> semua menu tampil
     *   2 = Petugas  -> Pasien, Pendaftaran (TANPA: Dokter, Petugas, Obat, Pemeriksaan, Rekam, Prediksi)
     *   3 = Dokter   -> Obat, Pemeriksaan, Rekam Medis, Prediksi (TANPA: Pasien, Dokter, Petugas, Pendaftaran)
     */
    public void applyRoleAccess(User user) {
        if (user == null) return;

        // Tampilkan info user di topbar
        if (lblUserInfo != null) {
            Label iconLabel = new Label("");
            iconLabel.getStyleClass().add("icon-mi");
            lblUserInfo.setGraphic(iconLabel);
            lblUserInfo.setContentDisplay(ContentDisplay.LEFT);
            lblUserInfo.setText(user.getNama() + "  |  " + user.getRoleName());
        }

        String role = user.getRoleName();
        if (role == null) role = "";

        switch (role) {
            case "Admin" -> {
                // Admin: semua menu tampil – tidak perlu menyembunyikan apapun
            }
            case "Petugas" -> {
                // Petugas: hanya Pasien + Pendaftaran
                btnDokter.setVisible(false);   btnDokter.setManaged(false);
                btnPetugas.setVisible(false);  btnPetugas.setManaged(false);
                btnObat.setVisible(false);     btnObat.setManaged(false);
                btnPoli.setVisible(false);     btnPoli.setManaged(false);
                btnJadwal.setVisible(false);   btnJadwal.setManaged(false);
                btnPemeriksaan.setVisible(false); btnPemeriksaan.setManaged(false);
                btnResepObat.setVisible(false); btnResepObat.setManaged(false);
                btnRekam.setVisible(false);    btnRekam.setManaged(false);
                btnPrediksi.setVisible(false); btnPrediksi.setManaged(false);
            }
            case "Dokter" -> {
                // Dokter: hanya Obat + Pemeriksaan + Resep + Rekam + Prediksi
                btnPasien.setVisible(false);   btnPasien.setManaged(false);
                btnDokter.setVisible(false);   btnDokter.setManaged(false);
                btnPetugas.setVisible(false);  btnPetugas.setManaged(false);
                btnPoli.setVisible(false);     btnPoli.setManaged(false);
                btnJadwal.setVisible(false);   btnJadwal.setManaged(false);
                btnPendaftaran.setVisible(false); btnPendaftaran.setManaged(false);
            }
            default -> {
                // Role tidak dikenal – sembunyikan semua sebagai keamanan
                btnPasien.setVisible(false);   btnPasien.setManaged(false);
                btnDokter.setVisible(false);   btnDokter.setManaged(false);
                btnPetugas.setVisible(false);  btnPetugas.setManaged(false);
                btnObat.setVisible(false);     btnObat.setManaged(false);
                btnPoli.setVisible(false);     btnPoli.setManaged(false);
                btnJadwal.setVisible(false);   btnJadwal.setManaged(false);
                btnPendaftaran.setVisible(false); btnPendaftaran.setManaged(false);
                btnPemeriksaan.setVisible(false); btnPemeriksaan.setManaged(false);
                btnResepObat.setVisible(false); btnResepObat.setManaged(false);
                btnRekam.setVisible(false);    btnRekam.setManaged(false);
                btnPrediksi.setVisible(false); btnPrediksi.setManaged(false);
            }
        }
    }

    // ==============================================
    // LOGOUT
    // ==============================================
    @FXML
    private void handleLogout() {
        try {
            SessionUtil.clearSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/style/global.css").toExternalForm());

            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMinWidth(420);
            stage.setMinHeight(580);
            stage.setWidth(480);
            stage.setHeight(620);
            stage.centerOnScreen();
            stage.setTitle("Smart Clinic – Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}