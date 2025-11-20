package vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public void start(Stage primaryStage)throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/InicioSesion.fxml"));
        Parent root = loader.load();

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 700, 650));
        primaryStage.setTitle("PopCorn Digital");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
