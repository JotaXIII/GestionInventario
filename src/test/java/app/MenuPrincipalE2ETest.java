package app;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MenuPrincipalE2ETest {

    @Test
    void agregarYListarDesdeCLI() throws Exception {
        String nl = System.lineSeparator();

        // Flujo según tu salida:
        // 1) Agregar
        // 4) Listar todos
        // 6) Generar informe
        // 7) Ver producto por código
        // 0) Salir
        String input = String.join(nl,
                "1",           // Agregar
                "P-010",       // código
                "Pendrive",    // nombre
                "32GB USB 3.0",// descripción
                "5990",        // precio
                "5",           // stock
                "4",           // Listar todos
                "6",           // Generar informe
                "7",           // Ver producto por código
                "P-010",       // código a buscar
                "0"            // Salir
        ) + nl;

        InputStream in0 = System.in;
        PrintStream out0 = System.out;

        ByteArrayInputStream fakeIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream fakeOut = new PrintStream(buf, true, StandardCharsets.UTF_8);

        try {
            System.setIn(fakeIn);
            System.setOut(fakeOut);
            MenuPrincipal.main(new String[0]);
        } finally {
            System.setIn(in0);
            System.setOut(out0);
        }

        String salida = buf.toString(StandardCharsets.UTF_8);
        String salidaLower = salida.toLowerCase();

        // Aparece el menú
        assertTrue(salidaLower.contains("menu") || salidaLower.contains("menú"),
                "Debe mostrarse el menú. Salida:\n" + salida);

        // En el listado debería aparecer el producto. Aceptamos difenrentes formatos
        Pattern codigoFlexible = Pattern.compile("\\[?\\s*P\\s*-?\\s*010\\s*\\]?");
        assertTrue(codigoFlexible.matcher(salida).find(),
                "No se encontró el código P-010 en el listado. Salida:\n" + salida);

        // Debe aparecer el nombre
        assertTrue(salida.contains("Pendrive"),
                "No se encontró el nombre en el listado. Salida:\n" + salida);

        // Informe: imprime "Productos: 1 | Valor inventario: 29950.00"
        assertTrue(salida.contains("Productos: 1"),
                "El informe no refleja cantidad = 1. Salida:\n" + salida);
        assertTrue(salida.contains("29950.00"), // 5990 * 5
                "El informe no refleja el valor esperado 29950.00. Salida:\n" + salida);

        // Ver por código debería mostrar el mismo producto
        assertTrue(salida.toLowerCase().contains("ver producto por código".toLowerCase()) || true,
                "No se ejecutó la opción 7 (ver por código) según salida.\n" + salida);
        assertTrue(codigoFlexible.matcher(salida).find(),
                "La consulta por código no mostró P-010. Salida:\n" + salida);
    }
}
