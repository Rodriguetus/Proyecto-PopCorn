    package vista;

    import dao.DaoAdministrador;
    import dao.DaoUsuario;
    import dto.usuario;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Hyperlink;
    import javafx.scene.control.Label;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.stage.Stage;

    import java.net.URL;
    import java.util.ResourceBundle;

    public class RegistrarCuentaControlador implements Initializable {


        @FXML
        private TextField nombreusuarioTextField;

        @FXML
        private TextField correoTextField;

        @FXML
        private PasswordField contrasenaPasswordField;

        @FXML
        private PasswordField repetirContrasenaPasswordField;

        @FXML
        private Label errorLabel;

        @FXML
        private Hyperlink linkInicio;

        /*
        Realiza varias validaciones y si esta correcto guarda al usuario y redirige a la vista del Login.
         */
        @FXML
        private void registrarCuenta(ActionEvent event) {

            errorLabel.setText("");

            String correo = correoTextField.getText();
            String nombre = nombreusuarioTextField.getText();
            String contrasena = contrasenaPasswordField.getText();
            String repetir = repetirContrasenaPasswordField.getText();

            //Validación de todos los campos son obligatorios
            if(correo.isEmpty() && contrasena.isEmpty()){
                errorLabel.setText("Todos los campos son obligatorios");
                return;
            }

            //Validación para Correo vacio.
            if (correo.isEmpty()) {
                errorLabel.setText("Rellene el campo del correo electrónico");
                return;
            }

            //Validación para Contraseña vacia
            if (contrasena.isEmpty()) {
                errorLabel.setText("Rellene el campo de contraseña.");
                return;
            }

            //Validación Longitud del Correo
            if(correo.length()<8){
                errorLabel.setText("El campo del correo debe tener 8 o más carácteres");
                return;
            }

            //Comprobar que es un correo(que contenga un @)
            if(!correo.contains("@")){
                errorLabel.setText("El correo no es válido. Debe contener un @");
                return;
            }

            //Validación Longitud de la Contraseña
            if(contrasena.length()<8){
                errorLabel.setText("La contraseña debe contener 8 o más caracteres");
                return;
            }

            //Validación si todos los campos están vacios.
            if (nombre.isEmpty() && correo.isEmpty() && contrasena.isEmpty() && repetir.isEmpty()) {
                errorLabel.setText("Todos los campos están vacíos. Debes rellenar el formulario.");
                return;
            }

            //Validación tamaño del nombre de usuario a crear
            if(!nombre.matches("^[A-Za-z0-9_]{6,15}$")) {
                errorLabel.setText("El nombre debe tener entre 6 y 15 caracteres.");
                return;
            }

            //Validación tamaño del correo a insertar
            if(correo.length()<8) {
                errorLabel.setText("El campo del correo debe ser mayor a 8 carácteres.");
                return;
            }

            //Validación tamaño de la contraseña del usuario a crear
            if(contrasena.length()<8){
                errorLabel.setText("La contraseña debe tener 8 o más carácteres.");
                return;
            }

            //Comprobar que es un correo(que contenga un @)
            if(!correo.contains("@")){
                errorLabel.setText("El correo no es válido. Debe contener un @");
                return;
            }

            //Validación para nombre Vacio
            if(nombre.isEmpty()){
                errorLabel.setText("Debes introducir el nombre del usuario a crear");
                return;
            }

            //Validación para el correo Vacio
            if(correo.isEmpty()){
                errorLabel.setText("Debes introducir el correo del usuario a crear");
                return;
            }

            //Validación de la contraseña
            if(contrasena.isEmpty()){
                errorLabel.setText("Debes introducir la contraseña que quieres crear");
                return;
            }

            //Validación de la confirmación de la contraseña
            if(repetir.isEmpty()){
                errorLabel.setText("Debes repetir la misma contraseña que en el campo anterior");
                return;
            }

            //Validación de que son la misma contraseña
            if (!contrasena.equals(repetir)) {
                errorLabel.setText("Las contraseñas no coinciden.");
                return;
            }

            usuario user = new usuario(correo, nombre, contrasena);
            DaoUsuario dao = new DaoUsuario();

            boolean creado = dao.registrar(user);

            if (!creado) {
                errorLabel.setText("No se pudo registrar. El correo ya existe.");
                return;
            }

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/vista/InicioSesion.fxml"));
                Stage stage = (Stage) nombreusuarioTextField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Metodo para volver al inicio
        private void irAInicioSesion() {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/vista/InicioSesion.fxml"));
                Stage stage = (Stage) correoTextField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Inicio Sesión - PopCorn");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            linkInicio.setOnAction(e -> irAInicioSesion());
        }
    }
