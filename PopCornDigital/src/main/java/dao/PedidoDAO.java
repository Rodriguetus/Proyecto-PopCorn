package dao;

import conexion.conexionDB;
import dto.pedido;

import java.sql.*;

public class PedidoDAO {
   public void insertar(pedido p) {
    String sql = "INSERT INTO pedido (idPedido, Estado, FechaCompra, FechaLlegada, idPelicula, Direccion) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = conexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, p.getId());
        stmt.setString(2, p.getEstado());
        stmt.setDate(3, (Date) p.getfCompra());
        stmt.setDate(4, (Date) p.getfLlegada());
        stmt.setInt(5, p.getIdPelicula());
        stmt.setString(6, p.getDireccion());

        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void modificar(pedido p) {
    String sql = "UPDATE pedido SET Estado=?, FechaCompra=?, FechaLlegada=?, idPelicula=?, Direccion=? WHERE idPedido=?";

    try (Connection conn = conexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(2, p.getEstado());
        stmt.setDate(3, (Date) p.getfCompra());
        stmt.setDate(4, (Date) p.getfLlegada());
        stmt.setInt(5, p.getIdPelicula());
        stmt.setString(6, p.getDireccion());
        stmt.setInt(1, p.getId());

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


}
