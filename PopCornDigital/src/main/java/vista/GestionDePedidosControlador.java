package vista;

import dao.PedidoDAO;
import dto.pedido;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class GestionDePedidosControlador {

    @FXML private Button btnEditar;
    @FXML private Button btnVolver;
    @FXML private Button btnCerrar;

    @FXML private TableView<pedido> tablaPedidos;

    @FXML private ComboBox<String> cmbEstado;

    @FXML private TableColumn<pedido, Integer> colId;
    @FXML private TableColumn<pedido, String> colEstado;
    @FXML private TableColumn<pedido, String> colFechaCompra;
    @FXML private TableColumn<pedido, String> colFechaLlegada;
    @FXML private TableColumn<pedido, Integer> colIdPelicula;
    @FXML private TableColumn<pedido, String> colDireccion;

    private PedidoDAO pedidoDAO;

    @FXML
    public void initialize() {
        pedidoDAO = new PedidoDAO();

        // Columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaCompra.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getfCompra().toString()));
        colFechaLlegada.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getfLlegada().toString()));
        colIdPelicula.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        cmbEstado.setItems(
                FXCollections.observableArrayList("pendiente", "pagado")
        );

        tablaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldPedido, newPedido) -> {
                    if (newPedido != null) {
                        cmbEstado.setValue(newPedido.getEstado());
                    }
                }
        );

        try {
            cargarPedido();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarPedido() throws SQLException {
        tablaPedidos.setItems(
                FXCollections.observableArrayList(pedidoDAO.getPedidos())
        );
    }

    @FXML
    private void editarPedido() {

        pedido p = tablaPedidos.getSelectionModel().getSelectedItem();

        if (p == null) {
            mostrarAlerta("Selecciona un pedido.");
            return;
        }

        String estado = cmbEstado.getValue();

        if (estado == null) {
            mostrarAlerta("Selecciona un estado.");
            return;
        }

        p.setEstado(estado);
        pedidoDAO.modificar(p);

        mostrarAlerta("Pedido actualizado.");
        tablaPedidos.refresh();
    }

    @FXML
    private void volverMenu() {
        cambiarVista("GestionDePeliculas.fxml", btnVolver);
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
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
