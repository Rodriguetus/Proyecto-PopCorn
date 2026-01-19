package vista;

import dto.pelicula;

import java.util.ArrayList;
import java.util.List;
/**
 * Servicio encargado de gestionar el carrito de alquiler de películas.
 *
 * <p>
 * Esta clase mantiene una lista estática de películas que el usuario ha
 * añadido para su alquiler. Proporciona métodos para añadir, eliminar,
 * obtener y limpiar el contenido del carrito.
 * </p>
 *
 * <p>
 * Al tratarse de un servicio con métodos estáticos, el carrito se comparte
 * durante toda la ejecución de la aplicación.
 * </p>
 *
 * @author LaureanoCL
 * @version 1.0
 * @since 1.0
 */
public class CarritoAlquilerService {

    /**
     * Lista estática que almacena las películas añadidas al carrito de alquiler.
     */
    private static final List<pelicula> carrito = new ArrayList<>();

    /**
     * Añade una película al carrito de alquiler.
     *
     * @param p película que se desea añadir al carrito
     */
    public static void addAlquiler(pelicula p) {
        carrito.add(p);
    }

    /**
     * Devuelve la lista de películas almacenadas en el carrito.
     *
     * @return lista de películas añadidas al carrito
     */
    public static List<pelicula> getCarrito() {
        return carrito;
    }

    /**
     * Elimina todas las películas almacenadas en el carrito de alquiler.
     */
    public static void clear() {
        carrito.clear();
    }
}
