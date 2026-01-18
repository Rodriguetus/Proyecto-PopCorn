package vista;

import dao.PedidoDAO;
import dao.DaoUsuario;
import dto.SesionIniciada;
import dto.pedido;
import dto.pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Controlador de la tarjeta de la compra de la pelicula.
 *
 * Gestiona la vista de los datos de la pelicula que se va a comprar ademas de la confirmación, eliminación o
 * detalles de esta.
 */
public class CompraControlador {

    @FXML private AnchorPane root;
    @FXML private ImageView imagenPelicula;
    @FXML private Label tituloLabel;
    @FXML private Label compraIdLabel;
    @FXML private Label cantidadLabel;
    @FXML private Label proveedorLabel;
    @FXML private Label fechaCompraLabel;
    @FXML private Label fechaEsperadaLabel;
    @FXML private Label estadoLabel;
    @FXML private Button btnRemove;
    @FXML private Button btnConfirmar;

    private pelicula pelicula;
    private pedido pedidoActual; // Guardamos el objeto pedido

    private Consumer<CompraControlador> onRemove;

    /**
     * Inicia el controlador y configura los eventos que hacen los botones
     *
     * Si el pedido esta Pagado y se queire devolver realiza un reembolso
     * Si el pedido esta Pendiente lo elimina de la base de datos si le da al botón de eliminar o
     * convierte su estado a Pagado si le da al botón de confirmar compra.
     * */
    @FXML
    public void initialize() {
        btnRemove.setOnAction(e -> {
            if (pedidoActual == null) return;

            boolean operacionExitosa = false;
            DaoUsuario daoUsuario = new DaoUsuario();
            PedidoDAO daoPedido = new PedidoDAO();

            //SI el pedido ya estaba PAGADO -> Hay que devolver el dinero y borrar
            if ("Pagado".equalsIgnoreCase(pedidoActual.getEstado())) {

                int idUsuario = SesionIniciada.getIdUsuario();
                double precio = pelicula.getPrecio();

                // 1. Devolvemos el dinero usando el metodo de sumar saldo.
                boolean devuelto = daoUsuario.sumarSaldo(idUsuario, precio);

                if (devuelto) {
                    // 2. Si se devolvió la plata, borramos el pedido de la BD
                    boolean eliminado = daoPedido.eliminarPedido(pedidoActual.getId());

                    if (eliminado) {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Compra Cancelada",
                                "Se ha eliminado la compra y se han devuelto " + precio + "€ a tu cuenta.");
                        operacionExitosa = true;
                    } else {
                        mostrarAlertaPersonalizada("Error Crítico", "Dinero devuelto pero error al borrar el registro.");
                    }
                } else {
                    mostrarAlertaPersonalizada("Error", "No se pudo devolver el saldo. La compra no se ha eliminado.");
                }
            }
            else {
                boolean eliminado = daoPedido.eliminarPedido(pedidoActual.getId());
                if (eliminado) {
                    System.out.println("Pedido pendiente eliminado.");
                    operacionExitosa = true;
                } else {
                    mostrarAlertaPersonalizada("Error", "No se pudo eliminar el pedido de la base de datos.");
                }
            }

            if (operacionExitosa) {
                if (onRemove != null) {
                    onRemove.accept(this);
                } else {
                    if (root.getParent() instanceof javafx.scene.layout.Pane pane) {
                        pane.getChildren().remove(root);
                    }
                }
            }
        });
    }
/**
 * Recibe los datos de la película de la compra para mostrarlos en la carta rellenando asi cada
 * etiqueta de esta misma.
 *
 * @param pelicula El objeto pelicula com la información del producto como su titulo, precio...
 * @param pedido EL objeto pedido al que esta asociado siendo null o en estado Pendiente
 */
    public void setDatosPelicula(pelicula pelicula, pedido pedido) {
        this.pelicula = pelicula;
        this.pedidoActual = pedido;

        tituloLabel.setText(pelicula.getNombre());
        proveedorLabel.setText("Proveedor: " + pelicula.getProveedor());
        cantidadLabel.setText("Cantidad: 1 unidad");

        if (pedido != null) {
            compraIdLabel.setText("Compra #" + pedido.getId());
            fechaCompraLabel.setText("Compra: " + pedido.getfCompra());
            fechaEsperadaLabel.setText("Entrega: " + pedido.getfLlegada());
            actualizarEstadoVisual(pedido.getEstado());
        } else {
            // Valores por defecto si aún no hay pedido (ej. vista carrito)
            compraIdLabel.setText("Pendiente de procesar");
            fechaCompraLabel.setText("--/--/----");
            fechaEsperadaLabel.setText("--/--/----");
            actualizarEstadoVisual("Pendiente");
        }

        if (pelicula.getImagen() != null && !pelicula.getImagen().isEmpty()) {
            try {
                imagenPelicula.setImage(
                        new Image(getClass().getResource(pelicula.getImagen()).toExternalForm())
                );
            } catch (Exception e) {
                System.out.println("No se pudo cargar la imagen: " + pelicula.getImagen());
            }
        }
    }

    /**
     * Actualiza el estado visual de la etiqueta de estado según la situación de la compra.
     * Habilita o deshabilita los botones dependiendo de en que estado este.
     *
     * @param estado El estado actual del pedido(Pendiente o Pagado)
     */
    private void actualizarEstadoVisual(String estado) {
        estadoLabel.setText("Estado: " + estado);
        if ("Pagado".equalsIgnoreCase(estado)) {
            estadoLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            if (btnConfirmar != null) btnConfirmar.setDisable(true);
        } else {
            estadoLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
            if (btnConfirmar != null) btnConfirmar.setDisable(false);
        }
    }

/**
 * Metodo que gestiona la accion de confirmar compra al hacer click en el boton
 *
 * Verifica si el usuario tiene saldo suficiente y si es asi actualiza el estado de la compra y resta el saldo
 * de su cuenta.
 *
 * @param event El evento de acción generado por el boton.
 */
    @FXML
    private void confirmarCompra(ActionEvent event) {
        // Si no hay pedido real cargado, no hacemos nada
        if (pedidoActual == null || "Pagado".equals(pedidoActual.getEstado())) {

            mostrarAlertaPersonalizada("Aviso", "No se puede procesar el pago de este elemento aún.");
            return;
        }

        int idUsuario = SesionIniciada.getIdUsuario();
        double precio = pelicula.getPrecio();

        DaoUsuario usuarioDAO = new DaoUsuario();
        double saldoActual = usuarioDAO.getSaldo(idUsuario);

        if (saldoActual >= precio) {
            boolean exito = usuarioDAO.restarSaldo(idUsuario, precio);
            if (exito) {
                PedidoDAO pedidoDAO = new PedidoDAO();
                pedidoDAO.actualizarEstado(pedidoActual.getId(), "Pagado");

                pedidoActual.setEstado("Pagado");
                actualizarEstadoVisual("Pagado");

                mostrarAlerta(Alert.AlertType.INFORMATION, "Pago realizado",
                        "Nuevo saldo: " + String.format("%.2f", (saldoActual - precio)) + "€");
            }
        } else {
            mostrarAlertaPersonalizada("Saldo insuficiente", "Te faltan " + String.format("%.2f", (precio - saldoActual)) + "€");
        }
    }

    /**
     * Abre la ventana de los detalles de la pelicula al hacer click en el AnchorPane.
     *
     * @param event El evento del ratón que dispara la acción.
     */
    @FXML
    private void verDetalles(MouseEvent event) {
        if (this.pelicula == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/DetallesMovieCard.fxml"));
            Parent root = loader.load();

            DetallesMovieCardControlador controlador = loader.getController();
            controlador.setDetallesMovieCard(this.pelicula);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalles - " + this.pelicula.getNombre());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: No se encuentra el archivo FXML.");
        }
    }

    /**
     * Muestra una alerta de JavaFx
     *
     * @param tipo El tipo de alerta que es
     * @param titulo El titulo de la ventana de la alerta.
     * @param contenido El mensaje principal de la alerta.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
/**
 * Muestra una alerta personalizada con estilos css.
 *
 * @param contenido El mensaje del error o información.
 * @param titulo El titulo de la alerta.
 */
    private void mostrarAlertaPersonalizada(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);

        //Aplicar Css
        // 1. Obtenemos el DialogPane de la alerta
        DialogPane dialogPane = alert.getDialogPane();

        // 2. Cargamos el CSS
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/Alerta.css").toExternalForm()
        );

        // 3. Añadimos la clase CSS específica
        dialogPane.getStyleClass().add("alerta-popcorn");
        alert.showAndWait();
    }

    public pelicula getPelicula() { return pelicula; }

    /**
     * Establece un callback que se ejecutará cuando se elmine la
     * tarjeta
     *
     * @param onRemove Acción a ejecutar al eliminar el elemento.
     */
    public void setOnRemove(Consumer<CompraControlador> onRemove) { this.onRemove = onRemove; }
    public AnchorPane getRoot() { return root; }
}