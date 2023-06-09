import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalculadorDiario extends CalculadorDeFechas {
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

    @Override
    public boolean caeEn(EventoRepetible repetible, LocalDate fecha) {
        LocalDate f = repetible.inicio.toLocalDate();

        for (int i = 0; i <= repetible.getCantidadRepeticiones() || repetible.esInfinito(); i++) {
            if (f.isEqual(fecha))
                return true;

            if (f.isAfter(fecha))
                break;

            f = sumarRepeticion(f, repetible.getFrecuenciaDiaria());
        }

        return false;
    }
}
