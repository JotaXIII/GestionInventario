package app;

import java.util.*;

/*
 * AGREGADO RAÍZ del dominio.
 * - Mantiene y controla la colección de productos (HashMap<codigo, Producto>).
 * - ÚNICO punto autorizado para CREAR y MODIFICAR productos.
 * - Hacia fuera devuelve SOLO 'ProductoRO' (read only) y listas inmodificables.
 *
 */
public final class Inventario {
    // Almacén interno: oculto tras la API pública
    private final Map<String, Producto> productos = new HashMap<>();

    // Normaliza cadenas: trim y evita vacíos
    private static String norm(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    // ---------- COMANDOS ----------

    /*
     * Crea y agrega un producto nuevo.
     * Rechaza códigos nulos, vacíos o duplicados.
     * Retorna true si se agregó o false en caso contrario.
     */
    public boolean agregarProducto(String codigo, String nombre, String descripcion, double precio, int stock) {
        String cod = norm(codigo);
        if (cod == null || productos.containsKey(cod)) return false;
        productos.put(cod, new Producto(cod, nombre, descripcion, precio, stock));
        return true;
    }

    /*
     * Elimina un producto por su código.
     * Retorna verdadero si existía y fue removido.
     */
    public boolean eliminarPorCodigo(String codigo) {
        return productos.remove(norm(codigo)) != null;
    }

    /*
     * Actualiza campos del producto.
     * Si precio o stock vienen null, no se tocan
     * si vienen con valores inválidos, se ignoran dentro de los setters.
     */
    public boolean actualizarProducto(String codigo, String nombre, String descripcion, Double precio, Integer stock) {
        Producto p = productos.get(norm(codigo));
        if (p == null) return false;
        if (nombre != null)      p.setNombre(nombre);
        if (descripcion != null) p.setDescripcion(descripcion);
        if (precio != null)      p.actualizarPrecio(precio);
        if (stock != null)       p.setStock(stock);
        return true;
    }

    // ---------- CONSULTAS ----------

    /*
     * Busca por código y devuelve un Optional<ProductoRO>.
     * Nunca expone la clase concreta 'Producto'.
     */
    public Optional<ProductoRO> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(productos.get(norm(codigo)));
    }

    /*
     * Búsqueda por texto en nombre o descripción
     * Devuelve una lista de ProductoRO.
     */
    public List<ProductoRO> buscarPorNombre(String texto) {
        if (texto == null || texto.isBlank()) return List.of();
        String t = texto.toLowerCase(Locale.ROOT);
        List<ProductoRO> out = new ArrayList<>();
        for (Producto p : productos.values()) {
            String n = p.getNombre() == null ? "" : p.getNombre().toLowerCase(Locale.ROOT);
            String d = p.getDescripcion() == null ? "" : p.getDescripcion().toLowerCase(Locale.ROOT);
            if (n.contains(t) || d.contains(t)) out.add(p); // p es un ProductoRO
        }
        return Collections.unmodifiableList(out);
    }

    /*
     * Lista completa del inventario como solo lectura.
     * No se expone el Map interno ni se permite 'clear/add' desde fuera.
     */
    public List<ProductoRO> listarTodos() {
        List<ProductoRO> copia = new ArrayList<>(productos.values());
        return Collections.unmodifiableList(copia);
    }

    /*
     * Informe simple del inventario
     * cantidad de productos y valor total
     */
    public String generarInforme() {
        int total = productos.size();
        double valor = 0.0;
        for (Producto p : productos.values()) {
            valor += p.getPrecio() * p.getStock();
        }
        return "Productos: " + total + " | Valor inventario: " + String.format("%.2f", valor);
    }

    // ---------- ADAPTADORES PARA PRUEBAS DE INTEGRACIÓN (E2E) ----------
    /*
     * API auxiliar para los tests E2E:
     * - agregar(Producto)
     * - eliminar(String)
     * - actualizarPrecio(String, double)
     * - actualizarStock(String, int)
     * - buscarPorTexto(String)
     * No reemplaza la API existente; solo la complementa.
     */

    // Agrega un producto ya construido
    public boolean agregar(Producto p) {
        if (p == null) return false;
        String cod = norm(p.getCodigo());
        if (cod == null) return false;
        if (productos.containsKey(cod)) return false; // evita duplicados
        productos.put(cod, p);
        return true;
    }

    // Elimina por código
    public boolean eliminar(String codigo) {
        return eliminarPorCodigo(codigo);
    }

    // Actualiza precio por código
    public boolean actualizarPrecio(String codigo, double nuevoPrecio) {
        if (nuevoPrecio < 0) return false;
        Producto p = productos.get(norm(codigo));
        if (p == null) return false;
        p.actualizarPrecio(nuevoPrecio);
        return true;
    }

    // Actualiza stock por código
    public boolean actualizarStock(String codigo, int nuevoStock) {
        if (nuevoStock < 0) return false;
        Producto p = productos.get(norm(codigo));
        if (p == null) return false;
        p.setStock(nuevoStock);
        return true;
    }

    // Búsqueda por texto en nombre/descripcion
    public List<ProductoRO> buscarPorTexto(String texto) {
        // Reutiliza la búsqueda ya implementada
        return buscarPorNombre(texto);
    }
}
