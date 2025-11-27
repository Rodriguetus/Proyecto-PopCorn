package vista;

import dao.PeliculaDAO;
import dto.pelicula;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
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
            addMovieCard(popularMoviesPane, p);
        }
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

    //Para abrir DetallesMovieCard
    private void abrirDetalles(pelicula movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/DetallesMovieCard.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la película
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
}
