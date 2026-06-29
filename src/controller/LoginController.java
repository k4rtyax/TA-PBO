package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import util.SessionUtil;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    @FXML
    private Button btnLogin;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Tekan Enter di field password = langsung login
        txtPassword.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> handleLogin();
                default -> {}
            }
        });
        txtUsername.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> txtPassword.requestFocus();
                default -> {}
            }
        });
    }

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        lblError.setText("");
        btnLogin.setDisable(true);
        btnLogin.setText("⏳  Memverifikasi...");

        try {
            User user = userService.login(username, password);

            // Simpan session
            SessionUtil.setCurrentUser(user);

            // Buka Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/style/global.css").toExternalForm());

            // Dapatkan controller dashboard dan set role
            DashboardController dashCtrl = loader.getController();
            dashCtrl.applyRoleAccess(user);

            Stage stage = (Stage) btnLogin.getScene().getWindow();

            // Reset semua constraint ukuran dari window login
            // PENTING: harus dilakukan SEBELUM setScene + setMaximized
            // agar tidak ada sisa constraint yang menyebabkan fullscreen buggy
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setWidth(1200);
            stage.setHeight(800);

            stage.setScene(scene);
            stage.setTitle("Smart Clinic – " + user.getNama() + " [" + user.getRoleName() + "]");

            // Maximize setelah scene sudah diset
            stage.setMaximized(true);

        } catch (Exception e) {
            lblError.setText("⚠ " + e.getMessage());
            txtPassword.clear();
        } finally {
            btnLogin.setDisable(false);
            btnLogin.setText("🔐  Masuk");
        }
    }
}
