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

/**
 * Controlador encargado de gestionar la vista de películas favoritas del usuario.
 * Carga las películas marcadas como favoritas y permite navegar entre distintas vistas.
 */
public class FavoritoControlador {

    // ---------------- ICONOS DE NAVEGACIÓN ----------------

    /** Icono del logo principal. */
    @FXML private ImageView logoImage;

    /** Icono para volver al inicio. */
    @FXML private ImageView homeIcon;

    /** Icono para ir al buscador. */
    @FXML private ImageView searchIcon;

    /** Icono para acceder a favoritos. */
    @FXML private ImageView favoritesIcon;

    /** Icono para acceder a configuración. */
    @FXML private ImageView settingsIcon;

    // ---------------- CONTENEDORES PRINCIPALES ----------------

    /** Scroll principal del catálogo. */
    @FXML private ScrollPane catalogScroll;

    /** Contenedor vertical del catálogo. */
    @FXML private VBox catalogContainer;

    // ---------------- SECCIONES DE PELÍCULAS ----------------

    /** Contenedor de películas populares. */
    @FXML private FlowPane popularMoviesPane;

    /** Contenedor de películas nuevas. */
    @FXML private FlowPane newMoviesPane;

    /** Contenedor de películas recomendadas. */
    @FXML private FlowPane recommendedMoviesPane;

    /** Contenedor donde se mostrarán las películas favoritas del usuario. */
    @FXML private FlowPane favoritosPane;

    /**
     * Método que se ejecuta automáticamente al cargar la vista.
     * Llama al método encargado de cargar las películas favoritas del usuario.
     */
    @FXML
    public void initialize() {
        cargarFavoritosUsuario();
    }

    /**
     * Consulta la base de datos para obtener las películas favoritas del usuario actual.
     * Por cada película encontrada, carga su tarjeta visual y la añade al contenedor correspondiente.
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

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MoviesFavoritas.fxml"));
                Parent card = loader.load();

                MoviesFavoritasControlador controlador = loader.getController();
                controlador.CrearMovieFav(peli);

                favoritosPane.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar películas favoritas.");
        }
    }

    /**
     * Navega a la vista de inicio de sesión.
     *
     * @param event evento de clic del usuario
     */
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

    /**
     * Navega a la vista de pedidos del usuario.
     *
     * @param event evento de clic del usuario
     */
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

    /**
     * Método reservado para futura implementación de la vista de saldo.
     *
     * @param mouseEvent evento de clic del usuario
     */
    public void irSaldo(MouseEvent mouseEvent) {
    }

    /**
     * Navega a la vista de alquileres del usuario.
     *
     * @param event evento de clic del usuario
     */
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

    /**
     * Regresa al catálogo principal de películas.
     *
     * @param event evento de clic del usuario
     */
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
