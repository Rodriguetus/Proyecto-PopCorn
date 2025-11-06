package conexion;

import java.sql.*;

public class conexionDB {

private static final String url="jdbc:mysql://localhost:3306/popcorn";

private static final String usuario="root";

private static final String contrasena="root";

public static Connection getConnection() {
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(url, usuario, contrasena);
        System.out.println("Conexión establecida correctamente.");
    } catch (SQLException e) {
        System.err.println("Error al establecer la conexión " + e.getMessage());
    }
    return conn;
}

public static void main(String[] args) {
    try {
        Connection conexion = conexionDB.getConnection();
        if (conexion != null) {
            conexion.close();
            System.out.println("🔒 Conexión cerrada correctamente.");
        }
    } catch (SQLException e) {
        System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
    }
}
}
