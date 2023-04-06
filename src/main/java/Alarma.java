import java.time.*;

// LocalDate -> Fecha
// LacalDateTime -> Fecha y hora

public class Alarma {
    private final LocalDateTime fechaHoraDisparo;

    // TODO: efectos.

    public Alarma(LocalDateTime fechaHoraDisparo) {
        this.fechaHoraDisparo = fechaHoraDisparo;
    }

    public LocalDateTime getFechaHoraDisparo() {
        return this.fechaHoraDisparo;
    }
}
