package vista;

import dto.pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controlador encargado de gestionar la tarjeta visual de una película dentro del catálogo.
 * Se encarga de mostrar el título, formato e imagen del póster.
 */
public class MovieCardControlador {

    /** Imagen del póster de la película. */
    @FXML private ImageView posterImage;

    /** Label que muestra el título de la película. */
    @FXML private Label titleLabel;

    /** Label que muestra el formato de la película. */
    @FXML private Label formatLabel;

    /**
     * Rellena la tarjeta con los datos de la película proporcionada.
     * Actualiza el título, formato y la imagen del póster.
     *
     * @param movie objeto {@link pelicula} que contiene los datos a mostrar
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
