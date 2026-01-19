package vista;

import dto.SesionIniciada;
import dto.alquiler;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * Controlador JavaFX encargado de gestionar la vista de los alquileres del usuario.
 *
 * <p>
 * Esta clase se encarga de cargar y mostrar todas las películas alquiladas
 * por el usuario que tiene la sesión iniciada. Cada alquiler se representa
 * mediante una tarjeta gráfica cargada dinámicamente desde un archivo FXML.
 * </p>
 *
 * <p>
 * El controlador obtiene la información desde la base de datos utilizando
 * consultas SQL y crea objetos {@link dto.pelicula} y {@link dto.alquiler}
 * para representar los datos obtenidos.
 * </p>
 *
 * <p>
 * Además, gestiona la navegación entre las distintas vistas principales
 * de la aplicación, como el catálogo, favoritos, pedidos y cierre de sesión.
 * </p>
 *
 * @author LaureanoCL
 * @version 1.0
 * @since 1.0
 */
public class AlquileresControlador {
    /** Imagen del logotipo de la aplicación */
    @FXML private ImageView logoImage;

    /** Icono de navegación al catálogo */
    @FXML private ImageView homeIcon;

    /** Icono de navegación al saldo */
    @FXML private ImageView SaldoIcon;

    /** Icono de navegación a pedidos */
    @FXML private ImageView searchIcon;

    /** Icono de navegación a favoritos */
    @FXML private ImageView favoritesIcon;

    /** Icono de navegación a la vista de alquileres */
    @FXML private ImageView AlquilerIcon;

    /** Icono de navegación para cerrar sesión */
    @FXML private ImageView settingsIcon;

    /** Contenedor con scroll para el catálogo */
    @FXML private ScrollPane catalogScroll;

    /** Contenedor vertical del catálogo */
    @FXML private VBox catalogContainer;

    /** Panel donde se muestran las tarjetas de alquiler */
    @FXML private FlowPane AlquilerPane;

    /**
     * Inicializa el controlador y carga los alquileres del usuario.
     *
     * <p>
     * Este método se ejecuta automáticamente al cargarse el archivo FXML
     * y llama al método encargado de obtener los alquileres del usuario
     * desde la base de datos.
     * </p>
     */
    @FXML
    public void initialize() {
        cargarAlquileresUsuario();
    }
    /**
     * Carga los alquileres asociados al usuario que tiene la sesión iniciada.
     *
     * <p>
     * Realiza una consulta a la base de datos para obtener las películas
     * alquiladas por el usuario y genera dinámicamente una tarjeta gráfica
     * para cada alquiler utilizando el archivo FXML correspondiente.
     * </p>
     *
     * <p>
     * Cada tarjeta es gestionada por {@link vista.MovieAlquileresControlador},
     * al que se le pasan los datos de la película y del alquiler.
     * </p>
     */
    private void cargarAlquileresUsuario() {

        AlquilerPane.getChildren().clear();
        int idUsuario = SesionIniciada.getIdUsuario();

        String sql = """
            SELECT 
                p.*,
                a.idAlquiler,
                a.fechaAlquiler,
                a.fechaDevolucion
            FROM alquiler a
            JOIN pelicula p ON a.idPelicula = p.idPelicula
            WHERE a.idUsuario = ?
        """;

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                // -------- PELÍCULA --------
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

                // -------- ALQUILER --------
                alquiler alq = new alquiler();
                alq.setId(rs.getInt("idAlquiler"));

                // ⚠️ PUEDEN SER NULL (y está bien)
                alq.setfAlquiler(rs.getDate("fechaAlquiler"));
                alq.setfDevolucion(rs.getDate("fechaDevolucion"));

                // -------- CARGAR TARJETA --------
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/vista/MoviesAlquiladas.fxml")
                );
                Parent card = loader.load();

                MovieAlquileresControlador controlador = loader.getController();
                controlador.setDatosPelicula(peli, alq);

                AlquilerPane.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar alquileres del usuario.");
        }
    }

    // ------------------ NAVEGACIÓN ------------------

    /**
     * Navega a la vista del catálogo principal.
     */
    @FXML
    private void irSaldo() {
        cambiarVista("Saldo.fxml", SaldoIcon);
    }

    @FXML
    private void irCatalogo() {
        cambiarVista("Catalogo.fxml", homeIcon);
    }

    /**
     * Navega a la vista de pedidos.
     */
    @FXML
    private void irPedido() {
        cambiarVista("Pedido.fxml", searchIcon);
    }

    /**
     * Navega a la vista de películas favoritas.
     */
    @FXML
    private void irFavoritos() {
        cambiarVista("Favoritos.fxml", favoritesIcon);
    }

    /**
     * Navega a la vista de alquileres del usuario.
     */
    @FXML
    private void irAlquiler() {
        cambiarVista("Alquiler.fxml", AlquilerIcon);
    }



        /**
         * Cierra la sesión actual y vuelve a la pantalla de inicio de sesión.
         */
    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", settingsIcon);
    }

    /**
     * Cambia la vista actual cargando un nuevo archivo FXML.
     *
     * <p>
     * Este método reutilizable permite navegar entre las distintas
     * pantallas de la aplicación reemplazando la escena actual.
     * </p>
     *
     * @param fxml nombre del archivo FXML que se desea cargar
     * @param origen imagen desde la que se obtiene la ventana actual
     */
    private void cambiarVista(String fxml, ImageView origen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/" + fxml));
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



