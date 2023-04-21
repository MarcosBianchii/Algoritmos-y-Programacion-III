import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalculadorSemanal implements CalculadorDeFechas {

    @Override
    public LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma) {
        LocalDateTime dia = alarma.getFechaHoraOriginal();

        int i = repetible.getDias().size() - 1;
        for (; i >= 0; i--)
            if (repetible.getDias().get(i))
                break;

        var fecha = dia.plusWeeks(repetible.getCantidadRepeticiones() + 1);
        return fecha.plusDays(i - dia.getDayOfWeek().getValue());
    }

    @Override
    public LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma) {
        LocalDateTime dia = alarma.getFechaHoraDisparo();
        
        int inicial = dia.getDayOfWeek().getValue();
        for (int i = 0; i < repetible.getDias().size(); i++) {
            int indice = (inicial + i) % repetible.getDias().size();
            if (repetible.getDias().get(indice)) {
                dia = dia.plusDays(i + 1);
                break;
            }
        }

        return dia;
    }

    @Override
    public LocalDate sumarRepeticion(LocalDate fecha, int repeticion) {
        return fecha.plusWeeks(repeticion);
    }
}
