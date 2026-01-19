package vista;

import dto.pelicula;
import java.util.ArrayList;
import java.util.List;

public class CarritoService {

    //Lista para almacenar las peliculas
    private static final List<pelicula> carrito = new ArrayList<>();

    //Añade la compra
    public static void addCompra(pelicula p) {
        carrito.add(p);
    }

    //Lista de las peliculas almacenadas
    public static List<pelicula> getCarrito() {
        return carrito;
    }

    //Elimina la compra
    public static void removeCompra(pelicula p) {
        carrito.remove(p);
    }

    //Limpia el carrito
    public static void clear() {
        carrito.clear();
    }
}
