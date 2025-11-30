package vista;

import dto.pelicula;
import java.util.ArrayList;
import java.util.List;

public class CarritoService {

    private static final List<pelicula> carrito = new ArrayList<>();

    public static void addCompra(pelicula p) {
        carrito.add(p);
    }

    public static List<pelicula> getCarrito() {
        return carrito;
    }

    public static void removeCompra(pelicula p) {
        carrito.remove(p);
    }

    public static void clear() {
        carrito.clear();
    }
}
