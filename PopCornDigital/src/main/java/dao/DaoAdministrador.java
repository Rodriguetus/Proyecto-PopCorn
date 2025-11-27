package dao;

import conexion.conexionDB;
import dto.administrador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DaoAdministrador {


    // Registrar nuevo usuario
    public boolean registrar(administrador admin) {
        String sql = "INSERT INTO administrador (Correo, Nombre, Contrasena) VALUES (?, ?, ?)";


        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ;
            ps.setString(1, admin.getCorreo());
            ps.setString(2, admin.getNombre());
            ps.setString(3, admin.getContrasena());


            return ps.executeUpdate() > 0;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public administrador login(String correo, String contrasena) {
        String sql = "SELECT * FROM administrador WHERE Correo = ? AND Contrasena = ?";


        try (Connection conn = conexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setString(1, correo);
            ps.setString(2, contrasena);


            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                administrador ad = new administrador();
                ad.setId(rs.getInt("idAdmin"));
                ad.setCorreo(rs.getString("Correo"));
                ad.setNombre(rs.getString("Nombre"));
                ad.setContrasena(rs.getString("Contrasena"));
                return ad;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}