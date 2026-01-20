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

/**
 * Controlador principal para la gestión visual de los pedidos
 *
 * Se encarga de mostrar la películas añadidas al carrito, gestiona la creación de nuevos pedidos
 * de la base de datos y la navegación hacia las otras funciones de la aplicación.
 */
public class PedidoControlador {

    //FlowPane encargado de almacernar las peliculas
    @FXML
    private FlowPane flowCompras;

    //Hilo para las opciones de la BDD
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    //Creación de una instancia PedidoDAO para poder usar el CRUD de Pedido
    private final PedidoDAO pedidoDAO = new PedidoDAO();


    public PedidoControlador() {
    }

    /**
     * Inicializa el controlador cargando los elementos del carrito al arrancar la vista.
     */
    @FXML
    public void initialize() {
        //Carga el carrito
        cargarCarrito();
    }


    /**
     * Carga las películas almacenadas dentro del servicio del carrito y genera las tarjetas de estas
     * para mostrarlas dentro de la interfaz.
     */
    private void cargarCarrito() {
        flowCompras.getChildren().clear();
        String correoUsuario = dto.SesionIniciada.getCorreo();
        var historial = pedidoDAO.obtenerHistorialPedidos(correoUsuario);

        for (PedidoDAO.ParPedidoPelicula par : historial) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
                Node node = loader.load();
                CompraControlador ctrl = loader.getController();

                ctrl.setDatosPelicula(par.pelicula, par.pedido);

                ctrl.setOnRemove(c -> {
                    flowCompras.getChildren().remove(ctrl.getRoot());
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

    /**
     * Muestra una alerta personalizada con css
     *
     * @param titulo Titulo de la ventana
     * @param contenido Mensaje de error
     */
    private void mostrarAlertaPersonalizada(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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


    /**
     * Registra una nueva compra en la base de datos y actualiza la interfaz.
     *
     * @param p La pelicula que desea comprar
     * @param quantity La cantidad de unidades
     * @param direccion La dirección de envio
     */
    public void addPurchase(pelicula p, int quantity, String direccion) {
        String correoUsuario = dto.SesionIniciada.getCorreo();

        dbExecutor.submit(() -> {
            try {
                int idPedido = PedidoDAO.createPedidoAndReduceStock(p.getId(), quantity, direccion, correoUsuario);

                // Si devuelve -1 es que falló
                if (idPedido == -1) {
                    Platform.runLater(() -> {
                        mostrarAlertaPersonalizada("Stock insuficiente", "No hay stock suficiente o ocurrió un error.");
                    });
                    return;
                }

                pedido nuevoPedido = new pedido();
                nuevoPedido.setId(idPedido);
                nuevoPedido.setEstado("Pendiente");
                nuevoPedido.setfCompra(new java.util.Date());
                nuevoPedido.setfLlegada(new java.util.Date(System.currentTimeMillis() + 7L * 24 * 3600 * 1000)); // Fecha +7 dias

                CarritoService.addCompra(p);

                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
                        Node node = loader.load();
                        CompraControlador ctrl = loader.getController();

                        // Al pasar el pedido con estado "Pendiente", se activará la vista de guiones en la tarjeta
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
                    mostrarAlertaPersonalizada("Error", e.getMessage());
                });
            }
        });
    }

    public void shutdown() {
        dbExecutor.shutdown();
    }

    //Metodos de navegacion

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

    @FXML
    public void irSaldo(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Saldo.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mi Cartera - PopCorn");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Saldo.fxml");
        }
    }
}