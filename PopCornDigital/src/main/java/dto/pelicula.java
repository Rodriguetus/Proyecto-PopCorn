package dto;

public class pelicula {
    private int id;
    private String nombre;
    private double precio;
    private Integer stock;

    private String genero;
    private String formato;
    private String proveedor;
    private int anoSalida;
    private String imagen;
    private String descripcion;

    public pelicula(int id, String nombre, double precio, Integer stock, String genero, String formato, String proveedor, int anoSalida, String imagen, String descripcion) {

        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.genero = genero;
        this.formato = formato;
        this.proveedor = proveedor;
        this.anoSalida = anoSalida;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public pelicula() {

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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getStock() {return stock;}

    public void setStock(Integer stock) {this.stock = stock;}

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

    public String getImagen() { return imagen; }

    public void setImagen(String portada) { this.imagen = portada; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
