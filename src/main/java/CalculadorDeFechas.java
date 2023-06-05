import java.time.LocalDateTime;
import java.time.LocalDate;
import java.io.Serializable;

public interface CalculadorDeFechas extends Serializable {
    LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma);
    LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma);
    LocalDate sumarRepeticion(LocalDate fecha, int repeticion);
}
