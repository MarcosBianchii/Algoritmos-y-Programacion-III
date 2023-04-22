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
        return fechaDeVencimiento;
    }

    public void setFechaDeVencimiento(LocalDateTime fechaDeVencimiento) {
        this.fechaDeVencimiento = fechaDeVencimiento;
    }

    public boolean toggleCompletacion() {
        completada = !completada;
        return completada;
    }

    public boolean toggleTodoElDia() {
        todoElDia = !todoElDia;
        return todoElDia;
    }
}
