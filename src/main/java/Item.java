import java.time.LocalDateTime;
import java.util.*;

public abstract class Item {
    protected String titulo;
    protected String descripcion;
    protected final HashMap<LocalDateTime, Alarma> alarmas = new HashMap<>();

    public abstract LocalDateTime getIdTiempo();

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }

    public void agregarAlarma(Alarma alarma) {
        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void agregarAlarmas(List<Alarma> alarmas) {
        for (var alarma : alarmas)
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void borrarAlarma(Alarma alarma) {
        this.alarmas.remove(alarma.getFechaHoraDisparo());
    }
}
