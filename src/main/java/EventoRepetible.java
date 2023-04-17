import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

enum Repeticion {
    DIARIA,
    SEMANAL,
    MENSUAL,
    ANUAL,
}

public class EventoRepetible extends Evento {
    private Repeticion repeticion;
    private final ArrayList<Boolean> dias = new ArrayList<>(); // MON, TUE, WED, THU, FRI, SAT, SUN
    private int cantidadRepeticiones = 0;
    private int frecuenciaDiaria = 0;
    private boolean esInfinito = false;

    public EventoRepetible(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
    }

    protected EventoRepetible(Evento evento) {
        super(evento.titulo, evento.descripcion, evento.inicio, evento.fin);
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
        if (cantidad == -1) this.esInfinito = true;
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
        if (cantidad == -1) this.esInfinito = true;
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
        if (cantidad == -1) this.esInfinito = true;
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

    public boolean caeEntre(LocalDate desde, LocalDate hasta) {
        LocalDate fecha = this.inicio.toLocalDate();

        for (int i = 0; i < this.cantidadRepeticiones || this.esInfinito; i++) {
            if (fecha.isAfter(desde.minusDays(1)) && fecha.isBefore(hasta))
                return true;

            if (fecha.isAfter(hasta))
                return false;

            switch (this.repeticion) {
                case DIARIA  -> fecha = fecha.plusDays(this.frecuenciaDiaria);
                case SEMANAL -> fecha = fecha.plusWeeks(1);
                case MENSUAL -> fecha = fecha.plusMonths(1);
                case ANUAL   -> fecha = fecha.plusYears(1);
            }
        }

        return false;
    }
}
