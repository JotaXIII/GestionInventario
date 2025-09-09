package app;

/*
 * Implementación CONCRETA del producto.
 * - NO es 'public' (package-private): solo es visible dentro del paquete 'app'.
 * - Implementa ProductoRO para ofrecer lectura sin permitir cambios externos.
 * - Las operaciones de cambio (setters) también son package-private: únicamente
 *   el código dentro de 'app' puede usarlas.
 *
 * Nota: El MenuPrincipal no puede crear ni modificar 'Producto' directamente.
 *       Debe pasar por 'Inventario'.
 */
final class Producto implements ProductoRO {
    private final String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    // Constructor package private
    Producto(String codigo, String nombre, String descripcion, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = Math.max(0.0, precio);
        this.stock  = Math.max(0, stock);
    }

    // ======= Lectura =======
    @Override public String getCodigo()      { return codigo; }
    @Override public String getNombre()      { return nombre; }
    @Override public String getDescripcion() { return descripcion; }
    @Override public double getPrecio()      { return precio; }
    @Override public int getStock()          { return stock; }

    // ======= Actualizar =======
    void setNombre(String nombre)            { this.nombre = nombre; }
    void setDescripcion(String descripcion)  { this.descripcion = descripcion; }
    void setStock(int stock)                 { if (stock >= 0) this.stock = stock; }
    void actualizarPrecio(double nuevoPrecio){ if (nuevoPrecio >= 0) this.precio = nuevoPrecio; }

    // ======= Presentación =======
    @Override
    public String descripcionDetallada() {
        return "[" + codigo + "] " + nombre + " - " + descripcion +
                " | Precio: " + String.format("%.2f", precio) +
                " | Stock: " + stock;
    }
}
