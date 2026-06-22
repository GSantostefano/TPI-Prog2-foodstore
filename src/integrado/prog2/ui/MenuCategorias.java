package integrado.prog2.ui;

import integrado.prog2.config.AppContext;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.FoodStoreException;
import integrado.prog2.services.CategoriaService;
import integrado.prog2.services.ProductoService;
import java.util.List;

public class MenuCategorias {

    private final CategoriaService categoriaService = AppContext.getCategoriaService();
    private final ProductoService productoService = AppContext.getProductoService();

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE CATEGORIAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            opcion = ConsolaUtil.leerEnteroEnRango("Seleccione: ", 0, 4);

            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> ConsolaUtil.error("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void listar() {
        List<Categoria> categorias = categoriaService.listar();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }
        System.out.println("\n--- Categorias ---");
        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }
        ConsolaUtil.pausar();
    }

    private void crear() {
        try {
            String nombre = ConsolaUtil.leerTexto("Nombre: ");
            String descripcion = ConsolaUtil.leerTexto("Descripcion: ");
            Categoria categoria = categoriaService.crear(nombre, descripcion);
            ConsolaUtil.exito("Categoria creada con id " + categoria.getId() + ".");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void editar() {
        try {
            List<Categoria> categorias = categoriaService.listar();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorias cargadas.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(categorias, "Categoria a editar (fila o ID): ");
            System.out.println("(Deje vacio para mantener el valor actual)");
            String nombre = ConsolaUtil.leerTextoOpcional("Nuevo nombre: ");
            String descripcion = ConsolaUtil.leerTextoOpcional("Nueva descripcion: ");

            Categoria categoria = categoriaService.editar(id, nombre, descripcion);
            ConsolaUtil.exito("Categoria actualizada: " + categoria);
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            List<Categoria> categorias = categoriaService.listar();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorias cargadas.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(categorias, "Categoria a eliminar (fila o ID): ");

            if (productoService.existenProductosEnCategoria(id)) {
                System.out.println("AVISO: Esta categoria tiene productos asociados activos.");
                System.out.println("No se puede eliminar mientras existan productos vinculados.");
                return;
            }

            if (!ConsolaUtil.leerConfirmacion("Confirma eliminacion logica")) {
                System.out.println("Operacion cancelada.");
                return;
            }

            categoriaService.eliminar(id);
            ConsolaUtil.exito("Categoria eliminada logicamente.");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }
}
