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
        var lista = this.items.get(item.getIdTiempo().toLocalDate());
        if (lista == null) {
            lista = new ArrayList<>();
            this.items.put(item.getIdTiempo().toLocalDate(), lista);
        }

        lista.add(item);
        this.alarmas.addAll(item.getAlarmas());
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

    public void eliminar(Item item) {
        var lista = this.items.get(item.getIdTiempo().toLocalDate());
        if (lista == null)
            return;

        lista.remove(item);
        this.alarmas.removeAll(item.getAlarmas());
    }

    public ArrayList<Item> getItems(LocalDateTime desde, LocalDateTime hasta) {
        var lista = new ArrayList<Item>();
        var fecha = desde.toLocalDate();
        while (fecha.isBefore(hasta.toLocalDate())) {
            var tareas = this.items.get(fecha);
            if (tareas != null)
                lista.addAll(tareas);

            fecha = fecha.plusDays(1);
        }

        return lista;
    }
}
