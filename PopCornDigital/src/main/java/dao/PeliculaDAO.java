package dao;

import dto.pelicula;
import conexion.conexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexion.conexionDB;

public class PeliculaDAO {

    public void insertar(pelicula p) {
        String sql = "INSERT INTO pelicula (idPelicula, Nombre, Precio, Stock, Genero, Formato, Proveedor, AnoSalida, Imagen, Descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getNombre());
            stmt.setDouble(3, p.getPrecio());
            stmt.setInt(4, p.getStock());
            stmt.setString(5, p.getGenero());
            stmt.setString(6, p.getFormato());
            stmt.setString(7, p.getProveedor());
            stmt.setInt(8, p.getAnoSalida());
            stmt.setString(9, p.getImagen());
            stmt.setString(10, p.getDescripcion());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(pelicula p) {
        String sql = "UPDATE pelicula SET Nombre=?, Precio=?, Stock=?, Genero=?, Formato=?, Proveedor=?, AnoSalida=?, Imagen=?, Descripcion=? WHERE idPelicula=?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setInt(3, p.getStock());
            stmt.setString(4, p.getGenero());
            stmt.setString(5, p.getFormato());
            stmt.setString(6, p.getProveedor());
            stmt.setInt(7, p.getAnoSalida());
            stmt.setString(8, p.getImagen());
            stmt.setString(9, p.getDescripcion());
            stmt.setInt(10, p.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pelicula WHERE idPelicula = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<pelicula> getPeliculas() throws SQLException {
        List<pelicula> list = new ArrayList<>();
        String sql = "SELECT * FROM pelicula";
        try (Connection conn = conexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new pelicula(
                        rs.getInt("idPelicula"),
                        rs.getString("Nombre"),
                        rs.getDouble("Precio"),
                        rs.getInt("Stock"),
                        rs.getString("Genero"),
                        rs.getString("Formato"),
                        rs.getString("Proveedor"),
                        rs.getInt("AnoSalida"),
                        rs.getString("Imagen"),
                        rs.getString("Descripcion")
                ));
            }
        }
        return list;
    }

    // Dentro de la clase dao.PeliculaDAO

    public List<pelicula> buscarPeliculasPorTitulo(String termino) throws SQLException {
        List<pelicula> peliculas = new ArrayList<>();

        // CORRECCIÓN: Cambiar 'id' por 'idPelicula' en la consulta SELECT
        String query = "SELECT idPelicula, Nombre, Precio, Stock, Genero, Formato, Proveedor, AnoSalida, Imagen, Descripcion FROM pelicula WHERE LOWER(Nombre) LIKE ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + termino.toLowerCase() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // CORRECCIÓN: Cambiar rs.getInt("id") por rs.getInt("idPelicula")
                    peliculas.add(new pelicula(
                            rs.getInt("idPelicula"), // ¡AQUÍ ESTABA EL ERROR!
                            rs.getString("Nombre"),
                            rs.getDouble("Precio"),
                            rs.getInt("Stock"),
                            rs.getString("Genero"),
                            rs.getString("Formato"),
                            rs.getString("Proveedor"),
                            rs.getInt("AnoSalida"),
                            rs.getString("Imagen"),
                            rs.getString("Descripcion")
                    ));
                }
            }
        }
        return peliculas;
    }
    public static List<pelicula> filtrarPeliculas(String formato, String proveedor, String genero, String anio) {
        List<pelicula> lista = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM pelicula WHERE 1=1");

        if (formato != null) query.append(" AND formato = '").append(formato).append("'");
        if (proveedor != null) query.append(" AND proveedor = '").append(proveedor).append("'");
        if (genero != null) query.append(" AND genero = '").append(genero).append("'");
        if (anio != null) query.append(" AND AnoSalida = ").append(anio);

        try (Connection conn = conexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            while (rs.next()) {
                pelicula p = new pelicula(
                        rs.getInt("idPelicula"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("genero"),
                        rs.getString("formato"),
                        rs.getString("proveedor"),
                        rs.getInt("AnoSalida"),
                        rs.getString("imagen"),
                        rs.getString("descripcion")
                );
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
