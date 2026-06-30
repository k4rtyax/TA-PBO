
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Daftarkan font ikon secara eksplisit (selain via @font-face di CSS) supaya
        // glyph-nya pasti ter-load sebelum FXML manapun dirender.
        Font.loadFont(getClass().getResourceAsStream("/view/fonts/MaterialIcons-Regular.ttf"), 16);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style/global.css").toExternalForm());
        stage.setTitle("Smart Clinic – Login");
        stage.setScene(scene);
        stage.setMinWidth(420);
        stage.setMinHeight(580);
        stage.setWidth(520);
        stage.setHeight(680);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}