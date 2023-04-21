import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalculadorDiario implements CalculadorDeFechas {
    @Override
    public LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma) {
        return alarma.getFechaHoraOriginal().plusDays((long) repetible.getCantidadRepeticiones() * repetible.getFrecuenciaDiaria());
    }

    @Override
    public LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma) {
        return alarma.getFechaHoraDisparo().plusDays(repetible.getFrecuenciaDiaria());
    }

    @Override
    public LocalDate sumarRepeticion(LocalDate fecha, int repeticion) {
        return fecha.plusDays(repeticion);
    }
}
