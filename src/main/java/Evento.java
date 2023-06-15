import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento extends Item {
    protected LocalDateTime inicio;
    protected LocalDateTime fin;

    public Evento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Evento(EventoRepetible repetible) {
        titulo = repetible.titulo;
        descripcion = repetible.descripcion;
        inicio = repetible.inicio;
        fin = repetible.fin;
        repeticion = Repeticion.NO_TIENE;
    }

    @Override
    public LocalDateTime getIdTiempo() {
        return inicio;
    }

    @Override
    public String toString() {
        var str = new StringBuilder();
        var tamanioTitulo = 30;
        str.append("E | ");
        str.append(titulo, 0, Math.min(tamanioTitulo, titulo.length()));

        if (str.length() < tamanioTitulo)
            str.append(" ".repeat(tamanioTitulo - str.length()));

        str.append(" | ").append(inicio.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        str.append(" | ").append(fin.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        str.append(!alarmas.isEmpty() ? " | " : "");
        str.append("*".repeat(alarmas.size()));
        return str.toString();
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
