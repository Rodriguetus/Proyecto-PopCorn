package vista;

import conexion.conexionDB;
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
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

//====================
import dto.SesionIniciada;
//====================

public class InicioSesionControlador implements Initializable {

    @FXML
    private TextField correoTextField;

    @FXML
    private PasswordField contrasenaPasswordField;

    @FXML
    private Label errormsj;

    @FXML
    private Hyperlink linkRegistro;

    //Metodo para abrir las ventanas y omitir repetir codigo.
    private void abrirVentana(String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Stage stage = (Stage) correoTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void iniciarSesion() {
        errormsj.setText("");
        String correo = correoTextField.getText();
        String contrasena = contrasenaPasswordField.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            errormsj.setText("Rellene todos los campos");
            return;
        }

        try (Connection conn = conexionDB.getConnection()) {

            //Inicio Sesion(Si es Admin)
            String sqlAdmin = "SELECT * FROM administrador WHERE Correo=? AND Contrasena=?";
            PreparedStatement psAdmin;
            psAdmin = conn.prepareStatement(sqlAdmin);
            psAdmin.setString(1, correo);
            psAdmin.setString(2, contrasena);
            ResultSet rsAdmin = psAdmin.executeQuery();

            if (rsAdmin.next()) {
                abrirVentana("/vista/MenuAdministrador.fxml");
                return;
            }

            //Inicio Sesion(Si es Usuario)
            String sqlUsuario = "SELECT * FROM usuario WHERE Correo=? AND Contrasena=?";
            PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, correo);
            psUsuario.setString(2, contrasena);
            ResultSet rsUsuario = psUsuario.executeQuery();

            if (rsUsuario.next()) {
                //====================
                int idUsuario = rsUsuario.getInt("idUsuario");
                dto.SesionIniciada.setIdUsuario(idUsuario);
                //====================
                abrirVentana("/vista/Catalogo.fxml");
                return;
            }
            errormsj.setText("Correo o contraseña incorrectos.");

        } catch (SQLException e) {
            errormsj.setText("Error accediendo a la base de datos.");
            e.printStackTrace();
        }
    }

    //Boton para ir a RegistrarCuenta
    @FXML
    private void irARegistrarCuenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/RegistrarCuenta.fxml"));
            Stage stage = (Stage) correoTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        linkRegistro.setOnAction(e -> irARegistrarCuenta());
    }
}
