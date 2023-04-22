import java.time.LocalDateTime;
import java.util.*;

public abstract class Item {
    protected String titulo;
    protected String descripcion;
    protected final HashMap<LocalDateTime,Alarma> alarmas = new HashMap<>();

    public abstract LocalDateTime getIdTiempo();

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
