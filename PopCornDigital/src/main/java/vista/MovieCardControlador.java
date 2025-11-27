package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MovieCardControlador {

    @FXML private AnchorPane cardRoot;
    @FXML private ImageView posterImage;
    @FXML private Label titleLabel;
    @FXML private Label formatLabel;

    private Runnable onClickHandler;

    public void setMovieData(String titulo, String formato, Image imagen) {
        titleLabel.setText(titulo);
        formatLabel.setText("Formato: " + formato);
        posterImage.setImage(imagen);
    }


}
