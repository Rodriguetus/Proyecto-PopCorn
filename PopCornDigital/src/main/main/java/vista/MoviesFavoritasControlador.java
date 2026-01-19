package vista;

import conexion.conexionDB;
import dto.SesionIniciada;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

/**
 * Controlador encargado de gestionar la visualización de una película dentro de la sección de favoritos.
 * Permite mostrar los datos de la película y eliminarla de la lista de favoritos del usuario.
 */
public class MoviesFavoritasControlador {

    /** Imagen del póster de la película. */
    @FXML private ImageView posterImage;

    /** Label que muestra el nombre de la película. */
    @FXML private Label nombreLabel;

    /** Label que muestra el precio de la película. */
    @FXML private Label precioLabel;

    /** Label que muestra el stock disponible. */
    @FXML private Label stockLabel;

    /** Label que muestra el año de salida de la película. */
    @FXML private Label anoLabel;

    /** Label que muestra el formato de la película. */
    @FXML private Label formatoLabel;

    /** Label que muestra el proveedor de la película. */
    @FXML private Label proveedorLabel;

    /** Label que muestra el género de la película. */
    @FXML private Label generoLabel;

    /** Label que muestra la descripción de la película. */
    @FXML private Label descripcionLabel;

    /** Botón para eliminar la película de favoritos. */
    @FXML private Button btnEliminarFavorito;

    /** Objeto película asociado a esta tarjeta. */
    private pelicula peli;

    /**
     * Rellena la tarjeta con los datos de la película proporcionada.
     *
     * @param peli objeto {@link pelicula} que contiene los datos a mostrar
     */
    public void CrearMovieFav(pelicula peli) {
        this.peli = peli;

        nombreLabel.setText(peli.getNombre());
        precioLabel.setText(peli.getPrecio() + " €");
        stockLabel.setText("Stock: " + peli.getStock());
        anoLabel.setText("Año: " + peli.getAnoSalida());
        formatoLabel.setText("Formato: " + peli.getFormato());
        proveedorLabel.setText("Proveedor: " + peli.getProveedor());
        generoLabel.setText("Género: " + peli.getGenero());
        descripcionLabel.setText(peli.getDescripcion());

        if (peli.getImagen() != null && !peli.getImagen().isEmpty()) {
            try {
                Image img = new Image(getClass().getResource(peli.getImagen()).toExternalForm());
                posterImage.setImage(img);
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }

    /**
     * Elimina la película actual de la lista de favoritos del usuario.
     * Muestra un cuadro de confirmación antes de proceder.
     * Si la eliminación es exitosa, recarga la vista de favoritos.
     */
    @FXML
    private void eliminarDeFavoritos() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro quieres eliminar de Favoritos?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            int idUsuario = SesionIniciada.getIdUsuario();
            int idPelicula = peli.getId();

            String sql = "DELETE FROM favoritos WHERE idUsuario = ? AND idPelicula = ?";

            try (Connection conn = conexionDB.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idPelicula);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    System.out.println("Película eliminada de favoritos: " + peli.getNombre());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Favoritos.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) posterImage.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error al eliminar la película de favoritos.");
            }

        } else {
            System.out.println("Eliminación cancelada por el usuario.");
        }
    }

}
