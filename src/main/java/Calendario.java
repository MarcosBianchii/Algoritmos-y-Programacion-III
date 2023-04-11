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

    public void crear(String titulo, String descripcion, LocalDateTime fechaDeVencimiento, boolean todoElDia) {
        var tarea = new Tarea(titulo, descripcion, fechaDeVencimiento, todoElDia);
        this.agregar(tarea);
    }

    public void crear(String titulo, String descripcion, LocalDateTime fechaDeVencimiento, boolean todoElDia, ArrayList<Alarma> alarmas) {
        var tarea = new Tarea(titulo, descripcion, fechaDeVencimiento, todoElDia);
        tarea.setAlarmas(alarmas);
        this.agregar(tarea);
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

    public void crear(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var evento = new Evento(titulo, descripcion, inicio, fin);
        this.agregar(evento);
    }

    public void crear(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin, ArrayList<Alarma> alarmas) {
        var evento = new Evento(titulo, descripcion, inicio, fin);
        evento.setAlarmas(alarmas);
        this.agregar(evento);
    }

    public void eliminar(Evento evento) {
        var lista = this.eventos.get(evento.getInicio().toLocalDate());
        if (lista == null)
            return;

        lista.remove(evento);
        this.alarmas.removeAll(evento.getAlarmas());
    }

    // TODO: agregar metodo de conseguir lo de un cierto intervalo de tiempo (en dias)
    // TODO: agregar metodos de creacion de eventos
    // TODO: agregar metodos de creacion de alarmas
    // TODO: agregar metodos para modificar items
    // TODO: agregar tests para todo
}
