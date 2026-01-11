package vista;

import dto.pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import conexion.conexionDB;
import dao.PeliculaDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class FiltradosController {

    @FXML private MenuButton btnFormatos;
    @FXML private MenuButton btnProveedor;
    @FXML private MenuButton btnGenero;
    @FXML private MenuButton btnAnio;

    private static final String SIN_FILTRO_LABEL = "Sin filtro";

    // REFERENCIA AL CONTROLADOR DEL CATÁLOGO
    private CatalogoControlador catalogoControlador;

    // Setter para conectar ambos controladores
    public void setCatalogoControlador(CatalogoControlador controller) {
        this.catalogoControlador = controller;
    }

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

    private void cargarOpciones(String query, MenuButton boton, String etiqueta) {

        boton.getItems().clear();

        MenuItem sinFiltroItem = new MenuItem(SIN_FILTRO_LABEL);
        sinFiltroItem.setOnAction(e -> {
            boton.setText(etiqueta);
            aplicarFiltros();   // ← IMPORTANTE
        });

        boton.getItems().add(sinFiltroItem);
        boton.getItems().add(new SeparatorMenuItem());

        try (Connection conn = conexionDB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String valor = rs.getString(1);
                if (valor != null && !valor.isBlank()) {
                    MenuItem item = new MenuItem(valor);

                    item.setOnAction(e -> {
                        boton.setText(etiqueta + ": " + valor);
                        aplicarFiltros();   // ← IMPORTANTE
                    });

                    boton.getItems().add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boton.setText(etiqueta);
    }


    // -------------------------------
    // APLICAR FILTROS
    // -------------------------------
    public void aplicarFiltros() {

        String formato = obtenerValorFiltro(btnFormatos, "Formato");
        String proveedor = obtenerValorFiltro(btnProveedor, "Proveedor");
        String genero = obtenerValorFiltro(btnGenero, "Género");
        String anio = obtenerValorFiltro(btnAnio, "Año");

        List<pelicula> peliculasFiltradas = PeliculaDAO.filtrarPeliculas(formato, proveedor, genero, anio);

        // Delegamos la actualización del catálogo al controlador principal
        if (catalogoControlador != null) {
            catalogoControlador.mostrarPeliculasFiltradas(peliculasFiltradas);
        }
    }

    // -------------------------------
    // OBTENER VALOR REAL DEL BOTÓN
    // -------------------------------
    private String obtenerValorFiltro(MenuButton boton, String etiquetaBase) {
        String texto = boton.getText();

        if (texto.equals(etiquetaBase)) {
            return null;
        }

        if (texto.contains(":")) {
            return texto.split(":")[1].trim();
        }

        return null;
    }
    @FXML
    private void reiniciarFiltros() throws SQLException {

        // Restaurar texto de los botones
        btnFormatos.setText("Formato");
        btnProveedor.setText("Proveedor");
        btnGenero.setText("Género");
        btnAnio.setText("Año");

        // Volver a cargar todas las películas
        if (catalogoControlador != null) {
            PeliculaDAO dao = new PeliculaDAO();
            List<pelicula> lista = dao.getPeliculas();
            catalogoControlador.mostrarPeliculasFiltradas(lista);
        }
    }

}
