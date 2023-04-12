import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TareaTest {
    @Test
    public void testAgregarAlarma() {
        var tarea = new Tarea("Tarea 1", "Descripcion", LocalDateTime.now(), false);
        var alarma = new Alarma(LocalDateTime.now());
        tarea.agregarAlarma(alarma);
        assertTrue(tarea.getAlarmas().contains(alarma));
    }

    @Test
    public void testBorrarAlarma() {
        var tarea = new Tarea("Tarea 1", "Descripcion", LocalDateTime.now(), false);
        var alarma = new Alarma(LocalDateTime.now());
        tarea.agregarAlarma(alarma);
        assertTrue(tarea.getAlarmas().contains(alarma));
        tarea.borrarAlarma(alarma.getFechaHoraDisparo());
        assertFalse(tarea.getAlarmas().contains(alarma));
    }

    @Test
    public void testGetSetAlarmas() {
        var tarea = new Tarea("Tarea 1", "Descripcion", LocalDateTime.now(), false);
        var alarma = new Alarma(LocalDateTime.now());
        var alarma2 = new Alarma(LocalDateTime.now().plusHours(1));
        var alarmas = new ArrayList<Alarma>();
        alarmas.add(alarma);
        alarmas.add(alarma2);
        tarea.setAlarmas(alarmas);
        assertTrue(alarmas.containsAll(tarea.getAlarmas()));
    }
}
