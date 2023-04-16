import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

enum Repeticion {
    NOTIENE,
    DIARIA,
    SEMANAL,
    MENSUAL,
    ANUAL,
}

public class EventoRepetible extends Evento {

    private Repeticion repeticion = Repeticion.NOTIENE;
    private final ArrayList<Boolean> dias = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false));
    private int cantidadRepeticiones = 0;
    private int frecuenciaDiaria = 0;

    public EventoRepetible(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
    }

    public EventoRepetible(Evento evento) {
        super(evento.titulo, evento.descripcion, evento.inicio, evento.fin);
        this.alarmas.putAll(evento.alarmas);
    }

    public Evento toEvento() {
        var evento = new Evento(titulo, descripcion, inicio, fin);
        evento.agregarAlarmas(this.getAlarmas());
        return evento;
    }

    public Repeticion getRepeticion() {
        return this.repeticion;
    }
    public int getCantidadRepeticiones() {
        return this.cantidadRepeticiones;
    }

    public ArrayList<Boolean> getDias() {
        return this.dias;
    }

    public void setRepeticion(Repeticion repeticion, int cantidad) {
        this.cantidadRepeticiones = cantidad;
        this.repeticion = repeticion;
    }

    public void setRepeticion(Repeticion repeticion, LocalDateTime hasta) {
        long cantidad = 0;
        switch (repeticion) {
            case MENSUAL -> cantidad = this.inicio.until(hasta, ChronoUnit.MONTHS);
            case ANUAL   -> cantidad = this.inicio.until(hasta, ChronoUnit.YEARS);
        }

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = repeticion;
    }

    // Si se repite todos los dias, el intervalo tiene que ser = 1
    public void setRepeticionDiaria(int intevalo, int cantidad) {
        this.repeticion = Repeticion.DIARIA;
        this.cantidadRepeticiones = cantidad;
        this.frecuenciaDiaria = intevalo;
    }

    public void setRepeticionDiaria(int intervalo, LocalDateTime hasta) {
        this.repeticion = Repeticion.DIARIA;
        this.frecuenciaDiaria = intervalo;
        long cantidad = this.inicio.until(hasta, ChronoUnit.DAYS);
        this.cantidadRepeticiones = Math.toIntExact((long) Math.floor((double) Math.toIntExact(cantidad) / intervalo));
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, int cantidad) {
        this.dias.clear();
        this.dias.addAll(dias);
        this.cantidadRepeticiones = cantidad;
        this.repeticion = Repeticion.SEMANAL;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, LocalDateTime hasta) {
        this.dias.clear();
        this.dias.addAll(dias);

        long cantidad = this.inicio.until(hasta, ChronoUnit.WEEKS);

        this.cantidadRepeticiones = Math.toIntExact(cantidad);
        this.repeticion = Repeticion.SEMANAL;
    }
}
