package dto;

public class pelicula {
    private int id;
    private String nombre;
    private float precio;
    private int stock;
    private String genero;
    private String formato;
    private String proveedor;
    private int anoSalida;

    public pelicula(int id, String nombre, float precio, int stock, String genero, String formato, String proveedor, anoSalida) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.genero = genero;
        this.formato = formato;
        this.proveedor = proveedor;
        this.anoSalida = anoSalida;
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

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getAnoSalida() {
        return anoSalida;
    }

    public void setAnoSalida(int anoSalida) {
        this.anoSalida = anoSalida;
    }

}
