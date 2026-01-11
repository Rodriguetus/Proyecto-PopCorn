package dao;

import conexion.conexionDB;

import java.sql.*;

public class AlquilerDAO {

    public static int crearAlquiler(
            int idPelicula,
            int cantidad,
            int idUsuario
    ) {

        String updateStockSql = """
            UPDATE pelicula
            SET stock = stock - ?
            WHERE idPelicula = ? AND stock >= ?
        """;

        String insertAlquilerSql = """
            INSERT INTO alquiler (idUsuario, idPelicula, FechaAlquiler, FechaDevolucion)
            VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY))
        """;

        try (Connection conn = conexionDB.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {
                psStock.setInt(1, cantidad);
                psStock.setInt(2, idPelicula);
                psStock.setInt(3, cantidad);

                if (psStock.executeUpdate() == 0) {
                    conn.rollback();
                    return -1;
                }
            }

            try (PreparedStatement psAlquiler =
                         conn.prepareStatement(insertAlquilerSql, Statement.RETURN_GENERATED_KEYS)) {

                psAlquiler.setInt(1, idUsuario);
                psAlquiler.setInt(2, idPelicula);
                psAlquiler.executeUpdate();

                ResultSet rs = psAlquiler.getGeneratedKeys();
                if (rs.next()) {
                    conn.commit();
                    return rs.getInt(1); // idAlquiler
                }
            }

            conn.rollback();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean tieneAlquilerActivo(int idPelicula, int idUsuario) {

        String sql = """
        SELECT 1
        FROM alquiler
        WHERE idPelicula = ?
          AND idUsuario = ?
          AND FechaDevolucion > CURRENT_DATE
        LIMIT 1
    """;

        try (Connection con = conexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPelicula);
            ps.setInt(2, idUsuario);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

}

