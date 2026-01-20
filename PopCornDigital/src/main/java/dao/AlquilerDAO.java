package dao;

import conexion.conexionDB;

import java.sql.*;

public class AlquilerDAO {

    public static int crearAlquilerPendiente(int idPelicula, int idUsuario) {

        String sql = """
        INSERT INTO alquiler (idPelicula, idUsuario, fechaAlquiler, fechaDevolucion)
        VALUES (?, ?, NULL, NULL)
    """;

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idPelicula);
            ps.setInt(2, idUsuario);

            int filas = ps.executeUpdate();
            if (filas == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean existeAlquilerUsuario(int idPelicula, int idUsuario) {

        String sql = """
        SELECT 1
        FROM alquiler
        WHERE idPelicula = ?
          AND idUsuario = ?
        LIMIT 1
    """;

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPelicula);
            stmt.setInt(2, idUsuario);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void eliminarAlquiler(int idAlquiler) {

        String sql = "DELETE FROM alquiler WHERE idAlquiler = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAlquiler);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



