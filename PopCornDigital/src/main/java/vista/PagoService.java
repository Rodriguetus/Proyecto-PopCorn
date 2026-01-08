package vista;

import dto.SesionIniciada;
import dto.pelicula;
import java.util.List;

public class PagoService {

    public static boolean puedePagar(List<pelicula> carrito) {
        if (SesionIniciada.getIdUsuario() <= 0) return false;
        if (carrito == null || carrito.isEmpty()) return false;

        for (pelicula p : carrito) {
            if (p.getStock() <= 0) return false;
        }

        // Aquí podrías agregar más validaciones (saldo, dirección, etc.)
        return true;
    }
}
