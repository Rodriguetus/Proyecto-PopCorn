package dao;

import conexion.conexionDB;
import dto.pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int insertar(pedido p) {

        String sql = "INSERT INTO pedido (Estado, FechaCompra, FechaLlegada, idPelicula, Direccion) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getEstado());
            stmt.setDate(2, new java.sql.Date(p.getfCompra().getTime()));
            stmt.setDate(3, new java.sql.Date(p.getfLlegada().getTime()));
            stmt.setInt(4, p.getIdPelicula());
            stmt.setString(5, p.getDireccion());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                return -1;
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // devuelve el idPedido generado
                }
            }

            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void modificar(pedido p) {
        String sql = "UPDATE pedido SET Estado=?, FechaCompra=?, FechaLlegada=?, idPelicula=?, Direccion=? WHERE idPedido=?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getEstado());

            stmt.setDate(2, new java.sql.Date(p.getfCompra().getTime()));
            stmt.setDate(3, new java.sql.Date(p.getfLlegada().getTime()));

            stmt.setInt(4, p.getIdPelicula());
            stmt.setString(5, p.getDireccion());

            stmt.setInt(6, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pedido WHERE idPedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<pedido> getPedidos() throws SQLException {
        List<pedido> list = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = conexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new pedido(
                        rs.getInt("idPedido"),
                        rs.getString("Estado"),
                        rs.getDate("FechaCompra"),
                        rs.getDate("FechaLlegada"),
                        rs.getInt("idPelicula"),
                        rs.getString("Direccion")
                ));
            }
        }
        return list;
    }

    public static boolean createPedidoAndReduceStock(int idPelicula, int quantity, String direccion) throws SQLException {
        String selectStock = "SELECT Stock FROM pelicula WHERE idPelicula = ? FOR UPDATE";
        String updateStock = "UPDATE pelicula SET Stock = Stock - ? WHERE idPelicula = ?";
        String insertPedido = "INSERT INTO pedido (FechaCompra, FechaLlegada, Estado, idPelicula, Direccion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psSelect = conn.prepareStatement(selectStock)) {
                psSelect.setInt(1, idPelicula);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    int stock = rs.getInt("Stock");
                    if (stock < quantity) {
                        conn.rollback();
                        return false; // insufficient stock
                    }
                }
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(updateStock)) {
                psUpdate.setInt(1, quantity);
                psUpdate.setInt(2, idPelicula);
                psUpdate.executeUpdate();
            }

            try (PreparedStatement psInsert = conn.prepareStatement(insertPedido)) {
                Date now = new Date(System.currentTimeMillis());
                // simple arrival date example: now + 7 days
                Date arrival = new Date(System.currentTimeMillis() + 7L * 24 * 3600 * 1000);
                psInsert.setDate(1, now);
                psInsert.setDate(2, arrival);
                psInsert.setString(3, "Pendiente");
                psInsert.setInt(4, idPelicula);
                psInsert.setString(5, direccion == null ? "" : direccion);
                psInsert.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
