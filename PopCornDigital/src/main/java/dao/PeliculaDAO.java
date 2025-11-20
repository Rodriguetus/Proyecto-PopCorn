package dao;

import dto.pelicula;
import conexion.conexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexion.conexionDB;

public class PeliculaDAO {

    public void insertar(pelicula p) {
        String sql = "INSERT INTO pelicula (idPelicula, nombre, precio, stock, anosalida, proveedor, formato, genero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getNombre());
            stmt.setDouble(3, p.getPrecio());
            stmt.setInt(4, p.getStock());
            stmt.setInt(5, p.getAnoSalida());
            stmt.setString(6, p.getProveedor());
            stmt.setString(7, p.getFormato());
            stmt.setString(8, p.getGenero());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(pelicula p) {
        String sql = "UPDATE pelicula SET nombre=?, precio=?, stock=?, anosalida=?, proveedor=?, formato=?, genero=?, portada=? WHERE idpelicula=?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setInt(3, p.getStock());
            stmt.setInt(4, p.getAnoSalida());
            stmt.setString(5, p.getProveedor());
            stmt.setString(6, p.getFormato());
            stmt.setString(7, p.getGenero());
            stmt.setInt(9, p.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";

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
        String sql = "SELECT * FROM movies";

        try (Connection conn = conexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new pelicula(
                        rs.getInt("idPelicula"),
                        rs.getString("Nombre"),
                        rs.getFloat("Precio"),
                        rs.getInt("Stock"),
                        rs.getString("Genero"),
                        rs.getString("Formato"),
                        rs.getString("Proveedor"),
                        rs.getInt("AnoSalida")
                ));
            }
        }
        return list;
    }
}
