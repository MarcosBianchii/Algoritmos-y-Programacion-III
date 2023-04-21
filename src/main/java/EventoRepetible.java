import java.time.temporal.ChronoUnit;
import java.time.*;
import java.util.*;

public class EventoRepetible extends Evento {
    private CalculadorDeFechas calculador = null;
    private int cantidadRepeticiones = 0;

    private final ArrayList<Boolean> dias = new ArrayList<>(); // MON, TUE, WED, THU, FRI, SAT, SUN
    private int frecuenciaDiaria = 0;

    public EventoRepetible(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
    }

    public EventoRepetible(Evento evento) {
        super(evento.titulo, evento.descripcion, evento.inicio, evento.fin);
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

    public int getCantidadRepeticiones() {
        return this.cantidadRepeticiones;
    }

    public ArrayList<Boolean> getDias() {
        return this.dias;
    }

    public int getFrecuenciaDiaria() {
        return this.frecuenciaDiaria;
    }

    // Si se repite todos los dias, el intervalo tiene que ser = 1
    public void setRepeticionDiaria(int intevalo, int cantidad) {
        this.calculador = new CalculadorDiario();
        this.cantidadRepeticiones = cantidad;
        this.frecuenciaDiaria = intevalo;
    }

    public void setRepeticionDiaria(int intervalo, LocalDateTime hasta) {
        this.calculador = new CalculadorDiario();
        long cantidad = this.inicio.until(hasta, ChronoUnit.DAYS);
        this.cantidadRepeticiones = Math.toIntExact((long) Math.floor((double) Math.toIntExact(cantidad) / intervalo));
        this.frecuenciaDiaria = intervalo;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, int cantidad) {
        this.calculador = new CalculadorSemanal();
        this.dias.clear();
        this.dias.addAll(dias);
        this.cantidadRepeticiones = cantidad;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, LocalDateTime hasta) {
        this.calculador = new CalculadorSemanal();
        this.dias.clear();
        this.dias.addAll(dias);
        long cantidad = this.inicio.until(hasta, ChronoUnit.WEEKS);
        this.cantidadRepeticiones = Math.toIntExact(cantidad);
    }

    public void setRepeticionMensual(int cantidad) {
        this.calculador = new CalculadorMensual();
        this.cantidadRepeticiones = cantidad;
    }

    public void setRepeticionMensual(LocalDateTime hasta) {
        this.calculador = new CalculadorMensual();
        long cantidad = this.inicio.until(hasta, ChronoUnit.MONTHS);
        this.cantidadRepeticiones = Math.toIntExact(cantidad);
    }

    public void setRepeticionAnual(int cantidad) {
        this.calculador = new CalculadorAnual();
        this.cantidadRepeticiones = cantidad;
    }

    public void setRepeticionAnual(LocalDateTime hasta) {
        this.calculador = new CalculadorAnual();
        long cantidad = this.inicio.until(hasta, ChronoUnit.YEARS);
        this.cantidadRepeticiones = Math.toIntExact(cantidad);
    }

    public boolean caeEntre(LocalDate desde, LocalDate hasta) {
        LocalDate fecha = this.inicio.toLocalDate();

        for (int i = 0; i < this.cantidadRepeticiones; i++) {
            if (fecha.isAfter(desde.minusDays(1)) && fecha.isBefore(hasta))
                return true;

            if (fecha.isAfter(hasta))
                break;

            fecha = this.calculador.sumarRepeticion(fecha, 1);
        }

        return false;
    }

    public LocalDateTime computarProximaFecha(Alarma alarma) {
        LocalDateTime fecha = this.calculador.calcularFechaSiguiente(this, alarma);
        LocalDateTime fechaLimite = this.calculador.calcularFechaLimite(this, alarma);
        return fecha.isAfter(fechaLimite) ? null : fecha;
    }
}
