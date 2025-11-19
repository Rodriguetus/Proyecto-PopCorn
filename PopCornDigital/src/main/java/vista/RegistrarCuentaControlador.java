package vista;
import dao.DaoAdministrador;
import dto.administrador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private void registrarCuenta(ActionEvent event) {
        try {
            String nombre = nombreusuarioTextField.getText();
            String correo = correoTextField.getText();
            String contrasena = contrasenaPasswordField.getText();


            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                System.out.println("❌ Todos los campos son obligatorios.");
                return;
            }


            administrador admin = new administrador(correo, nombre, contrasena);
            DaoAdministrador dao = new DaoAdministrador();


            boolean creado = dao.registrar(admin);


            if (creado) {
                System.out.println("✔ Cuenta creada correctamente.");


                Parent root = FXMLLoader.load(getClass().getResource("/vista/InicioSesion.fxml"));
                Stage stage = (Stage) nombreusuarioTextField.getScene().getWindow();
                stage.setScene(new Scene(root));


            } else {
                System.out.println("❌ Error al registrar la cuenta.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}