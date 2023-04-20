import java.time.LocalDateTime;

public class Tarea extends Item {
    private boolean todoElDia;
    private LocalDateTime fechaDeVencimiento;
    private boolean completada = false;

    public Tarea(String titulo, String descripcion, LocalDateTime fechaDeVencimiento) {
        this(titulo, descripcion, fechaDeVencimiento, false);
    }

    public Tarea(String titulo, String descripcion, LocalDateTime fechaDeVencimiento, boolean todoElDia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaDeVencimiento = fechaDeVencimiento;
        this.todoElDia = todoElDia;
    }

    @Override
    public LocalDateTime getIdTiempo() {
        return this.fechaDeVencimiento;
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
}
