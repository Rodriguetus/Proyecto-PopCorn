package vista;

import dao.AlquilerDAO;
import dto.alquiler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class GestionDeAlquilerControlador {

    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVolver;
    @FXML private Button btnCerrar;

    @FXML private TableView<alquiler> tablaAlquiler;

    @FXML private TextField txtEstado;
    @FXML private TextField txtFechaAlquiler;
    @FXML private TextField txtFechaDevolucion;
    @FXML private TextField txtPelicula;

    @FXML private TableColumn<alquiler, Integer> colId;
    @FXML private TableColumn<alquiler, String> colEstado;
    @FXML private TableColumn<alquiler, String> colFechaAlquiler;
    @FXML private TableColumn<alquiler, String> colFechaDevolucion;
    @FXML private TableColumn<alquiler, Integer> colIdPelicula;

    private AlquilerDAO alquilerDAO;

    @FXML
    public void initialize() {
        alquilerDAO = new AlquilerDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaAlquiler.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getfAlquiler().toString()));
        colFechaDevolucion.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getfDevolucion().toString()));
        colIdPelicula.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));

        try {
            cargarAlquiler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarAlquiler() throws SQLException {
        tablaAlquiler.setItems(
                FXCollections.observableArrayList(alquilerDAO.getAlquileres())
        );
    }

    @FXML
    private void anadirAlquiler() {

        if (txtEstado.getText().isEmpty() ||
                txtFechaAlquiler.getText().isEmpty() ||
                txtFechaDevolucion.getText().isEmpty() ||
                txtPelicula.getText().isEmpty()) {

            mostrarAlerta("Rellena todos los campos.");
            return;
        }

        alquiler a = new alquiler();
        a.setEstado(txtEstado.getText());
        a.setfAlquiler(Date.valueOf(txtFechaAlquiler.getText()));
        a.setfDevolucion(Date.valueOf(txtFechaDevolucion.getText()));
        a.setIdPelicula(Integer.parseInt(txtPelicula.getText()));

        alquilerDAO.alquiler(a);

        mostrarAlerta("Alquiler añadido.");
        limpiarCampos();
    }

    @FXML
    private void editarAlquiler() {

        alquiler a = tablaAlquiler.getSelectionModel().getSelectedItem();

        if (a == null) {
            mostrarAlerta("Selecciona un alquiler.");
            return;
        }

        if (!txtEstado.getText().isEmpty())
            a.setEstado(txtEstado.getText());

        if (!txtFechaAlquiler.getText().isEmpty())
            a.setfAlquiler(Date.valueOf(txtFechaAlquiler.getText()));

        if (!txtFechaDevolucion.getText().isEmpty())
            a.setfDevolucion(Date.valueOf(txtFechaDevolucion.getText()));

        if (!txtPelicula.getText().isEmpty())
            a.setIdPelicula(Integer.parseInt(txtPelicula.getText()));

        alquilerDAO.modificar(a);

        mostrarAlerta("Alquiler actualizado.");
        limpiarCampos();
    }

    @FXML
    private void quitarAlquiler() {

        alquiler a = tablaAlquiler.getSelectionModel().getSelectedItem();

        if (a == null) {
            mostrarAlerta("Selecciona un alquiler.");
            return;
        }

        boolean ok = alquilerDAO.eliminar(a.getId());

        if (ok) mostrarAlerta("Alquiler eliminado.");
        else mostrarAlerta("Error al eliminar.");
    }

    private void mostrarAlerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }

    private void limpiarCampos() {
        txtEstado.clear();
        txtFechaAlquiler.clear();
        txtFechaDevolucion.clear();
        txtPelicula.clear();
    }

    @FXML
    private void volverMenu() {
        cambiarVista("MenuAdministrador.fxml", btnVolver);
    }

    @FXML
    private void CerrarSesion() {
        cambiarVista("InicioSesion.fxml", btnCerrar);
    }

    private void cambiarVista(String fxml, Button origen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) origen.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


