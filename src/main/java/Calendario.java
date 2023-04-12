import java.time.*;
import java.util.*;

class ComparadorAlarmas implements Comparator<Alarma> {
    public int compare(Alarma a1, Alarma a2) {
        return a1.getFechaHoraDisparo().compareTo(a2.getFechaHoraDisparo());
    }
}

public class Calendario {
    private final PriorityQueue<Alarma> alarmas = new PriorityQueue<>(new ComparadorAlarmas());
    private final HashMap<LocalDate,ArrayList<Tarea>> tareas = new HashMap<>();
    private final HashMap<LocalDate,ArrayList<Evento>> eventos = new HashMap<>();

    public Alarma getProximaAlarma() {
        return this.alarmas.peek();
    }

    public void dispararAlarma() {
        if (this.alarmas.isEmpty())
            return;

        this.alarmas.poll().disparar();
    }

    // Tareas
    private void agregar(Tarea tarea) {
        var lista = this.tareas.get(tarea.getFechaDeVencimiento().toLocalDate());
        if (lista == null) {
            lista = new ArrayList<>();
            this.tareas.put(tarea.getFechaDeVencimiento().toLocalDate(), lista);
        }

        lista.add(tarea);
        this.alarmas.addAll(tarea.getAlarmas());
    }

    public Tarea crear(String titulo, String descripcion, LocalDateTime fechaDeVencimiento, boolean todoElDia) {
        var tarea = new Tarea(titulo, descripcion, fechaDeVencimiento, todoElDia);
        this.agregar(tarea);
        return tarea;
    }

    public void agregarAlarma(Tarea tarea, Alarma alarma) {
        tarea.agregarAlarma(alarma);
        this.alarmas.add(alarma);
    }

    public void borrarAlarma(Tarea tarea, Alarma alarma) {
        tarea.borrarAlarma(alarma.getFechaHoraDisparo());
        this.alarmas.remove(alarma);
    }

    public void eliminar(Tarea tarea) {
        var lista = this.tareas.get(tarea.getFechaDeVencimiento().toLocalDate());
        if (lista == null)
            return;

        lista.remove(tarea);
        this.alarmas.removeAll(tarea.getAlarmas());
    }

    // Eventos
    private void agregar(Evento evento) {
        var lista = this.eventos.get(evento.getInicio().toLocalDate());
        if (lista == null) {
            lista = new ArrayList<>();
            this.eventos.put(evento.getInicio().toLocalDate(), lista);
        }

        lista.add(evento);
        this.alarmas.addAll(evento.getAlarmas());
    }

    public Evento crear(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var evento = new Evento(titulo, descripcion, inicio, fin);
        this.agregar(evento);
        return evento;
    }

    public void agregarAlarma(Evento evento, Alarma alarma) {
        evento.agregarAlarma(alarma);
        this.alarmas.add(alarma);
    }

    public void borrarAlarma(Evento evento, Alarma alarma) {
        evento.borrarAlarma(alarma.getFechaHoraDisparo());
        this.alarmas.remove(alarma);
    }

    public void eliminar(Evento evento) {
        var lista = this.eventos.get(evento.getInicio().toLocalDate());
        if (lista == null)
            return;

        lista.remove(evento);
        this.alarmas.removeAll(evento.getAlarmas());
    }

    // Items
    public ArrayList<Tarea> getTareas(LocalDateTime desde, LocalDateTime hasta) {
        var lista = new ArrayList<Tarea>();
        var fecha = desde.toLocalDate();
        while (fecha.isBefore(hasta.toLocalDate())) {
            var tareas = this.tareas.get(fecha);
            if (tareas != null)
                lista.addAll(tareas);

            fecha = fecha.plusDays(1);
        }

        return lista;
    }

    public ArrayList<Evento> getEventos(LocalDateTime desde, LocalDateTime hasta) {
        var lista = new ArrayList<Evento>();
        var fecha = desde.toLocalDate();
        while (fecha.isBefore(hasta.toLocalDate())) {
            var eventos = this.eventos.get(fecha);
            if (eventos != null)
                lista.addAll(eventos);

            fecha = fecha.plusDays(1);
        }

        return lista;
    }

    /*Todo: poder modificar un item y sus alarmas
    * public void cambiarAlarma(item, nuevaAlarma) {
    *     busca la alarma en el item con el mismo disparo que la nueva
    *     la reemplaza por la nueva alarma
    *     la inserta en el heap de alarmas
    * }
    * */
}
