package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.ResourceBundle;

public class InicioSesionControlador implements Initializable {

    @FXML
    private Button InicioSesionButton;
    @FXML
    private ImageView popcornImage;
    @FXML
    private ImageView usuarioimage;
    @FXML
    private ImageView contrasena;
    @FXML

    private TextField usuario;
    @FXML
    private PasswordField password;
    @FXML
    private Label errormsj;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        File brandingFile = new File("..\\..\\..\\..\\Imagenes\\PopCorn.png");
        Image popcorn = new Image(brandingFile.toURI().toString());
        popcornImage.setImage(popcornImage.getImage());
    }

    public void InicioSesionButton(ActionEvent event){
        if(usuario.getText().isBlank()==false && password.getText().isBlank()==false){
        }else{
        errormsj.setText("Error, debe introducir sus datos de nuevo.");
        }
    }

    public void crearCuenta(){
        try{
            Parent root= FXMLLoader.load(getClass().getResource("InicioSesion.fxml"));
            Stage inicioStage=new Stage();
            inicioStage.initStyle(StageStyle.UNDECORATED);
            inicioStage.setScene(new Scene(root, 700, 500));
            inicioStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
