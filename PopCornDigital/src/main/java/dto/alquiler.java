package dto;

import java.util.Date;

public class alquiler {
    private int id;
    private String estado;
    private Date fDevolucion;
    private Date fAlquiler;
    private int idPelicula;

    public alquiler() {
        this.id = id;
        this.estado = estado;
        this.fAlquiler = fAlquiler;
        this.fDevolucion = fDevolucion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}
