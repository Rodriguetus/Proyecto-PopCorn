package dao;

import conexion.conexionDB;

import java.sql.*;

public class AlquilerDAO {


    public static boolean actualizarFechas(int idAlquiler) {

        String sql = """
        UPDATE alquiler
        SET fechaAlquiler = CURRENT_DATE,
            fechaDevolucion = DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY)
        WHERE id = ?
    """;

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAlquiler);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

}



