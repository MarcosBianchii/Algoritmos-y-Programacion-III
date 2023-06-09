import java.time.temporal.ChronoUnit;
import java.time.*;
import java.util.*;

public class EventoRepetible extends Evento {
    private CalculadorDeFechas calculador = null;
    private int cantidadRepeticiones = 0;
    private boolean infinito = false;

    private final ArrayList<Boolean> dias = new ArrayList<>(); // MON, TUE, WED, THU, FRI, SAT, SUN
    private int frecuenciaDiaria = 0;

    public EventoRepetible(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
    }

    public EventoRepetible(EventoRepetible repetible) {
        super(repetible);
        calculador = repetible.calculador;
        cantidadRepeticiones = repetible.cantidadRepeticiones;
        dias.addAll(repetible.dias);
        frecuenciaDiaria = repetible.frecuenciaDiaria;
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
        return cantidadRepeticiones;
    }

    public ArrayList<Boolean> getDias() {
        return dias;
    }

    public int getFrecuenciaDiaria() {
        return frecuenciaDiaria;
    }

    // Si se repite todos los dias, el intervalo tiene que ser = 1
    public void setRepeticionDiaria(int intevalo, int cantidad) {
        calculador = new CalculadorDiario();
        cantidadRepeticiones = cantidad;
        frecuenciaDiaria = intevalo;
        if (cantidad == 0) infinito = true;
    }

    public void setRepeticionDiaria(int intervalo, LocalDateTime hasta) {
        calculador = new CalculadorDiario();
        long cantidad = this.inicio.until(hasta, ChronoUnit.DAYS);
        cantidadRepeticiones = Math.toIntExact((long) Math.floor((double) Math.toIntExact(cantidad) / intervalo));
        frecuenciaDiaria = intervalo;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, int cantidad) {
        calculador = new CalculadorSemanal();
        this.dias.clear();
        this.dias.addAll(dias);
        cantidadRepeticiones = cantidad;
        if (cantidad == 0) infinito = true;
    }

    public void setRepeticionSemanal(ArrayList<Boolean> dias, LocalDateTime hasta) {
        calculador = new CalculadorSemanal();
        this.dias.clear();
        this.dias.addAll(dias);
        long cantidad = this.inicio.until(hasta, ChronoUnit.WEEKS);
        cantidadRepeticiones = Math.toIntExact(cantidad);
    }

    public void setRepeticionMensual(int cantidad) {
        calculador = new CalculadorMensual();
        cantidadRepeticiones = cantidad;
        if (cantidad == 0) infinito = true;
    }

    public void setRepeticionMensual(LocalDateTime hasta) {
        calculador = new CalculadorMensual();
        long cantidad = inicio.until(hasta, ChronoUnit.MONTHS);
        cantidadRepeticiones = Math.toIntExact(cantidad);
    }

    public void setRepeticionAnual(int cantidad) {
        calculador = new CalculadorAnual();
        cantidadRepeticiones = cantidad;
        if (cantidad == 0) infinito = true;
    }

    public void setRepeticionAnual(LocalDateTime hasta) {
        calculador = new CalculadorAnual();
        long cantidad = inicio.until(hasta, ChronoUnit.YEARS);
        cantidadRepeticiones = Math.toIntExact(cantidad);
    }

    public boolean caeEn(LocalDate fecha) {
        return calculador.caeEn(this, fecha);
    }

    public LocalDateTime computarProximaFecha(Alarma alarma) {
        LocalDateTime fecha = calculador.calcularFechaSiguiente(this, alarma);
        LocalDateTime fechaLimite = calculador.calcularFechaLimite(this, alarma);
        return fecha.isAfter(fechaLimite) ? null : fecha;
    }

    public boolean esInfinito() {
        return infinito;
    }
}
