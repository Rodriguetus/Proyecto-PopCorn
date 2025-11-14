package vista;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.*;

public class InicioSesionControlador{

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
