package dao;

import conexion.conexionDB;
import dto.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class  DaoUsuario {

    // Registrar nuevo usuario
    public boolean registrar(usuario user) {
        String sql = "INSERT INTO usuario (Correo, Nombre, Contrasena) VALUES (?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getCorreo());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getContrasena());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public usuario login(String correo, String contrasena) {
        String sql = "SELECT * FROM usuario WHERE Correo = ? AND Contrasena = ?";


        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario user = new usuario();
                user.setId(rs.getInt("idUsuario"));
                user.setCorreo(rs.getString("Correo"));
                user.setNombre(rs.getString("Nombre"));
                user.setContrasena(rs.getString("Contrasena"));
                return user;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener el saldo actual
    public double getSaldo(int idUsuario) {
        String sql = "SELECT Saldo FROM usuario WHERE idUsuario = ?";
        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Método para restar dinero
    public boolean restarSaldo(int idUsuario, double cantidad) {
        String sql = "UPDATE usuario SET Saldo = Saldo - ? WHERE idUsuario = ?";
        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, cantidad);
            stmt.setInt(2, idUsuario);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Metodo para devolver el dinero al cancelar el Pedido
    public boolean sumarSaldo(int idUsuario, double cantidad) {
        String sql = "UPDATE usuario SET Saldo = Saldo + ? WHERE idUsuario = ?";
        try (Connection conn = conexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, cantidad);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Metodo para comprobar si ya existe el correo en la base de datos
    public boolean existeCorreo(String correo) {
        String sql = "SELECT count(*) FROM usuario WHERE correo = ?";
        try (java.sql.Connection con = conexionDB.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Si devuelve > 0, es que existe
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}