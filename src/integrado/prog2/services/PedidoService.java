package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.FoodStoreException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.interfaces.Calculable;
import integrado.prog2.repository.Repositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoService {

    private final Repositorio<Pedido> pedidoRepo;
    private final Repositorio<Usuario> usuarioRepo;
    private final Repositorio<Producto> productoRepo;

    public PedidoService(Repositorio<Pedido> pedidoRepo, Repositorio<Usuario> usuarioRepo,
            Repositorio<Producto> productoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    public List<Pedido> listar() {
        return pedidoRepo.listarActivos();
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepo.buscarActivoPorId(usuarioId)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No se encontro el usuario con id " + usuarioId + "."));

        return pedidoRepo.listarActivos().stream()
                .filter(p -> p.getUsuario() != null && p.getUsuario().getId().equals(usuario.getId()))
                .collect(Collectors.toList());
    }

    public Pedido crear(Long usuarioId, LocalDate fecha, Estado estado, FormaPago formaPago,
            List<DetalleSolicitud> detallesSolicitud) {
        Usuario usuario = usuarioRepo.buscarActivoPorId(usuarioId)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "El usuario con id " + usuarioId + " no existe o esta eliminado."));

        if (detallesSolicitud == null || detallesSolicitud.isEmpty()) {
            throw new ValidacionException("El pedido debe tener al menos un detalle.");
        }

        Pedido pedido = new Pedido(fecha, estado, formaPago, usuario);
        List<Producto> productosDescontados = new ArrayList<>();
        List<Integer> cantidadesDescontadas = new ArrayList<>();

        try {
            for (DetalleSolicitud solicitud : detallesSolicitud) {
                Producto producto = productoRepo.buscarActivoPorId(solicitud.productoId())
                        .orElseThrow(() -> new EntidadNoEncontradaException(
                                "El producto con id " + solicitud.productoId() + " no existe o esta eliminado."));

                Double subtotal = solicitud.cantidad() * producto.getPrecio();
                pedido.addDetallePedido(solicitud.cantidad(), subtotal, producto);
                productosDescontados.add(producto);
                cantidadesDescontadas.add(solicitud.cantidad());
            }

            Calculable calculable = pedido;
            calculable.calcularTotal();
            pedidoRepo.guardar(pedido);
            return pedido;

        } catch (FoodStoreException e) {
            revertirStock(productosDescontados, cantidadesDescontadas);
            throw e;
        }
    }

    public Pedido actualizar(Long id, Estado estado, FormaPago formaPago) {
        Pedido pedido = obtenerActivo(id);

        if (estado != null) {
            if (estado == Estado.CANCELADO && pedido.getEstado() != Estado.CANCELADO) {
                reponeStock(pedido);
            }
            pedido.setEstado(estado);
        }
        if (formaPago != null) {
            pedido.setFormaPago(formaPago);
        }

        return pedido;
    }

    public void eliminar(Long id) {
        Pedido pedido = obtenerActivo(id);
        reponeStock(pedido);
        pedido.setEstado(Estado.CANCELADO);
        pedido.eliminarLogicamente();
    }

    public Pedido obtenerActivo(Long id) {
        return pedidoRepo.buscarActivoPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No se encontro el pedido con id " + id + " o esta eliminado."));
    }

    private void reponeStock(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (!detalle.isEliminado() && detalle.getProducto() != null) {
                detalle.getProducto().restaurarStock(detalle.getCantidad());
            }
        }
    }

    private void revertirStock(List<Producto> productos, List<Integer> cantidades) {
        for (int i = 0; i < productos.size() && i < cantidades.size(); i++) {
            productos.get(i).restaurarStock(cantidades.get(i));
        }
    }
}
