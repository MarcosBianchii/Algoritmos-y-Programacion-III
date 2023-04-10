import java.time.*;

// LocalDate -> Fecha
// LacalDateTime -> Fecha y hora

public class Alarma {
    private LocalDateTime fechaHoraDisparo;
    // private Sonido sonido;
    private String mail = null;
    private boolean notificacion = false;

    public Alarma(LocalDateTime fechaHoraDisparo) { // Sonido sonido
        this.fechaHoraDisparo = fechaHoraDisparo;
    }

    public LocalDateTime getFechaHoraDisparo() {
        return this.fechaHoraDisparo;
    }

    public boolean toggleNotificacion() {
        this.notificacion = !this.notificacion;
        return this.notificacion;
    }

    public void setEmail(String mail) {
        this.mail = mail;
    }

    public void setFechaHoraDisparo(LocalDateTime fechaHoraDisparo) {
        this.fechaHoraDisparo = fechaHoraDisparo;
    }

    public void disparar() {
        // TODO: Hacer sonar la alarma.
        // TODO: Enviar mail.
        // TODO: Mandar notificacion.
    }
}
