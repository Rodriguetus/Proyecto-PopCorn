package dao;

import conexion.conexionDB;
import dto.pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    /*Inserta un nuevo pedido en la base datos usando un Prepared Statement
    obteniendo el id para devolverla al finalizar*/
    public int insertar(pedido p) {

        String sql = "INSERT INTO pedido (Estado, FechaCompra, FechaLlegada, idPelicula, Direccion, Correo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getEstado());
            stmt.setDate(2, new Date(p.getfCompra().getTime()));
            stmt.setDate(3, new Date(p.getfLlegada().getTime()));
            stmt.setInt(4, p.getIdPelicula());
            stmt.setString(5, p.getDireccion());
            stmt.setString(6, p.getCorreo());

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
/*
Actualiza todos los campos de un pedido ya existente identificandolo
por su ID usando un update.
 */
    public void modificar(pedido p) {
        String sql = "UPDATE pedido SET Estado=?, FechaCompra=?, FechaLlegada=?, idPelicula=?, Direccion=?, Correo=? WHERE idPedido=?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getEstado());
            stmt.setDate(2, new Date(p.getfCompra().getTime()));
            stmt.setDate(3, new Date(p.getfLlegada().getTime()));
            stmt.setInt(4, p.getIdPelicula());
            stmt.setString(5, p.getDireccion());
            stmt.setString(6, p.getCorreo());
            stmt.setInt(7, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
Elimina un registro usando el id y una sentenia delete
 */
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
/*
Obtiene la lista de los pedidos mediante un select usando un ResultSet que mapea
cada objeto pedido y manda la lista de resultado.
 */
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
                        rs.getString("Direccion"),
                        rs.getString("Correo")
                ));
            }
        }
        return list;
    }

/*
Modifica la columna de los estados para cambia el estado.
 */
    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET Estado = ? WHERE idPedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
/*
Realiza una transacción completa bloqueando y verificando el stock disponible
reduciendolo si hay y creando el pedido o hace un rollback deshaciendo los cambios si da error.
 */
    public static int createPedidoAndReduceStock(int idPelicula, int quantity, String direccion, String correo) throws SQLException {
        String selectStock = "SELECT Stock FROM pelicula WHERE idPelicula = ? FOR UPDATE";
        String updateStock = "UPDATE pelicula SET Stock = Stock - ? WHERE idPelicula = ?";
        // Importante: Statement.RETURN_GENERATED_KEYS
        String insertPedido = "INSERT INTO pedido (FechaCompra, FechaLlegada, Estado, idPelicula, Direccion, Correo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psSelect = conn.prepareStatement(selectStock)) {
                psSelect.setInt(1, idPelicula);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return -1; }
                    if (rs.getInt("Stock") < quantity) { conn.rollback(); return -1; } // Stock insuficiente
                }
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(updateStock)) {
                psUpdate.setInt(1, quantity);
                psUpdate.setInt(2, idPelicula);
                psUpdate.executeUpdate();
            }

            int idGenerado = -1;
            // Pedimos que nos devuelva la ID generada
            try (PreparedStatement psInsert = conn.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)) {
                Date now = new Date(System.currentTimeMillis());
                Date arrival = new Date(System.currentTimeMillis() + 7L * 24 * 3600 * 1000);

                psInsert.setDate(1, now);
                psInsert.setDate(2, arrival);
                psInsert.setString(3, "Pendiente");
                psInsert.setInt(4, idPelicula);
                psInsert.setString(5, direccion == null ? "" : direccion);
                psInsert.setString(6, correo);

                int affected = psInsert.executeUpdate();
                if (affected > 0) {
                    try (ResultSet rs = psInsert.getGeneratedKeys()) {
                        if (rs.next()) {
                            idGenerado = rs.getInt(1); // ¡AQUÍ TENEMOS EL ID!
                        }
                    }
                }
            }

            if (idGenerado != -1) {
                conn.commit();
                return idGenerado; // Devolvemos el ID
            } else {
                conn.rollback();
                return -1;
            }

        } catch (SQLException ex) {
            throw ex;
        }
    }
/*
Realiza la búsqueda de la película del pedido para devolver el stock que se ha bajado.
 */
    public static boolean cancelarPedido(int idPedido) {
        String selectSql = "SELECT idPelicula FROM pedido WHERE idPedido = ?";
        String updateStock = "UPDATE pelicula SET Stock = Stock + 1 WHERE idPelicula = ?";
        String deletePedido = "DELETE FROM pedido WHERE idPedido = ?";

        Connection conn = null;
        try {
            conn = conexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciamos transacción

            // 1. Averiguar qué película es para devolverle el stock
            int idPelicula = -1;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, idPedido);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idPelicula = rs.getInt("idPelicula");
                }
            }

            // 2. Si encontramos la película, devolvemos el stock
            if (idPelicula != -1) {
                try (PreparedStatement ps = conn.prepareStatement(updateStock)) {
                    ps.setInt(1, idPelicula);
                    ps.executeUpdate();
                }
            }

            // 3. Borramos el pedido definitivamente
            try (PreparedStatement ps = conn.prepareStatement(deletePedido)) {
                ps.setInt(1, idPedido);
                ps.executeUpdate();
            }

            conn.commit(); // Confirmamos cambios
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Cerramos la conexión manual
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
/*
Busca y devuelve el último pedido realizado por un usuario para una película
ordenandolo por id de manera descendente y limitando los resultados a 1 para mostrar solo el ultimo.
 */
    public pedido buscarUltimoPedido(int idPelicula, String correo) {
        String sql = "SELECT * FROM pedido WHERE idPelicula = ? AND Correo = ? ORDER BY idPedido DESC LIMIT 1";

        try (java.sql.Connection conn = conexion.conexionDB.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPelicula);
            stmt.setString(2, correo);

            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto.pedido p = new dto.pedido();
                p.setId(rs.getInt("idPedido"));
                p.setEstado(rs.getString("Estado")); // Aquí vendrá "Pagado" si ya pagaste
                p.setfCompra(rs.getDate("FechaCompra"));
                p.setfLlegada(rs.getDate("FechaLlegada"));
                p.setIdPelicula(rs.getInt("idPelicula"));
                p.setDireccion(rs.getString("Direccion"));
                p.setCorreo(rs.getString("Correo"));
                return p;
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null; // Si devuelve null, la tarjeta se pondrá en modo default
    }
}