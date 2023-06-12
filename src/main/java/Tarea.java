import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public String toString() {
        var str = new StringBuilder();
        var tamanioTitulo = 30;
        str.append("T | ");
        str.append(titulo, 0, Math.min(tamanioTitulo, titulo.length()));

        if (str.length() < tamanioTitulo)
            str.append(" ".repeat(tamanioTitulo - str.length()));

        str.append(" | ").append(fechaDeVencimiento.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        str.append(" | ").append(completada ? "Completada" : "Pendiente");
        str.append(todoElDia ? " | Todo el dia" : "");
        str.append(!alarmas.isEmpty() ? " | " : "");
        str.append("*".repeat(alarmas.size()));
        return str.toString();
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
