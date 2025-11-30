package vista;

import dao.PedidoDAO;
import dto.SesionIniciada;
import dto.pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DetallesMovieCardControlador {

    // Labels principales
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

    // Labels de información extra
    @FXML private Label yearLabel;
    @FXML private Label formatLabel;
    @FXML private Label providerLabel;
    @FXML private Label genreLabel;
    @FXML private Label stockLabel;
    @FXML private Label priceLabel;

    // Botones
    @FXML private Button favoriteButton;
    private int idUsuario = SesionIniciada.getIdUsuario();
    @FXML private Button buyButton;
    @FXML private Button rentButton;

    // Imagen del póster
    @FXML private ImageView posterImage;
//==========================================
private pelicula peliculaActual;

    public void setPelicula(pelicula peli) {
        this.peliculaActual = peli;
        // aquí también puedes actualizar labels, imagen, etc.
    }

    /**
     * Método para rellenar la vista de detalles con los datos de la película.
     */

    public void setDetallesMovieCard(pelicula movie) {
        this.peliculaActual = movie;

        titleLabel.setText(movie.getNombre());
        descriptionLabel.setText(movie.getDescripcion());

        yearLabel.setText(String.valueOf(movie.getAnoSalida()));
        formatLabel.setText(movie.getFormato());
        providerLabel.setText(movie.getProveedor());
        genreLabel.setText(movie.getGenero());
        stockLabel.setText(String.valueOf(movie.getStock()));
        priceLabel.setText(movie.getPrecio() + " €");

        if (movie.getImagen() != null && !movie.getImagen().isEmpty()) {
            try {
                posterImage.setImage(new Image(getClass().getResource(movie.getImagen()).toExternalForm()));
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            }
        }

        if ("4K UHD".equalsIgnoreCase(movie.getFormato())) {
            rentButton.setVisible(true);
            buyButton.setVisible(false);
        } else {
            rentButton.setVisible(false);
            buyButton.setVisible(true);
        }
    }

    @FXML
    private void comprarPelicula(MouseEvent event) throws SQLException {

        if (peliculaActual == null) return;

        // 1. Guardar pedido en la BD (como hacía antes)
        PedidoDAO dao = new PedidoDAO();
        boolean ok = PedidoDAO.createPedidoAndReduceStock(peliculaActual.getId(), 1, "");

        if (!ok) {
            System.out.println("No hay stock suficiente.");
            return;
        }

        // 2. Guardar en memoria
        CarritoService.addCompra(peliculaActual);

        // 3. Abrir Pedido.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pedido.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void marcarFavorito(ActionEvent event) {
        if (idUsuario <= 0 || peliculaActual == null || peliculaActual.getId() <= 0) {
            System.out.println("Usuario o película no definidos.");
            return;
        }

        int idPelicula = peliculaActual.getId();

        // 1. Consulta para VERIFICAR si ya existe
        String checkSql = "SELECT COUNT(*) FROM favoritos WHERE idUsuario = ? AND idPelicula = ?";

        // 2. Consulta para INSERTAR (solo si no existe)
        String insertSql = "INSERT INTO favoritos (idUsuario, idPelicula) VALUES (?, ?)";

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, idUsuario);
            checkStmt.setInt(2, idPelicula);

            try (ResultSet rs = checkStmt.executeQuery()) {


                if (rs.next() && rs.getInt(1) > 0) {

                    //  existe
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Película Duplicada");
                    alert.setHeaderText(null); // No usamos cabecera
                    alert.setContentText("Esta película ya está en Favoritos");
                    alert.showAndWait();

                } else {

                    // No existe, procedemos a INSERTAR
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                        insertStmt.setInt(1, idUsuario);
                        insertStmt.setInt(2, idPelicula);

                        int filas = insertStmt.executeUpdate();
                        if (filas > 0) {
                            System.out.println("Película marcada como favorita para el usuario con ID " + idUsuario);
                            favoriteButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 8;");
                        } else {
                            System.out.println("No se pudo insertar el favorito.");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Opcional: Mostrar una alerta de error genérico si falla la BD
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Base de Datos");
            alert.setHeaderText("Ocurrió un error inesperado.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

