import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class CalendarioTest {
    @Test
    public void testConseguirItemsDesdeHastaNoRepetibles() {
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
    public void testConseguirItemsDesdeHastaRepetibles() {
        var momento = LocalDateTime.of(2020, 1, 1, 0, 0);
        var calendario = new Calendario("mail");

        var repetible0 = new EventoRepetible("Repetible 0", "Descripcion 0", momento, momento.plusHours(2));
        repetible0.setRepeticionDiaria(1, 10);
        calendario.agregar(repetible0);

        var items = calendario.getItems(momento, momento.plusDays(2));
        assertEquals(2, items.size());

        items = calendario.getItems(momento, momento.plusDays(7));
        assertEquals(7, items.size());

        calendario.eliminar(repetible0);

        items = calendario.getItems(momento, momento.plusDays(10));
        assertTrue(items.isEmpty());

        var repetible1 = new EventoRepetible("Repetible 1", "Descripcion 1", momento, momento.plusHours(2));
        repetible1.setRepeticionSemanal(new ArrayList<>(List.of(false, true, true, false, false, false, false)), 1);
        calendario.agregar(repetible1);

        items = calendario.getItems(momento, momento.plusWeeks(10));
        assertEquals(3, items.size());
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

        var alarmaTarea0 = new Alarma(momento);
        var alarmaTarea1 = new Alarma(momento.plusMonths(1));
        var alarmaEvento = new Alarma(momento.plusMonths(2));

        calendario.agregar(tarea0).agregarAlarma(tarea0, alarmaTarea0);
        calendario.agregar(tarea1).agregarAlarma(tarea1, alarmaTarea1);
        calendario.agregar(evento).agregarAlarma(evento, alarmaEvento);

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

        var alarma0 = new Alarma(momento);
        var alarma1 = new Alarma(momento.plusMonths(1));
        var alarma2 = new Alarma(momento.plusMonths(2));
        var alarma3 = new Alarma(momento.plusMonths(3));

        var lista0 = new ArrayList<>(List.of(new Alarma[]{alarma0, alarma1}));
        var lista1 = new ArrayList<>(List.of(new Alarma[]{alarma2, alarma3}));

        calendario.agregar(tarea).agregarAlarmas(tarea, lista0);
        calendario.agregar(evento).agregarAlarmas(evento, lista1);

        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma0.getFechaHoraDisparo()));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma1.getFechaHoraDisparo()));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma2.getFechaHoraDisparo()));
        calendario.dispararAlarma();
        assertTrue(calendario.getProximaAlarma().getFechaHoraDisparo().isEqual(alarma3.getFechaHoraDisparo()));
    }

    @Test
    public void testToRepetible() {
        var momento = LocalDateTime.of(2020, 1, 1, 0, 0);
        var calendario = new Calendario("mail");
        var evento = new Evento("Evento 0", "Descripcion 0", momento, momento);

        var alarma0 = new Alarma(momento);
        var alarma1 = new Alarma(momento.plusMonths(1));

        calendario.agregar(evento).agregarAlarmas(evento, new ArrayList<>(List.of(new Alarma[]{alarma0, alarma1})));

        var repetible = calendario.toRepetible(evento);
        repetible.setRepeticionMensual(-1);

        var items = calendario.getItems(momento.plusDays(1), momento.plusDays(2));
        assertEquals(0, items.size());
        assertEquals(2, repetible.getAlarmas().size());
    }

    @Test
    public void testAlarmasRepetibles() {
        var momento = LocalDateTime.of(2023, 4, 17, 0, 0);
        var repetible = new EventoRepetible("Evento 0", "Descripcion 0", momento, momento);
        var calendario = new Calendario("mail");
        var alarma = new Alarma(momento);
        var dias = new ArrayList<>(List.of(new Boolean[]{true, false, true, false, false, false, false}));

        repetible.setRepeticionSemanal(dias, 2);
        calendario.agregar(repetible).agregarAlarma(repetible, alarma);

        calendario.dispararAlarma();
        assertEquals(momento.plusDays(2), alarma.getFechaHoraDisparo());
        calendario.dispararAlarma();
        assertEquals(momento.plusDays(7), alarma.getFechaHoraDisparo());
        calendario.dispararAlarma();
        assertEquals(momento.plusDays(9), alarma.getFechaHoraDisparo());
        calendario.dispararAlarma();
        assertEquals(momento.plusDays(14), alarma.getFechaHoraDisparo());
        calendario.dispararAlarma();
        assertEquals(momento.plusDays(16), alarma.getFechaHoraDisparo());
        calendario.dispararAlarma();
        assertEquals(momento.plusDays(21), alarma.getFechaHoraDisparo());
        calendario.dispararAlarma();
        assertNull(alarma.getFechaHoraDisparo());
    }

    public void serializar(Calendario calendario, ByteArrayOutputStream bytes, LocalDateTime momento) {
        var repetible = new EventoRepetible("Evento 0", "Descripcion 0", momento, momento);
        var alarma = new Alarma(momento);
        var dias = new ArrayList<>(List.of(new Boolean[]{true, false, true, false, false, false, false}));
        var tarea = new Tarea("Tarea 0", "Descripcion 0", momento);

        repetible.setRepeticionSemanal(dias, 2);
        calendario.agregar(tarea);
        calendario.agregar(repetible).agregarAlarma(repetible, alarma);

        try {
            calendario.serializar(bytes);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            e.printStackTrace();
            fail();
        }
    }

    public Calendario deserializar(ByteArrayOutputStream bytes) {
        try {
            return Calendario.deserializar(new ByteArrayInputStream(bytes.toByteArray()));
        } catch (IOException | ClassNotFoundException e) {
            fail();
            return null;
        }
    }

    @Test
    public void serializarDeserializar() {
        var calendario1 = new Calendario("mail");
        var momento = LocalDateTime.of(2023, 4, 17, 0, 0);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        serializar(calendario1, bytes, momento);

        var calendario2 = deserializar(bytes);
        assertNotNull(calendario2);
        assertEquals(calendario1.getProximaAlarma().getFechaHoraDisparo(), calendario2.getProximaAlarma().getFechaHoraDisparo());

        var items1 = calendario1.getItems(momento, momento.plusDays(1));
        var items2 = calendario2.getItems(momento, momento.plusDays(1));

        assertEquals(items1.size(), items2.size());
        for (int i = 0; i < items1.size(); i++) {
            assertEquals(items1.get(i).getTitulo(), items2.get(i).getTitulo());
            assertEquals(items1.get(i).getDescripcion(), items2.get(i).getDescripcion());
            assertEquals(items1.get(i).getIdTiempo(), items2.get(i).getIdTiempo());
        }
    }
}
