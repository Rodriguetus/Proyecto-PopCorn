package vista;

import dao.PedidoDAO;
import dto.pelicula;
import dto.pedido;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PedidoControlador {

    //FlowPane encargado de almacernar las peliculas
    @FXML
    private FlowPane flowCompras;

    //Hilo para las opciones de la BDD
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    //Creación de una instancia PedidoDAO para poder usar el CRUD de Pedido
    private final PedidoDAO pedidoDAO = new PedidoDAO();


    public PedidoControlador() {}


    @FXML
    public void initialize() {
        //Carga el carrito
        cargarCarrito();
    }


    //Carga el carrito y genera las tarjetas correspondientes
    private void cargarCarrito() {
        flowCompras.getChildren().clear();

        // Simulación de usuario (o usa SesionIniciada.getCorreo() si lo tienes)
        String correoUsuario = "admin@gmail.com";

        for (pelicula p : CarritoService.getCarrito()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
                Node node = loader.load();
                CompraControlador ctrl = loader.getController();

                // 1. Buscamos el pedido real en la BD
                dto.pedido pedidoReal = pedidoDAO.buscarUltimoPedido(p.getId(), correoUsuario);

                // 2. Se lo pasamos a la tarjeta (Si es null, la tarjeta se mostrará pero no dejará pagar)
                ctrl.setDatosPelicula(p, pedidoReal);

                ctrl.setOnRemove(c -> {
                    flowCompras.getChildren().remove(ctrl.getRoot());
                    CarritoService.removeCompra(p);
                });

                flowCompras.getChildren().add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addPurchase(pelicula p) {
        addPurchase(p, 1, "");
    }

/*
Metodo para mostrar el Alert personalizado que implementa css
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


    //Añade una compra a la base de datos y al carrito modificando el stock
    public void addPurchase(pelicula p, int quantity, String direccion) {
        String correoUsuario = "admin@gmail.com"; // O SesionIniciada.getCorreo()

        dbExecutor.submit(() -> {
            try {
                // 1. CAMBIO: Ahora recibimos un int (idPedido)
                int idPedido = PedidoDAO.createPedidoAndReduceStock(p.getId(), quantity, direccion, correoUsuario);

                // Si devuelve -1 es que falló
                if (idPedido == -1) {
                    Platform.runLater(() -> {
                        mostrarAlertaPersonalizada("Stock insuficiente", "No hay stock suficiente o ocurrió un error.");
                    });
                    return;
                }

                // 2. CREAMOS EL OBJETO PEDIDO con los datos recién generados
                // Esto sirve para que la tarjeta tenga ID y sepa que está "Pendiente"
                dto.pedido nuevoPedido = new dto.pedido();
                nuevoPedido.setId(idPedido);
                nuevoPedido.setEstado("Pendiente");
                nuevoPedido.setfCompra(new java.util.Date()); // Fecha hoy
                nuevoPedido.setfLlegada(new java.util.Date(System.currentTimeMillis() + 7L * 24 * 3600 * 1000)); // Fecha +7 dias

                CarritoService.addCompra(p);

                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
                        Node node = loader.load();
                        CompraControlador ctrl = loader.getController();
                        ctrl.setDatosPelicula(p, nuevoPedido);

                        ctrl.setOnRemove(c -> {
                            flowCompras.getChildren().remove(ctrl.getRoot());
                            CarritoService.removeCompra(p);
                        });

                        flowCompras.getChildren().add(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }

    public void shutdown() {
        dbExecutor.shutdown();
    }

    //Navega hacia el Login
    @FXML
    private void volverLogin(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio Sesión - PopCorn");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Navega hacia el Pedido
    @FXML
    private void irPedido(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pedido.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Compra de Películas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Navega hacia Alquileres
    @FXML
    private void irAlquileres(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Alquileres.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Alquiler de Películas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Favoritos.fxml");
        }
    }

    //Navega hacia el Catalogo
    @FXML
    private void irCatalogo(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Catalogo.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Catálogo de Películas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Navega hacia Favoritos
    @FXML
    private void irFavoritos(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Favoritos.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lista Favoritos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Favoritos.fxml");
        }
    }
}