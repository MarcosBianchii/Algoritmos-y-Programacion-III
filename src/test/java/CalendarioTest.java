import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Arrays;

public class CalendarioTest {
    @Test
    public void testConseguirItemsDesdeHasta() {
        var momento0 = LocalDateTime.of(2020, 1, 1, 0, 0);
        var momento1 = momento0.plusWeeks(1);
        var momento2 = momento1.plusYears(10);

        var calendario = new Calendario();
        var tarea0 = calendario.crear("Tarea 0", "Descripcion 0", momento0, false);
        var tarea1 = calendario.crear("Tarea 1", "Descripcion 1", momento1, false);
        var tarea2 = calendario.crear("Tarea 2", "Descripcion 2", momento2, false);
        var evento0 = calendario.crear("Evento 0", "Descripcion 0", momento0, momento0);
        var evento1 = calendario.crear("Evento 1", "Descripcion 1", momento1, momento1);
        var evento2 = calendario.crear("Evento 2", "Descripcion 2", momento2, momento2);

        var tareas1 = calendario.getTareas(momento0, momento1);
        var eventos1 = calendario.getEventos(momento0, momento1);
        assertEquals(1, tareas1.size());
        assertEquals(1, eventos1.size());
        assertTrue(tareas1.contains(tarea0));
        assertTrue(eventos1.contains(evento0));

        var tareas2 = calendario.getTareas(momento0, momento2);
        var eventos2 = calendario.getEventos(momento0, momento2);
        assertEquals(2, tareas2.size());
        assertEquals(2, eventos2.size());
        assertTrue(tareas2.contains(tarea0));
        assertTrue(tareas2.contains(tarea1));
    }

    @Test
    public void testEliminarItems() {
        var momento0 = LocalDateTime.of(2020, 1, 1, 0, 0);
        var momento1 = momento0.plusWeeks(1);
        var momento2 = momento1.plusYears(10);

        var calendario = new Calendario();
        var tarea0 = calendario.crear("Tarea 0", "Descripcion 0", momento0, false);
        var tarea1 = calendario.crear("Tarea 1", "Descripcion 1", momento1, false);
        var tarea2 = calendario.crear("Tarea 2", "Descripcion 2", momento2, false);
        var evento0 = calendario.crear("Evento 0", "Descripcion 0", momento0, momento0);
        var evento1 = calendario.crear("Evento 1", "Descripcion 1", momento1, momento1);
        var evento2 = calendario.crear("Evento 2", "Descripcion 2", momento2, momento2);

        assertTrue(calendario.getTareas(momento0, momento2).contains(tarea1));
        assertTrue(calendario.getEventos(momento0, momento2).contains(evento1));

        calendario.eliminar(tarea1);
        calendario.eliminar(evento1);
        var tareas = calendario.getTareas(momento0, momento2);
        var eventos = calendario.getEventos(momento0, momento2);
        assertEquals(1, tareas.size());
        assertEquals(1, eventos.size());
        assertFalse(tareas.contains(tarea1));
        assertFalse(eventos.contains(evento1));
    }

    @Test
    public void testAgregarBorrarAlarmas() {
        var momento = LocalDateTime.of(2020, 1, 1, 0, 0);
        var calendario = new Calendario();

        var tarea0 = calendario.crear("Tarea 0", "Descripcion 0", momento, false);
        var tarea1 = calendario.crear("Tarea 2", "Descripcion 2", momento, false);
        var evento = calendario.crear("Evento 0", "Descripcion 0", momento, momento);

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
}
