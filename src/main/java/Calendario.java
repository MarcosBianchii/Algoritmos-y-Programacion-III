import java.util.HashMap;
import java.util.ArrayList;

public class Calendario {
    private final HashMap<String, Tarea> tareas = new HashMap<>();
    private final HashMap<String, Evento> eventos = new HashMap<>();

    // Tareas
    public Tarea getTarea(String titulo) {
        return this.tareas.get(titulo);
    }

    public boolean agregar(Tarea tarea) {
        if (tareas.containsKey(tarea.getTitulo()))
            return false;

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
        if (eventos.containsKey(evento.getTitulo()))
            return false;

        this.eventos.put(evento.getTitulo(), evento);
        return true;
    }

    public boolean borrarEvento(String titulo) {
        if (eventos.containsKey(titulo)) {
            this.eventos.remove(titulo);
            return true;
        }
        return false;
    }

    public ArrayList<Evento> getEventos() {
        return new ArrayList<>(this.eventos.values());
    }
}
