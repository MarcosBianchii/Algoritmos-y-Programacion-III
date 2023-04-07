import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

public class TareaTest {
    @Test
    public void testCambiarTituloVacio() {
        var tarea = new Tarea("Titulo", "Descripcion", LocalDateTime.now());
        assertFalse(tarea.setTitulo(""));
    }

    @Test
    public void testCambiarDescripcionVacia() {
        var tarea = new Tarea("Titulo", "Descripcion", LocalDateTime.now());
        assertFalse(tarea.setDescripcion(""));
    }

    @Test
    public void testToggleCompletacion() {
        var tarea = new Tarea("Titulo", "Descripcion", LocalDateTime.now());
        assertTrue(tarea.toggleCompletacion());
        assertFalse(tarea.toggleCompletacion());
    }

    @Test
    public void testAgregarAlarma() {
        var tarea = new Tarea("Titulo", "Descripcion", LocalDateTime.now());
        var alarma = new Alarma(LocalDateTime.now());
        assertTrue(tarea.agregarAlarma(alarma));
        assertFalse(tarea.agregarAlarma(alarma));
    }

    @Test
    public void testBorrarAlarma() {
        var tarea = new Tarea("Titulo", "Descripcion", LocalDateTime.now());
        var alarma = new Alarma(LocalDateTime.now());
        tarea.agregarAlarma(alarma);
        tarea.borrarAlarma(alarma.getFechaHoraDisparo());
        assertEquals(0, tarea.getAlarmas().size());
    }
}
