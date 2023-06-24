import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalculadorSemanal extends CalculadorDeFechas {
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

    private LocalDate proximaFecha(EventoRepetible repetible, LocalDate fecha) {
        var f = fecha;
        int inicial = fecha.getDayOfWeek().getValue();
        for (int i = 0; true; i++) {
            int indice = (inicial + i) % repetible.getDias().size();
            if (repetible.getDias().get(indice)) {
                f = f.plusDays(i + 1);
                break;
            }
        }

        return f;
    }

    @Override
    public boolean caeEn(EventoRepetible repetible, LocalDate fecha) {
        var inicio = repetible.getIdTiempo().toLocalDate();
        var fin = repetible.getFin().toLocalDate();
        long corte = repetible.getCantidadRepeticiones() * repetible.getDias().stream().filter(x -> x).count();

        for (int i = 0; i <= corte || repetible.esInfinito(); i++) {
            if (!inicio.isBefore(fecha) && !fin.isAfter(fecha))
                return true;

            if (inicio.isAfter(fecha))
                break;

            inicio = proximaFecha(repetible, inicio);
            fin = proximaFecha(repetible, fin);
        }

        return false;
    }
}
