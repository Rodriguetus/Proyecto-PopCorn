package vista;

import dto.pelicula;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para la gestión de las compras del carrito.
 *
 * Actua como un carrito para las películas que el usuario
 * desea comprar antes de realizar el pedido.
 */

public class CarritoService {

    //Lista para almacenar las peliculas
    private static final List<pelicula> carrito = new ArrayList<>();

    /**
     *Añade la película a lista del carrito.
     *
     * @param p La pelicula que se desea añadir al carrito.
     */
    public static void addCompra(pelicula p) {
        carrito.add(p);
    }

    /**
     * Obtiene la lista actual de las películas en el carrito.
     *
     * @return Lista de objetos que estan actualmente en el carrito.
     */
    public static List<pelicula> getCarrito() {
        return carrito;
    }

    /**
     * Elimina una pelicula especifica del carrito
     *
     * @param p La pelicula que se elimina de la lista
     */
    public static void removeCompra(pelicula p) {
        carrito.remove(p);
    }

    /**
     * Elimina el contenido del carrito.
     */
    public static void clear() {
        carrito.clear();
    }
}
