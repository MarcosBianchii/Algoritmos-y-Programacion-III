import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

enum Repeticion {
    NOTIENE,
    DIARIA,
    SEMANAL,
    MENSUAL,
    ANUAL,
}

public class Evento {
    private String titulo;
    private String descripcion;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private final HashMap<LocalDateTime, Alarma> alarmas = new HashMap<>();

    private Repeticion repeticion = Repeticion.NOTIENE;
    private final ArrayList<Boolean> dias = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false));
    private Integer cantidadRepeticiones = 0;

    public Evento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.setFecha(inicio, fin);
    }

    public LocalDateTime getDuracion() {
        return this.fin.minusNanos(this.inicio.getNano());
    }

    public boolean setTitulo(String nuevoTitulo) {
        if (nuevoTitulo.length() > 0) {
            this.titulo = nuevoTitulo;
            return true;
        }
        return false;
    }

    public boolean setDescripcion(String nuevaDescripcion) {
        if (nuevaDescripcion.length() > 0) {
            this.titulo = nuevaDescripcion;
            return true;
        }
        return false;
    }

    // Se considera que las fechas estan en orden.
    public void setFecha(LocalDateTime inicio, LocalDateTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public boolean agregarAlarma(Alarma alarma) {
        if (alarmas.containsKey(alarma.getFechaHoraDisparo()))
                return false;

        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
        return true;
    }

    public void borrarAlarma(Alarma alarma) {
        this.alarmas.remove(alarma.getFechaHoraDisparo());
    }

    public void setRepeticion(Repeticion repeticion, Integer cantidad) {
        if (repeticion == Repeticion.SEMANAL)
            return;

        this.cantidadRepeticiones = cantidad;
        this.repeticion = repeticion;
    }

    public void setRepeticion(Repeticion repeticion, LocalDateTime fecha) {
        if (repeticion == Repeticion.SEMANAL)
            return;

        long cantidad = 0;
        switch (repeticion) {
            case DIARIA -> cantidad = this.inicio.until(fecha, ChronoUnit.DAYS);
            case MENSUAL -> cantidad = this.inicio.until(fecha, ChronoUnit.MONTHS);
            case ANUAL -> cantidad = this.inicio.until(fecha, ChronoUnit.YEARS);
        }

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = repeticion;
    }

    public void setRepeticionSemanal(Repeticion repeticion, ArrayList<Boolean> dias, Integer cantidad) {
        this.dias.clear();
        this.dias.addAll(dias);
        this.cantidadRepeticiones = cantidad;
        this.repeticion = repeticion;
    }

    public void setRepeticionSemanal(Repeticion repeticion, ArrayList<Boolean> dias, LocalDateTime fecha) {
        this.dias.clear();
        this.dias.addAll(dias);

        long cantidad = this.inicio.until(fecha, ChronoUnit.WEEKS);

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = repeticion;
    }
}
