import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Main extends Application {
    // Vista principal
    @FXML private Button agregarTarea;
    @FXML private Button agregarEvento;
    @FXML private DatePicker fechaPicker;
    @FXML private ListView<Item> listaItems;

    // Vista agregar tarea
    @FXML private TextField TareaTitulo;
    @FXML private TextArea TareaDescripcion;
    @FXML private DatePicker TareaFechaInicial;
    @FXML private CheckBox TareaTodoElDia;
    @FXML private Button TareaAgregarAlarmas;
    @FXML private Button TareaListo;
    @FXML private Spinner<Integer> TareaHora;
    @FXML private Spinner<Integer> TareaMinutos;

    // Vista agregar evento
    @FXML private TextField EventoTitulo;
    @FXML private TextArea EventoDescripcion;
    @FXML private DatePicker EventoFechaInicial;
    @FXML private DatePicker EventoFechaFinal;
    @FXML private ChoiceBox<String> EventoRepeticion;
    @FXML private Button EventoAgregarAlarmas;
    @FXML private Button EventoListo;
    @FXML private Spinner<Integer> EventoHora;
    @FXML private Spinner<Integer> EventoMinutos;
    @FXML private Spinner<Integer> EventoRepeticionCantidad;
    @FXML private DatePicker EventoRepeticionFecha;
    @FXML private Spinner<Integer> EventoRepeticionIntervalo;
    @FXML private CheckBox EventoLunes;
    @FXML private CheckBox EventoMartes;
    @FXML private CheckBox EventoMiercoles;
    @FXML private CheckBox EventoJueves;
    @FXML private CheckBox EventoViernes;
    @FXML private CheckBox EventoSabado;
    @FXML private CheckBox EventoDomingo;

    // Vista agregar alarmas
    @FXML private Spinner<Integer> AlarmasHoraTarea;
    @FXML private Spinner<Integer> AlarmasMinutosTarea;
    @FXML private CheckBox AlarmasNotiTarea;
    @FXML private ChoiceBox<String> AlarmasRelatividadTarea;
    @FXML private Button AlarmasListoTarea;

    @FXML private Spinner<Integer> AlarmasHoraEvento;
    @FXML private Spinner<Integer> AlarmasMinutosEvento;
    @FXML private CheckBox AlarmasNotiEvento;
    @FXML private ChoiceBox<String> AlarmasRelatividadEvento;
    @FXML private Button AlarmasListoEvento;

    private Stage stageTarea;
    private Stage stageEvento;
    private Stage stageAlarmaTarea;
    private Stage stageAlarmaEvento;

    LocalDate ahora = LocalDate.now();
    LocalDate desde = ahora.minusDays(ahora.getDayOfMonth());
    LocalDate hasta = desde.plusMonths(1).minusDays(ahora.getDayOfMonth());
    ObservableList<Item> items = FXCollections.observableList(new ArrayList<>());
    int numeroIntervalo = 0;

    private final List<Alarma> alarmasBuffer = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        var calendario = obtenerCalendario();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vista.fxml"));
        loader.setController(this);
        Pane contenedor = loader.load();
        var scene = new Scene(contenedor);
        stage.setTitle("Calendario");
        stage.resizableProperty().setValue(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> guardarCalendario(calendario));
        SetearLista(calendario);

        // Vista principal
        stageTarea = inicializarVentanaTarea();
        stageEvento = inicializarVentanaEvento();
        stageAlarmaTarea = inicializarVentanaAlarmas("vistaAgregarAlarmaTarea.fxml");
        stageAlarmaEvento = inicializarVentanaAlarmas("vistaAgregarAlarmaEvento.fxml");
        ventanaTareaAlarmasSpinner();
        ventanaEventoAlarmasSpinner();
        agregarTarea.setOnAction(e -> stageTarea.show());
        agregarEvento.setOnAction(e -> stageEvento.show());

        // Vista agregar tarea
        TareaListo.setOnAction(e -> tareaListo(stageTarea, calendario));
        TareaAgregarAlarmas.setOnAction(e -> stageAlarmaTarea.show());
        TareaFechaInicial.showingProperty().addListener((obs, old, newV) -> TareaFechaInicialListener());

        // Vista agregar evento
        EventoListo.setOnAction(e -> eventoListo(stageEvento, calendario));
        EventoAgregarAlarmas.setOnAction(e -> stageAlarmaEvento.show());
        EventoRepeticion.showingProperty().addListener((obs, old, newV) -> RepeticionListener());
        EventoFechaInicial.showingProperty().addListener((obs, old, newV) -> EventoFechaInicialListener());

        // Vista agregar alarmas
        AlarmasListoTarea.setOnAction(e -> alarmasListoTarea(stageAlarmaTarea));
        AlarmasListoEvento.setOnAction(e -> alarmasListoEvento(stageAlarmaEvento));
    }

    private void SetearLista(Calendario calendario) {
        ahora = LocalDate.now();
        desde = ahora.minusDays(ahora.getDayOfMonth()).plusMonths(numeroIntervalo);
        hasta = desde.plusMonths(1).minusDays(ahora.getDayOfMonth()).plusMonths(numeroIntervalo);
        listaItems.getItems().clear();
        items.clear();
        items.addAll(calendario.getItems(desde, hasta));
        listaItems.setItems(items);
    }

    private void TareaFechaInicialListener() {
        TareaAgregarAlarmas.setDisable(TareaFechaInicial.getValue() == null);
    }

    private void EventoFechaInicialListener() {
        EventoAgregarAlarmas.setDisable(EventoFechaInicial.getValue() == null);
    }

    private void RepeticionListener() {
        if (EventoRepeticion.getValue() == null) return;
        switch (EventoRepeticion.getValue()) {
            case "No tiene" -> {
                EventoRepeticionCantidad.setDisable(true);
                EventoRepeticionIntervalo.setDisable(true);
                EventoRepeticionFecha.setDisable(true);
                setRepeticionDias(true);
            }

            case "Diario" -> {
                EventoRepeticionCantidad.setDisable(false);
                EventoRepeticionIntervalo.setDisable(false);
                EventoRepeticionFecha.setDisable(false);
                setRepeticionDias(true);
            }

            case "Semanal" -> {
                EventoRepeticionCantidad.setDisable(false);
                EventoRepeticionIntervalo.setDisable(true);
                EventoRepeticionFecha.setDisable(false);
                setRepeticionDias(false);
            }

            case "Mensual", "Anual" -> {
                EventoRepeticionCantidad.setDisable(false);
                EventoRepeticionIntervalo.setDisable(true);
                EventoRepeticionFecha.setDisable(false);
                setRepeticionDias(true);
            }
        }
    }

    private void setRepeticionDias(boolean b) {
        EventoLunes.setDisable(b);
        EventoMartes.setDisable(b);
        EventoMiercoles.setDisable(b);
        EventoJueves.setDisable(b);
        EventoViernes.setDisable(b);
        EventoSabado.setDisable(b);
        EventoDomingo.setDisable(b);
    }

    private boolean chequearCamposTarea() {
        return !TareaTitulo.getText().isEmpty()
                && !TareaDescripcion.getText().isEmpty()
                && TareaFechaInicial.getValue() != null
                && TareaHora.getValue() != null
                && TareaMinutos.getValue() != null;
    }

    private boolean chequearCamposEvento() {
        return !EventoTitulo.getText().isEmpty()
                && !EventoDescripcion.getText().isEmpty()
                && EventoFechaInicial.getValue() != null
                && EventoHora.getValue() != null
                && EventoMinutos.getValue() != null
                && EventoFechaFinal.getValue() != null
                && EventoRepeticion.getValue() != null;
    }

    private void tareaListo(Stage stage, Calendario calendario) {
        if (!chequearCamposTarea()) return;
        var titulo = TareaTitulo.getText();
        var descripcion = TareaDescripcion.getText();
        var fechaInicial = TareaFechaInicial.getValue();
        var todoElDia = TareaTodoElDia.isSelected();
        var fecha = fechaInicial.atTime(TareaHora.getValue(), TareaMinutos.getValue());
        var tarea = new Tarea(titulo, descripcion, fecha, todoElDia);
        calendario.agregar(tarea).agregarAlarmas(tarea, alarmasBuffer);
        alarmasBuffer.clear();
        SetearLista(calendario);
        stage.close();
    }

    private void eventoListo(Stage stage, Calendario calendario) {
        if (!chequearCamposEvento()) return;
        var titulo = EventoTitulo.getText();
        var descripcion = EventoDescripcion.getText();
        var fechaInicial = EventoFechaInicial.getValue().atTime(EventoHora.getValue(), EventoMinutos.getValue());
        var fechaFinal = EventoFechaFinal.getValue().atTime(EventoHora.getValue(), EventoMinutos.getValue());
        var repeticion = EventoRepeticion.getValue();
        if (fechaFinal.isBefore(fechaInicial)) return;
        var evento = new Evento(titulo, descripcion, fechaInicial, fechaFinal);
        calendario.agregar(evento).agregarAlarmas(evento, alarmasBuffer);
        alarmasBuffer.clear();

        switch (repeticion) {
            case "Diario" -> {
                var repetible = calendario.toRepetible(evento);
                if (EventoRepeticionFecha.getValue() != null)
                    repetible.setRepeticionDiaria(EventoRepeticionCantidad.getValue(), EventoRepeticionFecha.getValue().atStartOfDay());
                else
                    repetible.setRepeticionDiaria(EventoRepeticionIntervalo.getValue(), EventoRepeticionCantidad.getValue());
            }
            case "Semanal" -> {
                var repetible = calendario.toRepetible(evento);
                var lista = new ArrayList<>(List.of(
                        EventoLunes.isSelected(),
                        EventoMartes.isSelected(),
                        EventoMiercoles.isSelected(),
                        EventoJueves.isSelected(),
                        EventoViernes.isSelected(),
                        EventoSabado.isSelected(),
                        EventoDomingo.isSelected()
                ));

                repetible.setRepeticionSemanal(lista, EventoRepeticionCantidad.getValue());
            }
            case "Mensual" -> {
                var repetible = calendario.toRepetible(evento);
                repetible.setRepeticionMensual(EventoRepeticionCantidad.getValue());
            }
            case "Anual" -> {
                var repetible = calendario.toRepetible(evento);
                repetible.setRepeticionAnual(EventoRepeticionCantidad.getValue());
            }
        }

        SetearLista(calendario);
        stage.close();
    }

    private void alarmasListoTarea(Stage stage) {
        var hora = LocalTime.of(AlarmasHoraTarea.getValue(), AlarmasMinutosTarea.getValue());
        var fecha = TareaFechaInicial.getValue().atTime(TareaHora.getValue(), TareaMinutos.getValue());
        switch (AlarmasRelatividadTarea.getValue()) {
            case "Antes"   -> fecha = fecha.minusHours(hora.getHour()).minusMinutes(hora.getMinute());
            case "Después" -> fecha = fecha.plusHours(hora.getHour()).plusMinutes(hora.getMinute());
            default        -> fecha = LocalDateTime.of(TareaFechaInicial.getValue(), hora);
        }

        var alarma = new Alarma(fecha).setConfig(false, false, AlarmasNotiTarea.isSelected());
        alarmasBuffer.add(alarma);
        stage.close();
    }

    private void alarmasListoEvento(Stage stage) {
        var hora = LocalTime.of(AlarmasHoraEvento.getValue(), AlarmasMinutosEvento.getValue());
        var fecha = EventoFechaInicial.getValue().atTime(EventoHora.getValue(), EventoMinutos.getValue());
        switch (AlarmasRelatividadEvento.getValue()) {
            case "Antes"   -> fecha = fecha.minusHours(hora.getHour()).minusMinutes(hora.getMinute());
            case "Después" -> fecha = fecha.plusHours(hora.getHour()).plusMinutes(hora.getMinute());
            default        -> fecha = LocalDateTime.of(EventoFechaInicial.getValue(), hora);
        }

        var alarma = new Alarma(fecha).setConfig(false, false, AlarmasNotiEvento.isSelected());
        alarmasBuffer.add(alarma);
        stage.close();
    }

    private Stage obtenerStage(String path, String nombreVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setController(this);
            Parent ventana = loader.load();
            Stage stage = new Stage();
            stage.setTitle(nombreVentana);
            stage.setScene(new Scene(ventana));
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de " + nombreVentana);
            return null;
        }
    }

    private Stage inicializarVentanaTarea() {
        try {
            Stage stage = obtenerStage("vistaAgregarTarea.fxml", "Agregar Tarea");
            SpinnerValueFactory<Integer> horas = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            SpinnerValueFactory<Integer> minutos = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            TareaHora.setValueFactory(horas);
            TareaMinutos.setValueFactory(minutos);
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de agregar tarea");
            return null;
        }
    }

    private Stage inicializarVentanaEvento() {
        try {
            Stage stage = obtenerStage("vistaAgregarEvento.fxml", "Agregar Evento");
            SpinnerValueFactory<Integer> horas = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            SpinnerValueFactory<Integer> minutos = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            SpinnerValueFactory<Integer> cantidad = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
            SpinnerValueFactory<Integer> intervalo = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
            EventoHora.setValueFactory(horas);
            EventoMinutos.setValueFactory(minutos);
            EventoRepeticionCantidad.setValueFactory(cantidad);
            EventoRepeticionIntervalo.setValueFactory(intervalo);
            EventoRepeticion.setItems(FXCollections.observableArrayList("No tiene", "Diario", "Semanal", "Mensual", "Anual"));
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de agregar evento");
            return null;
        }
    }

    private void ventanaTareaAlarmasSpinner() {
        SpinnerValueFactory<Integer> horas = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        SpinnerValueFactory<Integer> minutos = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        AlarmasHoraTarea.setValueFactory(horas);
        AlarmasMinutosTarea.setValueFactory(minutos);
        AlarmasRelatividadTarea.setItems(FXCollections.observableArrayList("No tiene", "Antes", "Despues"));
    }

    private void ventanaEventoAlarmasSpinner() {
        SpinnerValueFactory<Integer> horas = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        SpinnerValueFactory<Integer> minutos = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        AlarmasHoraEvento.setValueFactory(horas);
        AlarmasMinutosEvento.setValueFactory(minutos);
        AlarmasRelatividadEvento.setItems(FXCollections.observableArrayList("No tiene", "Antes", "Despues"));
    }

    private Stage inicializarVentanaAlarmas(String path) {
        return obtenerStage(path, "Agregar Alarma");
    }

    private static Calendario obtenerCalendario() {
        try {
            return Calendario.deserializar(new FileInputStream("src/main/calendario.bin"));
        } catch (Exception e) {
            return new Calendario("mail@fi.uba.ar");
        }
    }

    private static void guardarCalendario(Calendario calendario) {
        try {
            calendario.serializar(new FileOutputStream("src/main/calendario.bin"));
        } catch (IOException e) {
            // No puede pasar.
            System.out.println("Error al guardar el calendario");
        }
    }

    // TODO: formatear los items
    // TODO: los botones para navegar los meses
    // TODO: mirar las alarmas y hacerlas sonar
    // TODO: hacer que se puedan editar los items
}
