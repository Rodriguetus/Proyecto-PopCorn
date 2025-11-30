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

    //Nodo de la raíz tarjeta
    @FXML private AnchorPane root;

    //Objetos de la tarjeta Compra
    @FXML private ImageView imagenPelicula;
    @FXML private Label tituloLabel;
    @FXML private Label compraIdLabel;
    @FXML private Label cantidadLabel;
    @FXML private Label proveedorLabel;
    @FXML private Label fechaCompraLabel;
    @FXML private Label fechaEsperadaLabel;
    @FXML private Label estadoLabel;
    @FXML private Button btnRemove;

    //Pelicula vinculada a la tarjeta
    private pelicula pelicula;

    //Callback para manejar la eliminación
    private Consumer<CompraControlador> onRemove;

    @FXML
    //El botín de remover ejecuta el callback y elimina la tarjeta
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

    //Rellena los datos de la tarjeta con la película actual
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
//Getter y Setter para la pelicula(Obtener la pelicula y eliminarla)
    public pelicula getPelicula() {
        return pelicula;
    }

    public void setOnRemove(Consumer<CompraControlador> onRemove) {
        this.onRemove = onRemove;
    }

    //Recupera la tarjeta para eliminarla
    public AnchorPane getRoot() {
        return root;
    }
}
