package integrado.prog2.ui;

import integrado.prog2.entities.Base;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utilidades de consola con validacion de entradas y seleccion por fila o ID.
 */
public final class ConsolaUtil {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsolaUtil() {
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero entero valido.");
            }
        }
    }

    public static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Error: ingrese un valor entre %d y %d.%n", min, max);
        }
    }

    public static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) {
                return valor;
            }
            System.out.println("Error: la cantidad debe ser mayor a 0.");
        }
    }

    public static long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SCANNER.nextLine().trim();
            try {
                return Long.parseLong(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }

    public static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SCANNER.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero decimal valido.");
            }
        }
    }

    public static Double leerDoubleOpcional(String mensaje) {
        System.out.print(mensaje);
        String entrada = SCANNER.nextLine().trim().replace(',', '.');
        if (entrada.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Numero decimal invalido.");
        }
    }

    public static Integer leerEnteroOpcional(String mensaje) {
        System.out.print(mensaje);
        String entrada = SCANNER.nextLine().trim();
        if (entrada.isEmpty()) {
            return null;
        }
        return Integer.parseInt(entrada);
    }

    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = SCANNER.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Error: el valor no puede estar vacio.");
        }
    }

    public static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        String valor = SCANNER.nextLine().trim();
        return valor.isEmpty() ? null : valor;
    }

    public static boolean leerConfirmacion(String mensaje) {
        return leerBoolean(mensaje);
    }

    public static boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String respuesta = SCANNER.nextLine().trim().toUpperCase();
            if (respuesta.equals("S") || respuesta.equals("SI")) {
                return true;
            }
            if (respuesta.equals("N") || respuesta.equals("NO")) {
                return false;
            }
            System.out.println("Error: responda S o N.");
        }
    }

    public static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        SCANNER.nextLine();
    }

    public static void exito(String mensaje) {
        System.out.println("OK " + mensaje);
    }

    public static void error(String mensaje) {
        System.out.println("ERROR " + mensaje);
    }

    public static long leerIdDesdeListado(List<? extends Base> registros, String mensaje) {
        if (registros.isEmpty()) {
            throw new IllegalStateException("No hay registros para seleccionar.");
        }

        System.out.println("\nSeleccione por numero de fila [N] o por ID del registro:");
        for (int i = 0; i < registros.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, registros.get(i));
        }

        String idsValidos = registros.stream()
                .map(r -> String.valueOf(r.getId()))
                .collect(Collectors.joining(", "));

        while (true) {
            System.out.print(mensaje);
            String entrada = SCANNER.nextLine().trim();
            try {
                long valor = Long.parseLong(entrada);

                if (valor >= 1 && valor <= registros.size()) {
                    return registros.get((int) valor - 1).getId();
                }

                for (Base registro : registros) {
                    if (registro.getId().equals(valor)) {
                        return valor;
                    }
                }

                System.out.println("Error: el valor no corresponde a ningun registro de la lista.");
                System.out.printf("Use el numero de fila [1-%d] o uno de estos IDs: %s.%n",
                        registros.size(), idsValidos);
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }
}
