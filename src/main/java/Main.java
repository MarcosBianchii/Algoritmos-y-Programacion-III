import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Objects;

public class Main extends Application {
    // Vista principal
    @FXML private Button agregarTarea;
    @FXML private Button agregarEvento;
    @FXML private ListView<Item> listaItems;
    @FXML private Button mesIzquierda;
    @FXML private Button mesDerecha;
    @FXML private Label fechaActual;
    @FXML private ChoiceBox<String> vistaIntervalo;

    // Vista agregar tarea
    @FXML private TextField TareaTitulo;
    @FXML private TextArea TareaDescripcion;
    @FXML private DatePicker TareaFechaInicial;
    @FXML private CheckBox TareaTodoElDia;
    @FXML private Button TareaAgregarAlarmas;
    @FXML private Button TareaListo;
    @FXML private Spinner<Integer> TareaHora;
    @FXML private Spinner<Integer> TareaMinutos;

    @FXML private TextField TareaEdicionTitulo;
    @FXML private TextField TareaEdicionDescripcion;
    @FXML private Text TareaEdicionVencimiento;
    @FXML private CheckBox TareaEdicionTodoElDia;
    @FXML private Text TareaEdicionAlarmas;
    @FXML private Button TareaEdicionGuardar;
    @FXML private Button TareaEdicionEliminar;
    @FXML private CheckBox TareaEdicionCompletada;

    // Vista agregar evento
    @FXML private TextField EventoTitulo;
    @FXML private TextArea EventoDescripcion;
    @FXML private DatePicker EventoFechaInicial;
    @FXML private DatePicker EventoFechaFinal;
    @FXML private ChoiceBox<String> EventoRepeticion;
    @FXML private Button EventoAgregarAlarmas;
    @FXML private Button EventoListo;
    @FXML private Spinner<Integer> EventoHoraInicio;
    @FXML private Spinner<Integer> EventoMinutosInicio;
    @FXML private Spinner<Integer> EventoHoraFin;
    @FXML private Spinner<Integer> EventoMinutosFin;
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

    @FXML private TextField EventoEdicionTitulo;
    @FXML private TextField EventoEdicionDescripcion;
    @FXML private Text EventoEdicionInicio;
    @FXML private Text EventoEdicionFin;
    @FXML private Text EventoEdicionAlarmas;
    @FXML private Button EventoEdicionGuardar;
    @FXML private Button EventoEdicionEliminar;
    @FXML private Text EventoEdicionRepeticion;

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

    @FXML private Label notiAlarmaTitulo;
    @FXML private Label notiAlarmaDesc;
    @FXML private Button cerrarNotiAlarma;

    private Stage stageTarea;
    private Stage stageEvento;
    private Stage stageAlarmaTarea;
    private Stage stageAlarmaEvento;
    private Stage stageMostrarAlarma;
    private Stage stageEdicionTarea;
    private Stage stageEdicionEvento;

    private final Calendario calendario = obtenerCalendario();
    private final ObservableList<Item> items = FXCollections.observableList(new ArrayList<>());
    private final List<Alarma> alarmasBuffer = new ArrayList<>();
    private int numeroIntervalo = 0;
    private Tarea tareaEditada = null;
    private Evento eventoEditado = null;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vista.fxml"));
        loader.setController(this);
        Pane contenedor = loader.load();
        var scene = new Scene(contenedor);
        stage.setTitle("Calendario");
        stage.setOnCloseRequest(e -> guardarCalendario());
        stage.resizableProperty().setValue(false);
        stage.setScene(scene);
        stage.show();
        tomarIntervalo();
        actualizarLista();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                chequearAlarma();
            }
        }.start();

        // Vista principal
        stageTarea = inicializarVentanaTarea();
        stageEvento = inicializarVentanaEvento();
        agregarTarea.setOnAction(e -> stageTarea.show());
        agregarEvento.setOnAction(e -> stageEvento.show());
        listaItems.setOnMouseClicked(e -> listaClick());
        mesIzquierda.setOnAction(e -> sumarMes(-1));
        mesDerecha.setOnAction(e -> sumarMes(1));
        vistaIntervalo.setItems(FXCollections.observableArrayList("Diario", "Semanal", "Mensual"));
        vistaIntervalo.showingProperty().addListener((obs, old, newV) -> tomarIntervalo());

        // Vista edicion tarea
        stageEdicionTarea = inicializarVentanaEdicionTarea();
        TareaEdicionGuardar.setOnAction(e -> cambiarDatosTarea());
        TareaEdicionEliminar.setOnAction(e -> eliminarItem(tareaEditada));

        // Vista edicion Evento
        stageEdicionEvento = inicializarVentanaEdicionEvento();
        EventoEdicionGuardar.setOnAction(e -> cambiarDatosEvento());
        EventoEdicionEliminar.setOnAction(e -> eliminarItem(eventoEditado));

        // Vista agregar tarea
        TareaListo.setOnAction(e -> tareaListo(stageTarea));
        TareaAgregarAlarmas.setOnAction(e -> stageAlarmaTarea.show());
        TareaFechaInicial.showingProperty().addListener((obs, old, newV) -> tareaFechaInicialListener());

        // Vista agregar evento
        EventoListo.setOnAction(e -> eventoListo(stageEvento));
        EventoAgregarAlarmas.setOnAction(e -> stageAlarmaEvento.show());
        EventoRepeticion.showingProperty().addListener((obs, old, newV) -> repeticionListener());
        EventoFechaInicial.showingProperty().addListener((obs, old, newV) -> eventoFechaInicialListener());

        // Vista alarmas
        stageAlarmaTarea = inicializarVentanaAlarmas("vistaAgregarAlarmaTarea.fxml");
        stageAlarmaEvento = inicializarVentanaAlarmas("vistaAgregarAlarmaEvento.fxml");
        stageMostrarAlarma = inicializarVentanaMostrarAlarma();
        ventanaTareaAlarmasSpinner();
        ventanaEventoAlarmasSpinner();

        AlarmasListoTarea.setOnAction(e -> alarmasListoTarea(stageAlarmaTarea));
        AlarmasListoEvento.setOnAction(e -> alarmasListoEvento(stageAlarmaEvento));
        AlarmasRelatividadTarea.showingProperty().addListener((obs, old, newV) -> alarmasRelatividadTareaListener());
        AlarmasRelatividadEvento.showingProperty().addListener((obs, old, newV) -> alarmasRelatividadEventoListener());
        cerrarNotiAlarma.setOnAction(e -> stageMostrarAlarma.close());
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
            System.out.println(e.getMessage());
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
            SpinnerValueFactory<Integer> horasInicio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            SpinnerValueFactory<Integer> minutosInicio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            SpinnerValueFactory<Integer> horasFin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            SpinnerValueFactory<Integer> minutosFin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            SpinnerValueFactory<Integer> cantidad = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
            SpinnerValueFactory<Integer> intervalo = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
            EventoHoraInicio.setValueFactory(horasInicio);
            EventoMinutosInicio.setValueFactory(minutosInicio);
            EventoHoraFin.setValueFactory(horasFin);
            EventoMinutosFin.setValueFactory(minutosFin);
            EventoRepeticionCantidad.setValueFactory(cantidad);
            EventoRepeticionIntervalo.setValueFactory(intervalo);
            EventoRepeticion.setItems(FXCollections.observableArrayList("No tiene", "Diario", "Semanal", "Mensual", "Anual"));
            return stage;
        } catch (Exception e) {
            System.out.println("Error al inicializar la ventana de agregar evento");
            return null;
        }
    }

    public Stage inicializarVentanaMostrarAlarma() {
        try {
            return obtenerStage("vistaMostrarAlarma.fxml", "Alarma");
        } catch(Exception e) {
            System.out.println("Error al inicializar la ventana de mostrar alarma");
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

    private Stage inicializarVentanaEdicionTarea() {
        return obtenerStage("vistaEditarTarea.fxml", "Tarea");
    }

    private Stage inicializarVentanaEdicionEvento() {
        return obtenerStage("vistaEditarEvento.fxml", "Evento");
    }

    private void tomarIntervalo() {
        if (vistaIntervalo.getValue() == null)
            vistaIntervalo.setValue("Mensual");

        numeroIntervalo = 0;
        actualizarLista();
    }

    private void sumarMes(int i) {
        numeroIntervalo += i;
        actualizarLista();
    }

    private void actualizarLista() {
        LocalDate desde = LocalDate.now();
        LocalDate hasta = null;
        switch (vistaIntervalo.getValue()) {
            case "Diario" -> {
                desde = desde.plusDays(numeroIntervalo);
                hasta = desde.plusDays(1);
            }

            case "Semanal" -> {
                desde = desde.minusDays(desde.getDayOfWeek().getValue() - 1).plusWeeks(numeroIntervalo);
                hasta = desde.plusWeeks(1);
            }

            case "Mensual" -> {
                desde = desde.minusDays(desde.getDayOfMonth() - 1).plusMonths(numeroIntervalo);
                hasta = desde.plusMonths(1);
            }
        }

        fechaActual.setText(desde.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        items.clear();
        items.addAll(calendario.getItems(desde, hasta));
        listaItems.setItems(items);
    }

    private void listaClick() {
        Item item = listaItems.getSelectionModel().getSelectedItem();
        if (item != null) descripcionItem(item);
    }

    private void descripcionItem(Item item) {
        if (item.getRepeticion() == Repeticion.NO_REPETIBLE) {
            var tarea = (Tarea) item;
            TareaEdicionTitulo.setText(tarea.getTitulo());
            TareaEdicionDescripcion.setText(tarea.getDescripcion());
            TareaEdicionVencimiento.setText(tarea.getIdTiempo().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            TareaEdicionTodoElDia.setSelected(tarea.esTodoElDia());
            TareaEdicionAlarmas.setText(String.format("%d", tarea.getAlarmas().size()));
            tareaEditada = tarea;
            stageEdicionTarea.show();
        }

        else {
            var evento = (Evento) item;
            EventoEdicionTitulo.setText(evento.getTitulo());
            EventoEdicionDescripcion.setText(evento.getDescripcion());
            EventoEdicionInicio.setText(evento.getIdTiempo().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            EventoEdicionFin.setText(evento.getFin().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            EventoEdicionAlarmas.setText(String.format("%d", evento.getAlarmas().size()));
            EventoEdicionRepeticion.setText(evento.getRepeticion().toString());
            eventoEditado = evento;
            stageEdicionEvento.show();
        }
    }

    private void cambiarDatosTarea() {
        tareaEditada.setTitulo(TareaEdicionTitulo.getText());
        tareaEditada.setDescripcion(TareaEdicionDescripcion.getText());
        tareaEditada.setTodoElDia(TareaEdicionTodoElDia.isSelected());
        tareaEditada.setCompletada(TareaEdicionCompletada.isSelected());
        guardarCalendario();
        actualizarLista();
        stageEdicionTarea.close();
    }

    private void cambiarDatosEvento() {
        eventoEditado.setTitulo(EventoEdicionTitulo.getText());
        eventoEditado.setDescripcion(EventoEdicionDescripcion.getText());
        guardarCalendario();
        actualizarLista();
        stageEdicionEvento.close();
    }

    private void eliminarItem(Item item) {
        calendario.eliminar(item);
        guardarCalendario();
        actualizarLista();
        stageEdicionTarea.close();
        stageEdicionEvento.close();
    }

    private void chequearAlarma() {
        try {
            if (calendario.getProximaAlarma().getFechaHoraDisparo().isBefore(LocalDateTime.now())) {
                var alarma = calendario.getProximaAlarma();
                var duenio = alarma.getDuenio();
                calendario.dispararAlarma();

                if (alarma.notifica()) {
                    notiAlarmaTitulo.setText(duenio.getTitulo());
                    notiAlarmaDesc.setText(duenio.getDescripcion());
                    stageMostrarAlarma.show();
                }

                guardarCalendario();
                actualizarLista();
            }
        } catch (NullPointerException ignored) {}
    }

    private void tareaFechaInicialListener() {
        TareaAgregarAlarmas.setDisable(TareaFechaInicial.getValue() == null && TareaTitulo.getText() == null);
    }

    private void eventoFechaInicialListener() {
        EventoAgregarAlarmas.setDisable(EventoFechaInicial.getValue() == null && TareaTitulo.getText() == null);
    }

    private void alarmasRelatividadTareaListener() {
        AlarmasListoTarea.setDisable(AlarmasRelatividadTarea.getValue() == null);
        if (AlarmasRelatividadTarea.getValue() != null) AlarmasListoTarea.setDisable(false);
    }

    private void alarmasRelatividadEventoListener() {
        AlarmasListoEvento.setDisable(AlarmasRelatividadEvento.getValue() == null);
        if (AlarmasRelatividadEvento.getValue() != null) AlarmasListoEvento.setDisable(false);
    }

    private void repeticionListener() {
        if (EventoRepeticion.getValue() == null) return;
        switch (EventoRepeticion.getValue()) {
            case "No tiene" -> {
                EventoRepeticionCantidad.setDisable(true);
                EventoRepeticionIntervalo.setDisable(true);
                EventoRepeticionFecha.setDisable(true);
                setRepeticionDiasDisable(true);
            }

            case "Diario" -> {
                EventoRepeticionCantidad.setDisable(false);
                EventoRepeticionIntervalo.setDisable(false);
                EventoRepeticionFecha.setDisable(false);
                setRepeticionDiasDisable(true);
            }

            case "Semanal" -> {
                EventoRepeticionCantidad.setDisable(false);
                EventoRepeticionIntervalo.setDisable(true);
                EventoRepeticionFecha.setDisable(false);
                setRepeticionDiasDisable(false);
            }

            case "Mensual", "Anual" -> {
                EventoRepeticionCantidad.setDisable(false);
                EventoRepeticionIntervalo.setDisable(true);
                EventoRepeticionFecha.setDisable(false);
                setRepeticionDiasDisable(true);
            }
        }
    }

    private void setRepeticionDiasDisable(boolean b) {
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
                && EventoHoraInicio.getValue() != null
                && EventoMinutosInicio.getValue() != null
                && EventoHoraFin.getValue() != null
                && EventoMinutosFin.getValue() != null
                && EventoFechaFinal.getValue() != null
                && EventoRepeticion.getValue() != null;
    }

    private void tareaListo(Stage stage) {
        if (!chequearCamposTarea()) return;
        var titulo = TareaTitulo.getText();
        var descripcion = TareaDescripcion.getText();
        var fechaInicial = TareaFechaInicial.getValue();
        var todoElDia = TareaTodoElDia.isSelected();
        var fecha = fechaInicial.atTime(TareaHora.getValue(), TareaMinutos.getValue());
        var tarea = new Tarea(titulo, descripcion, fecha, todoElDia);

        alarmasBuffer.forEach(x -> x.setDuenio(tarea));
        calendario.agregar(tarea).agregarAlarmas(tarea, alarmasBuffer);
        alarmasBuffer.clear();
        guardarCalendario();
        actualizarLista();
        stage.close();
    }

    private void eventoListo(Stage stage) {
        if (!chequearCamposEvento()) return;
        var titulo = EventoTitulo.getText();
        var descripcion = EventoDescripcion.getText();
        var fechaInicial = EventoFechaInicial.getValue().atTime(EventoHoraInicio.getValue(), EventoMinutosInicio.getValue());
        var fechaFinal = EventoFechaFinal.getValue().atTime(EventoHoraFin.getValue(), EventoMinutosFin.getValue());
        var repeticion = EventoRepeticion.getValue();
        if (fechaFinal.isBefore(fechaInicial)) return;
        var evento = new Evento(titulo, descripcion, fechaInicial, fechaFinal);

        alarmasBuffer.forEach(x -> x.setDuenio(evento));
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

        guardarCalendario();
        actualizarLista();
        stage.close();
    }

    private void alarmasListoTarea(Stage stage) {
        if (AlarmasRelatividadTarea.getValue() == null) return;

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
        if (AlarmasRelatividadEvento.getValue() == null) return;

        var hora = LocalTime.of(AlarmasHoraEvento.getValue(), AlarmasMinutosEvento.getValue());
        var fecha = EventoFechaInicial.getValue().atTime(EventoHoraInicio.getValue(), EventoMinutosInicio.getValue());
        switch (AlarmasRelatividadEvento.getValue()) {
            case "Antes"   -> fecha = fecha.minusHours(hora.getHour()).minusMinutes(hora.getMinute());
            case "Después" -> fecha = fecha.plusHours(hora.getHour()).plusMinutes(hora.getMinute());
            default        -> fecha = LocalDateTime.of(EventoFechaInicial.getValue(), hora);
        }

        var alarma = new Alarma(fecha).setConfig(false, false, AlarmasNotiEvento.isSelected());
        alarmasBuffer.add(alarma);
        stage.close();
    }

    private Calendario obtenerCalendario() {
        try {
            return Calendario.deserializar(getClass().getResourceAsStream("calendario.bin"));
        } catch (Exception e) {
            return new Calendario("mail@fi.uba.ar");
        }
    }

    private void guardarCalendario() {
        try {
            var path = Objects.requireNonNull(getClass().getResource("calendario.bin")).getPath();
            calendario.serializar(new FileOutputStream(path));
        } catch (Exception ignored) {}
    }
}
