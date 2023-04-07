import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;

// LocalDate -> Fecha
// LacalDateTime -> Fecha y hora

public class Tarea {
    private String titulo;
    private String descripcion;
    private boolean completada = false;
    private LocalDateTime fechaDeVencimiento;
    private final HashMap<LocalDateTime, Alarma> alarmas = new HashMap<>();

    public Tarea(String titulo, String descripcion, LocalDateTime fechaDeVencimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaDeVencimiento = fechaDeVencimiento;
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

    public boolean toggleCompletacion() {
        this.completada = !this.completada;
        return this.completada;
    }

    public void setFechaDeVencimiento(LocalDateTime nuevaFechaDeVencimiento) {
        this.fechaDeVencimiento = nuevaFechaDeVencimiento;
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

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }
}
