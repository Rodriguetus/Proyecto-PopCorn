package vista;

import dao.PeliculaDAO;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogoControlador {

    @FXML private FlowPane popularMoviesPane;
    @FXML private FlowPane newMoviesPane;
    @FXML private FlowPane recommendedMoviesPane;

    public void initialize() throws SQLException {
            PeliculaDAO dao = new PeliculaDAO();
        List<pelicula> peliculas = dao.getPeliculas();

        for (pelicula p : peliculas) {
            addMovieCard(popularMoviesPane, p.getNombre(), p.getFormato(), p.getImagen());
        }
    }




    private void addMovieCard(FlowPane pane, String title, String format, String imagePath) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MovieCard.fxml"));
            AnchorPane card = loader.load();
            MovieCardControlador controller = loader.getController();

            // Cargar imagen desde ruta en recursos
            Image imagen = new Image(getClass().getResource(imagePath).toExternalForm());
            controller.setMovieData(title, format, imagen);

            pane.getChildren().add(card);
        } catch (Exception e) {
            System.err.println("Error al cargar tarjeta: " + title + " → " + e.getMessage());
        }
    }

    private List<pelicula> obtenerPeliculasDesdeBD() {
        List<pelicula> lista = new ArrayList<>();
        String sql = "SELECT Nombre, Precio, Stock, Genero, Formato, Proveedor, AnoSalida, Imagen FROM pelicula";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/popcorn", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new pelicula(
                        rs.getString("Nombre"),
                        rs.getFloat("Precio"),
                        rs.getInt("Stock"),
                        rs.getString("Genero"),
                        rs.getString("Formato"),
                        rs.getString("Proveedor"),
                        rs.getInt("AnoSalida"),
                        rs.getString("Imagen")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }

        return lista;
    }
}
