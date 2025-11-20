package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieCardControlador {

    @FXML private ImageView posterImage;
    @FXML private Label titleLabel;
    @FXML private Label formatLabel;

    public void setMovieData(String titulo, String formato, Image imagen) {
        titleLabel.setText(titulo);
        formatLabel.setText("Formato: " + formato);
        posterImage.setImage(imagen);
    }


}
