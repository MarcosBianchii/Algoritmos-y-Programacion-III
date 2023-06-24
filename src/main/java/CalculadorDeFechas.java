import java.time.LocalDateTime;
import java.time.LocalDate;
import java.io.Serializable;

public abstract class CalculadorDeFechas implements Serializable {
    public abstract LocalDateTime calcularFechaLimite(EventoRepetible repetible, Alarma alarma);
    public abstract LocalDateTime calcularFechaSiguiente(EventoRepetible repetible, Alarma alarma);
    public abstract LocalDate sumarRepeticion(LocalDate fecha, int repeticion);

    public boolean caeEn(EventoRepetible repetible, LocalDate fecha) {
        var inicio = repetible.inicio.toLocalDate();
        var fin = repetible.fin.toLocalDate();

        for (int i = 0; i <= repetible.getCantidadRepeticiones() || repetible.esInfinito(); i++) {
            if (!inicio.isBefore(fecha) && !fin.isAfter(fecha))
                return true;

            if (inicio.isAfter(fecha))
                break;

            inicio = sumarRepeticion(inicio, 1);
            fin = sumarRepeticion(fin, 1);
        }

        return false;
    }
}
