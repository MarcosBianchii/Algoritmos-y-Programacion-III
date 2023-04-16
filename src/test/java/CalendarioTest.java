import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class CalendarioTest {
    @Test
    public void testConseguirItemsDesdeHasta() {
        var momento0 = LocalDateTime.of(2020, 1, 1, 0, 0);
        var momento1 = momento0.plusWeeks(1);
        var momento2 = momento1.plusYears(10);

        var calendario = new Calendario("mail");
        var tarea0 = new Tarea("Tarea 0", "Descripcion 0", momento0);
        var tarea1 = new Tarea("Tarea 1", "Descripcion 1", momento1);
        var tarea2 = new Tarea("Tarea 2", "Descripcion 2", momento2);
        var evento0 = new Evento("Evento 0", "Descripcion 0", momento0, momento0);
        var evento1 = new Evento("Evento 1", "Descripcion 1", momento1, momento1);
        var evento2 = new Evento("Evento 2", "Descripcion 2", momento2, momento2);
        calendario.agregar(tarea0);
        calendario.agregar(tarea1);
        calendario.agregar(tarea2);
        calendario.agregar(evento0);
        calendario.agregar(evento1);
        calendario.agregar(evento2);

        var items = calendario.getItems(momento0, momento1);
        assertEquals(2, items.size());
        assertTrue(items.contains(tarea0));
        assertTrue(items.contains(evento0));

        items = calendario.getItems(momento0, momento2);
        assertEquals(4, items.size());
        assertTrue(items.contains(tarea0));
        assertTrue(items.contains(tarea1));
        assertTrue(items.contains(evento0));
        assertTrue(items.contains(evento1));
    }

    @Test
    public void testEliminarItems() {
        var momento0 = LocalDateTime.of(2020, 1, 1, 0, 0);
        var momento1 = momento0.plusWeeks(1);
        var momento2 = momento1.plusYears(10);

        var calendario = new Calendario("mail");
        var tarea0 = new Tarea("Tarea 0", "Descripcion 0", momento0);
        var tarea1 = new Tarea("Tarea 1", "Descripcion 1", momento1);
        var tarea2 = new Tarea("Tarea 2", "Descripcion 2", momento2);
        var evento0 = new Evento("Evento 0", "Descripcion 0", momento0, momento0);
        var evento1 = new Evento("Evento 1", "Descripcion 1", momento1, momento1);
        var evento2 = new Evento("Evento 2", "Descripcion 2", momento2, momento2);
        calendario.agregar(tarea0);
        calendario.agregar(tarea1);
        calendario.agregar(tarea2);
        calendario.agregar(evento0);
        calendario.agregar(evento1);
        calendario.agregar(evento2);

        assertTrue(calendario.getItems(momento0, momento2).contains(tarea1));

        calendario.eliminar(tarea1);
        calendario.eliminar(evento1);

        var items = calendario.getItems(momento0, momento2);
        assertEquals(2, items.size());
        assertFalse(items.contains(tarea1));
        assertFalse(items.contains(evento1));
    }

    @Test
    public void testAgregarBorrarAlarmas() {
        var momento = LocalDateTime.of(2020, 1, 1, 0, 0);
        var calendario = new Calendario("mail");

        var tarea0 = new Tarea("Tarea 0", "Descripcion 0", momento);
        var tarea1 = new Tarea("Tarea 2", "Descripcion 2", momento);
        var evento = new Evento("Evento 0", "Descripcion 0", momento, momento);
        calendario.agregar(tarea0);
        calendario.agregar(tarea1);
        calendario.agregar(evento);

        var alarmaTarea0 = new Alarma(momento);
        var alarmaTarea1 = new Alarma(momento.plusMonths(1));
        var alarmaEvento = new Alarma(momento.plusMonths(2));

        calendario.agregarAlarma(tarea0, alarmaTarea0);
        calendario.agregarAlarma(tarea1, alarmaTarea1);
        calendario.agregarAlarma(evento, alarmaEvento);

        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(momento));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarmaTarea1.getFechaHoraDisparo()));
        calendario.borrarAlarma(tarea1, alarmaTarea1);
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarmaEvento.getFechaHoraDisparo()));
    }

    @Test
    public void agregarAlarmas() {
        var momento = LocalDateTime.of(2020, 1, 1, 0, 0);
        var calendario = new Calendario("mail");

        var tarea = new Tarea("Tarea 0", "Descripcion 0", momento);
        var evento = new Evento("Evento 0", "Descripcion 0", momento, momento);
        calendario.agregar(tarea);
        calendario.agregar(evento);

        var alarma0 = new Alarma(momento);
        var alarma1 = new Alarma(momento.plusMonths(1));
        var alarma2 = new Alarma(momento.plusMonths(2));
        var alarma3 = new Alarma(momento.plusMonths(3));

        var lista0 = new ArrayList<>(List.of(new Alarma[]{alarma0, alarma1}));
        var lista1 = new ArrayList<>(List.of(new Alarma[]{alarma2, alarma3}));
        calendario.agregarAlarmas(tarea, lista0);
        calendario.agregarAlarmas(evento, lista1);

        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma0.getFechaHoraDisparo()));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma1.getFechaHoraDisparo()));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma2.getFechaHoraDisparo()));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma3.getFechaHoraDisparo()));
    }
}
