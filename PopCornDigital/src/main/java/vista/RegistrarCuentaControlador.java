package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrarCuentaControlador implements Initializable {

    //Botones y enlaces
    @FXML
    private Hyperlink linkInicio;

    @FXML
    private TextField nombreusuarioTextField;

    @FXML
    private TextField contrasenaTextField;

    @FXML
    private TextField nombreusuarioTextField1; // correo electrónico

    @FXML
    private TextField contrasenaTextField1; // repetir contraseña

    @FXML
    private Button InicioSesionButton;

    // Imagen del logo (asegúrate de poner fx:id="popcornImage" en el FXML)
    @FXML
    private ImageView popcornImage;

    public void initialize(URL url, ResourceBundle resource) {
        // Acción del enlace hacia el registro
        linkInicio.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("InicioSesion.fxml"));
                Stage stage = (Stage) linkInicio.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void crearCuenta() {
        String usuario = nombreusuarioTextField.getText();
        String correo = nombreusuarioTextField1.getText();
        String contrasena = contrasenaTextField.getText();
        String repetirContrasena = contrasenaTextField1.getText();

        if (usuario.isBlank() || correo.isBlank() || contrasena.isBlank() || repetirContrasena.isBlank()) {
            System.out.println("Error: Debe rellenar todos los campos.");
            return;
        }

        if (!contrasena.equals(repetirContrasena)) {
            System.out.println("Error: Las contraseñas no coinciden.");
            return;
        }

        // Aquí iría la lógica de guardar en base de datos
        System.out.println("Cuenta creada correctamente para: " + usuario + " con correo " + correo);
    }
}
