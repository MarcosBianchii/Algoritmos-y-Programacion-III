import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.*;

public abstract class Item implements Serializable {
    protected String titulo;
    protected String descripcion;
    protected final Map<LocalDateTime,Alarma> alarmas = new HashMap<>();

    public abstract LocalDateTime getIdTiempo();

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Alarma> getAlarmas() {
        return new ArrayList<>(alarmas.values());
    }

    public void agregarAlarma(Alarma alarma) {
        alarmas.put(alarma.getFechaHoraDisparo(), alarma);
        alarma.marcarComoNoRepetible();
    }

    public void agregarAlarmas(List<Alarma> alarmas) {
        for (var alarma : alarmas) {
            alarma.marcarComoNoRepetible();
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
        }
    }

    public void borrarAlarma(Alarma alarma) {
        alarmas.remove(alarma.getFechaHoraDisparo());
    }
}
