import java.util.ArrayList;
import java.time.LocalDateTime;

public interface Item {
    LocalDateTime getIdTiempo();
    ArrayList<Alarma> getAlarmas();
    void agregarAlarma(Alarma alarma);
    void agregarAlarmas(ArrayList<Alarma> alarmas);
    void borrarAlarma(Alarma alarma);
}
