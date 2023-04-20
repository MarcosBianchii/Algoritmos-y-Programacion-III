import java.time.LocalDateTime;

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
        this.titulo = repetible.titulo;
        this.descripcion = repetible.descripcion;
        this.inicio = repetible.inicio;
        this.fin = repetible.fin;
    }

    @Override
    public LocalDateTime getIdTiempo() {
        return this.inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
