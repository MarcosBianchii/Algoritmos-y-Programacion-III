import java.time.LocalDateTime;
import java.io.PrintStream;
import java.io.Serializable;

public class Alarma implements Serializable {
    private LocalDateTime fechaHoraDisparo;
    private final LocalDateTime fechaHoraOriginal;
    private Item duenio = null;
    private EventoRepetible duenioRepetible = null;
    private transient PrintStream impresora = System.out;

    private boolean mandaMail = false;
    private boolean suena = false;
    private boolean muestraNotificacion = false;
    // private Sonido sonido;

    public Alarma(LocalDateTime fechaHoraDisparo) { // Sonido sonido
        this.fechaHoraDisparo = fechaHoraDisparo;
        this.fechaHoraOriginal = fechaHoraDisparo;
    }

    public Alarma(LocalDateTime fechaHoraDisparo, Item duenio) {
        this(fechaHoraDisparo);
        this.duenio = duenio;
    }

    public Alarma(LocalDateTime fechaHoraDisparo, PrintStream out) {
        this(fechaHoraDisparo);
        this.impresora = out;
    }

    public Item getDuenio() {
        return this.duenio;
    }

    public Alarma setDuenio(Item duenio) {
        this.duenio = duenio;
        return this;
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

    public boolean notifica() {
        return muestraNotificacion;
    }

    public Alarma setConfig(boolean mandaMail, boolean suena, boolean muestraNotificacion) {
        this.mandaMail = mandaMail;
        this.suena = suena;
        this.muestraNotificacion = muestraNotificacion;
        return this;
    }

    // En el caso de querer agregar mas funcionalidad, habria que aplicar algun patron como Command
    public void disparar(String mail) {
        if (impresora != null) {
            if (this.mandaMail)
                impresora.println("Enviando mail a " + mail);

            if (this.suena)
                impresora.println("Sonando");

            if (this.muestraNotificacion)
                impresora.println("Mostrando notificacion");
        }

        if (this.duenioRepetible != null) {
            this.setFechaHoraDisparo(this.duenioRepetible.computarProximaFecha(this));
            if (this.getFechaHoraDisparo() == null)
                this.duenioRepetible.borrarAlarma(this);
        } else {
            duenio.borrarAlarma(this);
            this.setFechaHoraDisparo(null);
        }
    }
}
