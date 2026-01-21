package vista;

import dao.AlquilerDAO;
import dao.DaoUsuario;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controlador encargado de gestionar la vista de detalles de una película.
 * Permite mostrar información detallada, comprar, alquilar y marcar como favorita.
 */
public class DetallesMovieCardControlador {

    /** Label que muestra el título de la película. */
    @FXML private Label titleLabel;

    /** Label que muestra la descripción de la película. */
    @FXML private Label descriptionLabel;

    /** Label que muestra el año de salida. */
    @FXML private Label yearLabel;

    /** Label que muestra el formato de la película. */
    @FXML private Label formatLabel;

    /** Label que muestra el proveedor de la película. */
    @FXML private Label providerLabel;

    /** Label que muestra el género de la película. */
    @FXML private Label genreLabel;

    /** Label que muestra el stock disponible. */
    @FXML private Label stockLabel;

    @FXML private Label stock;

    /** Label que muestra el precio de la película. */
    @FXML private Label priceLabel;

    /** Botón para marcar como favorita. */
    @FXML private Button favoriteButton;

    /** ID del usuario actualmente logueado. */
    private int idUsuario = SesionIniciada.getIdUsuario();

    /** Botón para comprar la película. */
    @FXML private Button buyButton;

    /** Botón para alquilar la película. */
    @FXML private Button rentButton;

    /** Imagen del póster de la película. */
    @FXML private ImageView posterImage;

    /** Objeto película actualmente mostrado en la vista. */
    private pelicula peliculaActual;

    /**
     * Establece la película actual sin actualizar la interfaz.
     *
     * @param peli película seleccionada
     */
    public void setPelicula(pelicula peli) {
        this.peliculaActual = peli;
    }

    /**
     * Rellena la vista con los datos de la película seleccionada.
     *
     * @param movie película cuyos detalles se mostrarán
     */
    public void setDetallesMovieCard(pelicula movie) {
        String correoUsuario = SesionIniciada.getCorreo();
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
            stockLabel.setVisible(false);
            stock.setVisible(false);
            // SINCRONIZAR ESTADO DEL BOTÓN DE ALQUILER
            actualizarEstadoBotonAlquiler();

        } else {
            rentButton.setVisible(false);
            buyButton.setVisible(true);
            stockLabel.setVisible(true);
            stock.setVisible(true);
            // lógica de compras (correcta)
            boolean compradaEnBD = PedidoDAO.haCompradoPelicula(correoUsuario, movie.getId());

            boolean enCarrito = false;
            for (pelicula p : CarritoService.getCarrito()) {
                if (p.getId() == movie.getId()) {
                    enCarrito = true;
                    break;
                }
            }

            if (compradaEnBD || enCarrito) {
                buyButton.setDisable(true);
                buyButton.setText("En compras");
            } else {
                buyButton.setDisable(false);
                buyButton.setText("Comprar");
            }
        }

    }

    /**
     * Procesa la compra de la película actual.
     *
     * @param event evento de acción del botón
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    @FXML
    private void comprarPelicula(ActionEvent event) throws SQLException {

        if (peliculaActual == null) return;

        String correoUsuario = SesionIniciada.getCorreo();

        int idPedido = PedidoDAO.createPedidoAndReduceStock(peliculaActual.getId(), 1, "", correoUsuario);

        if (idPedido == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de Stock");
            alert.setContentText("No se pudo procesar la compra. Verifique el stock.");
            alert.showAndWait();
            return;
        }

        CarritoService.addCompra(peliculaActual);

        buyButton.setDisable(true);
        buyButton.setText("En compras");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Compra insertada en pedidos correctamente");
        alert.setHeaderText("Compra insertada en pedidos");
        alert.showAndWait();
    }

    /**
     * Marca la película actual como favorita para el usuario.
     *
     * @param event evento del botón
     */
    @FXML
    private void marcarFavorito(ActionEvent event) {
        if (idUsuario <= 0 || peliculaActual == null || peliculaActual.getId() <= 0) {
            System.out.println("Usuario o película no definidos.");
            return;
        }

        int idPelicula = peliculaActual.getId();

        String checkSql = "SELECT COUNT(*) FROM favoritos WHERE idUsuario = ? AND idPelicula = ?";
        String insertSql = "INSERT INTO favoritos (idUsuario, idPelicula) VALUES (?, ?)";

        try (Connection conn = conexion.conexionDB.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, idUsuario);
            checkStmt.setInt(2, idPelicula);

            try (ResultSet rs = checkStmt.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Película Duplicada");
                    alert.setHeaderText(null);
                    alert.setContentText("Esta película ya está en Favoritos");
                    alert.showAndWait();

                } else {

                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                        insertStmt.setInt(1, idUsuario);
                        insertStmt.setInt(2, idPelicula);

                        int filas = insertStmt.executeUpdate();
                        if (filas > 0) {
                            favoriteButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 8;");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Base de Datos");
            alert.setHeaderText("Ocurrió un error inesperado.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Procesa el alquiler de la película actual.
     *
     * @param event evento del botón
     */
    @FXML
    private void alquilarPelicula(ActionEvent event) {

        if (peliculaActual == null) return;

        //  Usuario realmente logueado
        int idUsuario = SesionIniciada.getIdUsuario();

        if (idUsuario <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sesión no válida");
            alert.setContentText("Debes iniciar sesión para alquilar una película.");
            alert.showAndWait();
            return;
        }

        //  Evitar alquilar dos veces la misma película
        if (AlquilerDAO.existeAlquilerUsuario(peliculaActual.getId(), idUsuario)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Película ya alquilada");
            alert.setContentText("Esta película ya está alquilada o pendiente de confirmar.");
            alert.showAndWait();
            return;
        }

        // 1️ Crear alquiler PENDIENTE (sin fechas, sin cobro)
        AlquilerDAO.crearAlquilerPendiente(
                peliculaActual.getId(),
                idUsuario
        );


        // 2 Desactivar botón
        actualizarEstadoBotonAlquiler();

        // 3 Feedback al usuario
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alquiler añadido correctamente");
        alert.setHeaderText("Alquiler insertado");
        alert.setContentText("La película se ha añadido a tus alquileres.");
        alert.showAndWait();
    }


    private void actualizarEstadoBotonAlquiler() {

        if (peliculaActual == null) {
            System.out.println("peliculaActual es null");
            return;
        }

        int idUsuario = SesionIniciada.getIdUsuario();
        if (idUsuario <= 0) {
            System.out.println("usuario no válido");
            return;
        }

        boolean existe = AlquilerDAO.existeAlquilerUsuario(
                peliculaActual.getId(),
                idUsuario
        );

        System.out.println("¿Está alquilada? " + existe);

        if (existe) {
            rentButton.setDisable(true);
            rentButton.setText("En alquiler");
        } else {
            rentButton.setDisable(false);
            rentButton.setText("Alquilar");
        }
    }
}
