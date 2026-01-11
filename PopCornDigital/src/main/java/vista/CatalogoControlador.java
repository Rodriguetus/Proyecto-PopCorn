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

public class CatalogoControlador {

    @FXML private FlowPane popularMoviesPane;
    @FXML private FlowPane newMoviesPane;
    @FXML private FlowPane recommendedMoviesPane;

    // AÑADIDO: Campo de texto para el buscador
    @FXML private TextField txtBuscador;

    private final PeliculaDAO peliculaDAO = new PeliculaDAO();

    public void initialize() throws SQLException {
        // Inicialización usando el nuevo método auxiliar
        cargarCatalogoCompleto(popularMoviesPane);
    }

    // --- MÉTODOS AUXILIARES DE VISUALIZACIÓN ---

    private void cargarCatalogoCompleto(FlowPane pane) throws SQLException {
        List<pelicula> peliculas = peliculaDAO.getPeliculas();
        mostrarPeliculasEnFlowPane(pane, peliculas);
    }

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
    public void mostrarPeliculasFiltradas(List<pelicula> peliculas) {
        mostrarPeliculasEnFlowPane(popularMoviesPane, peliculas);
    }


    private void addMovieCard(FlowPane pane, pelicula movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MovieCard.fxml"));
            AnchorPane card = loader.load();
            MovieCardControlador controller = loader.getController();

            // Pasamos el objeto completo al controlador de la tarjeta
            controller.setMovieData(movie);

            // Evento de clic: abrir ventana de detalles con el objeto completo
            card.setOnMouseClicked(event -> abrirDetalles(movie));

            pane.getChildren().add(card);
        } catch (Exception e) {
            System.err.println("Error al cargar tarjeta: " + movie.getNombre() + " → " + e.getMessage());
        }
    }

    // --- FUNCIÓN DE BÚSQUEDA ---

    // La firma se ha corregido para no aceptar argumentos, evitando el "argument type mismatch"
    @FXML
    private void buscarPeliculas() {
        String textoBusqueda = txtBuscador.getText().trim();

        try {
            if (textoBusqueda.isEmpty()) {
                // Si el buscador está vacío, recarga todo el catálogo
                cargarCatalogoCompleto(popularMoviesPane);
            } else {
                // Llama al nuevo método del DAO
                List<pelicula> resultados = peliculaDAO.buscarPeliculasPorTitulo(textoBusqueda);
                // Muestra los resultados de la búsqueda
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