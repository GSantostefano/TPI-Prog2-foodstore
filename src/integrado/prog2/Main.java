package integrado.prog2;

import integrado.prog2.config.CargaInicial;
import integrado.prog2.ui.MenuPrincipal;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Punto de entrada de Food Store. Solo wiring y arranque del menu.
 */
public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        CargaInicial.ejecutar();

        new MenuPrincipal().iniciar();
    }
}
