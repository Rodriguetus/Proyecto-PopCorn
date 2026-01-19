package dto;

public class usuario {
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    public usuario(int id, String correo, String nombre,  String contrasena) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public usuario() {

    }

    public usuario(String correo, String nombre, String contrasena) {
        this.correo = correo;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}