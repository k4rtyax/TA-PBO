package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
public class DBConnection {
    private static Connection conn;

    public static synchronized Connection connect() {
        try {
            if (conn != null && !conn.isClosed()) return conn;
        } catch (SQLException ignored) {
            // koneksi lama sudah tidak valid, lanjut buat baru di bawah
        }
        try {
            // LOAD DRIVER
            Class.forName("com.mysql.cj.jdbc.Driver");
            String pass = System.getenv("SMARTCLINIC_DB_PASSWORD");
            if (pass == null) pass = "";
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smart_clinic",
                "root",
                pass
            );
            System.out.println("Koneksi berhasil!");
            return conn;
        } catch (Exception e) {
            System.out.println("Koneksi gagal!");
            e.printStackTrace();
            showAlert("ERROR", e.getMessage());
            return null;
        }
    }

    private static void showAlert(
        String title,String msg) {Alert alert =new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
