package vista;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem; // Importar el SeparatorMenuItem
import conexion.conexionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FiltradosController {

    @FXML private MenuButton btnFormatos;
    @FXML private MenuButton btnProveedor;
    @FXML private MenuButton btnGenero;
    @FXML private MenuButton btnAnio;

    // Etiqueta constante para el estado sin filtro
    private static final String SIN_FILTRO_LABEL = "Sin filtro";

    @FXML
    public void initialize() {
        cargarFormatosDesdeBD();
        cargarProveedoresDesdeBD();
        cargarGenerosDesdeBD();
        cargarAniosDesdeBD();
    }

    private void cargarFormatosDesdeBD() {
        cargarOpciones("SELECT DISTINCT formato FROM pelicula", btnFormatos, "Formato");
    }

    private void cargarProveedoresDesdeBD() {
        cargarOpciones("SELECT DISTINCT proveedor FROM pelicula", btnProveedor, "Proveedor");
    }

    private void cargarGenerosDesdeBD() {
        cargarOpciones("SELECT DISTINCT genero FROM pelicula", btnGenero, "Género");
    }

    private void cargarAniosDesdeBD() {
        cargarOpciones("SELECT DISTINCT AnoSalida FROM pelicula WHERE AnoSalida IS NOT NULL ORDER BY AnoSalida DESC", btnAnio, "Año");
    }

    // --- Método modificado para incluir "Sin filtro" ---
    private void cargarOpciones(String query, MenuButton boton, String etiqueta) {

        // 1. Limpiar opciones anteriores por seguridad
        boton.getItems().clear();

        // 2. Crear y añadir la opción "Sin filtro"
        MenuItem sinFiltroItem = new MenuItem(SIN_FILTRO_LABEL);

        // La acción de "Sin filtro" debe restablecer el texto del botón a solo la etiqueta base
        sinFiltroItem.setOnAction(e -> {
            boton.setText(etiqueta); // Restablece el texto a solo la etiqueta base (ej: "Formato")
            System.out.println("Filtro de " + etiqueta + " restablecido.");
            // Aquí puedes llamar a tu método para recargar el catálogo completo
        });

        boton.getItems().add(sinFiltroItem);

        // 3. Opcional: Añadir un separador visual
        boton.getItems().add(new SeparatorMenuItem());

        // 4. Cargar opciones desde la BD (Tu lógica existente)
        try (Connection conn = conexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String valor = rs.getString(1);
                if (valor != null && !valor.isBlank()) {
                    MenuItem item = new MenuItem(valor);

                    item.setOnAction(e -> {
                        boton.setText(etiqueta + ": " + valor);
                        System.out.println("Seleccionado " + etiqueta + ": " + valor);
                        // Aquí puedes aplicar el filtro con el 'valor' seleccionado
                    });

                    boton.getItems().add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 5. Inicializar el botón con la etiqueta base
        boton.setText(etiqueta);
    }
}