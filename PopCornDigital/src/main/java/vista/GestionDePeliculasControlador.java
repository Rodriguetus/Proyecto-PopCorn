package vista;

import dao.PeliculaDAO;
import dto.pelicula;
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
 * Controlador JavaFX encargado de la gestión de las películas del sistema.
 *
 * <p>
 * Esta clase permite realizar operaciones CRUD sobre las películas:
 * añadir, editar y eliminar registros. Las películas se muestran en
 * una tabla y se gestionan mediante un formulario asociado.
 * </p>
 *
 * <p>
 * El controlador se comunica con la capa de acceso a datos a través de
 * {@link PeliculaDAO}, encargándose de la persistencia de la información.
 * </p>
 *
 * <p>
 * También gestiona la navegación entre vistas administrativas y el
 * cierre de sesión del usuario.
 * </p>
 *
 * @author LaureanoCL
 * @version 1.0
 * @since 1.0
 */
public class GestionDePeliculasControlador {

    /** Botón para añadir una nueva película */
    @FXML private Button btnAnadir;

    /** Botón para editar la película seleccionada */
    @FXML private Button btnEditar;

    /** Botón para eliminar la película seleccionada */
    @FXML private Button btnQuitar;

    /** Botón para volver al menú anterior */
    @FXML private Button btnVolver;

    /** Botón para cerrar la sesión */
    @FXML private Button btnCerrar;

    /** Campo de texto para el nombre de la película */
    @FXML private TextField txtNombre;

    /** Campo de texto para el año de salida */
    @FXML private TextField txtAno;

    /** Campo de texto para el stock disponible */
    @FXML private TextField txtStock;

    /** ComboBox para seleccionar el formato de la película */
    @FXML private ComboBox<String> cmbFormato;

    /** Campo de texto para el género */
    @FXML private TextField txtGenero;

    /** Campo de texto para el proveedor */
    @FXML private TextField txtProveedor;

    /** Campo de texto para el precio */
    @FXML private TextField txtPrecio;

    /** Campo de texto para la ruta de la imagen */
    @FXML private TextField txtImagen;

    /** Campo de texto para la descripción */
    @FXML private TextField txtDescripcion;

    /** Tabla que muestra la lista de películas */
    @FXML private TableView<pelicula> tablaPeliculas;

    /** Columnas de la tabla de películas */
    @FXML private TableColumn<pelicula, String> colNombre;
    @FXML private TableColumn<pelicula, Integer> colAno;
    @FXML private TableColumn<pelicula, Double> colPrecio;
    @FXML private TableColumn<pelicula, Integer> colStock;
    @FXML private TableColumn<pelicula, String> colGenero;
    @FXML private TableColumn<pelicula, String> colFormato;
    @FXML private TableColumn<pelicula, String> colProveedor;
    @FXML private TableColumn<pelicula, String> colImagen;
    @FXML private TableColumn<pelicula, String> colDescripcion;

    /** Ruta de la imagen por defecto */
    private static final String IMAGEN_POR_DEFECTO = "imagenes/default.jpg";

    /** DAO encargado del acceso a datos de películas */
    private PeliculaDAO peliculaDAO;

    /**
     * Inicializa el controlador y configura los componentes de la vista.
     *
     * <p>
     * Define las columnas de la tabla, inicializa el ComboBox de formatos,
     * establece los listeners necesarios y carga las películas desde la
     * base de datos.
     * </p>
     */
    @FXML
    public void initialize() {
        peliculaDAO = new PeliculaDAO();

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoSalida"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        cmbFormato.setItems(
                FXCollections.observableArrayList("DVD", "Blu-ray", "4K UHD")
        );

        // Desactivar stock si el formato es 4K UHD
        cmbFormato.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("4K UHD".equals(newVal)) {
                txtStock.clear();
                txtStock.setDisable(true);
            } else {
                txtStock.setDisable(false);
            }
        });

        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldPeli, newPeli) -> {
                    if (newPeli != null) {
                        cmbFormato.setValue(newPeli.getFormato());
                    }
                }
        );

        try {
            cargarPeliculas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de películas desde la base de datos.
     *
     * @throws SQLException si ocurre un error durante el acceso a datos
     */
    private void cargarPeliculas() throws SQLException {
        tablaPeliculas.setItems(
                FXCollections.observableArrayList(peliculaDAO.getPeliculas())
        );
    }

    /**
     * Añade una nueva película al sistema.
     *
     * <p>
     * Valida los campos obligatorios y los valores numéricos antes
     * de insertar la película en la base de datos.
     * </p>
     *
     * @throws SQLException si ocurre un error durante la inserción
     */
    @FXML
    private void anadirPelicula() throws SQLException {

        if (txtNombre.getText().isEmpty()
                || txtAno.getText().isEmpty()
                || cmbFormato.getValue() == null
                || txtGenero.getText().isEmpty()
                || txtProveedor.getText().isEmpty()
                || txtPrecio.getText().isEmpty()
                || txtDescripcion.getText().isEmpty()) {

            mostrarAlerta("Rellena todos los campos obligatorios.");
            return;
        }

        try {
            int ano = Integer.parseInt(txtAno.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = txtStock.isDisabled() ? 0 : Integer.parseInt(txtStock.getText());

            if (precio < 0 || stock < 0) {
                mostrarAlerta("El precio y el stock no pueden ser negativos.");
                return;
            }

            if ("4K UHD".equals(cmbFormato.getValue()) && stock != 0) {
                mostrarAlerta("El formato 4K UHD no puede tener stock.");
                return;
            }

            pelicula nueva = new pelicula();
            nueva.setNombre(txtNombre.getText());
            nueva.setAnoSalida(ano);
            nueva.setStock(stock);
            nueva.setFormato(cmbFormato.getValue());
            nueva.setGenero(txtGenero.getText());
            nueva.setProveedor(txtProveedor.getText());
            nueva.setPrecio(precio);
            nueva.setImagen(
                    txtImagen.getText().isEmpty() ? IMAGEN_POR_DEFECTO : txtImagen.getText()
            );
            nueva.setDescripcion(txtDescripcion.getText());

            peliculaDAO.insertar(nueva);

            mostrarAlerta("Película añadida correctamente.");
            cargarPeliculas();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Valores numéricos inválidos.");
        }
    }

    /**
     * Edita la película seleccionada en la tabla.
     *
     * @throws SQLException si ocurre un error durante la modificación
     */
    @FXML
    private void editarPelicula() throws SQLException {

        pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selecciona una película para editar.");
            return;
        }

        try {
            if (!txtNombre.getText().isEmpty())
                seleccionada.setNombre(txtNombre.getText());

            if (!txtAno.getText().isEmpty())
                seleccionada.setAnoSalida(Integer.parseInt(txtAno.getText()));

            if (!txtPrecio.getText().isEmpty()) {
                double precio = Double.parseDouble(txtPrecio.getText());
                if (precio < 0) {
                    mostrarAlerta("El precio no puede ser negativo.");
                    return;
                }
                seleccionada.setPrecio(precio);
            }

            if (!txtStock.isDisabled() && !txtStock.getText().isEmpty()) {
                int stock = Integer.parseInt(txtStock.getText());
                if (stock < 0) {
                    mostrarAlerta("El stock no puede ser negativo.");
                    return;
                }
                seleccionada.setStock(stock);
            }

            if (cmbFormato.getValue() != null) {
                seleccionada.setFormato(cmbFormato.getValue());
                if ("4K UHD".equals(cmbFormato.getValue())) {
                    seleccionada.setStock(null);
                }
            }

            if (!txtGenero.getText().isEmpty())
                seleccionada.setGenero(txtGenero.getText());

            if (!txtProveedor.getText().isEmpty())
                seleccionada.setProveedor(txtProveedor.getText());

            if (!txtImagen.getText().isEmpty())
                seleccionada.setImagen(txtImagen.getText());

            if (!txtDescripcion.getText().isEmpty())
                seleccionada.setDescripcion(txtDescripcion.getText());

            peliculaDAO.modificar(seleccionada);

            mostrarAlerta("Película actualizada correctamente.");
            cargarPeliculas();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Valores numéricos inválidos.");
        }
    }

    /**
     * Elimina la película seleccionada del sistema.
     *
     * @throws SQLException si ocurre un error durante la eliminación
     */
    @FXML
    private void quitarPelicula() throws SQLException {

        pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selecciona una película para eliminar.");
            return;
        }

        if (peliculaDAO.eliminar(seleccionada.getId())) {
            mostrarAlerta("Película eliminada correctamente.");
            cargarPeliculas();
        } else {
            mostrarAlerta("Error al eliminar la película.");
        }
    }

    /**
     * Muestra una alerta informativa al usuario.
     *
     * @param mensaje mensaje a mostrar
     */
    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).show();
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos() {
        txtNombre.clear();
        txtAno.clear();
        txtStock.clear();
        txtStock.setDisable(false);
        cmbFormato.setValue(null);
        txtGenero.clear();
        txtProveedor.clear();
        txtPrecio.clear();
        txtImagen.clear();
        txtDescripcion.clear();
    }

    /**
     * Vuelve al menú de gestión de pedidos.
     */
    @FXML
    private void volverMenu() {
        cambiarVista("GestionDePedidos.fxml", btnVolver);
    }

    /**
     * Cierra la sesión actual y vuelve a la pantalla de inicio de sesión.
     */
    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", btnCerrar);
    }

    /**
     * Cambia la vista actual cargando un nuevo archivo FXML.
     *
     * @param fxml nombre del archivo FXML
     * @param origen botón desde el que se obtiene la ventana actual
     */
    private void cambiarVista(String fxml, Button origen) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) origen.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
