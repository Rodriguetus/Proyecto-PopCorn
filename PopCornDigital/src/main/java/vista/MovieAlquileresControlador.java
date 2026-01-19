package vista;

import conexion.conexionDB;
import dto.SesionIniciada;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MovieAlquileresControlador {

    @FXML private ImageView posterImage;
    @FXML private Label nombreLabel;
    @FXML private Label precioLabel;
    @FXML private Label stockLabel;
    @FXML private Label anoLabel;
    @FXML private Label formatoLabel;
    @FXML private Label proveedorLabel;
    @FXML private Label generoLabel;
    @FXML private Label descripcionLabel;
    @FXML private Label Falquilar;
    @FXML private Label Fcaduca;
    @FXML private Button rentButton;   // 🔥 botón de alquiler

    private pelicula peli;

    public void CrearMovieAlquiler(pelicula peli, LocalDate fAlquila, LocalDate fCaduca) {
        this.peli = peli;

        nombreLabel.setText(peli.getNombre());
        precioLabel.setText(peli.getPrecio() + " €");
        stockLabel.setText("Stock: " + peli.getStock());
        anoLabel.setText("Año: " + peli.getAnoSalida());
        formatoLabel.setText("Formato: " + peli.getFormato());
        proveedorLabel.setText("Proveedor: " + peli.getProveedor());
        generoLabel.setText("Género: " + peli.getGenero());
        descripcionLabel.setText(peli.getDescripcion());

        cargarFechasAlquiler();

        if (peli.getImagen() != null && !peli.getImagen().isEmpty()) {
            try {
                Image img = new Image(getClass().getResource(peli.getImagen()).toExternalForm());
                posterImage.setImage(img);
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }

    private void cargarFechasAlquiler() {
        String sql = "SELECT fechaAlquiler, fechaDevolucion FROM alquiler WHERE idPelicula = ?";

        try (Connection con = conexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, peli.getId());

            var rs = ps.executeQuery();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (rs.next()) {
                LocalDate fAlquila = rs.getDate("fechaAlquiler").toLocalDate();
                LocalDate fCaduca = rs.getDate("fechaDevolucion").toLocalDate();

                Falquilar.setText("Alquilado: " + fAlquila.format(format));
                Fcaduca.setText("Caduca: " + fCaduca.format(format));
            } else {
                // no existe alquiler
                Falquilar.setText("Alquilado: --/--/----");
                Fcaduca.setText("Caduca: --/--/----");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
