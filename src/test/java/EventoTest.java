import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Arrays;

public class EventoTest {
    @Test
    public void testSetTitulo() {
        var evento = new Evento("Evento 1", "Descripcion 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertTrue(evento.setTitulo("Evento 2"));
        assertFalse(evento.setTitulo(""));
    }

    @Test
    public void testSetDescripcion() {
        var evento = new Evento("Evento 1", "Descripcion 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertTrue(evento.setDescripcion("Descripcion 2"));
        assertFalse(evento.setDescripcion(""));
    }

    @Test
    public void testAgregarAlarma() {
        var evento = new Evento("Evento 1", "Descripcion 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        var alarma = new Alarma(LocalDateTime.now());
        assertTrue(evento.agregarAlarma(alarma));
        assertFalse(evento.agregarAlarma(alarma));
    }

    @Test
    public void testBorrarAlarma() {
        var evento = new Evento("Evento 1", "Descripcion 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        var alarma = new Alarma(LocalDateTime.now());
        evento.agregarAlarma(alarma);
        evento.borrarAlarma(alarma.getFechaHoraDisparo());
        assertEquals(0, evento.getAlarmas().size());
    }

    @Test
    public void testSetRepeticion() {
        var evento = new Evento("Evento 1", "Descripcion 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertTrue(evento.setRepeticion(Repeticion.DIARIA, 1));
        assertTrue(evento.setRepeticion(Repeticion.DIARIA, LocalDateTime.now()));
        assertFalse(evento.setRepeticion(Repeticion.SEMANAL, 1));
        assertFalse(evento.setRepeticion(Repeticion.SEMANAL, LocalDateTime.now()));
        assertTrue(evento.setRepeticion(Repeticion.MENSUAL, 1));
        assertTrue(evento.setRepeticion(Repeticion.MENSUAL, LocalDateTime.now()));
        assertTrue(evento.setRepeticion(Repeticion.ANUAL, 1));
        assertTrue(evento.setRepeticion(Repeticion.ANUAL, LocalDateTime.now().plusYears(1)));
    }

    @Test
    public void testSetRepeticionSemanal() {
        var evento = new Evento("Titulo", "Descripcion", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        var dias = new ArrayList<>(Arrays.asList(true, false, false, false, false, false, false));
        evento.setRepeticionSemanal(new ArrayList<>(dias), 1);
        assertEquals(dias, evento.getDias());
    }

    @Test
    public void testGetAlarmas() {
        var evento = new Evento("Titulo", "Descripcion", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        var alarma = new Alarma(LocalDateTime.now());
        evento.agregarAlarma(alarma);
        assertEquals(alarma, evento.getAlarmas().get(0));
    }
}
