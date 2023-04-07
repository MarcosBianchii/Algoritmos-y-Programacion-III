import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Arrays;

public class CalendarioTest {
    @Test
    public void testGetTareaInexistente() {
        Calendario calendario = new Calendario();
        assertNull(calendario.getTarea("Tarea 1"));
    }

    @Test
    public void testAgregarTarea() {
        Calendario calendario = new Calendario();
        Tarea tarea = new Tarea("Tarea 1", "Descripcion", LocalDateTime.now());
        assertTrue(calendario.agregar(tarea));
        assertEquals(tarea, calendario.getTarea("Tarea 1"));
    }

    @Test
    public void testBorrarTarea() {
        Calendario calendario = new Calendario();
        Tarea tarea = new Tarea("Tarea 1", "Descripcion", LocalDateTime.now());
        calendario.agregar(tarea);
        calendario.borrarTarea("Tarea 1");
        assertNull(calendario.getTarea("Tarea 1"));
    }

    @Test
    public void testGetTarea() {
        Calendario calendario = new Calendario();
        Tarea tarea1 = new Tarea("Tarea 1", "Descripcion", LocalDateTime.now());
        Tarea tarea2 = new Tarea("Tarea 2", "Descripcion", LocalDateTime.now());
        calendario.agregar(tarea1);
        calendario.agregar(tarea2);
        ArrayList<Tarea> tareas = new ArrayList<>(Arrays.asList(tarea2, tarea1));
        assertEquals(tareas, calendario.getTareas());
    }

    @Test
    public void testAgregarEvento() {
        Calendario calendario = new Calendario();
        Evento evento = new Evento("Evento 1", "Descripcion", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertTrue(calendario.agregar(evento));
        assertEquals(evento, calendario.getEvento("Evento 1"));
    }


    @Test
    public void testBorrarEvento() {
        Calendario calendario = new Calendario();
        Evento evento = new Evento("Evento 1", "Descripcion", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        calendario.agregar(evento);
        calendario.borrarEvento("Evento 1");
        assertNull(calendario.getEvento("Evento 1"));
    }

    @Test
    public void testGetEventos() {
        Calendario calendario = new Calendario();
        Evento evento1 = new Evento("Evento 1", "Descripcion", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Evento evento2 = new Evento("Evento 2", "Descripcion", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        calendario.agregar(evento1);
        calendario.agregar(evento2);
        ArrayList<Evento> eventos = new ArrayList<>(Arrays.asList(evento1, evento2));
        assertEquals(eventos, calendario.getEventos());
    }
}
