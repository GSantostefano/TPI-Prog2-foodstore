package integrado.prog2.config;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.repository.Repositorio;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.PedidoService;
import integrado.prog2.services.ProductoService;
import integrado.prog2.services.UsuarioService;

/**
 * Contexto de la aplicacion: repositorios y servicios en memoria.
 */
public final class AppContext {

    private static final Repositorio<Categoria> categoriaRepo = new Repositorio<>();
    private static final Repositorio<Producto> productoRepo = new Repositorio<>();
    private static final Repositorio<Usuario> usuarioRepo = new Repositorio<>();
    private static final Repositorio<Pedido> pedidoRepo = new Repositorio<>();

    private static final CategoriaService categoriaService = new CategoriaService(categoriaRepo, productoRepo);
    private static final ProductoService productoService = new ProductoService(productoRepo, categoriaRepo);
    private static final UsuarioService usuarioService = new UsuarioService(usuarioRepo);
    private static final PedidoService pedidoService = new PedidoService(
            pedidoRepo, usuarioRepo, productoRepo);

    private AppContext() {
    }

    public static CategoriaService getCategoriaService() {
        return categoriaService;
    }

    public static ProductoService getProductoService() {
        return productoService;
    }

    public static UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public static PedidoService getPedidoService() {
        return pedidoService;
    }
}
