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

}
