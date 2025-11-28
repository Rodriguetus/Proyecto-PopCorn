package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import conexion.conexionDB;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NuevaCompraControlador implements Initializable {

    @FXML
    private ComboBox<String> peliculaComboBox;

    @FXML
    private Label infoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Cargar nombres desde la base de datos
        ArrayList<String> peliculas = obtenerNombresPeliculas();

        peliculaComboBox.getItems().addAll(peliculas);

        peliculaComboBox.setOnAction(event -> {
            String seleccion = peliculaComboBox.getValue();
            infoLabel.setText("Has seleccionado: " + seleccion);
        });
    }

    private ArrayList<String> obtenerNombresPeliculas() {
        ArrayList<String> lista = new ArrayList<>();

        String sql = "SELECT Nombre FROM pelicula";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("Nombre"));
            }

        } catch (Exception e) {
            System.err.println("Error al cargar nombres de películas → " + e.getMessage());
        }

        return lista;
    }

    @FXML
    private void volveraPedido(MouseEvent event) {
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
}
