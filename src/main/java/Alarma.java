import java.time.*;

// LocalDate -> Fecha
// LacalDateTime -> Fecha y hora

public class Alarma {
    private LocalDateTime fechaHoraDisparo;
    private boolean mandaMail = false;
    private boolean suena = false;
    private boolean muestraNotificacion = false;
    // private Sonido sonido;

    public Alarma(LocalDateTime fechaHoraDisparo) { // Sonido sonido
        this.fechaHoraDisparo = fechaHoraDisparo;
    }

    public void setConfig(boolean mandaMail, boolean suena, boolean muestraNotificacion) {
        this.mandaMail = mandaMail;
        this.suena = suena;
        this.muestraNotificacion = muestraNotificacion;
    }

    public LocalDateTime getFechaHoraDisparo() {
        return this.fechaHoraDisparo;
    }

    public void setFechaHoraDisparo(LocalDateTime fechaHoraDisparo) {
        this.fechaHoraDisparo = fechaHoraDisparo;
    }

    public void disparar(String mail) {
        if (this.mandaMail) {
            System.out.println("Enviando mail a " + mail);
        }

        if (this.suena) {
            System.out.println("Sonando");
        }

        if (this.muestraNotificacion) {
            System.out.println("Mostrando notificacion");
        }
    }
}
