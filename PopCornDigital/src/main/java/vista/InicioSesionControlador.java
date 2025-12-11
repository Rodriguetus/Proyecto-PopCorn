package vista;

import conexion.conexionDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import dto.SesionIniciada;


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
    private void abrirVentana(String rutaFXML, String tituloVentana) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Stage stage = (Stage) correoTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(tituloVentana);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void iniciarSesion() {
        errormsj.setText("");
        String correo = correoTextField.getText();
        String contrasena = contrasenaPasswordField.getText();

        //Validación de todos los campos son obligatorios
        if(correo.isEmpty() && contrasena.isEmpty()){
            errormsj.setText("Todos los campos son obligatorios");
            return;
        }

        //Validación para Correo vacio.
        if (correo.isEmpty()) {
            errormsj.setText("Rellene el campo del correo electrónico");
            return;
        }

        //Validación para Contraseña vacia
        if (contrasena.isEmpty()) {
            errormsj.setText("Rellene el campo de contraseña.");
            return;
        }

        //Validación Longitud del Correo
        if(correo.length()<8){
            errormsj.setText("El campo del correo debe tener 8 o más carácteres");
            return;
        }

        //Comprobar que es un correo(que contenga un @)
        if(!correo.contains("@")){
            errormsj.setText("El correo no es válido. Debe contener un @");
        }

        //Validación Longitud de la Contraseña
        if(contrasena.length()<8){
            errormsj.setText("La contraseña debe contener 8 o más caracteres");
            return;
        }

        //Validación de que la contraseña contenga por lo menos una mayúscula y un número
        if (!contrasena.matches("^(?=.*[A-Z])(?=.*\\d).+$")) {
            errormsj.setText("La contraseña debe incluir mayúscula y número.");
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
                abrirVentana("/vista/MenuAdministrador.fxml", "Panel de Admin");
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
                SesionIniciada.setIdUsuario(idUsuario);
                //====================
                abrirVentana("/vista/Catalogo.fxml", "Catálogo de Películas");
                return;
            }

            //Alert para cuando este incorrecto el correo o contraseña
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al iniciar sesión");
            alert.setHeaderText(null); // sin cabecera
            alert.setContentText("Correo o contraseña incorrectos.");
            alert.showAndWait();

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
            stage.setTitle("Registrar Cuenta - PopCorn");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        linkRegistro.setOnAction(e -> irARegistrarCuenta());
    }
}
