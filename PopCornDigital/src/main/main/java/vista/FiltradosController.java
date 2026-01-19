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

/**
 * Controlador encargado de gestionar los filtros del catálogo de películas.
 * Permite cargar dinámicamente las opciones desde la base de datos y aplicar
 * los filtros seleccionados sobre el catálogo principal.
 */
public class FiltradosController {

    /** Botón desplegable para filtrar por formato. */
    @FXML private MenuButton btnFormatos;

    /** Botón desplegable para filtrar por proveedor. */
    @FXML private MenuButton btnProveedor;

    /** Botón desplegable para filtrar por género. */
    @FXML private MenuButton btnGenero;

    /** Botón desplegable para filtrar por año de salida. */
    @FXML private MenuButton btnAnio;

    /** Texto mostrado cuando no se aplica ningún filtro. */
    private static final String SIN_FILTRO_LABEL = "Sin filtro";

    /** Referencia al controlador del catálogo para actualizar la vista. */
    private CatalogoControlador catalogoControlador;

    /**
     * Establece el controlador del catálogo para permitir comunicación entre vistas.
     *
     * @param controller instancia del controlador del catálogo
     */
    public void setCatalogoControlador(CatalogoControlador controller) {
        this.catalogoControlador = controller;
    }

    /**
     * Método que se ejecuta al inicializar la vista.
     * Carga las opciones de filtros desde la base de datos.
     */
    @FXML
    public void initialize() {
        cargarFormatosDesdeBD();
        cargarProveedoresDesdeBD();
        cargarGenerosDesdeBD();
        cargarAniosDesdeBD();
    }

    /**
     * Carga los formatos disponibles desde la base de datos.
     */
    private void cargarFormatosDesdeBD() {
        cargarOpciones("SELECT DISTINCT formato FROM pelicula", btnFormatos, "Formato");
    }

    /**
     * Carga los proveedores disponibles desde la base de datos.
     */
    private void cargarProveedoresDesdeBD() {
        cargarOpciones("SELECT DISTINCT proveedor FROM pelicula", btnProveedor, "Proveedor");
    }

    /**
     * Carga los géneros disponibles desde la base de datos.
     */
    private void cargarGenerosDesdeBD() {
        cargarOpciones("SELECT DISTINCT genero FROM pelicula", btnGenero, "Género");
    }

    /**
     * Carga los años de salida disponibles desde la base de datos.
     */
    private void cargarAniosDesdeBD() {
        cargarOpciones("SELECT DISTINCT AnoSalida FROM pelicula WHERE AnoSalida IS NOT NULL ORDER BY AnoSalida DESC",
                btnAnio, "Año");
    }

    /**
     * Carga opciones dinámicas en un MenuButton según la consulta SQL proporcionada.
     *
     * @param query consulta SQL para obtener valores únicos
     * @param boton botón donde se cargarán las opciones
     * @param etiqueta texto base del botón
     */
    private void cargarOpciones(String query, MenuButton boton, String etiqueta) {

        boton.getItems().clear();

        MenuItem sinFiltroItem = new MenuItem(SIN_FILTRO_LABEL);
        sinFiltroItem.setOnAction(e -> {
            boton.setText(etiqueta);
            aplicarFiltros();
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
                        aplicarFiltros();
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

    /**
     * Obtiene los valores seleccionados en los filtros y solicita al catálogo
     * que actualice la lista de películas mostradas.
     */
    public void aplicarFiltros() {

        String formato = obtenerValorFiltro(btnFormatos, "Formato");
        String proveedor = obtenerValorFiltro(btnProveedor, "Proveedor");
        String genero = obtenerValorFiltro(btnGenero, "Género");
        String anio = obtenerValorFiltro(btnAnio, "Año");

        List<pelicula> peliculasFiltradas = PeliculaDAO.filtrarPeliculas(formato, proveedor, genero, anio);

        if (catalogoControlador != null) {
            catalogoControlador.mostrarPeliculasFiltradas(peliculasFiltradas);
        }
    }

    // -------------------------------
    // OBTENER VALOR REAL DEL BOTÓN
    // -------------------------------

    /**
     * Extrae el valor real seleccionado en un MenuButton.
     *
     * @param boton botón del cual obtener el valor
     * @param etiquetaBase texto base del botón
     * @return valor seleccionado o null si no hay filtro
     */
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

    /**
     * Restablece todos los filtros a su estado inicial y recarga todas las películas.
     *
     * @throws SQLException si ocurre un error al obtener las películas
     */
    @FXML
    private void reiniciarFiltros() throws SQLException {

        btnFormatos.setText("Formato");
        btnProveedor.setText("Proveedor");
        btnGenero.setText("Género");
        btnAnio.setText("Año");

        if (catalogoControlador != null) {
            PeliculaDAO dao = new PeliculaDAO();
            List<pelicula> lista = dao.getPeliculas();
            catalogoControlador.mostrarPeliculasFiltradas(lista);
        }
    }

}
