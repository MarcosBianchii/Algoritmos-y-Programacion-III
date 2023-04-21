import java.time.LocalDateTime;

public class Alarma {
    private LocalDateTime fechaHoraDisparo;
    private final LocalDateTime fechaHoraOriginal;
    private EventoRepetible duenioRepetible = null;

    private boolean mandaMail = false;
    private boolean suena = false;
    private boolean muestraNotificacion = false;
    // private Sonido sonido;

    public Alarma(LocalDateTime fechaHoraDisparo) { // Sonido sonido
        this.fechaHoraDisparo = fechaHoraDisparo;
        this.fechaHoraOriginal = fechaHoraDisparo;
    }

    public LocalDateTime getFechaHoraDisparo() {
        return this.fechaHoraDisparo;
    }

    public LocalDateTime getFechaHoraOriginal() {
        return this.fechaHoraOriginal;
    }

    public void setFechaHoraDisparo(LocalDateTime fechaHoraDisparo) {
        this.fechaHoraDisparo = fechaHoraDisparo;
    }

    public void marcarComoRepetible(EventoRepetible duenioRepetible) {
        this.duenioRepetible = duenioRepetible;
    }

    public void marcarComoNoRepetible() {
        this.duenioRepetible = null;
    }

    public void setConfig(boolean mandaMail, boolean suena, boolean muestraNotificacion) {
        this.mandaMail = mandaMail;
        this.suena = suena;
        this.muestraNotificacion = muestraNotificacion;
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

        if (this.duenioRepetible != null) {
            this.setFechaHoraDisparo(this.duenioRepetible.computarProximaFecha(this));
            if (this.getFechaHoraDisparo() == null)
                this.duenioRepetible.borrarAlarma(this);
        }

        else this.setFechaHoraDisparo(null);
    }

}
