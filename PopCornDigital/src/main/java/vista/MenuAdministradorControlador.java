package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MenuAdministradorControlador {

    @FXML private Button btnGestPelicula;
    @FXML private Button btnGestPedido;
    @FXML private Button btnGestAlquiler;
    @FXML private Button btnCerrar;

    @FXML
    private void irGestionPelicula() {
        cambiarVista("GestionDePeliculas.fxml", btnGestPelicula);
    }

    @FXML
    private void irGestionPedido() {
        //cambiarVista("GestionDePedidos.fxml", btnGestPedido);
    }

    @FXML
    private void irGestionAlquiler() {
        //cambiarVista("GestionDeAlquileres.fxml", btnGestAlquiler);
    }

    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", btnCerrar);
    }

    private void cambiarVista(String fxml, Button origen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) origen.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error cargando vista: " + fxml);
            e.printStackTrace();
        }
    }
}

