package app;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/*
 * Interfaz de usuario por CONSOLA.
 * - Solo usa la API pública de Inventario y el contrato de lectura ProductoRO.
 *
 * Flujo: muestra menú → lee opción → delega en los métodos de Inventario →
 *        muestra resultados en texto.
 */
public class MenuPrincipal {
    private final Inventario inventario = new Inventario();
    private final Scanner sc = new Scanner(System.in);

    // Punto de entrada de la app
    public static void main(String[] args) {
        new MenuPrincipal().run();
    }

    // Bucle principal del menú
    private void run() {
        int op;
        do {
            System.out.println("\n=== Menú ===");
            System.out.println("1) Agregar producto");
            System.out.println("2) Eliminar producto");
            System.out.println("3) Buscar por nombre o descripciòn");
            System.out.println("4) Listar todos");
            System.out.println("5) Actualizar producto");
            System.out.println("6) Generar informe");
            System.out.println("7) Ver producto por código");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            op = leerEntero();

            switch (op) {
                case 1 -> agregar();
                case 2 -> eliminar();
                case 3 -> buscarTexto();
                case 4 -> listar();
                case 5 -> actualizar();
                case 6 -> informe();
                case 7 -> verPorCodigo();
                case 0 -> System.out.println("Adiós.");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // ---------- Acciones de menú ----------

    // Pide datos y delega la creación en Inventario
    private void agregar() {
        String codigo = leerLinea("Código: ");
        String nombre = leerLinea("Nombre: ");
        String desc   = leerLinea("Descripción: ");
        double precio = leerDouble("Precio: ");
        int stock     = leerEntero("Stock: ");
        boolean ok = inventario.agregarProducto(codigo, nombre, desc, precio, stock);
        System.out.println(ok ? "Agregado." : "No se pudo agregar, código duplicado o inválido.");
    }

    // Elimina por código
    private void eliminar() {
        String codigo = leerLinea("Código a eliminar: ");
        System.out.println(inventario.eliminarPorCodigo(codigo) ? "Eliminado." : "No existe.");
    }

    // Busca por nombre o descripción
    private void buscarTexto() {
        String texto = leerLinea("Texto a buscar: ");
        List<ProductoRO> r = inventario.buscarPorNombre(texto);
        if (r.isEmpty()) System.out.println("Sin resultados.");
        else r.forEach(p -> System.out.println(p.descripcionDetallada()));
    }

    // Listar todos
    private void listar() {
        List<ProductoRO> todos = inventario.listarTodos();
        if (todos.isEmpty()) System.out.println("Inventario vacío.");
        else todos.forEach(p -> System.out.println(p.descripcionDetallada()));
    }

    // Actualiza campos
    private void actualizar() {
        String codigo = leerLinea("Código a actualizar: ");

        String nombre = leerLinea("Nuevo nombre: ");
        if (nombre.isBlank()) nombre = null;

        String desc = leerLinea("Nueva descripción: ");
        if (desc.isBlank()) desc = null;

        String sPrecio = leerLinea("Nuevo precio: ");
        Double precio = sPrecio.isBlank() ? null : Double.parseDouble(sPrecio);

        String sStock = leerLinea("Nuevo stock: ");
        Integer stock = sStock.isBlank() ? null : Integer.parseInt(sStock);

        boolean ok = inventario.actualizarProducto(codigo, nombre, desc, precio, stock);
        System.out.println(ok ? "Actualizado." : "No existe el código.");
    }

    // Muestra informe del inventario
    private void informe() {
        System.out.println(inventario.generarInforme());
    }

    // Busca por código
    private void verPorCodigo() {
        String codigo = leerLinea("Código: ");
        Optional<ProductoRO> p = inventario.buscarPorCodigo(codigo);
        System.out.println(p.map(ProductoRO::descripcionDetallada).orElse("No existe."));
    }

    // ---------- Entradas seguras ----------

    private String leerLinea(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private int leerEntero() {
        return leerEntero(null);
    }
    private int leerEntero(String prompt) {
        while (true) {
            try {
                if (prompt != null) System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Número inválido, intenta de nuevo.");
            }
        }
    }

    private double leerDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Número inválido, intenta de nuevo.");
            }
        }
    }
}
