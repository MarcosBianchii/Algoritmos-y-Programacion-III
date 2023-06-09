import java.time.LocalDateTime;
import java.time.LocalDate;

public class CalculadorMensual extends CalculadorDeFechas {
    @Override
    public LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma) {
        return alarma.getFechaHoraOriginal().plusMonths(repetible.getCantidadRepeticiones());
    }

    @Override
    public LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma) {
        return alarma.getFechaHoraDisparo().plusMonths(1);
    }

    @Override
    public LocalDate sumarRepeticion(LocalDate fecha, int repeticion) {
        return fecha.plusMonths(repeticion);
    }
}
