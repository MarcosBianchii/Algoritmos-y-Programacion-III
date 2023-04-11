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

    public boolean getTodoElDia() {
        return this.todoElDia;
    }

    public boolean getCompletada() {
        return this.completada;
    }

    public LocalDateTime getFechaDeVencimiento() {
        return this.fechaDeVencimiento;
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
            this.descripcion = nuevaDescripcion;
            return true;
        }
        return false;
    }

    public boolean toggleCompletacion() {
        this.completada = !this.completada;
        return this.completada;
    }

    public boolean toggleTodoElDia() {
        this.todoElDia = !this.todoElDia;
        return this.todoElDia;
    }

    public void setTodoElDia(boolean nuevoTodoElDia) {
        this.todoElDia = nuevoTodoElDia;
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
