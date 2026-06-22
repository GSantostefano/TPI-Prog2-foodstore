package integrado.prog2.ui;

public class MenuPrincipal {

    private final MenuCategorias menuCategorias = new MenuCategorias();
    private final MenuProductos menuProductos = new MenuProductos();
    private final MenuUsuarios menuUsuarios = new MenuUsuarios();
    private final MenuPedidos menuPedidos = new MenuPedidos();

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            opcion = ConsolaUtil.leerEnteroEnRango("Seleccione: ", 0, 4);

            switch (opcion) {
                case 1 -> menuCategorias.mostrar();
                case 2 -> menuProductos.mostrar();
                case 3 -> menuUsuarios.mostrar();
                case 4 -> menuPedidos.mostrar();
                case 0 -> System.out.println("Gracias por usar Food Store. Hasta pronto!");
                default -> ConsolaUtil.error("Opcion invalida.");
            }
        } while (opcion != 0);
    }
}
