package app;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración simple sobre la API de dominio:
 * Crear -> Buscar -> Actualizar -> Eliminar, verificando el flujo completo.
 */
class InventarioE2ETest {

    @Test
    void flujoCrudBasico() {
        Inventario inv = new Inventario();

        // Crear
        Producto p1 = new Producto("P-001", "Mouse", "Inalámbrico", 15990, 20);
        Producto p2 = new Producto("P-002", "Teclado", "Mecánico", 29990, 10);

        assertTrue(inv.agregar(p1), "Debe permitir agregar P-001");
        assertTrue(inv.agregar(p2), "Debe permitir agregar P-002");

        // Buscar por código
        Optional<ProductoRO> op1 = inv.buscarPorCodigo("P-001");
        assertTrue(op1.isPresent(), "P-001 debe existir");
        assertEquals("Mouse", op1.get().getNombre(), "Nombre debe coincidir");

        // Listar
        assertEquals(2, inv.listarTodos().size(), "Deben existir 2 productos");

        // Actualizar precio y stock
        assertTrue(inv.actualizarPrecio("P-002", 27990), "Debe actualizar precio");
        assertEquals(27990.0, inv.buscarPorCodigo("P-002").get().getPrecio(), 0.0001);

        assertTrue(inv.actualizarStock("P-001", 25), "Debe actualizar stock");
        assertEquals(25, inv.buscarPorCodigo("P-001").get().getStock());

        // Buscar por nombre o descripción
        assertFalse(inv.buscarPorTexto("inalámbrico").isEmpty(), "Debe encontrar por descripción");
        assertFalse(inv.buscarPorTexto("Mouse").isEmpty(), "Debe encontrar por nombre");

        // Eliminar
        assertTrue(inv.eliminar("P-001"), "Debe eliminar P-001");
        assertTrue(inv.buscarPorCodigo("P-001").isEmpty(), "P-001 ya no debe existir");
        assertEquals(1, inv.listarTodos().size(), "Debe quedar un solo producto");
    }
}
