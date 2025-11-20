package vista;

import dao.DaoUsuario;
import dto.usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrarCuentaControlador implements Initializable {

    @FXML
    private TextField nombreusuarioTextField;

    @FXML
    private TextField correoTextField;

    @FXML
    private PasswordField contrasenaPasswordField;

    @FXML
    private PasswordField repetirContrasenaPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Hyperlink linkInicio;

    @FXML
    private void registrarCuenta(ActionEvent event) {

        errorLabel.setText("");

        String nombre = nombreusuarioTextField.getText();
        String correo = correoTextField.getText();
        String contrasena = contrasenaPasswordField.getText();
        String repetir = repetirContrasenaPasswordField.getText();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetir.isEmpty()) {
            errorLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!contrasena.equals(repetir)) {
            errorLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        usuario user = new usuario(correo, nombre, contrasena);
        DaoUsuario dao = new DaoUsuario();

        boolean creado = dao.registrar(user);

        if (!creado) {
            errorLabel.setText("No se pudo registrar. El correo ya existe.");
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/InicioSesion.fxml"));
            Stage stage = (Stage) nombreusuarioTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void irAInicioSesion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/InicioSesion.fxml"));
            Stage stage = (Stage) correoTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        linkInicio.setOnAction(e -> irAInicioSesion());
    }
}
