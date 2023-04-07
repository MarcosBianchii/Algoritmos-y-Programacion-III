import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

public class AlarmaTest {
    @Test
    public void testCrearAlarma() {
        var ahora = LocalDateTime.now();
        var alarma = new Alarma(ahora);
        assertEquals(ahora, alarma.getFechaHoraDisparo());
    }

    @Test
    public void testCambiarFechaHoraDisparo() {
        var ahora = LocalDateTime.now();
        var despues = ahora.plusHours(1);
        var alarma = new Alarma(ahora);
        alarma.setFechaHoraDisparo(despues);
        assertEquals(despues, alarma.getFechaHoraDisparo());
    }
}
