package vista;

import dao.PedidoDAO;
import dao.DaoUsuario;
import dto.SesionIniciada;
import dto.pedido;
import dto.pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
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
            //Confirmación de cancelar compra
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Cancelación");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que deseas eliminar este pedido?");
            try {
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/css/Alerta.css").toExternalForm());
                dialogPane.getStyleClass().add("alerta-popcorn");
            } catch (Exception ex) {}
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (pedidoActual == null) {
                    if (onRemove != null) {
                        onRemove.accept(this);
                    } else if (root.getParent() instanceof javafx.scene.layout.Pane pane) {
                        pane.getChildren().remove(root);
                    }
                    // También debemos quitarlo del servicio del carrito
                    CarritoService.removeCompra(pelicula);
                    return;
                }

                boolean operacionExitosa = false;
                DaoUsuario daoUsuario = new DaoUsuario();
                PedidoDAO daoPedido = new PedidoDAO();

                //SI el pedido ya estaba PAGADO -> Hay que devolver el dinero y borrar
                if ("Pagado".equalsIgnoreCase(pedidoActual.getEstado())) {

                    int idUsuario = SesionIniciada.getIdUsuario();
                    double precio = pelicula.getPrecio(); // O pedidoActual.getPrecioTotal() si lo tienes

                    // 1. Devolvemos el dinero usando el metodo de sumar saldo.
                    boolean devuelto = daoUsuario.sumarSaldo(idUsuario, precio);

                    if (devuelto) {
                        // 2. Si se devolvió la plata, borramos el pedido de la BD
                        boolean eliminado = daoPedido.eliminarPedido(pedidoActual.getId());

                        if (eliminado) {
                            mostrarAlertaPersonalizada("Compra Cancleada", "Se ha eliminado la compra y se han devuelto \" + precio + \"€ a tu cuenta.");
                            operacionExitosa = true;
                        } else {
                            mostrarAlertaPersonalizada("Error Crítico", "Dinero devuelto pero error al borrar el registro.");
                        }
                    } else {
                        mostrarAlertaPersonalizada("Error", "No se pudo devolver el saldo. La compra no se ha eliminado.");
                    }
                } else {
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
            }
        });
    }
    /*
    Recibe los datos de la película de la compra para mostrarlos en la carta rellenando asi cada
    etiqueta de esta misma.
     */
    public void setDatosPelicula(pelicula pelicula, pedido pedido) {
        this.pelicula = pelicula;
        this.pedidoActual = pedido; // Aquí se guarda el ID real que viene de la BD

        tituloLabel.setText(pelicula.getNombre());
        proveedorLabel.setText("Proveedor: " + pelicula.getProveedor());
        cantidadLabel.setText("Cantidad: 1 unidad");

        // Si el pedido es null O el estado es Pendiente, forzamos los guiones
        if (pedido == null || "Pendiente".equalsIgnoreCase(pedido.getEstado())) {
            compraIdLabel.setText("Pendiente de procesar");
            fechaCompraLabel.setText("--/--/----");
            fechaEsperadaLabel.setText("--/--/----");
            actualizarEstadoVisual("Pendiente");
        } else {
            // Solo cuando esté "Pagado" mostrará los datos reales
            compraIdLabel.setText("Compra #" + pedido.getId());
            fechaCompraLabel.setText("Compra: " + pedido.getfCompra());
            fechaEsperadaLabel.setText("Entrega: " + pedido.getfLlegada());
            actualizarEstadoVisual(pedido.getEstado());
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
                setDatosPelicula(this.pelicula, this.pedidoActual);
                mostrarAlertaPersonalizada("Pago realizado", "Nuevo saldo: \" + String.format(\"%.2f\", (saldoActual - precio)) + \"€");
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

    @FXML
    private void verDetalles(MouseEvent event) {
        if (this.pelicula == null) return;

        try {
            // Carga la vista de detalles
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/DetallesMovieCard.fxml"));
            Parent root = loader.load();

            // Pasa la película al controlador de detalles
            // Nota: Asegúrate de que el nombre del controlador sea el correcto
            DetallesMovieCardControlador controlador = loader.getController();
            controlador.setDetallesMovieCard(this.pelicula);

            // Crea y muestra la nueva ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalles - " + this.pelicula.getNombre());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: No se encuentra el archivo FXML de detalles.");
        }
    }

    public pelicula getPelicula() { return pelicula; }
    public void setOnRemove(Consumer<CompraControlador> onRemove) { this.onRemove = onRemove; }
    public AnchorPane getRoot() { return root; }
}