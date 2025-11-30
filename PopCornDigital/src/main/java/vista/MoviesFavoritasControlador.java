package vista;

import conexion.conexionDB;
import dto.SesionIniciada;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MoviesFavoritasControlador {

    @FXML private ImageView posterImage;
    @FXML private Label nombreLabel;
    @FXML private Label precioLabel;
    @FXML private Label stockLabel;
    @FXML private Label anoLabel;
    @FXML private Label formatoLabel;
    @FXML private Label proveedorLabel;
    @FXML private Label generoLabel;
    @FXML private Label descripcionLabel;
    @FXML private Button btnEliminarFavorito;
    private pelicula peli;

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
    @FXML
    private void eliminarDeFavoritos() {
        int idUsuario = SesionIniciada.getIdUsuario();
        int idPelicula = peli.getId();

        String sql = "DELETE FROM favoritos WHERE idUsuario = ? AND idPelicula = ?";

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPelicula);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Película eliminada de favoritos: " + peli.getNombre());

                //Recargar la escena Favoritos.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Favoritos.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) posterImage.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
