package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PedidoControlador {

    @FXML
    private FlowPane contenedorCompras;

    @FXML
    private Button btnGuardar;

    public void agregarCompra(Node compra) {
        contenedorCompras.getChildren().add(compra);
    }

    @FXML
    private void volverYPasarCompra() {

        try {
            // 1. Cargar compra.fxml
            FXMLLoader loaderCompra = new FXMLLoader(getClass().getResource("/vista/compra.fxml"));
            Parent compraNode = loaderCompra.load();

            // 2. Cargar pedido.fxml
            FXMLLoader loaderPedido = new FXMLLoader(getClass().getResource("/vista/Pedido.fxml"));
            Parent root = loaderPedido.load();

            // 3. Obtener controlador del pedido
            PedidoControlador pedidoControlador = loaderPedido.getController();

            // 4. Insertar la compra dentro del FlowPane
            pedidoControlador.agregarCompra(compraNode);

            // 5. Cambiar a la escena de Pedido
            Stage stage = (Stage) btnGuardar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR al volver y añadir la compra: " + e.getMessage());
        }
    }


    @FXML
    private void irPedidos(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pedido.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el acceso a los pedidos.fxml");
        }
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
            System.out.println("Error al cargar InicioSesion.fxml");
        }
    }

    //Funcion del boton de Catalogo
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
            System.out.println("Error al cargar el acceso a el catalogo.fxml");
        }
    }
}
