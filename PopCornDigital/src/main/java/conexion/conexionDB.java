package conexion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class conexionDB {


    private static final String url = "jdbc:mysql://localhost:3306/popcorn?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String usuario = "root"; // cambia si usas otra contraseña
    private static final String contrasena = "root";


    private static Connection conn = null;


    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, usuario, contrasena);
                System.out.println("Conexión establecida correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}