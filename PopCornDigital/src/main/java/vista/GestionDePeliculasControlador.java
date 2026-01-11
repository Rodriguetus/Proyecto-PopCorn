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

public class GestionDePeliculasControlador {

    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVolver;
    @FXML private Button btnCerrar;

    @FXML private TextField txtNombre;
    @FXML private TextField txtAno;
    @FXML private TextField txtStock;
    @FXML private ComboBox<String> cmbFormato;
    @FXML private TextField txtGenero;
    @FXML private TextField txtProveedor;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtImagen;
    @FXML private TextField txtDescripcion;

    @FXML private TableView<pelicula> tablaPeliculas;
    @FXML private TableColumn<pelicula, String> colNombre;
    @FXML private TableColumn<pelicula, Integer> colAno;
    @FXML private TableColumn<pelicula, Double> colPrecio;
    @FXML private TableColumn<pelicula, Integer> colStock;
    @FXML private TableColumn<pelicula, String> colGenero;
    @FXML private TableColumn<pelicula, String> colFormato;
    @FXML private TableColumn<pelicula, String> colProveedor;
    @FXML private TableColumn<pelicula, String> colImagen;
    @FXML private TableColumn<pelicula, String> colDescripcion;

    private PeliculaDAO peliculaDAO;

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

    private void cargarPeliculas() throws SQLException {
        tablaPeliculas.setItems(
                FXCollections.observableArrayList(peliculaDAO.getPeliculas())
        );
    }

    @FXML
    private void anadirPelicula() throws SQLException {

        if (txtNombre.getText().isEmpty() || txtAno.getText().isEmpty()
                || txtStock.getText().isEmpty() || cmbFormato.getValue() == null
                || txtGenero.getText().isEmpty() || txtProveedor.getText().isEmpty()
                || txtPrecio.getText().isEmpty() || txtImagen.getText().isEmpty()
                || txtDescripcion.getText().isEmpty()) {

            mostrarAlerta("Rellena todos los campos antes de añadir.");
            return;
        }

        try {
            pelicula nueva = new pelicula();
            nueva.setNombre(txtNombre.getText());
            nueva.setAnoSalida(Integer.parseInt(txtAno.getText()));
            nueva.setStock(Integer.parseInt(txtStock.getText()));
            nueva.setFormato(cmbFormato.getValue());
            nueva.setGenero(txtGenero.getText());
            nueva.setProveedor(txtProveedor.getText());
            nueva.setPrecio(Double.parseDouble(txtPrecio.getText()));
            nueva.setImagen(txtImagen.getText());
            nueva.setDescripcion(txtDescripcion.getText());

            peliculaDAO.insertar(nueva);

            mostrarAlerta("Película añadida correctamente.");
            cargarPeliculas();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Valores numéricos inválidos.");
        }
    }

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

            if (!txtStock.getText().isEmpty())
                seleccionada.setStock(Integer.parseInt(txtStock.getText()));

            if (cmbFormato.getValue() != null)
                seleccionada.setFormato(cmbFormato.getValue());

            if (!txtGenero.getText().isEmpty())
                seleccionada.setGenero(txtGenero.getText());

            if (!txtProveedor.getText().isEmpty())
                seleccionada.setProveedor(txtProveedor.getText());

            if (!txtPrecio.getText().isEmpty())
                seleccionada.setPrecio(Double.parseDouble(txtPrecio.getText()));

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

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).show();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtAno.clear();
        txtStock.clear();
        cmbFormato.setValue(null);
        txtGenero.clear();
        txtProveedor.clear();
        txtPrecio.clear();
        txtImagen.clear();
        txtDescripcion.clear();
    }

    @FXML
    private void volverMenu() {
        cambiarVista("GestionDePedidos.fxml", btnVolver);
    }

    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", btnCerrar);
    }

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

