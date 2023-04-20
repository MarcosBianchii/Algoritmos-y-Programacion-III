import java.time.temporal.ChronoUnit;
import java.time.*;
import java.util.*;

public class EventoRepetible extends Evento {
    private Repeticion repeticion;
    private final ArrayList<Boolean> dias = new ArrayList<>(); // MON, TUE, WED, THU, FRI, SAT, SUN
    private int cantidadRepeticiones = 0;
    private int frecuenciaDiaria = 0;

    public EventoRepetible(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
    }

    public EventoRepetible(Evento evento) {
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

    @Override
    public void agregarAlarma(Alarma alarma) {
        super.agregarAlarma(alarma);
        alarma.marcarComoRepetible(this);
    }

    @Override
    public void agregarAlarmas(List<Alarma> alarmas) {
        super.agregarAlarmas(alarmas);
        for (var alarma : alarmas)
            alarma.marcarComoRepetible(this);
    }

    private void setRepeticion(Repeticion repeticion, int cantidad) {
        this.cantidadRepeticiones = cantidad;
        this.repeticion = repeticion;
    }

    private void setRepeticion(Repeticion repeticion, LocalDateTime hasta) {
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

    public void setRepeticionMensual(int cantidad) {
        setRepeticion(Repeticion.MENSUAL, cantidad);
    }

    public void setRepeticionMensual(LocalDateTime hasta) {
        setRepeticion(Repeticion.MENSUAL, hasta);
    }

    public void setRepeticionAnual(int cantidad) {
        setRepeticion(Repeticion.ANUAL, cantidad);
    }

    public void setRepeticionAnual(LocalDateTime hasta) {
        setRepeticion(Repeticion.ANUAL, hasta);
    }

    public boolean caeEntre(LocalDate desde, LocalDate hasta) {
        LocalDate fecha = this.inicio.toLocalDate();

        for (int i = 0; i < this.cantidadRepeticiones; i++) {
            if (fecha.isAfter(desde.minusDays(1)) && fecha.isBefore(hasta))
                return true;

            if (fecha.isAfter(hasta))
                break;

            switch (this.repeticion) {
                case DIARIA  -> fecha = fecha.plusDays(this.frecuenciaDiaria);
                case SEMANAL -> fecha = fecha.plusWeeks(1);
                case MENSUAL -> fecha = fecha.plusMonths(1);
                case ANUAL   -> fecha = fecha.plusYears(1);
            }
        }

        return false;
    }

    private LocalDateTime calcularSiguienteFechaSemenal(LocalDateTime dia) {
        int inicial = dia.getDayOfWeek().getValue();
        for (int i = 0; i < this.dias.size(); i++) {
            int indice = (inicial + i) % this.dias.size();
            if (this.dias.get(indice)) {
                dia = dia.plusDays(i + 1);
                break;
            }
        }

        return dia;
    }

    private LocalDateTime calcularFechaFinalSemanal(LocalDateTime dia) {
        int i = this.dias.size() - 1;
        for (; i >= 0; i--)
            if (this.dias.get(i))
                break;

        var fecha = dia.plusWeeks(this.cantidadRepeticiones + 1);
        return fecha.plusDays(i - dia.getDayOfWeek().getValue());
    }

    public LocalDateTime computarProximaFecha(Alarma alarma) {
        LocalDateTime fecha = alarma.getFechaHoraDisparo();
        LocalDateTime fechaFinal = alarma.getFechaHoraOriginal();

        switch (this.repeticion) {
            case DIARIA -> {
                fecha = fecha.plusDays(this.frecuenciaDiaria);
                if (this.cantidadRepeticiones == -1) return fecha;
                fechaFinal = fechaFinal.plusDays((long) this.frecuenciaDiaria * this.cantidadRepeticiones);
            }

            case SEMANAL -> {
                fecha = calcularSiguienteFechaSemenal(fecha);
                if (this.cantidadRepeticiones == -1) return fecha;
                fechaFinal = calcularFechaFinalSemanal(fechaFinal);
            }

            case MENSUAL -> {
                fecha = fecha.plusMonths(1);
                if (this.cantidadRepeticiones == -1) return fecha;
                fechaFinal = fechaFinal.plusMonths(this.cantidadRepeticiones);
            }

            case ANUAL -> {
                fecha = fecha.plusYears(1);
                if (this.cantidadRepeticiones == -1) return fecha;
                fechaFinal = fechaFinal.plusYears(this.cantidadRepeticiones);
            }
        }

        if (fecha.isAfter(fechaFinal))
            return null;

        return fecha;
    }
}
