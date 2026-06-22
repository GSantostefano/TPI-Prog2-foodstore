package integrado.prog2.config;

import integrado.prog2.enums.Rol;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.ProductoService;
import integrado.prog2.services.UsuarioService;

/**
 * Carga datos de ejemplo al iniciar para facilitar pruebas y la demo del TPI.
 */
public final class CargaInicial {

    private CargaInicial() {
    }

    public static void ejecutar() {
        CategoriaService categoriaService = AppContext.getCategoriaService();
        ProductoService productoService = AppContext.getProductoService();
        UsuarioService usuarioService = AppContext.getUsuarioService();

        var bebidas = categoriaService.crear("Bebidas", "Gaseosas, aguas y jugos");
        var pizzas = categoriaService.crear("Pizzas", "Pizzas artesanales a la piedra");
        var postres = categoriaService.crear("Postres", "Postres caseros");

        productoService.crear("Coca-Cola 500ml", "Gaseosa linea Coca-Cola",
                1200.0, 50, "coca.jpg", true, bebidas.getId());
        productoService.crear("Agua mineral", "Agua sin gas 500ml",
                800.0, 40, "agua.jpg", true, bebidas.getId());
        productoService.crear("Pizza Muzzarella", "Muzzarella y salsa de tomate",
                6500.0, 20, "muzza.jpg", true, pizzas.getId());
        productoService.crear("Pizza Napolitana", "Muzzarella, tomate y ajo",
                7200.0, 15, "napo.jpg", true, pizzas.getId());
        productoService.crear("Flan casero", "Flan con dulce de leche",
                2500.0, 25, "flan.jpg", true, postres.getId());

        usuarioService.crear("Juan", "Perez", "juan.perez@example.com",
                "1130000001", "1234", Rol.ADMIN);
        usuarioService.crear("Ana", "Gomez", "ana.gomez@example.com",
                "1130000002", "1234", Rol.USUARIO);

        System.out.println("Datos de ejemplo cargados (3 categorias, 5 productos, 2 usuarios).");
    }
}
