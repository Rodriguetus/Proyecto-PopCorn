package dao;

import conexion.conexionDB;
import dto.alquiler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlquilerDAO {
    public void alquiler(alquiler p) {
        String sql = "INSERT INTO alquiler (idAlquiler, idUsuario, FechaDevolucion, FechaAlquiler, idPelicula) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setInt(2, p.getIdUsuario());
            stmt.setDate(3, (Date) p.getfDevolucion());
            stmt.setDate(4, (Date) p.getfAlquiler());
            stmt.setInt(5, p.getIdPelicula());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(alquiler p) {
        String sql = "UPDATE alquiler SET idUsuario=?, FechaDevolucion=?, FechaAlquiler=?, idPelicula=? WHERE idAlquiler=?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getIdUsuario());
            stmt.setDate(2, (Date) p.getfDevolucion());
            stmt.setDate(3, (Date) p.getfAlquiler());
            stmt.setInt(4, p.getIdPelicula());
            stmt.setInt(5, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM alquiler WHERE idAlquiler = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<alquiler> getAlquileres() throws SQLException {
        List<alquiler> list = new ArrayList<>();
        String sql = "SELECT * FROM alquiler";

        try (Connection conn = conexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                alquiler a = new alquiler();
                a.setId(rs.getInt("idAlquiler"));
                a.setIdUsuario(rs.getInt("idUsuario"));
                a.setfAlquiler(rs.getDate("FechaAlquiler"));
                a.setfDevolucion(rs.getDate("FechaDevolucion"));
                a.setIdPelicula(rs.getInt("idPelicula"));

                list.add(a);
            }
        }
        return list;
    }


}
