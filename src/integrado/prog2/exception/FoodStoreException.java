package integrado.prog2.exception;

/**
 * Excepcion base del sistema. Permite capturar errores de negocio de forma
 * unificada en la capa de consola con un solo catch.
 */
public class FoodStoreException extends RuntimeException {

    public FoodStoreException(String mensaje) {
        super(mensaje);
    }
}
