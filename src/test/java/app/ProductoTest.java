package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Pruebas unitarias de la clase concreta Producto.
 * Este test está en el MISMO paquete 'app', por lo que puede:
 *  - Instanciar 'Producto' (no es 'public').
 *  - Llamar a sus mutadores package-private.
 *
 */
class ProductoTest {

    @Test
    void constructor_normaliza_negativos() {
        Producto p = new Producto("A1","Lápiz","HB", -10.0, -5);
        // Se corrigen los valores inválidos:
        assertEquals(0.0, p.getPrecio());
        assertEquals(0, p.getStock());
        // Resto de campos quedan tal cual:
        assertEquals("A1", p.getCodigo());
        assertEquals("Lápiz", p.getNombre());
        assertEquals("HB", p.getDescripcion());
    }

    @Test
    void mutadores_respetan_limites() {
        Producto p = new Producto("X","Nombre","Desc",100.0,10);

        // Cambios válidos
        p.setNombre("Nuevo");
        p.setDescripcion("Actualizado");
        p.setStock(7);
        p.actualizarPrecio(250.0);

        assertEquals("Nuevo", p.getNombre());
        assertEquals("Actualizado", p.getDescripcion());
        assertEquals(7, p.getStock());
        assertEquals(250.0, p.getPrecio());

        // Cambios inválidos
        p.setStock(-1);
        p.actualizarPrecio(-5.0);
        assertEquals(7, p.getStock());
        assertEquals(250.0, p.getPrecio());
    }

    @Test
    void descripcionDetallada_incluye_campos_clave() {
        Producto p = new Producto("A1","Lapiz","HB", 100.0, 1);
        String s = p.descripcionDetallada();
        assertTrue(s.contains("A1"));
        assertTrue(s.contains("Lapiz"));
        assertTrue(s.contains("100.00"));
    }
}
