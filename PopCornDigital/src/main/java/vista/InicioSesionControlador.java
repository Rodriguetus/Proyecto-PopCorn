package vista;

import javafx.scene.*;

import java.io.File;
import java.net.*;
import java.sql.*;

public class InicioSesionControlador{

    public void crearCuenta(){
    try{
        Parent root=FXMLLoader.load(getClass().getResource("InicioSesion.fxml"));
        Stage inicioStage=new Stage();
        inicioStage.initStyle(StageStyle.UNDECORATED);
        inicioStage.setScene(new Scene(root, 700, 500));
        inicioStage.show();
    }
    }

}