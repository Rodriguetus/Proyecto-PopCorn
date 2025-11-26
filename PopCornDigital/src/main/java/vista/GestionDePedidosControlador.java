package vista;

import dao.PedidoDAO;
import dto.pedido;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class GestionDePedidosControlador {

    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVolver;
    @FXML private Button btnCerrar;

    @FXML private TableView<pedido> tablaPedidos;

    @FXML private TextField txtEstado;
    @FXML private TextField txtFechaCompra;
    @FXML private TextField txtFechaLlegada;
    @FXML private TextField txtPelicula;
    @FXML private TextField txtDireccion;

    @FXML private TableColumn<pedido, Integer> colId;
    @FXML private TableColumn<pedido, String> colEstado;
    @FXML private TableColumn<pedido, Date> colFechaCompra;
    @FXML private TableColumn<pedido, Date> colFechaLlegada;
    @FXML private TableColumn<pedido, Integer> colIdPelicula;
    @FXML private TableColumn<pedido, String> colDireccion;


    private PedidoDAO pedidoDAO;

    @FXML
    public void initialize() {
        pedidoDAO = new PedidoDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaCompra.setCellValueFactory(new PropertyValueFactory<>("fCompra"));
        colFechaLlegada.setCellValueFactory(new PropertyValueFactory<>("fLlegada"));
        colIdPelicula.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

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
    private void anadirPedido() {

        if (txtEstado.getText().isEmpty() ||
                txtFechaCompra.getText().isEmpty() ||
                txtFechaLlegada.getText().isEmpty() ||
                txtPelicula.getText().isEmpty() ||
                txtDireccion.getText().isEmpty()) {

            mostrarAlerta("Rellena todos los campos.");
            return;
        }

        pedido p = new pedido();
        p.setEstado(txtEstado.getText());
        p.setfCompra(Date.valueOf(txtFechaCompra.getText()));
        p.setfLlegada(Date.valueOf(txtFechaLlegada.getText()));
        p.setIdPelicula(Integer.parseInt(txtPelicula.getText()));
        p.setDireccion(txtDireccion.getText());

        pedidoDAO.insertar(p);

        mostrarAlerta("Pedido añadido.");
        limpiarCampos();
    }

    @FXML
    private void editarPedido() {

        pedido p = tablaPedidos.getSelectionModel().getSelectedItem();

        if (p == null) {
            mostrarAlerta("Selecciona un pedido.");
            return;
        }

        if (!txtEstado.getText().isEmpty())
            p.setEstado(txtEstado.getText());

        if (!txtFechaCompra.getText().isEmpty())
            p.setfCompra(Date.valueOf(txtFechaCompra.getText()));

        if (!txtFechaLlegada.getText().isEmpty())
            p.setfLlegada(Date.valueOf(txtFechaLlegada.getText()));

        if (!txtPelicula.getText().isEmpty())
            p.setIdPelicula(Integer.parseInt(txtPelicula.getText()));

        if (!txtDireccion.getText().isEmpty())
            p.setDireccion(txtDireccion.getText());

        pedidoDAO.modificar(p);

        mostrarAlerta("Pedido actualizado.");
        limpiarCampos();
    }

    @FXML
    private void quitarPedido() {

        pedido p = tablaPedidos.getSelectionModel().getSelectedItem();

        if (p == null) {
            mostrarAlerta("Selecciona un pedido.");
            return;
        }

        boolean ok = pedidoDAO.eliminar(p.getId());

        if (ok) mostrarAlerta("Pedido eliminado.");
        else mostrarAlerta("Error al eliminar.");
    }

    private void mostrarAlerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }

    private void limpiarCampos() {
        txtEstado.clear();
        txtFechaCompra.clear();
        txtFechaLlegada.clear();
        txtPelicula.clear();
        txtDireccion.clear();
    }

    @FXML
    private void volverMenu() {
        cambiarVista("MenuAdministrador.fxml", btnVolver);
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
}
