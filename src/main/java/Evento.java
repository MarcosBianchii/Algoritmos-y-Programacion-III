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

    public String getTitulo() {
        return this.titulo;
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

    public void borrarAlarma(LocalDateTime fechaHoraDisparo) {
        this.alarmas.remove(fechaHoraDisparo);
    }

    public boolean setRepeticion(Repeticion repeticion, Integer cantidad) {
        if (repeticion == Repeticion.SEMANAL)
            return false;

        this.cantidadRepeticiones = cantidad;
        this.repeticion = repeticion;
        return true;
    }

    public boolean setRepeticion(Repeticion repeticion, LocalDateTime fecha) {
        if (repeticion == Repeticion.SEMANAL)
            return false;

        long cantidad = 0;
        switch (repeticion) {
            case DIARIA -> cantidad = this.inicio.until(fecha, ChronoUnit.DAYS);
            case MENSUAL -> cantidad = this.inicio.until(fecha, ChronoUnit.MONTHS);
            case ANUAL -> cantidad = this.inicio.until(fecha, ChronoUnit.YEARS);
        }

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = repeticion;
        return true;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, Integer cantidad) {
        this.dias.clear();
        this.dias.addAll(dias);
        this.cantidadRepeticiones = cantidad;
        this.repeticion = Repeticion.SEMANAL;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, LocalDateTime fecha) {
        this.dias.clear();
        this.dias.addAll(dias);

        long cantidad = this.inicio.until(fecha, ChronoUnit.WEEKS);

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = Repeticion.SEMANAL;
    }

    public ArrayList<Boolean> getDias() {
        return this.dias;
    }

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }
}
