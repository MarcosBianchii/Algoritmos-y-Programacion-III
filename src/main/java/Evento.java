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
        titulo = repetible.titulo;
        descripcion = repetible.descripcion;
        inicio = repetible.inicio;
        fin = repetible.fin;
    }

    @Override
    public LocalDateTime getIdTiempo() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
