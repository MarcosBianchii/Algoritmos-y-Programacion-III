import java.time.*;
import java.util.*;

public class Calendario {
    static class ComparadorAlarmas implements Comparator<Alarma> {
        public int compare(Alarma a1, Alarma a2) {
            return a1.getFechaHoraDisparo().compareTo(a2.getFechaHoraDisparo());
        }
    }

    private final PriorityQueue<Alarma> alarmas = new PriorityQueue<>(new ComparadorAlarmas());
    private final HashMap<LocalDate,ArrayList<Item>> items = new HashMap<>();
    private final ArrayList<EventoRepetible> repetibles = new ArrayList<>();
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

        this.alarmas.poll().disparar(this.mail);
    }

    public void agregar(Item item) {
        var lista = this.items.computeIfAbsent(item.getIdTiempo().toLocalDate(), k -> new ArrayList<>());

        lista.add(item);
        this.alarmas.addAll(item.getAlarmas());
        if (item instanceof EventoRepetible)
            this.repetibles.add((EventoRepetible)item);
    }

    public void eliminar(Item item) {
        var lista = this.items.computeIfAbsent(item.getIdTiempo().toLocalDate(), k -> new ArrayList<>());

        lista.remove(item);
        this.alarmas.removeAll(item.getAlarmas());
        if (item instanceof EventoRepetible)
            this.repetibles.remove(item);
    }

    public void agregarAlarma(Item item, Alarma alarma) {
        item.agregarAlarma(alarma);
        this.alarmas.add(alarma);
    }

    public void agregarAlarmas(Item item, ArrayList<Alarma> alarmas) {
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
        this.agregarAlarmas(evento, repetible.getAlarmas());
        return evento;
    }

    public EventoRepetible toRepetible(Evento evento) {
        var repetible = new EventoRepetible(evento);
        this.eliminar(evento);
        this.agregar(repetible);
        this.agregarAlarmas(repetible, evento.getAlarmas());
        return repetible;
    }

    public ArrayList<Item> getItems(LocalDate desde, LocalDate hasta) {
        var lista = new ArrayList<Item>();
        var fecha = desde;

        while (fecha.isBefore(hasta)) {
            var items = this.items.get(fecha);
            if (items != null) {
                for (var item : items)
                    if (!(item instanceof EventoRepetible))
                        lista.add(item);
            }

            fecha = fecha.plusDays(1);
        }

        for (var repetible : this.repetibles)
            if (repetible.caeEntre(desde, hasta))
                lista.add(repetible);

        return lista;
    }

    public ArrayList<Item> getItems(LocalDateTime desde, LocalDateTime hasta) {
        return getItems(desde.toLocalDate(), hasta.toLocalDate());
    }
}
