import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.*;
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

        // Vista principal
        stageTarea = inicializarVentanaTarea();
        stageAlarmaTarea = inicializarVentanaAlarmaTarea();
        stageEvento = inicializarVentanaEvento();
        stageAlarmaEvento = inicializarVentanaAlarmaEvento();
        assertGraciasIntelliJ(stageTarea, stageEvento, stageAlarmaTarea, stageAlarmaEvento);
        agregarTarea.setOnAction(e -> stageTarea.show());
        agregarEvento.setOnAction(e -> stageEvento.show());

        // Vista agregar tarea
        TareaListo.setOnAction(e -> tareaListo(stageTarea, calendario));

        // Vista agregar evento
        EventoListo.setOnAction(e -> eventoListo(stageEvento, calendario));

        // Vista agregar alarmas
        AlarmasListoTarea.setOnAction(e -> alarmasListoTarea(stageAlarmaTarea));
        AlarmasListoEvento.setOnAction(e -> alarmasListoEvento(stageAlarmaEvento));
    }

    public void tareaListo(Stage stage, Calendario calendario) {
        var titulo = TareaTitulo.getText();
        var descripcion = TareaDescripcion.getText();
        var fechaInicial = TareaFechaInicial.getValue();
        var todoElDia = TareaTodoElDia.isSelected();
        var fecha = fechaInicial.atTime(TareaHora.getValue(), TareaMinutos.getValue());
        var tarea = new Tarea(titulo, descripcion, fecha, todoElDia);
        calendario.agregar(tarea).agregarAlarmas(tarea, alarmasBuffer);
        alarmasBuffer.clear();
        stage.close();
    }

    public void eventoListo(Stage stage, Calendario calendario) {
        // Evento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin)
        var titulo = EventoTitulo.getText();
        var descripcion = EventoDescripcion.getText();
        var fechaInicial = EventoFechaInicial.getValue().atTime(EventoHora.getValue(), EventoMinutos.getValue());
        var fechaFinal = EventoFechaFinal.getValue().atTime(EventoHora.getValue(), EventoMinutos.getValue());
        var repeticion = EventoRepeticion.getValue();
        var evento = new Evento(titulo, descripcion, fechaInicial, fechaFinal);
        calendario.agregar(evento).agregarAlarmas(evento, alarmasBuffer);
        alarmasBuffer.clear();

        if (repeticion.equals("Diario")) {
            var repetible = calendario.toRepetible(evento);
            repetible.setRepeticionDiaria(0, 0); // Meter esto.
        }

        stage.close();
    }

    public void alarmasListoTarea(Stage stage) {
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

    public void alarmasListoEvento(Stage stage) {
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

    public Stage inicializarVentanaTarea() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistaAgregarTarea.fxml"));
            loader.setController(this);
            Parent ventana = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Tarea");
            stage.setScene(new Scene(ventana));
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

    public Stage inicializarVentanaAlarmaTarea() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistaAgregarAlarmaTarea.fxml"));
            loader.setController(this);
            Parent ventana = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Alarma");
            stage.setScene(new Scene(ventana));
            SpinnerValueFactory<Integer> horas = TareaHora.getValueFactory();
            SpinnerValueFactory<Integer> minutos = TareaMinutos.getValueFactory();
            AlarmasHoraTarea.setValueFactory(horas);
            AlarmasMinutosTarea.setValueFactory(minutos);
            AlarmasRelatividadTarea.setItems(FXCollections.observableArrayList("No tiene", "Antes", "Despues"));
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de agregar alarmas");
            return null;
        }
    }

    public Stage inicializarVentanaEvento() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistaAgregarEvento.fxml"));
            loader.setController(this);
            Parent ventana = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Evento");
            stage.setScene(new Scene(ventana));
            SpinnerValueFactory<Integer> horas = TareaHora.getValueFactory();
            SpinnerValueFactory<Integer> minutos = TareaMinutos.getValueFactory();
            EventoHora.setValueFactory(horas);
            EventoMinutos.setValueFactory(minutos);
            EventoRepeticion.setItems(FXCollections.observableArrayList("No tiene", "Diario"));
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de agregar evento");
            return null;
        }
    }

    private Stage inicializarVentanaAlarmaEvento() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("vistaAgregarAlarmaEvento.fxml"));
            loader.setController(this);
            Parent ventana = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Alarma");
            stage.setScene(new Scene(ventana));
            SpinnerValueFactory<Integer> horas = TareaHora.getValueFactory();
            SpinnerValueFactory<Integer> minutos = TareaMinutos.getValueFactory();
            AlarmasHoraEvento.setValueFactory(horas);
            AlarmasMinutosEvento.setValueFactory(minutos);
            AlarmasRelatividadEvento.setItems(FXCollections.observableArrayList("No tiene", "Antes", "Despues"));
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de agregar alarmas");
            return null;
        }
    }

    public void assertGraciasIntelliJ(Stage stageTarea, Stage stageEvento, Stage stageAlarmaTarea, Stage stageAlarmaEvento) {
        assert stageTarea != null;
        assert stageEvento != null;
        assert stageAlarmaTarea != null;
        assert stageAlarmaEvento != null;
    }

    public static Calendario obtenerCalendario() {
        try {
            return Calendario.deserializar(new FileInputStream("src/main/calendario.bin"));
        } catch (Exception e) {
            return new Calendario("mail@fi.uba.ar");
        }
    }

    public static void guardarCalendario(Calendario calendario) {
        try {
            calendario.serializar(new FileOutputStream("src/main/calendario.bin"));
        } catch (IOException e) {
            // No puede pasar.
            System.out.println("Error al guardar el calendario");
        }
    }

    // TODO: La lista del principio
    // TODO: los botones para navegar los dias / semanas / meses
    // TODO: mirar las alarmas y hacerlas sonar
    // TODO: rehacer la interfaz de agregar eventos para poder hacer repetibles
    // TODO: hacer que se puedan editar los items
    // TODO:
}
