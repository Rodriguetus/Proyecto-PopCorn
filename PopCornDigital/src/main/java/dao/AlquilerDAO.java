package dao;

import conexion.conexionDB;
import dto.alquiler;

import java.sql.*;

public class AlquilerDAO {
    public void alquiler(alquiler p) {
        String sql = "INSERT INTO pedido (idPedido, Estado, FechaDevolucion, FechaAlquiler, idPelicula) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getEstado());
            stmt.setDate(4, (Date) p.getfDevolucion());
            stmt.setDate(3, (Date) p.getfAlquiler());
            stmt.setInt(5, p.getIdPelicula());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(alquiler p) {
        String sql = "UPDATE alquiler SET Estado=?, FechaDevolucion=?, FechaAlquiler=?, idPelicula=? WHERE idAlquiler=?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(2, p.getEstado());
            stmt.setDate(4, (Date) p.getfDevolucion());
            stmt.setDate(3, (Date) p.getfAlquiler());
            stmt.setInt(5, p.getIdPelicula());
            stmt.setInt(1, p.getId());

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


}
