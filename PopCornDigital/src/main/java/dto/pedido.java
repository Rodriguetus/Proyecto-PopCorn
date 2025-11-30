package dto;


import java.util.Date;


public class pedido {
    private int id;
    private String estado;
    private Date fCompra;
    private Date fLlegada;
    private int idPelicula;
    private String direccion;


    public pedido(int id, String estado, Date fCompra, Date fLlegada, int idPelicula, String direccion) {
        this.id=id;
        this.estado = estado;
        this.fCompra = fCompra;
        this.fLlegada = fLlegada;
        this.idPelicula = idPelicula;
        this.direccion = direccion;
    }


    public pedido(String estado, Date fCompra, Date fLlegada, int idPelicula, String direccion) {
        this.estado = estado;
        this.fCompra = fCompra;
        this.fLlegada = fLlegada;
        this.idPelicula = idPelicula;
        this.direccion = direccion;
    }


    public pedido() {}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Date getfCompra() {
        return fCompra;
    }
    public void setfCompra(Date fCompra) {
        this.fCompra = fCompra;
    }
    public Date getfLlegada() {
        return fLlegada;
    }
    public void setfLlegada(Date fLlegada) {
        this.fLlegada = fLlegada;
    }
    public int getIdPelicula() {
        return idPelicula;
    }
    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}


