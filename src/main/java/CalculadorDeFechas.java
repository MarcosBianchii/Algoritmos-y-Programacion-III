import java.time.LocalDateTime;
import java.time.LocalDate;

public interface CalculadorDeFechas {
    LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma);
    LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma);
    LocalDate sumarRepeticion(LocalDate fecha, int repeticion);
}
