package conexion;

import java.sql.*;

public class conexionDB {

    private static final String url = "jdbc:mysql://localhost:3306/popcorn";

    private static final String usuario = "root";

    private static final String contrasena = "Laure2200";

    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, usuario, contrasena);
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}
