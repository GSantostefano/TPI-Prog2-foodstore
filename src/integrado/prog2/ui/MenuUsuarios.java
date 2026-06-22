package integrado.prog2.ui;

import integrado.prog2.config.AppContext;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.FoodStoreException;
import integrado.prog2.services.UsuarioService;
import java.util.List;

public class MenuUsuarios {

    private final UsuarioService usuarioService = AppContext.getUsuarioService();

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE USUARIOS ---");
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
        List<Usuario> usuarios = usuarioService.listar();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        System.out.println("\n--- Usuarios ---");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
        ConsolaUtil.pausar();
    }

    private void crear() {
        try {
            String nombre = ConsolaUtil.leerTexto("Nombre: ");
            String apellido = ConsolaUtil.leerTexto("Apellido: ");
            String mail = ConsolaUtil.leerTexto("Mail: ");
            String celular = ConsolaUtil.leerTexto("Celular: ");
            String contrasenia = ConsolaUtil.leerTexto("Contrasenia: ");
            Rol rol = leerRol();

            Usuario usuario = usuarioService.crear(nombre, apellido, mail, celular, contrasenia, rol);
            ConsolaUtil.exito("Usuario creado con id " + usuario.getId() + ".");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void editar() {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios cargados.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(usuarios, "Usuario a editar (fila o ID): ");
            System.out.println("(Deje vacio para mantener el valor actual)");
            String nombre = ConsolaUtil.leerTextoOpcional("Nuevo nombre: ");
            String apellido = ConsolaUtil.leerTextoOpcional("Nuevo apellido: ");
            String mail = ConsolaUtil.leerTextoOpcional("Nuevo mail: ");
            String celular = ConsolaUtil.leerTextoOpcional("Nuevo celular: ");
            String contrasenia = ConsolaUtil.leerTextoOpcional("Nueva contrasenia: ");
            String rolStr = ConsolaUtil.leerTextoOpcional("Cambiar rol? (S/N): ");
            Rol rol = rolStr != null && rolStr.equalsIgnoreCase("S") ? leerRol() : null;

            Usuario usuario = usuarioService.editar(id, nombre, apellido, mail, celular, contrasenia, rol);
            ConsolaUtil.exito("Usuario actualizado: " + usuario);
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios cargados.");
                return;
            }

            long id = ConsolaUtil.leerIdDesdeListado(usuarios, "Usuario a eliminar (fila o ID): ");
            if (!ConsolaUtil.leerConfirmacion("Confirma eliminacion logica")) {
                System.out.println("Operacion cancelada.");
                return;
            }
            usuarioService.eliminar(id);
            ConsolaUtil.exito("Usuario eliminado logicamente.");
        } catch (FoodStoreException e) {
            ConsolaUtil.error(e.getMessage());
        }
    }

    private Rol leerRol() {
        System.out.println("Roles: 1-ADMIN, 2-USUARIO");
        int opcion = ConsolaUtil.leerEnteroEnRango("Seleccione rol: ", 1, 2);
        return opcion == 1 ? Rol.ADMIN : Rol.USUARIO;
    }
}
