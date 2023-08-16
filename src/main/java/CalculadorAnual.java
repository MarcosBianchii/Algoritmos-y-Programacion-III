import java.time.LocalDateTime;
import java.time.LocalDate;

public class CalculadorAnual extends CalculadorDeFechas {
    @Override
    public LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma) {
        return alarma.getFechaHoraOriginal().plusYears(repetible.getCantidadRepeticiones());
    }

    @Override
    public LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma) {
        return alarma.getFechaHoraDisparo().plusYears(1);
    }

    @Override
    public LocalDate sumarRepeticion(LocalDate fecha, int repeticion) {
        return fecha.plusYears(repeticion);
    }
}
