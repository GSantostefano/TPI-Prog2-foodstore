package integrado.prog2.services;

/**
 * DTO para solicitar la creacion de un detalle de pedido desde la consola.
 */
public class DetalleSolicitud {

    private final Long productoId;
    private final int cantidad;

    public DetalleSolicitud(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Long productoId() {
        return productoId;
    }

    public int cantidad() {
        return cantidad;
    }
}
