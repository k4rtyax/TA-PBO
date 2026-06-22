package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AlertUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class PrediksiController {

    @FXML private TextField txtPregnancies;
    @FXML private TextField txtGlucose;
    @FXML private TextField txtBloodPressure;
    @FXML private TextField txtSkinThickness;
    @FXML private TextField txtInsulin;
    @FXML private TextField txtBMI;
    @FXML private TextField txtDPF;
    @FXML private TextField txtAge;

    @FXML private Label lblHasil;
    @FXML private Label lblProbabilitas;

    @FXML
    private void handlePrediksi() {

        try {

            String pregnancies   = txtPregnancies.getText().trim();
            String glucose       = txtGlucose.getText().trim();
            String bloodPressure = txtBloodPressure.getText().trim();
            String skinThickness = txtSkinThickness.getText().trim();
            String insulin       = txtInsulin.getText().trim();
            String bmi           = txtBMI.getText().trim();
            String dpf           = txtDPF.getText().trim();
            String age           = txtAge.getText().trim();

            if (pregnancies.isEmpty() || glucose.isEmpty() || bloodPressure.isEmpty()
                    || skinThickness.isEmpty() || insulin.isEmpty()
                    || bmi.isEmpty() || dpf.isEmpty() || age.isEmpty()) {

                AlertUtil.error("Semua field harus diisi!");
                return;
            }

            String projectDir = new File("").getAbsolutePath();
            String scriptPath = projectDir + "/ml/predict.py";

            ProcessBuilder pb = new ProcessBuilder(
                    "python3", scriptPath,
                    pregnancies, glucose, bloodPressure,
                    skinThickness, insulin, bmi, dpf, age
            );

            pb.redirectErrorStream(false);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String output = reader.readLine();
            process.waitFor();

            if (output == null || output.isEmpty()) {
                AlertUtil.error("Prediksi gagal: tidak ada output dari Python.");
                return;
            }

            String[] parts = output.split("\\|");
            String hasil       = parts[0].trim();
            String probabilitas = parts.length > 1 ? parts[1].trim() : "";

            lblHasil.setText(hasil);
            lblProbabilitas.setText("Probabilitas: " + probabilitas);

            if (hasil.contains("TINGGI")) {
                lblHasil.setStyle(
                    "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
            } else {
                lblHasil.setStyle(
                    "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");
            }

        } catch (NumberFormatException e) {

            AlertUtil.error("Input harus berupa angka!");

        } catch (Exception e) {

            AlertUtil.error("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset() {

        txtPregnancies.clear();
        txtGlucose.clear();
        txtBloodPressure.clear();
        txtSkinThickness.clear();
        txtInsulin.clear();
        txtBMI.clear();
        txtDPF.clear();
        txtAge.clear();

        lblHasil.setText("-");
        lblHasil.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #64748b;");
        lblProbabilitas.setText("");
    }

    @FXML
    private void handleTutup() {

        Stage stage = (Stage) lblHasil.getScene().getWindow();
        stage.close();
    }
}
