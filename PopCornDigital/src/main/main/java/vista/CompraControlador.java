package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CompraControlador {

@FXML
private Button nuevaCompra;

    /*Metodo para abrir las ventanas y omitir repetir codigo.
    private void abrirVentana(String rutaFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Stage stage = (Stage) correoTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void crearCompra(){

    }

}
