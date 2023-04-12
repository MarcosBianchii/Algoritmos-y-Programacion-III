import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;

// LocalDate -> Fecha
// LacalDateTime -> Fecha y hora

public class Tarea {
    private String titulo;
    private String descripcion;
    private boolean todoElDia;
    private LocalDateTime fechaDeVencimiento;

    private boolean completada = false;
    private final HashMap<LocalDateTime,Alarma> alarmas = new HashMap<>();

    public Tarea(String titulo, String descripcion, LocalDateTime fechaDeVencimiento, boolean todoElDia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaDeVencimiento = fechaDeVencimiento;
        this.todoElDia = todoElDia;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public boolean getEsTodoElDia() {
        return this.todoElDia;
    }

    public boolean getCompletada() {
        return this.completada;
    }

    public LocalDateTime getFechaDeVencimiento() {
        return this.fechaDeVencimiento;
    }

    public void setTitulo(String nuevoTitulo) {
        this.titulo = nuevoTitulo;
    }

    public void setDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    public void setFechaDeVencimiento(LocalDateTime nuevaFechaDeVencimiento) {
        this.fechaDeVencimiento = nuevaFechaDeVencimiento;
    }

    public boolean toggleCompletacion() {
        this.completada = !this.completada;
        return this.completada;
    }

    public boolean toggleTodoElDia() {
        this.todoElDia = !this.todoElDia;
        return this.todoElDia;
    }

    public void agregarAlarma(Alarma alarma) {
        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void borrarAlarma(LocalDateTime fechaHoraDisparo) {
        this.alarmas.remove(fechaHoraDisparo);
    }

    public void setAlarmas(ArrayList<Alarma> nuevasAlarmas) {
        this.alarmas.clear();
        for (Alarma alarma : nuevasAlarmas) {
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
        }
    }

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }
}
