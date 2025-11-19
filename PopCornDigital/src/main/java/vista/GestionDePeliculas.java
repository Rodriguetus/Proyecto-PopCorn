package vista;

import dao.PeliculaDAO;
import dto.pelicula;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GestionDePeliculas {
    private final ObservableList<pelicula> peliculas = FXCollections.observableArrayList();

    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVolver;

    @FXML private TableView<pelicula> tablaPeliculas;
    @FXML private TableColumn<pelicula, Integer> colId;
    @FXML private TableColumn<pelicula, String> colNombre;

    private PeliculaDAO peliculaDAO;

    @FXML
    public void initialize() throws SQLException {

        peliculaDAO = new PeliculaDAO();

        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));

        cargarPeliculas();
    }

    private void cargarPeliculas() throws SQLException {
        ObservableList<pelicula> lista = FXCollections.observableArrayList(
                peliculaDAO.getPeliculas());
        tablaPeliculas.setItems(lista);
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


    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.show();
    }
}

