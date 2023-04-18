import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;

// LocalDate -> Fecha
// LacalDateTime -> Fecha y hora

public class Tarea implements Item {
    private String titulo;
    private String descripcion;
    private boolean todoElDia;
    private LocalDateTime fechaDeVencimiento;

    private boolean completada = false;
    private final HashMap<LocalDateTime,Alarma> alarmas = new HashMap<>();

    public Tarea(String titulo, String descripcion, LocalDateTime fechaDeVencimiento) {
        this(titulo, descripcion, fechaDeVencimiento, false);
    }

    public Tarea(String titulo, String descripcion, LocalDateTime fechaDeVencimiento, boolean todoElDia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaDeVencimiento = fechaDeVencimiento;
        this.todoElDia = todoElDia;
    }

    public LocalDateTime getIdTiempo() {
        return this.fechaDeVencimiento;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaDeVencimiento(LocalDateTime fechaDeVencimiento) {
        this.fechaDeVencimiento = fechaDeVencimiento;
    }

    public boolean toggleCompletacion() {
        this.completada = !this.completada;
        return this.completada;
    }

    public boolean toggleTodoElDia() {
        this.todoElDia = !this.todoElDia;
        return this.todoElDia;
    }

    public ArrayList<Alarma> getAlarmas() {
        return new ArrayList<>(this.alarmas.values());
    }

    public void agregarAlarma(Alarma alarma) {
        this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void agregarAlarmas(ArrayList<Alarma> nuevasAlarmas) {
        for (var alarma : nuevasAlarmas)
            this.alarmas.put(alarma.getFechaHoraDisparo(), alarma);
    }

    public void borrarAlarma(Alarma alarma) {
        this.alarmas.remove(alarma.getFechaHoraDisparo());
    }
}
