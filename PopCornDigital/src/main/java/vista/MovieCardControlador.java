package vista;

import dto.pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieCardControlador {

    @FXML private ImageView posterImage;
    @FXML private Label titleLabel;
    @FXML private Label formatLabel;

    /**
     * Rellena la tarjeta con los datos de la película.
     */
    public void setMovieData(pelicula movie) {
        // Título
        titleLabel.setText(movie.getNombre());

        // Formato
        formatLabel.setText("Formato: " + movie.getFormato());

        // Imagen del póster
        if (movie.getImagen() != null && !movie.getImagen().isEmpty()) {
            try {
                // Si la ruta está en resources, debe empezar con "/"
                posterImage.setImage(new Image(getClass().getResource(movie.getImagen()).toExternalForm()));
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }
}
