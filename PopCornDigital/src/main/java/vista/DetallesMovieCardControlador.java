package vista;

import dto.SesionIniciada;
import dto.pelicula;
import javafx.event.ActionEvent;
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
    private int idUsuario = SesionIniciada.getIdUsuario();


    @FXML private Button buyButton;
    @FXML private Button rentButton;

    // Imagen del póster
    @FXML private ImageView posterImage;
//==========================================
private pelicula peliculaActual;

    public void setPelicula(pelicula peli) {
        this.peliculaActual = peli;
        // aquí también puedes actualizar labels, imagen, etc.
    }

    /**
     * Método para rellenar la vista de detalles con los datos de la película.
     */


    public void setDetallesMovieCard(pelicula peli) {
        this.peliculaActual = peli;

        if (peliculaActual != null) {
            titleLabel.setText(peliculaActual.getNombre());
            descriptionLabel.setText(peliculaActual.getDescripcion());

            yearLabel.setText(String.valueOf(peliculaActual.getAnoSalida()));
            formatLabel.setText(peliculaActual.getFormato());
            providerLabel.setText(peliculaActual.getProveedor());
            genreLabel.setText(peliculaActual.getGenero());
            stockLabel.setText(String.valueOf(peliculaActual.getStock()));
            priceLabel.setText(peliculaActual.getPrecio() + " €");

            if (peliculaActual.getImagen() != null && !peliculaActual.getImagen().isEmpty()) {
                try {
                    posterImage.setImage(new Image(getClass().getResource(peliculaActual.getImagen()).toExternalForm()));
                } catch (Exception e) {
                    System.err.println("No se pudo cargar la imagen: " + e.getMessage());
                }
            }

            if ("4K UHD".equalsIgnoreCase(peliculaActual.getFormato())) {
                rentButton.setVisible(true);
                buyButton.setVisible(false);
            } else {
                rentButton.setVisible(false);
                buyButton.setVisible(true);
            }
        }
    }

    @FXML
    private void marcarFavorito(ActionEvent event) {

        if (idUsuario <= 0 || peliculaActual == null || peliculaActual.getId() <= 0) {
            System.out.println("Usuario o película no definidos.");
            return;
        }

        int idPelicula = peliculaActual.getId(); // ID de la película actual

        String sql = "UPDATE usuario SET Fav = ? WHERE idUsuario = ?";

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPelicula);
            stmt.setInt(2, idUsuario);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Película marcada como favorita para el usuario con ID " + idUsuario);
            } else {
                System.out.println("No se pudo actualizar el favorito.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

