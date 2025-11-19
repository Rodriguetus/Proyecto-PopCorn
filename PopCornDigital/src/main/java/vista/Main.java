package vista;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Main extends Application {

    public void start(Stage primaryStage)throws Exception{
        Parent root= FXMLLoader.load(getClass().getResource("/vista/InicioSesion.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setTitle("PopCorn Digital");
        primaryStage.show();
    }

    }
