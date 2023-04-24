import java.io.PrintStream;
import java.time.LocalDateTime;

public class Alarma {
    private LocalDateTime fechaHoraDisparo;
    private final LocalDateTime fechaHoraOriginal;
    private EventoRepetible duenioRepetible = null;
    private PrintStream impresora = System.out;

    private boolean mandaMail = false;
    private boolean suena = false;
    private boolean muestraNotificacion = false;
    // private Sonido sonido;

    public Alarma(LocalDateTime fechaHoraDisparo) { // Sonido sonido
        this.fechaHoraDisparo = fechaHoraDisparo;
        this.fechaHoraOriginal = fechaHoraDisparo;
    }

    public Alarma(LocalDateTime fechaHoraDisparo, PrintStream out) {
        this(fechaHoraDisparo);
        this.impresora = out;
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

    // En el caso de querer agregar mas funcionalidad, habria que aplicar algun patron como Command
    public void disparar(String mail) {
        if (this.mandaMail) {
            impresora.println("Enviando mail a " + mail);
        }

        if (this.suena) {
            impresora.println("Sonando");
        }

        if (this.muestraNotificacion) {
            impresora.println("Mostrando notificacion");
        }

        if (this.duenioRepetible != null) {
            this.setFechaHoraDisparo(this.duenioRepetible.computarProximaFecha(this));
            if (this.getFechaHoraDisparo() == null)
                this.duenioRepetible.borrarAlarma(this);
        }

        else this.setFechaHoraDisparo(null);
    } // test branches
}
