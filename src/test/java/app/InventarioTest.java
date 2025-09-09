package app;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Pruebas de la API PÚBLICA de Inventario.
 * Verifica:
 *  - Altas con validaciones (código nulo/vacío/duplicado).
 *  - Eliminación y actualizaciones.
 *  - Búsquedas y listados inmodificables.
 *  - Generación de informe simple.
 */
class InventarioTest {

    @Test
    void alta_valida_y_duplicado() {
        Inventario inv = new Inventario();
        assertTrue(inv.agregarProducto("A1","Lapiz","HB",500,10));
        assertFalse(inv.agregarProducto("A1","Otro","",100,1)); // duplicado
        assertFalse(inv.agregarProducto("   ","X","",1,1));     // código inválido
        assertFalse(inv.agregarProducto(null,"X","",1,1));      // código nulo
    }

    @Test
    void eliminar_existente_y_noExistente() {
        Inventario inv = new Inventario();
        inv.agregarProducto("A1","Lapiz","HB",500,10);
        assertTrue(inv.eliminarPorCodigo("A1"));
        assertFalse(inv.eliminarPorCodigo("A1")); // ya no existe
    }

    @Test
    void actualizar_por_servicio_refleja_cambios() {
        Inventario inv = new Inventario();
        inv.agregarProducto("A1","Lapiz","HB",500,10);
        assertTrue(inv.actualizarProducto("A1","Lápiz grafito",null,700.0,null));

        Optional<ProductoRO> p = inv.buscarPorCodigo("A1");
        assertTrue(p.isPresent());
        assertEquals("Lápiz grafito", p.get().getNombre());
        assertEquals(700.0, p.get().getPrecio());
    }

    @Test
    void buscar_y_listar_devuelven_vistas_inmodificables() {
        Inventario inv = new Inventario();
        inv.agregarProducto("A1","Lapiz","Grafito HB",500,10);
        inv.agregarProducto("B2","Cuaderno","80 hojas",1200,5);

        List<ProductoRO> r1 = inv.buscarPorNombre("lap"); // debe encontrar "Lapiz"
        List<ProductoRO> r2 = inv.buscarPorNombre("zzz"); // sin resultados
        assertEquals(1, r1.size());
        assertTrue(r2.isEmpty());

        List<ProductoRO> todos = inv.listarTodos();
        assertEquals(2, todos.size());
        assertThrows(UnsupportedOperationException.class, () -> todos.clear());
    }

    @Test
    void informe_resumen_simple() {
        Inventario inv = new Inventario();
        inv.agregarProducto("A","X","",100,2); // 200
        inv.agregarProducto("B","Y","",50,3);  // 150
        String informe = inv.generarInforme();
        assertTrue(informe.contains("Productos: 2"));
        assertTrue(informe.contains("350.00"));
    }
}
