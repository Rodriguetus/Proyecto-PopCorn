package vista;


import dao.DaoAdministrador;
import dto.administrador;
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


public class InicioSesionControlador implements Initializable {


    @FXML
    private TextField correoTextField;


    @FXML
    private PasswordField contrasenaPasswordField;


    @FXML
    private void iniciarSesion() {
        String correo = correoTextField.getText();
        String contrasena = contrasenaPasswordField.getText();


        DaoAdministrador dao = new DaoAdministrador();
        administrador admin = dao.login(correo, contrasena);


        if (admin != null) {
            System.out.println("✔ Inicio de sesión exitoso: " + admin.getNombre());


            try {
                Parent root = FXMLLoader.load(getClass().getResource("/vista/MenuPrincipal.fxml"));
                Stage stage = (Stage) correoTextField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("❌ Credenciales incorrectas.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}