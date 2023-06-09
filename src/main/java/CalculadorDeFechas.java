import java.time.LocalDateTime;
import java.time.LocalDate;
import java.io.Serializable;

public abstract class CalculadorDeFechas implements Serializable {
    public abstract LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma);
    public abstract LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma);
    public abstract LocalDate sumarRepeticion(LocalDate fecha, int repeticion);

    public boolean caeEn(EventoRepetible repetible, LocalDate fecha) {
        var f = repetible.inicio.toLocalDate();

        for (int i = 0; i <= repetible.getCantidadRepeticiones() || repetible.esInfinito(); i++) {
            if (f.isEqual(fecha))
                return true;

            if (f.isAfter(fecha))
                break;

            f = this.sumarRepeticion(f, 1);
        }

        return false;
    }
}
