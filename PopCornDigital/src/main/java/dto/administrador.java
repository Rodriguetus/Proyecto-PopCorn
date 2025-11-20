package dto;


import conexion.conexionDB;

import java.sql.*;

public class administrador {
    private int idAdmin;
    private String Correo;
    private String Nombre;
    private String Contrasena;


    public administrador() {}

    public administrador(String correo, String nombre, String contrasena) {
        this.Correo = correo;
        this.Nombre = nombre;
        this.Contrasena = contrasena;
    }


    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }


    public String getCorreo() { return Correo; }
    public void setCorreo(String correo) { Correo = correo; }


    public String getNombre() { return Nombre; }
    public void setNombre(String nombre) { Nombre = nombre; }


    public String getContrasena() { return Contrasena; }
    public void setContrasena(String contrasena) { Contrasena = contrasena; }
}