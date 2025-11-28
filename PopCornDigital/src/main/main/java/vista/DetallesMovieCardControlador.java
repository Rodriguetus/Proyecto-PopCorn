package vista;

import dto.pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

//HACER LOS FAVORITOS Y BUSCAR LA ID DEL USUARIO QUE HA INICIADO SESION
    private void marcarFavorito() {
        if (usuario == null || pelicula == null) {
            System.out.println("Usuario o película no definidos.");
            return;
        }

        String sql = "UPDATE usuario SET fav = ? WHERE id = ?";

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pelicula.getId());   // id de la película
            stmt.setInt(2, usuario.getId());    // id del usuario actual

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                System.out.println("Película marcada como favorita para el usuario: " + usuario.getNombre());
            } else {
                System.out.println("No se pudo actualizar el favorito.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
