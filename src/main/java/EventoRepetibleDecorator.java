import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EventoRepetibleDecorator extends EventoRepetible {
    private final EventoRepetible repetible;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public EventoRepetibleDecorator(EventoRepetible repetible, LocalDate fecha) {
        super(repetible);
        this.repetible = repetible;
        this.fechaInicio = fecha.atTime(repetible.inicio.toLocalTime());
        this.fechaFin = fechaInicio.plus(repetible.inicio.until(repetible.fin, ChronoUnit.MILLIS), ChronoUnit.MILLIS);
    }

    public EventoRepetible getRepetible() {
        return repetible;
    }

    @Override
    public LocalDateTime getIdTiempo() {
        return fechaInicio;
    }

    @Override
    public String toString() {
        var str = new StringBuilder();
        var tamanioTitulo = 30;
        str.append("R | ");
        str.append(titulo, 0, Math.min(tamanioTitulo, titulo.length()));

        if (str.length() < tamanioTitulo)
            str.append(" ".repeat(tamanioTitulo - str.length()));

        str.append(" | ").append(fechaInicio.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        str.append(" | ").append(fechaFin.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        str.append(!repetible.getAlarmas().isEmpty() ? " | " : "");
        str.append("*".repeat(repetible.getAlarmas().size()));
        return str.toString();
    }

    @Override
    public LocalDateTime getFin() {
        return fechaFin;
    }

    @Override
    public String getTitulo() {
        return repetible.getTitulo();
    }

    @Override
    public String getDescripcion() {
        return repetible.getDescripcion();
    }

    @Override
    public void setTitulo(String titulo) {
        repetible.setTitulo(titulo);
    }

    @Override
    public void setDescripcion(String descripcion) {
        repetible.setDescripcion(descripcion);
    }

    @Override
    public void setInicio(LocalDateTime inicio) {
        repetible.setInicio(inicio);
        fechaInicio = inicio;
    }

    @Override
    public void setFin(LocalDateTime fin) {
        repetible.setFin(fin);
        fechaFin = fin;
    }

    @Override
    public void agregarAlarma(Alarma alarma) {
        repetible.agregarAlarma(alarma);
    }

    @Override
    public void agregarAlarmas(List<Alarma> alarmas) {
        repetible.agregarAlarmas(alarmas);
    }

    @Override
    public void borrarAlarma(Alarma alarma) {
        repetible.borrarAlarma(alarma);
    }
}
