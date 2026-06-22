package integrado.prog2.entities;

import integrado.prog2.exception.CantidadInvalidaException;
import integrado.prog2.exception.ValidacionException;

public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) throws ValidacionException {
        super();
        this.producto = producto;
        setCantidad(cantidad);
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) throws CantidadInvalidaException {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad del detalle debe ser mayor a 0.");
        }
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    private Double calcularSubtotal() {
        if (producto == null || producto.getPrecio() == null) {
            return 0.0;
        }
        return cantidad * producto.getPrecio();
    }

    @Override
    public String toString() {
        return String.format("DetallePedido{id=%d, producto='%s', cantidad=%d, subtotal=%.2f}",
                getId(),
                producto != null ? producto.getNombre() : "sin producto",
                cantidad,
                subtotal);
    }
}
