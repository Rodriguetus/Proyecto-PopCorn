package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class InicioSesionControlador implements Initializable {

//Botones y Enlaces a otros fxml
    @FXML
    private Button InicioSesionButton;

    @FXML
    private Hyperlink linkRegistro;

//TextFields para el usuario y la contraseña de la cuenta
    @FXML
    private TextField nombreusuarioTextField;

    @FXML
    private TextField contrasenaTextField;

//Mensaje que muestra si da error el login
    @FXML
    private Label errormsj;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        // Acción del enlace hacia el registro
        linkRegistro.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("RegistrarCuenta.fxml"));
                Stage stage = (Stage) linkRegistro.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {

        String usuario = nombreusuarioTextField.getText();
        String contrasena = contrasenaTextField.getText();

        if (usuario.isBlank() || contrasena.isBlank()) {
            errormsj.setText("Error: Debe introducir todos los datos.");
            return;
        }

        // Aquí iría la conexión con base de datos (opcional)
        // Ejemplo de validación simple:
        if (usuario.equals("admin") && contrasena.equals("1234")) {

            try {
                Parent root = FXMLLoader.load(getClass().getResource("MenuPrincipal.fxml"));
                Stage stage = (Stage) InicioSesionButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            errormsj.setText("Usuario o contraseña incorrectos.");
        }
    }

    // Método para crear cuenta (si se usa desde algún botón)
    public void crearCuenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("RegistrarCuenta.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 700, 500));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}