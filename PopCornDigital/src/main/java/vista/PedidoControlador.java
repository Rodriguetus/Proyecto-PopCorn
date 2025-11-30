package vista;


import dao.PedidoDAO;
import dto.pelicula;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PedidoControlador {


    @FXML
    private FlowPane flowCompras;


    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private final PedidoDAO pedidoDAO = new PedidoDAO();


    public PedidoControlador() {}


    @FXML
    public void initialize() {
        cargarCarrito();
    }


    /**
     * Carga el carrito desde CarritoService y genera las tarjetas de compra.
     */
    private void cargarCarrito() {


        flowCompras.getChildren().clear();


        for (pelicula p : CarritoService.getCarrito()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
                Node node = loader.load();

                CompraControlador ctrl = loader.getController();
                ctrl.setDatosPelicula(p);


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


    /**
     * Añade una compra nueva y la guarda en CarritoService.
     */
    public void addPurchase(pelicula p) {
        addPurchase(p, 1, "");
    }


    public void addPurchase(pelicula p, int quantity, String direccion) {


        dbExecutor.submit(() -> {
            try {


                boolean ok = PedidoDAO.createPedidoAndReduceStock(p.getId(), quantity, direccion);
                if (!ok) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Stock insuficiente");
                        alert.setHeaderText(null);
                        alert.setContentText("No hay stock suficiente para: " + p.getNombre());
                        alert.showAndWait();
                    });
                    return;
                }


                CarritoService.addCompra(p);


                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
                        Node node = loader.load();


                        CompraControlador ctrl = loader.getController();
                        ctrl.setDatosPelicula(p);


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
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No se pudo procesar la compra: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }


    public void shutdown() {
        dbExecutor.shutdown();
    }


    @FXML
    private void volverLogin(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void irPedido(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pedido.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void irCatalogo(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Catalogo.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

