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

/**
 * Controlador encargado de la vista del saldo pudiendo tanto realizar una vista del saldo
 * que posee el usuario como el poder ingresar más saldo dentro de la cuenta.
 */
public class SaldoControlador implements Initializable {

    @FXML private Label labelCorreo;
    @FXML private Label labelSaldo;

    private final DaoUsuario daoUsuario = new DaoUsuario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDatosUsuario();
    }

    /**
     * Obtiene el correo electrónico del usuario mediante los getters de SesionIniciada para mostrarlos en la vista.
     */
    private void cargarDatosUsuario() {
        // Obtenemos datos de la sesión
        String correo = SesionIniciada.getCorreo();
        int idUsuario = SesionIniciada.getIdUsuario();

        labelCorreo.setText(correo);

        double saldoActual = daoUsuario.getSaldo(idUsuario);
        labelSaldo.setText(String.format("%.2f €", saldoActual));
    }

    /**
     * Abre un diálogo de entrada para que el usuario ingrese el saldo a su cuenta
     *
     * El metodo incorporarSaldo gestiona la validación de que sea una cantidad tanto numérica como positiva
     * y la actualiza de manera persistente en la base de datos.
     *
     * @param event El evento disparado con el botón al que se le ha hecho click.
     */
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
                    mostrarAlertaPersonalizada("Cantidad inválida", "Por favor introduce una cantidad positiva.");
                    return;
                }

                int idUsuario = SesionIniciada.getIdUsuario();
                boolean exito = daoUsuario.sumarSaldo(idUsuario, cantidad);

                if (exito) {
                    cargarDatosUsuario();

                    mostrarAlertaPersonalizada("Operación Exitosa", "Se ha aumentado el saldo de la cuenta con éxito.");
                } else {
                    mostrarAlertaPersonalizada("Error", "No se pudo conectar con la base de datos.");
                    }

            } catch (NumberFormatException e) {
                mostrarAlertaPersonalizada("Error de Formato","Por favor introduce un número válido." );
                }
        });
    }

    /**
     * Muestra una alerta informativa
     *
     * @param tipo El tipo de alerta
     * @param titulo El titulo de la ventana
     * @param contenido El contenido del mensaje informativo
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta personalizada con css
     *
     * @param titulo Titulo de la ventana
     * @param contenido Información principal del alert
     */
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

    //Metodos de navegación hacia las otras vistas.

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