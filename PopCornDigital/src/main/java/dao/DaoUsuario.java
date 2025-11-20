package dao;

import conexion.conexionDB;
import dto.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DaoUsuario {


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
                user.setIdUsuario(rs.getInt("idUsuario"));
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
}