
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Scanner;

/**
 * Clase principal que solicita una URL, la analiza y obtiene
 * información de la conexión.
 *
 * Nombre del alumno: Ana García Pérez (Ejemplo)
 * Formato: apellido1apellido2nombre_URL
 */
public class garciaperezana_URL {

    public static void main(String[] args) {

        // Usamos try-with-resources para asegurar que el Scanner se cierra
        // al final, incluso si ocurren errores.
        try (Scanner scanner = new Scanner(System.in)) {

            // 1. Solicitar al usuario una URL por consola
            System.out.print("Introduce una URL: ");
            String urlString = scanner.nextLine();

            // El bloque try-catch principal envuelve la lógica que puede fallar
            try {

                // 2. Crear un objeto de la clase URL
                // Esta línea puede lanzar MalformedURLException si la cadena no es válida
                URL url = new URL(urlString);

                // 3. Mostrar las partes de la URL
                System.out.println("\n=== ANÁLISIS DE LA URL ===");
                System.out.println("Protocolo: " + url.getProtocol());
                System.out.println("Host: " + url.getHost());

                // url.getPort() devuelve -1 si no se especifica un puerto en la URL
                int puerto = url.getPort();
                System.out.println("Puerto: " + (puerto == -1 ? "No especificado (puerto por defecto del protocolo)" : puerto));

                // url.getPath() puede devolver vacío si la URL no tiene path (ej: http://host.com)
                String path = url.getPath();
                System.out.println("Ruta: " + (path == null || path.isEmpty() ? "/" : path));

                // getQuery() y getRef() devuelven null si esas partes no existen
                System.out.println("Consulta: " + url.getQuery());
                System.out.println("Referencia: " + url.getRef());

                // 4. Establecer una conexión con el recurso remoto
                System.out.println("\n=== INFORMACIÓN DE LA CONEXIÓN ===");

                // openConnection() prepara la conexión pero no la establece
                // Puede lanzar IOException
                URLConnection connection = url.openConnection();

                // connect() establece físicamente la conexión (sockets, etc.)
                // Puede lanzar IOException (ej. host no encontrado, timeout)
                connection.connect();

                // 5. Mostrar la información básica de la conexión (metadatos/cabeceras)
                System.out.println("Content-Type: " + connection.getContentType());

                // Usamos getContentLengthLong() para soportar tamaños grandes
                long contentLength = connection.getContentLengthLong();
                System.out.println("Content-Length: " + (contentLength == -1 ? "No informado" : contentLength));

                String encoding = connection.getContentEncoding();
                System.out.println("Content-Encoding: " + (encoding == null ? "No informado por el servidor" : encoding));

                // getDate() devuelve 0 si la cabecera 'Date' no está presente
                long dateMillis = connection.getDate();
                System.out.println("Date: " + (dateMillis == 0 ? "No informado" : new Date(dateMillis)));

                // getLastModified() devuelve 0 si la cabecera 'Last-Modified' no está
                long lastModifiedMillis = connection.getLastModified();
                System.out.println("Last-Modified: " + (lastModifiedMillis == 0 ? "No informado por el servidor" : new Date(lastModifiedMillis)));

                // 6. Manejo específico para conexiones HTTP/HTTPS
                // Verificamos si la conexión es de tipo HTTP para obtener el código de estado
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;

                    // getResponseCode() también puede implicar una conexión si no se hizo antes
                    // Puede lanzar IOException
                    int statusCode = httpConnection.getResponseCode();
                    System.out.println("Código de respuesta HTTP: " + statusCode);

                    // Es buena práctica desconectar explícitamente las HttpURLConnection
                    httpConnection.disconnect();
                }

                // 7. Manejo de errores controlados
            } catch (MalformedURLException e) {
                // Ocurre si la URL introducida (urlString) tiene un formato inválido
                System.err.println("\nError: la URL introducida no es válida.");
            } catch (IOException e) {
                // Ocurre si hay un problema al abrir o establecer la conexión
                // Ej: no hay internet, el host no existe (UnknownHostException),
                // el servidor rechaza la conexión (Connection refused)
                System.err.println("\nError: No se pudo establecer la conexión con el recurso.");
                System.err.println("Detalle: " + e.getMessage());
            } catch (Exception e) {
                // Captura genérica para cualquier otro error inesperado
                System.err.println("\nHa ocurrido un error inesperado:");
                e.printStackTrace();
            }

        } // El try-with-resources cierra el scanner aquí

        // 8. Cierre del programa (implícito al finalizar main)
    }
}