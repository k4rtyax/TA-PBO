package controller;

import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
// import javafx.stage.Stage;
// import javafx.animation.TranslateTransition;
// import javafx.util.Duration;
import util.SceneUtil;

public class DashboardController {
    @FXML
    private VBox sidebar;
    @FXML
    private Label logoTitle;
    @FXML
    private Button btnDashboard,btnPasien,btnDokter,btnPetugas,btnObat;
    private boolean collapsed = false;
    @FXML
    private Label lblMaster, lblTransaksi, lblLaporan;
    @FXML
    private Button btnPendaftaran,btnPemeriksaan,btnRekam,btnPrediksi;

    @FXML
    private void toggleSidebar(){

        if(!collapsed){
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
        }else{
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
        SceneUtil.openMaximizedWindow("/view/pasien.fxml","Data Pasien");
    }
    @FXML
    private void openDokter() {
        //SceneUtil.openMaximizedWindow("/view/dokter.fxml","Data Dokter");
    }
    @FXML
    private void openPendaftaran() {
        //SceneUtil.openMaximizedWindow("/view/pendaftaran.fxml","Pendaftaran");
    }
      @FXML
    private void openPemeriksaan() {
        //SceneUtil.openMaximizedWindow("/view/pemeriksaan.fxml","pemeriksaan");
    }
    @FXML
    private void openPrediksi() {
        SceneUtil.openMaximizedWindow("/view/prediksi.fxml", "Prediksi Risiko Diabetes");
    }
}