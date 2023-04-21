import java.time.*;
import java.util.*;

public class Calendario {
    static class ComparadorAlarmas implements Comparator<Alarma> {
        public int compare(Alarma a1, Alarma a2) {
            return a1.getFechaHoraDisparo().compareTo(a2.getFechaHoraDisparo());
        }
    }

    private final PriorityQueue<Alarma> alarmas = new PriorityQueue<>(new ComparadorAlarmas());
    private final Map<LocalDate,HashSet<Item>> items = new HashMap<>();
    private final Set<EventoRepetible> repetibles = new HashSet<>();
    private final String mail;

    public Calendario(String mail) {
        this.mail = mail;
    }

    public Alarma getProximaAlarma() {
        return this.alarmas.peek();
    }

    public void dispararAlarma() {
        if (this.alarmas.isEmpty())
            return;

        var alarma = this.alarmas.poll();
        alarma.disparar(this.mail);
        if (alarma.getFechaHoraDisparo() != null) {
            this.alarmas.add(alarma);
        }
    }

    public Calendario agregar(Item item) {
        var set = this.items.computeIfAbsent(item.getIdTiempo().toLocalDate(), k -> new HashSet<>());
        set.add(item);
        return this;
    }

    public Calendario agregar(EventoRepetible repetible) {
        this.repetibles.add(repetible);
        return this;
    }

    public void eliminar(Item item) {
        var set = this.items.computeIfAbsent(item.getIdTiempo().toLocalDate(), k -> new HashSet<>());

        set.remove(item);
        this.alarmas.removeAll(item.getAlarmas());
    }

    public void eliminar(EventoRepetible repetible) {
        this.repetibles.remove(repetible);
        this.alarmas.removeAll(repetible.getAlarmas());
    }

    public void agregarAlarma(Item item, Alarma alarma) {
        item.agregarAlarma(alarma);
        this.alarmas.add(alarma);
    }

    public void agregarAlarmas(Item item, List<Alarma> alarmas) {
        item.agregarAlarmas(alarmas);
        this.alarmas.addAll(alarmas);
    }

    public void borrarAlarma(Item item, Alarma alarma) {
        item.borrarAlarma(alarma);
        this.alarmas.remove(alarma);
    }

    public Evento toEvento(EventoRepetible repetible) {
        var evento = new Evento(repetible);
        this.eliminar(repetible);
        this.agregar(evento);
        List<Alarma> alarmas = repetible.getAlarmas();
        for (var alarma : alarmas)
            alarma.marcarComoNoRepetible(evento);
        this.agregarAlarmas(evento, alarmas);
        return evento;
    }

    public EventoRepetible toRepetible(Evento evento) {
        var repetible = new EventoRepetible(evento);
        this.eliminar(evento);
        this.agregar(repetible);
        List<Alarma> alarmas = evento.getAlarmas();
        for (var alarma : alarmas)
            alarma.marcarComoRepetible(repetible);
        this.agregarAlarmas(repetible, alarmas);
        return repetible;
    }

    public Set<Item> getItems(LocalDate desde, LocalDate hasta) {
        var set = new HashSet<Item>();
        for (var fecha = desde; fecha.isBefore(hasta); fecha = fecha.plusDays(1)) {
            var items = this.items.get(fecha);
            if (items != null) set.addAll(items);
        }

        for (var repetible : repetibles)
            if (repetible.caeEntre(desde, hasta))
                set.add(repetible);

        return set;
    }

    public Set<Item> getItems(LocalDateTime desde, LocalDateTime hasta) {
        return getItems(desde.toLocalDate(), hasta.toLocalDate());
    }
}
