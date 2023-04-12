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
    private int cantidadRepeticiones = 0;

    public Evento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.setFecha(inicio, fin);
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public LocalDateTime getInicio() {
        return this.inicio;
    }

    public LocalDateTime getFin() {
        return this.fin;
    }

    public Repeticion getRepeticion() {
        return this.repeticion;
    }

    public int getCantidadRepeticiones() {
        return this.cantidadRepeticiones;
    }

    public void setTitulo(String nuevoTitulo) {
        this.titulo = nuevoTitulo;
    }

    public void setDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    // Se considera que las fechas estan en orden.
    public void setFecha(LocalDateTime inicio, LocalDateTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public void agregarAlarma(Alarma alarma) {
        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void borrarAlarma(LocalDateTime fechaHoraDisparo) {
        this.alarmas.remove(fechaHoraDisparo);
    }

    public void  setRepeticion(Repeticion repeticion, Integer cantidad) {
        this.cantidadRepeticiones = cantidad;
        this.repeticion = repeticion;
    }

    public void setRepeticion(Repeticion repeticion, LocalDateTime fecha) {
        long cantidad = 0;
        switch (repeticion) {
            case DIARIA  -> cantidad = this.inicio.until(fecha, ChronoUnit.DAYS);
            case MENSUAL -> cantidad = this.inicio.until(fecha, ChronoUnit.MONTHS);
            case ANUAL   -> cantidad = this.inicio.until(fecha, ChronoUnit.YEARS);
        }

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = repeticion;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, Integer cantidad) {
        this.dias.clear();
        this.dias.addAll(dias);
        this.cantidadRepeticiones = cantidad;
        this.repeticion = Repeticion.SEMANAL;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, LocalDateTime hasta) {
        this.dias.clear();
        this.dias.addAll(dias);

        long cantidad = this.inicio.until(hasta, ChronoUnit.WEEKS);

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = Repeticion.SEMANAL;
    }

    public void setAlarmas(ArrayList<Alarma> alarmas) {
        this.alarmas.clear();
        for (Alarma alarma : alarmas)
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public ArrayList<Boolean> getDias() {
        return this.dias;
    }

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }
}
