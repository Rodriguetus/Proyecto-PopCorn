package vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Clase principal de la aplicación JavaFX.
 * Se encarga de cargar la vista inicial y mostrar la ventana principal.
 */
public class Main extends Application {

    /**
     * Método de inicio de la aplicación JavaFX.
     * Carga el archivo FXML correspondiente y configura la ventana principal.
     *
     * @param primaryStage la ventana principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 700, 506);
            primaryStage.setTitle("Inicio Sesión - PopCorn");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/PopCorn.png")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal que lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
