package dto;

import java.util.Date;

public class alquiler {
    private int id;
    private int idUsuario;
    private Date fDevolucion;
    private Date fAlquiler;
    private int idPelicula;

    public alquiler() {

    }

    public alquiler(int id, int idUsuario, Date fDevolucion, Date fAlquiler, int idPelicula) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fDevolucion = fDevolucion;
        this.fAlquiler = fAlquiler;
        this.idPelicula = idPelicula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getfDevolucion() {
        return fDevolucion;
    }

    public void setfDevolucion(Date fDevolucion) {
        this.fDevolucion = fDevolucion;
    }

    public Date getfAlquiler() {
        return fAlquiler;
    }

    public void setfAlquiler(Date fAlquiler) {
        this.fAlquiler = fAlquiler;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}