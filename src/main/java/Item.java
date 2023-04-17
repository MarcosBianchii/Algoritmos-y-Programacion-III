import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;

public interface Item {
    LocalDateTime getIdTiempo();
    void agregarAlarma(Alarma alarma);
    void agregarAlarmas(ArrayList<Alarma> alarmas);
    void borrarAlarma(Alarma alarma);
    ArrayList<Alarma> getAlarmas();
}
