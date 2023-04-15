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

    public LocalDateTime getIdTiempo() {
        return this.inicio;
    }

    public void setFecha(LocalDateTime inicio, LocalDateTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public void agregarAlarma(Alarma alarma) {
        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void borrarAlarma(Alarma alarma) {
        this.alarmas.remove(alarma.getFechaHoraDisparo());
    }

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }

    public void agregarAlarmas(ArrayList<Alarma> alarmas) {
        for (Alarma alarma : alarmas)
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }
}
