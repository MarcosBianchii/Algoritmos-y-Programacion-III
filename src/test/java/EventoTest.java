import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Arrays;

public class EventoTest {
    @Test
    public void testAgregarAlarma() {
        var momento = LocalDateTime.now();
        var evento = new Evento("Evento 1", "Descripcion 1", momento, momento);
        var alarma = new Alarma(momento);
        evento.agregarAlarma(alarma);
        assertTrue(evento.getAlarmas().contains(alarma));
    }

    @Test
    public void testBorrarAlarma() {
        var momento = LocalDateTime.now();
        var evento = new Evento("Evento 1", "Descripcion 1", momento, momento);
        var alarma = new Alarma(momento);
        evento.agregarAlarma(alarma);
        assertTrue(evento.getAlarmas().contains(alarma));
        evento.borrarAlarma(alarma.getFechaHoraDisparo());
        assertFalse(evento.getAlarmas().contains(alarma));
    }

    @Test
    public void testGetSetAlarmas() {
        var momento = LocalDateTime.now();
        var evento = new Evento("Tarea 1", "Descripcion", momento, momento);
        var alarma = new Alarma(momento);
        var alarma2 = new Alarma(momento.plusHours(1));
        var alarmas = new ArrayList<Alarma>();
        alarmas.add(alarma);
        alarmas.add(alarma2);
        evento.setAlarmas(alarmas);
        assertTrue(alarmas.containsAll(evento.getAlarmas()));
    }

    @Test
    public void testSetRepeticionAnioBisiesto() {
        testSetRepeticionFecha(LocalDateTime.of(2023, 4, 12, 12, 0));
    }

    @Test
    public void testSetRepeticionAnioNoBisiesto() {
        testSetRepeticionFecha(LocalDateTime.of(2022, 4, 12, 12, 0));
    }

    private void testSetRepeticionFecha(LocalDateTime momento) {
        var evento = new Evento("Evento 1", "Descripcion 1", momento, momento);

        // Repeticion diaria
        evento.setRepeticion(Repeticion.DIARIA, evento.getInicio().plusYears(1));
        var diasEnAnio = momento.getYear() % 4 == 3 ? 366 : 365; // No funcionaba el toLocalTime().isLeapYear()
        assertEquals(Repeticion.DIARIA, evento.getRepeticion());
        assertEquals(diasEnAnio, evento.getCantidadRepeticiones());

        // Repeticion semanal
        var dias = new ArrayList<>(Arrays.asList(true, false, false, false, true, false, false));
        evento.setRepeticionSemanal(dias, momento.plusWeeks(10));
        assertEquals(Repeticion.SEMANAL, evento.getRepeticion());
        assertEquals(10, evento.getCantidadRepeticiones());
        assertTrue(dias.containsAll(evento.getDias()));

        // Repeticion mensual
        evento.setRepeticion(Repeticion.MENSUAL, evento.getInicio().plusYears(1));
        assertEquals(Repeticion.MENSUAL, evento.getRepeticion());
        assertEquals(12, evento.getCantidadRepeticiones());

        // Repeticion anual
        evento.setRepeticion(Repeticion.ANUAL, evento.getInicio().plusYears(5));
        assertEquals(Repeticion.ANUAL, evento.getRepeticion());
        assertEquals(5, evento.getCantidadRepeticiones());
    }
}
