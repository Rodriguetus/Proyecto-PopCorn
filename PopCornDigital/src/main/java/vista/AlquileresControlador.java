package vista;

import dto.SesionIniciada;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlquileresControlador {

    @FXML private ImageView logoImage;
    @FXML private ImageView homeIcon;
    @FXML private ImageView searchIcon;
    @FXML private ImageView favoritesIcon;
    @FXML private ImageView AlquilerIcon;
    @FXML private ImageView settingsIcon;

    @FXML private ScrollPane catalogScroll;
    @FXML private VBox catalogContainer;
    @FXML private FlowPane AlquilerPane;

    @FXML
    public void initialize() {
        cargarAlquileresUsuario();
    }

    private void cargarAlquileresUsuario() {

        int idUsuario = SesionIniciada.getIdUsuario();
        List<AlquilerDTO> lista = new ArrayList<>();

        String sql = """
                SELECT p.*, a.fechaAlquiler, a.fechaDevolucion
                FROM alquiler a 
                JOIN pelicula p ON a.idPelicula = p.idPelicula 
                WHERE a.idUsuario = ?
                """;

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                pelicula peli = new pelicula();
                peli.setId(rs.getInt("idPelicula"));
                peli.setNombre(rs.getString("nombre"));
                peli.setDescripcion(rs.getString("descripcion"));
                peli.setAnoSalida(rs.getInt("anoSalida"));
                peli.setFormato(rs.getString("formato"));
                peli.setProveedor(rs.getString("proveedor"));
                peli.setGenero(rs.getString("genero"));
                peli.setStock(rs.getInt("stock"));
                peli.setPrecio(rs.getDouble("precio"));
                peli.setImagen(rs.getString("imagen"));

                AlquilerDTO data = new AlquilerDTO();
                data.peli = peli;
                data.fAlquila = rs.getDate("fechaAlquiler").toLocalDate();
                data.fCaduca = rs.getDate("fechaDevolucion").toLocalDate();

                lista.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar películas alquiladas.");
        }

        for (AlquilerDTO d : lista) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MoviesAlquiladas.fxml"));
                Parent card = loader.load();

                MovieAlquileresControlador controlador = loader.getController();
                controlador.CrearMovieAlquiler(d.peli, d.fAlquila, d.fCaduca);

                AlquilerPane.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class AlquilerDTO {
        pelicula peli;
        LocalDate fAlquila;
        LocalDate fCaduca;
    }

    @FXML
    private void irCatalogo() {
        cambiarVista("Catalogo.fxml", homeIcon);
    }

    @FXML
    private void irPedido() {
        cambiarVista("Pedido.fxml", searchIcon);
    }

    @FXML
    private void irFavoritos() {
        cambiarVista("Favoritos.fxml", favoritesIcon);
    }

    @FXML
    private void irAlquiler() {
        cambiarVista("Alquiler.fxml", AlquilerIcon);
    }

    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", settingsIcon);
    }

    private void cambiarVista(String fxml, ImageView origen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) origen.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error cargando vista: " + fxml);
            e.printStackTrace();
        }
    }
}

