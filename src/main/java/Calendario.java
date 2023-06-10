import java.time.*;
import java.util.*;
import java.io.*;

public class Calendario implements Serializable {
    static class ComparadorAlarmas implements Serializable, Comparator<Alarma> {
        @Override
        public int compare(Alarma a1, Alarma a2) {
            return a1.getFechaHoraDisparo().compareTo(a2.getFechaHoraDisparo());
        }
    }

    private final PriorityQueue<Alarma> alarmas = new PriorityQueue<>(new ComparadorAlarmas());
    private final Map<LocalDate,List<Item>> items = new HashMap<>();
    private final List<EventoRepetible> repetibles = new ArrayList<>();
    private final String mail;

    public Calendario(String mail) {
        this.mail = mail;
    }

    public Alarma getProximaAlarma() {
        return alarmas.peek();
    }

    public void dispararAlarma() {
        if (alarmas.isEmpty())
            return;

        var alarma = alarmas.poll();
        alarma.disparar(mail);
        if (alarma.getFechaHoraDisparo() != null) {
            alarmas.add(alarma);
        }
    }

    public Calendario agregar(Item item) {
        var lista = items.computeIfAbsent(item.getIdTiempo().toLocalDate(), k -> new ArrayList<>());
        lista.add(item);
        return this;
    }

    public Calendario agregar(EventoRepetible repetible) {
        repetibles.add(repetible);
        return this;
    }

    public void eliminar(Item item) {
        var lista = items.computeIfAbsent(item.getIdTiempo().toLocalDate(), k -> new ArrayList<>());

        lista.remove(item);
        alarmas.removeAll(item.getAlarmas());
    }

    public void eliminar(EventoRepetible repetible) {
        repetibles.remove(repetible);
        alarmas.removeAll(repetible.getAlarmas());
    }

    public void agregarAlarma(Item item, Alarma alarma) {
        if (alarma == null) return;
        item.agregarAlarma(alarma);
        alarmas.add(alarma);
    }

    public void agregarAlarmas(Item item, List<Alarma> alarmas) {
        if (alarmas == null) return;
        item.agregarAlarmas(alarmas);
        this.alarmas.addAll(alarmas);
    }

    public void borrarAlarma(Item item, Alarma alarma) {
        item.borrarAlarma(alarma);
        alarmas.remove(alarma);
    }

    public Evento toEvento(EventoRepetible repetible) {
        var evento = new Evento(repetible);
        eliminar(repetible);
        agregar(evento);
        agregarAlarmas(evento, repetible.getAlarmas());
        return evento;
    }

    public EventoRepetible toRepetible(Evento evento) {
        var repetible = new EventoRepetible(evento);
        eliminar(evento);
        agregar(repetible);
        agregarAlarmas(repetible, evento.getAlarmas());
        return repetible;
    }

    public List<Item> getItems(LocalDate desde, LocalDate hasta) {
        var lista = new ArrayList<Item>();
        for (var fecha = desde; fecha.isBefore(hasta); fecha = fecha.plusDays(1)) {
            var items = this.items.get(fecha);
            if (items != null) lista.addAll(items);

            for (var repetible : repetibles)
                if (repetible.caeEn(fecha))
                    lista.add(new EventoRepetibleDecorator(repetible, fecha));
        }

        return lista;
    }

    public List<Item> getItems(LocalDateTime desde, LocalDateTime hasta) {
        return getItems(desde.toLocalDate(), hasta.toLocalDate());
    }

    public void serializar(OutputStream os) throws IOException {
        var out = new ObjectOutputStream(os);
        out.writeObject(this);
        out.flush();
    }

    public static Calendario deserializar(InputStream is) throws IOException, ClassNotFoundException {
        var in = new ObjectInputStream(is);
        return (Calendario) in.readObject();
    }
}
