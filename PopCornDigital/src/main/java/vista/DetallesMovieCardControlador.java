package vista;

import dto.pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DetallesMovieCardControlador {

    // Labels principales
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    // Labels de información extra
    @FXML private Label yearLabel;
    @FXML private Label formatLabel;
    @FXML private Label providerLabel;
    @FXML private Label genreLabel;
    @FXML private Label stockLabel;
    @FXML private Label priceLabel;

    // Botones
    @FXML private Button favoriteButton;
    @FXML private Button buyButton;
    @FXML private Button rentButton;

    // Imagen del póster
    @FXML private ImageView posterImage;

    /**
     * Método para rellenar la vista de detalles con los datos de la película.
     */
    public void setDetallesMovieCard(pelicula movie) {
        titleLabel.setText(movie.getNombre());
        descriptionLabel.setText(movie.getDescripcion()); // 👉 ahora sí desde la BD

        yearLabel.setText(String.valueOf(movie.getAnoSalida()));
        formatLabel.setText(movie.getFormato());
        providerLabel.setText(movie.getProveedor());
        genreLabel.setText(movie.getGenero());
        stockLabel.setText(String.valueOf(movie.getStock()));
        priceLabel.setText(movie.getPrecio() + " €");

        if (movie.getImagen() != null && !movie.getImagen().isEmpty()) {
            try {
                posterImage.setImage(new Image(getClass().getResource(movie.getImagen()).toExternalForm()));
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }

        if ("4K UHD".equalsIgnoreCase(movie.getFormato())) {
            rentButton.setVisible(true);
            buyButton.setVisible(false);
        } else {
            rentButton.setVisible(false);
            buyButton.setVisible(true);
        }
    }

}
