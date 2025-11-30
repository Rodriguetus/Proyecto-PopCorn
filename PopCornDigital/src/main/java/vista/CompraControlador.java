package vista;

import dto.pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.function.Consumer;

public class CompraControlador {

    @FXML private AnchorPane root;

    @FXML private ImageView imagenPelicula;
    @FXML private Label tituloLabel;
    @FXML private Label compraIdLabel;
    @FXML private Label cantidadLabel;
    @FXML private Label proveedorLabel;
    @FXML private Label fechaCompraLabel;
    @FXML private Label fechaEsperadaLabel;
    @FXML private Label estadoLabel;
    @FXML private Button btnRemove;

    private pelicula pelicula;
    private Consumer<CompraControlador> onRemove;

    @FXML
    public void initialize() {
        btnRemove.setOnAction(e -> {
            if (onRemove != null) {
                onRemove.accept(this);
            } else {
                if (root.getParent() instanceof javafx.scene.layout.Pane pane) {
                    pane.getChildren().remove(root);
                }
            }
        });
    }

    /**
     * Método principal para rellenar la tarjeta de compra con los datos de la película.
     */
    public void setDatosPelicula(pelicula pelicula) {
        this.pelicula = pelicula;

        tituloLabel.setText(pelicula.getNombre());
        proveedorLabel.setText("Proveedor: " + pelicula.getProveedor());
        cantidadLabel.setText("Cantidad: 1 unidad");

        compraIdLabel.setText("Compra #" + System.currentTimeMillis());
        fechaCompraLabel.setText("Compra: " + java.time.LocalDate.now());
        fechaEsperadaLabel.setText("Entrega: " + java.time.LocalDate.now().plusDays(7));

        estadoLabel.setText("Estado: Pendiente");

        if (pelicula.getImagen() != null && !pelicula.getImagen().isEmpty()) {
            try {
                imagenPelicula.setImage(
                        new Image(getClass().getResource(pelicula.getImagen()).toExternalForm())
                );
            } catch (Exception e) {
                System.out.println("No se pudo cargar la imagen: " + pelicula.getImagen());
            }
        }
    }

    public pelicula getPelicula() {
        return pelicula;
    }

    public void setOnRemove(Consumer<CompraControlador> onRemove) {
        this.onRemove = onRemove;
    }

    public AnchorPane getRoot() {
        return root;
    }
}
