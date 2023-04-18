import java.time.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Evento implements Item {
    protected String titulo;
    protected String descripcion;
    protected LocalDateTime inicio;
    protected LocalDateTime fin;
    protected final HashMap<LocalDateTime, Alarma> alarmas = new HashMap<>();

    public Evento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        setFecha(inicio, fin);
    }

    public Evento(EventoRepetible repetible) {
        this.titulo = repetible.titulo;
        this.descripcion = repetible.descripcion;
        setFecha(repetible.inicio, repetible.fin);
    }

    public LocalDateTime getIdTiempo() {
        return this.inicio;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    private void setFecha(LocalDateTime inicio, LocalDateTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }

    public void agregarAlarma(Alarma alarma) {
        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void agregarAlarmas(ArrayList<Alarma> alarmas) {
        for (var alarma : alarmas)
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void borrarAlarma(Alarma alarma) {
        this.alarmas.remove(alarma.getFechaHoraDisparo());
    }
}
