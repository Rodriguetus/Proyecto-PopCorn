package vista;

import dao.PeliculaDAO;
import dto.pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador encargado de gestionar la vista del catálogo de películas.
 * Permite cargar, buscar, filtrar y visualizar películas dentro de distintos paneles.
 * También gestiona la navegación hacia otras vistas del sistema.
 */
public class CatalogoControlador {

    /** Contenedor donde se muestran las películas populares. */
    @FXML private FlowPane popularMoviesPane;

    /** Contenedor donde se muestran las películas nuevas. */
    @FXML private FlowPane newMoviesPane;

    /** Contenedor donde se muestran las películas recomendadas. */
    @FXML private FlowPane recommendedMoviesPane;

    /** Campo de texto utilizado para realizar búsquedas por título. */
    @FXML private TextField txtBuscador;

    /** Acceso a datos de películas. */
    private final PeliculaDAO peliculaDAO = new PeliculaDAO();

    /**
     * Método de inicialización del controlador.
     * Se ejecuta automáticamente al cargar la vista.
     *
     * @throws SQLException si ocurre un error al obtener las películas desde la base de datos
     */
    public void initialize() throws SQLException {
        cargarCatalogoCompleto(popularMoviesPane);
    }

    // --- MÉTODOS AUXILIARES DE VISUALIZACIÓN ---

    /**
     * Carga todas las películas disponibles y las muestra en el FlowPane indicado.
     *
     * @param pane contenedor donde se mostrarán las películas
     * @throws SQLException si ocurre un error al consultar la base de datos
     */
    private void cargarCatalogoCompleto(FlowPane pane) throws SQLException {
        List<pelicula> peliculas = peliculaDAO.getPeliculas();
        mostrarPeliculasEnFlowPane(pane, peliculas);
    }

    /**
     * Muestra una lista de películas dentro del FlowPane especificado.
     * Si la lista está vacía, muestra un mensaje informativo.
     *
     * @param pane contenedor donde se mostrarán las películas
     * @param peliculas lista de películas a mostrar
     */
    private void mostrarPeliculasEnFlowPane(FlowPane pane, List<pelicula> peliculas) {
        pane.getChildren().clear();

        if (peliculas.isEmpty()) {
            pane.getChildren().add(new Label("No se encontraron resultados para la búsqueda."));
        } else {
            for (pelicula p : peliculas) {
                addMovieCard(pane, p);
            }
        }
    }

    /**
     * Muestra una lista filtrada de películas en el panel principal del catálogo.
     *
     * @param peliculas lista filtrada de películas
     */
    public void mostrarPeliculasFiltradas(List<pelicula> peliculas) {
        mostrarPeliculasEnFlowPane(popularMoviesPane, peliculas);
    }

    /**
     * Crea y añade una tarjeta visual (MovieCard) al FlowPane correspondiente.
     *
     * @param pane contenedor donde se añadirá la tarjeta
     * @param movie película cuyos datos se mostrarán en la tarjeta
     */
    private void addMovieCard(FlowPane pane, pelicula movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MovieCard.fxml"));
            AnchorPane card = loader.load();
            MovieCardControlador controller = loader.getController();

            controller.setMovieData(movie);

            card.setOnMouseClicked(event -> abrirDetalles(movie));

            pane.getChildren().add(card);
        } catch (Exception e) {
            System.err.println("Error al cargar tarjeta: " + movie.getNombre() + " → " + e.getMessage());
        }
    }

    // --- FUNCIÓN DE BÚSQUEDA ---

    /**
     * Realiza una búsqueda de películas según el texto ingresado en el buscador.
     * Si el campo está vacío, recarga todo el catálogo.
     */
    @FXML
    private void buscarPeliculas() {
        String textoBusqueda = txtBuscador.getText().trim();

        try {
            if (textoBusqueda.isEmpty()) {
                cargarCatalogoCompleto(popularMoviesPane);
            } else {
                List<pelicula> resultados = peliculaDAO.buscarPeliculasPorTitulo(textoBusqueda);
                mostrarPeliculasEnFlowPane(popularMoviesPane, resultados);
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la búsqueda en la BD: " + e.getMessage());
            e.printStackTrace();
            popularMoviesPane.getChildren().clear();
            popularMoviesPane.getChildren().add(new Label("Error de conexión al buscar películas."));
        }
    }

    // --- MÉTODOS DE NAVEGACIÓN ---

    /**
     * Regresa a la vista de inicio de sesión.
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
     * Navega a la vista de pedidos.
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
     * Navega a la vista de favoritos.
     *
     * @param event evento de clic del usuario
     */
    @FXML
    private void irFavoritos(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Favoritos.fxml"));
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
     * Navega a la vista de alquileres.
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
     * Método reservado para futura implementación de la vista de saldo.
     *
     * @param event evento de clic del usuario
     */
    public void irSaldo(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Saldo.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mi Cartera - PopCorn");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar Saldo.fxml");
        }
    }

    /**
     * Abre una ventana emergente con los detalles completos de una película.
     *
     * @param movie película cuyos detalles se mostrarán
     */
    private void abrirDetalles(pelicula movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/DetallesMovieCard.fxml"));
            Parent root = loader.load();

            DetallesMovieCardControlador controller = loader.getController();
            controller.setDetallesMovieCard(movie);

            Stage stage = new Stage();
            stage.setTitle("Detalles de la Película");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al abrir DetallesMovieCard.fxml");
        }
    }

    /**
     * Abre la ventana de filtrado de películas.
     *
     * @param event evento de acción del usuario
     */
    @FXML
    private void abrirFiltrados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Filtrados.fxml"));
            Parent root = loader.load();

            FiltradosController filtrosController = loader.getController();
            filtrosController.setCatalogoControlador(this);

            Stage stage = new Stage();
            stage.setTitle("Filtrados");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
