package vista;

import javafx.*;

public class Main extends Application{

    public void start(Stage primaryStage)throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("InicioSesion.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {

    }
}