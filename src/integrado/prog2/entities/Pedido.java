package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.CantidadInvalidaException;
import integrado.prog2.exception.StockInvalidoException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles;
    private Usuario usuario;

    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        if (usuario == null || usuario.isEliminado()) {
            throw new ValidacionException("No se puede crear un pedido sin un usuario valido.");
        }
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
        setUsuario(usuario);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null && !usuario.getPedidos().contains(this)) {
            usuario.agregarPedido(this);
        }
    }

    @Override
    public void calcularTotal() {
        double suma = 0.0;
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                suma += detalle.getSubtotal();
            }
        }
        this.total = suma;
    }

    /**
     * Agrega un detalle al pedido, validando stock y calculando subtotal.
     */
    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        if (producto == null || producto.isEliminado()) {
            throw new ValidacionException("El producto no existe o esta eliminado.");
        }
        if (!producto.isDisponible()) {
            throw new ValidacionException("El producto " + producto.getNombre() + " no esta disponible.");
        }
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor a 0.");
        }
        if (producto.getStock() < cantidad) {
            throw new StockInvalidoException(
                    "Stock insuficiente para " + producto.getNombre()
                            + ". Disponible: " + producto.getStock() + ", solicitado: " + cantidad);
        }

        double subtotalEsperado = cantidad * producto.getPrecio();
        if (subtotal == null || Math.abs(subtotal - subtotalEsperado) > 0.001) {
            throw new ValidacionException(
                    "El subtotal informado no coincide con cantidad x precio del producto.");
        }

        DetallePedido existente = findDetallePedidoByProducto(producto);
        if (existente != null) {
            int nuevaCantidad = existente.getCantidad() + cantidad;
            if (producto.getStock() < nuevaCantidad) {
                throw new StockInvalidoException(
                        "Stock insuficiente para " + producto.getNombre()
                                + ". Disponible: " + producto.getStock() + ", solicitado: " + nuevaCantidad);
            }
            existente.setCantidad(nuevaCantidad);
        } else {
            DetallePedido detalle = new DetallePedido(cantidad, producto);
            detalles.add(detalle);
        }

        producto.descontarStock(cantidad);
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        if (producto == null) {
            return null;
        }
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()
                    && detalle.getProducto() != null
                    && detalle.getProducto().getId().equals(producto.getId())) {
                return detalle;
            }
        }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalles.remove(detalle);
            calcularTotal();
        }
    }

    public void eliminarLogicamente() {
        setEliminado(true);
        for (DetallePedido detalle : detalles) {
            detalle.setEliminado(true);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Pedido{id=%d, fecha=%s, usuario=%s, estado=%s, formaPago=%s, total=%.2f, detalles=%d}",
                getId(),
                fecha,
                usuario != null ? usuario.getNombreCompleto() : "sin usuario",
                estado,
                formaPago,
                total,
                detalles.size());
    }
}
