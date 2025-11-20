package vista;

import dao.PeliculaDAO;
import dto.pelicula;
import javafx.collections.*;
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

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtAno;
    @FXML private TextField txtStock;
    @FXML private TextField txtFormato;
    @FXML private TextField txtGenero;
    @FXML private TextField txtProveedor;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtImagen;

    @FXML private TableView<pelicula> tablaPeliculas;
    @FXML private TableColumn<pelicula, Integer> colId;
    @FXML private TableColumn<pelicula, String> colNombre;
    @FXML private TableColumn<pelicula, Integer> colAno;

    private PeliculaDAO peliculaDAO;

    @FXML
    public void initialize() {
        peliculaDAO = new PeliculaDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoSalida"));

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
    private void quitarPelicula() throws SQLException {
        pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selecciona una película para eliminar.");
            return;
        }

        boolean eliminado = peliculaDAO.eliminar(seleccionada.getId());

        if (eliminado) {
            mostrarAlerta("Película eliminada correctamente.");
            cargarPeliculas();
        } else {
            mostrarAlerta("Error al eliminar la película.");
        }
    }

    @FXML
    private void anadirPelicula() throws SQLException {
        if (txtNombre.getText().isEmpty() || txtAno.getText().isEmpty() || txtStock.getText().isEmpty() ||
                txtFormato.getText().isEmpty() || txtGenero.getText().isEmpty() || txtProveedor.getText().isEmpty() ||
                txtPrecio.getText().isEmpty() || txtImagen.getText().isEmpty()) {

            mostrarAlerta("Rellena todos los campos antes de añadir.");
            return;
        }

        try {
            pelicula nueva = new pelicula();
            nueva.setNombre(txtNombre.getText());
            nueva.setPrecio(Double.parseDouble(txtPrecio.getText()));
            nueva.setStock(Integer.parseInt(txtStock.getText()));
            nueva.setFormato(txtFormato.getText());
            nueva.setGenero(txtGenero.getText());
            nueva.setProveedor(txtProveedor.getText());
            nueva.setAnoSalida(Integer.parseInt(txtAno.getText()));
            nueva.setImagen(txtImagen.getText());

            peliculaDAO.insertar(nueva);

            mostrarAlerta("Película añadida correctamente.");
            cargarPeliculas();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Valores numéricos inválidos en Año, Stock o Precio.");
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
            if (!txtNombre.getText().isEmpty()) {
                seleccionada.setNombre(txtNombre.getText());
            }
            if (!txtAno.getText().isEmpty()) {
                seleccionada.setAnoSalida(Integer.parseInt(txtAno.getText()));
            }
            if (!txtStock.getText().isEmpty()) {
                seleccionada.setStock(Integer.parseInt(txtStock.getText()));
            }
            if (!txtFormato.getText().isEmpty()) {
                seleccionada.setFormato(txtFormato.getText());
            }
            if (!txtGenero.getText().isEmpty()) {
                seleccionada.setGenero(txtGenero.getText());
            }
            if (!txtProveedor.getText().isEmpty()) {
                seleccionada.setProveedor(txtProveedor.getText());
            }
            if (!txtPrecio.getText().isEmpty()) {
                seleccionada.setPrecio(Double.parseDouble(txtPrecio.getText()));
            }
            if (!txtImagen.getText().isEmpty()) {
                seleccionada.setImagen(txtImagen.getText());
            }

            peliculaDAO.modificar(seleccionada);

            mostrarAlerta("Película actualizada correctamente.");
            cargarPeliculas();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Valores numéricos inválidos en Año, Stock o Precio.");
        }
    }



    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.show();
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtAno.clear();
        txtStock.clear();
        txtFormato.clear();
        txtGenero.clear();
        txtProveedor.clear();
        txtPrecio.clear();
        txtImagen.clear();
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
            System.err.println("Error cargando vista: " + fxml);
            e.printStackTrace();
        }
    }
}


