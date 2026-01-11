package vista;

import dao.PedidoDAO;
import dao.DaoUsuario;
import dto.SesionIniciada;
import dto.pedido;
import dto.pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.function.Consumer;

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

/*
Inicializa los componentes cargando la vista.
 */
    @FXML
    public void initialize() {
        btnRemove.setOnAction(e -> {

            if (pedidoActual != null) {
                // Llamamos al DAO para que sume 1 al stock y borre la fila en BD
                boolean borrado = PedidoDAO.cancelarPedido(pedidoActual.getId());

                if (borrado) {
                    System.out.println("Pedido cancelado y stock recuperado.");
                } else {
                    System.out.println("Error al cancelar el pedido en BD.");
                }
            }

            if (onRemove != null) {
                onRemove.accept(this);
            } else {
                if (root.getParent() instanceof javafx.scene.layout.Pane pane) {
                    pane.getChildren().remove(root);
                }
            }
        });
    }
/*
Recibe los datos de la película de la compra para mostrarlos en la carta rellenando asi cada
etiqueta de esta misma.
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
/*
Metodo auxiliar que cambia los estados de Pendiente a Pagado de manera visual.
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

/*
Metodo que realiza al hacer click en el boton de confirmar compra verificando si hay saldo suficiente
reduciendo su saldo y actualizando el estadod del pedido de pendiente a pagado en la base de datos
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
/*
Metodo para implementar la alerta personalizada del css.
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
    public void setOnRemove(Consumer<CompraControlador> onRemove) { this.onRemove = onRemove; }
    public AnchorPane getRoot() { return root; }
}