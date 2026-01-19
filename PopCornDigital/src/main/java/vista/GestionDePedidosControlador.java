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

/**
 * Controlador JavaFX encargado de la gestión de los pedidos del sistema.
 *
 * <p>
 * Esta clase permite visualizar, editar y actualizar el estado de los pedidos
 * realizados por los usuarios. Los pedidos se muestran en una tabla y su estado
 * puede modificarse mediante un desplegable.
 * </p>
 *
 * <p>
 * El controlador interactúa con la capa de acceso a datos a través de
 * {@link PedidoDAO}, encargándose de obtener y actualizar la información
 * persistida en la base de datos.
 * </p>
 *
 * <p>
 * Además, gestiona la navegación entre vistas administrativas y el cierre
 * de sesión del usuario.
 * </p>
 *
 * @author LaureanoCL
 * @version 1.0
 * @since 1.0
 */
public class GestionDePedidosControlador {

    /** Botón para editar el estado de un pedido */
    @FXML private Button btnEditar;

    /** Botón para volver al menú anterior */
    @FXML private Button btnVolver;

    /** Botón para cerrar la sesión */
    @FXML private Button btnCerrar;

    /** Tabla que muestra la lista de pedidos */
    @FXML private TableView<pedido> tablaPedidos;

    /** ComboBox para seleccionar el estado del pedido */
    @FXML private ComboBox<String> cmbEstado;

    /** Columna que muestra el identificador del pedido */
    @FXML private TableColumn<pedido, Integer> colId;

    /** Columna que muestra el estado del pedido */
    @FXML private TableColumn<pedido, String> colEstado;

    /** Columna que muestra la fecha de compra */
    @FXML private TableColumn<pedido, String> colFechaCompra;

    /** Columna que muestra la fecha estimada de llegada */
    @FXML private TableColumn<pedido, String> colFechaLlegada;

    /** Columna que muestra el identificador de la película asociada */
    @FXML private TableColumn<pedido, Integer> colIdPelicula;

    /** Columna que muestra la dirección de envío */
    @FXML private TableColumn<pedido, String> colDireccion;

    /** Objeto DAO para el acceso a los datos de pedidos */
    private PedidoDAO pedidoDAO;

    /**
     * Inicializa el controlador y configura los componentes de la vista.
     *
     * <p>
     * Define las columnas de la tabla, inicializa el ComboBox de estados,
     * establece los listeners necesarios y carga los pedidos desde la
     * base de datos.
     * </p>
     */
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

    /**
     * Carga la lista de pedidos desde la base de datos.
     *
     * @throws SQLException si ocurre un error durante el acceso a los datos
     */
    private void cargarPedido() throws SQLException {
        tablaPedidos.setItems(
                FXCollections.observableArrayList(pedidoDAO.getPedidos())
        );
    }

    /**
     * Modifica el estado del pedido seleccionado.
     *
     * <p>
     * Valida que exista un pedido seleccionado y que se haya elegido
     * un estado válido antes de actualizar la información en la base
     * de datos.
     * </p>
     */
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

    /**
     * Vuelve al menú de gestión de películas.
     */
    @FXML
    private void volverMenu() {
        cambiarVista("GestionDePeliculas.fxml", btnVolver);
    }

    /**
     * Cierra la sesión actual y redirige a la pantalla de inicio de sesión.
     */
    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", btnCerrar);
    }

    /**
     * Cambia la vista actual cargando un nuevo archivo FXML.
     *
     * @param fxml nombre del archivo FXML que se desea cargar
     * @param origen botón desde el que se obtiene la ventana actual
     */
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

    /**
     * Muestra una alerta informativa al usuario.
     *
     * @param msg mensaje que se desea mostrar
     */
    private void mostrarAlerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
