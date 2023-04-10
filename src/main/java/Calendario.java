import java.util.*;

class ComparadorAlarmas implements Comparator<Alarma> {
    public int compare(Alarma a1, Alarma a2) {
        return a1.getFechaHoraDisparo().compareTo(a2.getFechaHoraDisparo());
    }
}

public class Calendario {
    private final PriorityQueue<Alarma> alarmas = new PriorityQueue<>(new ComparadorAlarmas());
    private final HashMap<String,Tarea> tareas = new HashMap<>();
    private final HashMap<String,Evento> eventos = new HashMap<>();

    // Alarma
    public void agregarAlarma(Tarea tarea, Alarma alarma) {
        if (tarea.agregarAlarma(alarma))
            this.alarmas.add(alarma);
    }

    public void agregarAlarma(Evento evento, Alarma alarma) {
        if (evento.agregarAlarma(alarma))
            this.alarmas.add(alarma);
    }

    public Alarma getProximaAlarma() {
        return this.alarmas.peek();
    }

    public void dispararAlarma() {
        if (this.alarmas.isEmpty())
            return;

        this.alarmas.poll().disparar();
    }

    // Tareas
    public Tarea getTarea(String titulo) {
        return this.tareas.get(titulo);
    }

    public boolean agregar(Tarea tarea) {
        this.tareas.put(tarea.getTitulo(), tarea);
        return true;
    }

    public void borrarTarea(String titulo) {
        this.tareas.remove(titulo);
    }

    public ArrayList<Tarea> getTareas() {
        return new ArrayList<>(this.tareas.values());
    }

    // Eventos
    public Evento getEvento(String titulo) {
        return this.eventos.get(titulo);
    }

    public boolean agregar(Evento evento) {
        this.eventos.put(evento.getTitulo(), evento);
        return true;
    }

    public void borrarEvento(String titulo) {
        this.eventos.remove(titulo);
    }

    public ArrayList<Evento> getEventos() {
        return new ArrayList<>(this.eventos.values());
    }
}
