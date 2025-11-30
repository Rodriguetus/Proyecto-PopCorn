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
import java.time.format.DateTimeFormatter;

public class GestionDeAlquilerControlador {

    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVolver;
    @FXML private Button btnCerrar;

    @FXML private TableView<alquiler> tablaAlquiler;

    @FXML private TextField txtUsuario;
    @FXML private TextField txtFechaAlquiler;
    @FXML private TextField txtFechaDevolucion;
    @FXML private TextField txtPelicula;

    @FXML private TableColumn<alquiler, Integer> colId;
    @FXML private TableColumn<alquiler, Integer> colIdUsuario;
    @FXML private TableColumn<alquiler, String> colFechaAlquiler;
    @FXML private TableColumn<alquiler, String> colFechaDevolucion;
    @FXML private TableColumn<alquiler, Integer> colIdPelicula;

    private AlquilerDAO alquilerDAO;

    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {

        alquilerDAO = new AlquilerDAO();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colFechaAlquiler.setCellValueFactory(cell -> {
            Date d = (Date) cell.getValue().getfAlquiler();
            return new SimpleStringProperty(
                    d != null ? new java.sql.Date(d.getTime()).toLocalDate().toString() : "--"
            );
        });

        colFechaDevolucion.setCellValueFactory(cell -> {
            Date d = (Date) cell.getValue().getfDevolucion();
            return new SimpleStringProperty(
                    d != null ? new java.sql.Date(d.getTime()).toLocalDate().toString() : "--"
            );
        });
        colIdPelicula.setCellValueFactory(new PropertyValueFactory<>("idPelicula"));

        cargarAlquileres();
    }

    private void cargarAlquileres() {
        try {
            tablaAlquiler.setItems(
                    FXCollections.observableArrayList(alquilerDAO.getAlquileres())
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void anadirAlquiler() {

        if (txtUsuario.getText().isEmpty() ||
                txtFechaAlquiler.getText().isEmpty() ||
                txtFechaDevolucion.getText().isEmpty() ||
                txtPelicula.getText().isEmpty()) {
            mostrarAlerta("Rellena todos los campos.");
            return;
        }

        alquiler a = new alquiler();
        a.setIdPelicula(Integer.parseInt(txtUsuario.getText()));
        a.setfAlquiler(Date.valueOf(txtFechaAlquiler.getText()));
        a.setfDevolucion(Date.valueOf(txtFechaDevolucion.getText()));
        a.setIdPelicula(Integer.parseInt(txtPelicula.getText()));

        alquilerDAO.alquiler(a);

        mostrarAlerta("Alquiler añadido.");
        limpiarCampos();
        cargarAlquileres();
    }

    @FXML
    private void editarAlquiler() {

        alquiler a = tablaAlquiler.getSelectionModel().getSelectedItem();

        if (a == null) {
            mostrarAlerta("Selecciona un alquiler.");
            return;
        }

        if (!txtUsuario.getText().isEmpty())
            a.setIdUsuario(Integer.parseInt(txtUsuario.getText()));

        if (!txtFechaAlquiler.getText().isEmpty())
            a.setfAlquiler(Date.valueOf(txtFechaAlquiler.getText()));

        if (!txtFechaDevolucion.getText().isEmpty())
            a.setfDevolucion(Date.valueOf(txtFechaDevolucion.getText()));

        if (!txtPelicula.getText().isEmpty())
            a.setIdPelicula(Integer.parseInt(txtPelicula.getText()));

        alquilerDAO.modificar(a);

        mostrarAlerta("Alquiler actualizado.");
        limpiarCampos();
        cargarAlquileres();
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

        cargarAlquileres();
    }

    private void mostrarAlerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }

    private void limpiarCampos() {
        txtUsuario.clear();
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



