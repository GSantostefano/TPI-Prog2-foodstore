package integrado.prog2.ui;

import integrado.prog2.config.AppContext;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.FoodStoreException;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.ProductoService;
import java.util.List;

public class MenuProductos {

    private final ProductoService productoService = AppContext.getProductoService();
    private final CategoriaService categoriaService = AppContext.getCategoriaService();

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE PRODUCTOS ---");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar por categoria");
            System.out.println("3. Crear");
            System.out.println("4. Editar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opcion = ConsolaUtil.leerEnteroEnRango("Seleccione: ", 0, 5);

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> listarPorCategoria();
                case 3 -> crear();
                case 4 -> editar();
                case 5 -> eliminar();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> ConsolaUtil.error("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listarTodos() {
        List<Producto> productos = productoService.listar();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }
        System.out.println("\n--- Productos ---");
        for (Producto producto : productos) {
            System.out.println(producto);
        }
        ConsolaUtil.pausar();
    }

    private void listarPorCategoria() {
        try {
            List<Categoria> categorias = categoriaService.listar();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorias cargadas.");
                return;
            }
            long categoriaId = ConsolaUtil.leerIdDesdeListado(categorias, "Categoria (fila o ID): ");
            List<Producto> productos = productoService.listarPorCategoria(categoriaId);
            if (productos.isEmpty()) {
                System.out.println("No hay productos para esa categoria.");
                return;
            }
            System.out.println("\n--- Productos de la categoria ---");
            for (Producto producto : productos) {
                System.out.println(producto);
            }
            ConsolaUtil.pausar();
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void crear() {
        try {
            List<Categoria> categorias = categoriaService.listar();
            if (categorias.isEmpty()) {
                System.out.println("Debe crear al menos una categoria antes de cargar productos.");
                return;
            }

            String nombre = ConsolaUtil.leerTexto("Nombre: ");
            String descripcion = ConsolaUtil.leerTexto("Descripcion: ");
            double precio = ConsolaUtil.leerDouble("Precio: ");
            int stock = ConsolaUtil.leerEntero("Stock: ");
            String imagen = ConsolaUtil.leerTexto("Imagen (nombre archivo): ");
            boolean disponible = ConsolaUtil.leerBoolean("Disponible");
            long categoriaId = ConsolaUtil.leerIdDesdeListado(categorias, "Categoria (fila o ID): ");

            Producto producto = productoService.crear(
                    nombre, descripcion, precio, stock, imagen, disponible, categoriaId);
            ConsolaUtil.exito("Producto creado con id " + producto.getId() + ".");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void editar() {
        try {
            List<Producto> productos = productoService.listar();
            if (productos.isEmpty()) {
                System.out.println("No hay productos cargados.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(productos, "Producto a editar (fila o ID): ");
            System.out.println("(Deje vacio para mantener el valor actual)");
            String nombre = ConsolaUtil.leerTextoOpcional("Nuevo nombre: ");
            String descripcion = ConsolaUtil.leerTextoOpcional("Nueva descripcion: ");
            Double precio = ConsolaUtil.leerDoubleOpcional("Nuevo precio: ");
            Integer stock = ConsolaUtil.leerEnteroOpcional("Nuevo stock: ");
            String imagen = ConsolaUtil.leerTextoOpcional("Nueva imagen: ");
            String disponibleStr = ConsolaUtil.leerTextoOpcional("Cambiar disponibilidad? (S/N): ");
            Boolean disponible = disponibleStr == null ? null : disponibleStr.equalsIgnoreCase("S");

            Long categoriaId = null;
            String cambiarCat = ConsolaUtil.leerTextoOpcional("Cambiar categoria? (S/N): ");
            if (cambiarCat != null && cambiarCat.equalsIgnoreCase("S")) {
                List<Categoria> categorias = categoriaService.listar();
                categoriaId = ConsolaUtil.leerIdDesdeListado(categorias, "Nueva categoria (fila o ID): ");
            }

            Producto producto = productoService.editar(id, nombre, descripcion, precio, stock,
                    imagen, disponible, categoriaId);
            ConsolaUtil.exito("Producto actualizado: " + producto);
        } catch (FoodStoreException | NumberFormatException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            List<Producto> productos = productoService.listar();
            if (productos.isEmpty()) {
                System.out.println("No hay productos cargados.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(productos, "Producto a eliminar (fila o ID): ");
            if (!ConsolaUtil.leerConfirmacion("Confirma eliminacion logica")) {
                System.out.println("Operacion cancelada.");
                return;
            }
            productoService.eliminar(id);
            ConsolaUtil.exito("Producto eliminado logicamente.");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }
}
