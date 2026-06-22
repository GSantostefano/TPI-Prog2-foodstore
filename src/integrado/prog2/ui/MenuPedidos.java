package integrado.prog2.ui;

import integrado.prog2.config.AppContext;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.FoodStoreException;
import integrado.prog2.services.DetalleSolicitud;
import integrado.prog2.services.PedidoService;
import integrado.prog2.services.ProductoService;
import integrado.prog2.services.UsuarioService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuPedidos {

    private final PedidoService pedidoService = AppContext.getPedidoService();
    private final UsuarioService usuarioService = AppContext.getUsuarioService();
    private final ProductoService productoService = AppContext.getProductoService();

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE PEDIDOS ---");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar por usuario");
            System.out.println("3. Crear");
            System.out.println("4. Actualizar estado/forma de pago");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opcion = ConsolaUtil.leerEnteroEnRango("Seleccione: ", 0, 5);

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> listarPorUsuario();
                case 3 -> crear();
                case 4 -> actualizar();
                case 5 -> eliminar();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> ConsolaUtil.error("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listarTodos() {
        List<Pedido> pedidos = pedidoService.listar();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
            return;
        }
        imprimirPedidos(pedidos);
        ConsolaUtil.pausar();
    }

    private void listarPorUsuario() {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios cargados.");
                return;
            }
            long usuarioId = ConsolaUtil.leerIdDesdeListado(usuarios, "Usuario (fila o ID): ");
            List<Pedido> pedidos = pedidoService.listarPorUsuario(usuarioId);
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos para ese usuario.");
                return;
            }
            imprimirPedidos(pedidos);
            ConsolaUtil.pausar();
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void imprimirPedidos(List<Pedido> pedidos) {
        System.out.println("\n--- Pedidos ---");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (!detalle.isEliminado()) {
                    System.out.println("   " + detalle);
                }
            }
        }
    }

    private void crear() {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            if (usuarios.isEmpty()) {
                System.out.println("Debe crear al menos un usuario antes de registrar pedidos.");
                return;
            }
            long usuarioId = ConsolaUtil.leerIdDesdeListado(usuarios, "Usuario (fila o ID): ");

            List<DetalleSolicitud> detalles = armarDetalles();
            if (detalles.isEmpty()) {
                ConsolaUtil.error("No se agregaron detalles. Pedido cancelado.");
                return;
            }

            Estado estado = leerEstado();
            FormaPago formaPago = leerFormaPago();

            Pedido pedido = pedidoService.crear(
                    usuarioId, LocalDate.now(), estado, formaPago, detalles);
            ConsolaUtil.exito("Pedido creado con id " + pedido.getId()
                    + ". Total: $" + String.format("%.2f", pedido.getTotal()));
        } catch (FoodStoreException e) {
            ConsolaUtil.error("Pedido cancelado: " + e.getMessage());
        }
    }

    private List<DetalleSolicitud> armarDetalles() {
        List<DetalleSolicitud> detalles = new ArrayList<>();
        boolean agregarMas;

        do {
            List<Producto> productos = productoService.listarDisponiblesParaPedido();
            if (productos.isEmpty()) {
                ConsolaUtil.error("No hay productos disponibles con stock.");
                break;
            }

            try {
                long productoId = ConsolaUtil.leerIdDesdeListado(productos, "Producto (fila o ID): ");
                int cantidad = ConsolaUtil.leerEnteroPositivo("Cantidad: ");
                detalles.add(new DetalleSolicitud(productoId, cantidad));
                ConsolaUtil.exito("Detalle agregado.");
            } catch (FoodStoreException e) {
                ConsolaUtil.error(e.getMessage());
            }

            agregarMas = ConsolaUtil.leerBoolean("Agregar otro producto");
        } while (agregarMas);

        return detalles;
    }

    private void actualizar() {
        try {
            List<Pedido> pedidos = pedidoService.listar();
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos cargados.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(pedidos, "Pedido a actualizar (fila o ID): ");

            Estado estado = null;
            if (ConsolaUtil.leerBoolean("Cambiar estado")) {
                estado = leerEstado();
            }
            FormaPago formaPago = null;
            if (ConsolaUtil.leerBoolean("Cambiar forma de pago")) {
                formaPago = leerFormaPago();
            }

            Pedido pedido = pedidoService.actualizar(id, estado, formaPago);
            ConsolaUtil.exito("Pedido actualizado: " + pedido);
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            List<Pedido> pedidos = pedidoService.listar();
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos cargados.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(pedidos, "Pedido a eliminar (fila o ID): ");
            if (!ConsolaUtil.leerConfirmacion("Confirma eliminacion logica (se repondra el stock)")) {
                System.out.println("Operacion cancelada.");
                return;
            }
            pedidoService.eliminar(id);
            ConsolaUtil.exito("Pedido eliminado logicamente. Stock repuesto.");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private Estado leerEstado() {
        System.out.println("Estados: 1-PENDIENTE, 2-CONFIRMADO, 3-TERMINADO, 4-CANCELADO");
        return switch (ConsolaUtil.leerEnteroEnRango("Seleccione estado: ", 1, 4)) {
            case 1 -> Estado.PENDIENTE;
            case 2 -> Estado.CONFIRMADO;
            case 3 -> Estado.TERMINADO;
            default -> Estado.CANCELADO;
        };
    }

    private FormaPago leerFormaPago() {
        System.out.println("Formas de pago: 1-TARJETA, 2-TRANSFERENCIA, 3-EFECTIVO");
        return switch (ConsolaUtil.leerEnteroEnRango("Seleccione forma de pago: ", 1, 3)) {
            case 1 -> FormaPago.TARJETA;
            case 2 -> FormaPago.TRANSFERENCIA;
            default -> FormaPago.EFECTIVO;
        };
    }
}
