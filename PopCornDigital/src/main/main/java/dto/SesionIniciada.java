//====================
package dto;

public class SesionIniciada {
    private static int idUsuario;
    private static String correo;

    public static void setIdUsuario(int id) {
        idUsuario = id;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        SesionIniciada.correo = correo;
    }
}
