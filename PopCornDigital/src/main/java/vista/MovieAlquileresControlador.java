package vista;

import dao.AlquilerDAO;
import dao.DaoUsuario;
import dto.SesionIniciada;
import dto.alquiler;
import dto.pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
/**
 * Controlador JavaFX encargado de gestionar la vista de un alquiler de película.
 *
 * <p>
 * Esta clase se utiliza para mostrar los detalles de una película alquilada,
 * incluyendo información como el título, proveedor, fechas de alquiler
 * y la imagen asociada. Además, permite confirmar el alquiler pendiente
 * realizando el pago correspondiente.
 * </p>
 *
 * <p>
 * El controlador interactúa con la capa de acceso a datos mediante
 * {@link dao.AlquilerDAO} y {@link dao.DaoUsuario}, y obtiene información
 * del usuario autenticado a través de {@link dto.SesionIniciada}.
 * </p>
 *
 * <p>
 * Está diseñada para ser utilizada junto a un archivo FXML que define
 * la interfaz gráfica del alquiler.
 * </p>
 *
 * @author LaureanoCL
 * @version 1.0
 * @since 1.0
 */
public class MovieAlquileresControlador {

    /** Contenedor raíz de la vista */
    @FXML private AnchorPane root;

    /** Imagen representativa de la película */
    @FXML private ImageView imagenPelicula;

    /** Etiqueta que muestra el título de la película */
    @FXML private Label tituloLabel;

    /** Etiqueta que muestra el identificador del alquiler */
    @FXML private Label alquilerIdLabel;

    /** Etiqueta que muestra la cantidad alquilada */
    @FXML private Label cantidadLabel;

    /** Etiqueta que muestra el proveedor de la película */
    @FXML private Label proveedorLabel;

    /** Etiqueta que muestra la fecha de inicio del alquiler */
    @FXML private Label fechaInicioLabel;

    /** Etiqueta que muestra la fecha de fin del alquiler */
    @FXML private Label fechaFinLabel;

    /** Botón para confirmar el alquiler */
    @FXML private Button btnConfirmar;

    /** Película asociada al alquiler */
    private pelicula pelicula;

    /** Alquiler actual de la película */
    private alquiler alquilerActual;

    /**
     * Inicializa el controlador y la vista asociada.
     *
     * <p>
     * Este método se ejecuta automáticamente al cargarse el archivo FXML.
     * En esta implementación no contiene lógica adicional, ya que los datos
     * se cargan dinámicamente mediante el método {@link #setDatosPelicula}.
     * </p>
     */
    @FXML
    public void initialize() {
        // Ya no hay lógica de eliminación
    }

    /**
     * Asigna los datos de la película y del alquiler a la vista.
     *
     * <p>
     * Muestra la información básica de la película y, si el alquiler ya ha sido
     * confirmado, también las fechas correspondientes. En caso contrario,
     * se muestra como alquiler pendiente.
     * </p>
     *
     * @param pelicula película asociada al alquiler
     * @param alquiler alquiler actual de la película
     */
    public void setDatosPelicula(pelicula pelicula, alquiler alquiler) {

        this.pelicula = pelicula;
        this.alquilerActual = alquiler;

        tituloLabel.setText(pelicula.getNombre());
        proveedorLabel.setText("Proveedor: " + pelicula.getProveedor());
        cantidadLabel.setText("Cantidad: 1 unidad");

        // permitir alquileres SIN fechas
        if (alquiler != null && alquiler.getfAlquiler() != null) {

            alquilerIdLabel.setText("Alquiler #" + alquiler.getId());
            fechaInicioLabel.setText("Inicio: " + alquiler.getfAlquiler());
            fechaFinLabel.setText("Fin: " + alquiler.getfDevolucion());

        } else {

            // ALQUILER PENDIENTE (fechas NULL)
            alquilerIdLabel.setText("Pendiente de confirmar");
            fechaInicioLabel.setText("--/--/----");
            fechaFinLabel.setText("--/--/----");
        }

        // Imagen (con control de errores)
        if (pelicula.getImagen() != null && !pelicula.getImagen().isBlank()) {
            try {
                imagenPelicula.setImage(
                        new javafx.scene.image.Image(
                                getClass().getResource(pelicula.getImagen()).toExternalForm()
                        )
                );
            } catch (Exception e) {
                System.out.println("No se pudo cargar la imagen: " + pelicula.getImagen());
            }
        }
    }

    /**
     * Confirma el alquiler pendiente realizando el pago correspondiente.
     *
     * <p>
     * Comprueba si el usuario dispone de saldo suficiente para pagar el alquiler.
     * En caso afirmativo, descuenta el saldo y actualiza las fechas del alquiler.
     * Si el saldo es insuficiente o el alquiler ya está confirmado, se muestra
     * un mensaje de error.
     * </p>
     *
     * @param event evento generado al pulsar el botón de confirmar alquiler
     */
    @FXML
    private void confirmarAlquiler(ActionEvent event) {

        // Ya está pagado o no es válido
        if (alquilerActual == null || alquilerActual.getfAlquiler() != null) {
            mostrarAlertaPersonalizada(
                    "Aviso",
                    "Este alquiler ya está pagado o no se puede procesar."
            );
            return;
        }

        int idUsuario = SesionIniciada.getIdUsuario();
        double precio = pelicula.getPrecio();

        DaoUsuario usuarioDAO = new DaoUsuario();
        double saldoActual = usuarioDAO.getSaldo(idUsuario);

        if (saldoActual >= precio) {
            boolean exito = usuarioDAO.restarSaldo(idUsuario, precio);
            if (exito) {
                AlquilerDAO.actualizarFechas(alquilerActual.getId());

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Pago realizado",
                        "Nuevo saldo: " + String.format("%.2f", saldoActual - precio) + "€"
                );
            }
        } else {
            mostrarAlertaPersonalizada(
                    "Saldo insuficiente",
                    "Te faltan " + String.format("%.2f", precio - saldoActual) + "€"
            );
        }
    }

    /**
     * Muestra una alerta estándar al usuario.
     *
     * @param tipo tipo de alerta a mostrar
     * @param titulo título de la ventana de alerta
     * @param contenido mensaje informativo
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta personalizada con estilos CSS.
     *
     * @param titulo título de la alerta
     * @param contenido mensaje de error mostrado al usuario
     */
    private void mostrarAlertaPersonalizada(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/alerta.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("alerta-popcorn");

        alert.showAndWait();
    }

    /**
     * Devuelve la película asociada al alquiler.
     *
     * @return película actual
     */
    public pelicula getPelicula() {
        return pelicula;
    }
    /**
     * Devuelve el contenedor raíz de la vista.
     *
     * @return panel raíz (AnchorPane)
     */
    public AnchorPane getRoot() {
        return root;
    }
}


