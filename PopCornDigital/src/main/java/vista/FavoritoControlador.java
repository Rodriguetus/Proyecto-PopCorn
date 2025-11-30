package vista;

import dto.SesionIniciada;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FavoritoControlador {

    // Iconos de navegación lateral
    @FXML private ImageView logoImage;
    @FXML private ImageView homeIcon;
    @FXML private ImageView searchIcon;
    @FXML private ImageView favoritesIcon;
    @FXML private ImageView settingsIcon;

    // Contenedor principal
    @FXML private ScrollPane catalogScroll;
    @FXML private VBox catalogContainer;

    // Secciones de películas
    @FXML private FlowPane popularMoviesPane;
    @FXML private FlowPane newMoviesPane;
    @FXML private FlowPane recommendedMoviesPane;

    /**
     * Se ejecuta al cargar la vista. Crea las cartas de películas favoritas.
     */
    @FXML private FlowPane favoritosPane; // contenedor en Favoritos.fxml donde se mostrarán las cartas

    @FXML
    public void initialize() {
        cargarFavoritosUsuario();
    }

    /**
     * Consulta la tabla favoritos según el idUsuario de SesionIniciada
     * y crea las cartas llamando al método CrearMovieFav de MoviesFavoritasControlador.
     */
    private void cargarFavoritosUsuario() {
        int idUsuario = SesionIniciada.getIdUsuario();
        String sql = "SELECT p.* FROM favoritos f JOIN pelicula p ON f.idPelicula = p.idPelicula WHERE f.idUsuario = ?";

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

                //Cargar la carta de favoritos y pasarle la película
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MoviesFavoritas.fxml"));
                Parent card = loader.load();

                MoviesFavoritasControlador controlador = loader.getController();
                controlador.CrearMovieFav(peli);

                favoritosPane.getChildren().add(card); // añadir la carta al FlowPane
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar películas favoritas.");
        }
    }


    @FXML
    private void volverLogin(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar InicioSesion.fxml");
        }
    }

    //Funcion del boton de pedidos
    @FXML
    private void irPedido(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pedido.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el acceso a los pedidos.fxml");
        }
    }
    @FXML
    private void irAlquileres(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Alquileres.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Favoritos.fxml");
        }
    }

    @FXML
    private void volverCatalogo(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Catalogo.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Catalogo.fxml");
        }
    }


}
