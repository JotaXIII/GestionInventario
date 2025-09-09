package app;

/*
 * Interfaz de SOLO LECTURA para un producto.
 * La UI u otras capas trabajan contra este contrato: pueden leer,
 * pero no pueden modificar el estado
 *
 */
public interface ProductoRO {
    String getCodigo();
    String getNombre();
    String getDescripcion();
    double getPrecio();
    int getStock();

    String descripcionDetallada();
}
