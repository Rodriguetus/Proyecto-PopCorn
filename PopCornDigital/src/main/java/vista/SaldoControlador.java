package vista;

import dao.DaoUsuario;
import dto.SesionIniciada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SaldoControlador implements Initializable {

    @FXML private Label labelCorreo;
    @FXML private Label labelSaldo;

    private final DaoUsuario daoUsuario = new DaoUsuario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        // Obtenemos datos de la sesión
        String correo = SesionIniciada.getCorreo();
        int idUsuario = SesionIniciada.getIdUsuario();

        labelCorreo.setText(correo);

        double saldoActual = daoUsuario.getSaldo(idUsuario);
        labelSaldo.setText(String.format("%.2f €", saldoActual));
    }

    @FXML
    private void incorporarSaldo(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Incorporar Saldo");
        dialog.setHeaderText("Añadir fondos a tu cuenta");
        dialog.setContentText("Introduce la cantidad a ingresar (€):");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(cantidadStr -> {
            try {
                double cantidad = Double.parseDouble(cantidadStr.replace(",", "."));

                if (cantidad <= 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Cantidad inválida", "Por favor introduce una cantidad positiva.");
                    return;
                }

                int idUsuario = SesionIniciada.getIdUsuario();
                boolean exito = daoUsuario.sumarSaldo(idUsuario, cantidad);

                if (exito) {
                    cargarDatosUsuario();

                    mostrarAlertaPersonalizada("Operación Exitosa", "Se ha aumentado el saldo de la cuenta con éxito.");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo conectar con la base de datos.");
                }

            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "Por favor introduce un número válido.");
            }
        });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void mostrarAlertaPersonalizada(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);

        try {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/Alerta.css").toExternalForm());
            dialogPane.getStyleClass().add("alerta-popcorn");
        } catch (Exception e) {
        }
        alert.showAndWait();
    }

    @FXML
    private void irCatalogo(MouseEvent event) { navegar(event, "/vista/Catalogo.fxml", "Catálogo"); }
    @FXML
    private void irPedido(MouseEvent event) { navegar(event, "/vista/Pedido.fxml", "Mis Pedidos"); }
    @FXML
    private void irFavoritos(MouseEvent event) { navegar(event, "/vista/Favoritos.fxml", "Favoritos"); }
    @FXML
    private void irAlquileres(MouseEvent event) { navegar(event, "/vista/Alquileres.fxml", "Alquileres"); }
    @FXML
    private void volverLogin(MouseEvent event) { navegar(event, "/vista/InicioSesion.fxml", "Inicio Sesión"); }

    private void navegar(MouseEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}