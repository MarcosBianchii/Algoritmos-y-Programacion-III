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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Main extends Application {
    // Vista principal
    @FXML private Button agregarTarea;
    @FXML private Button agregarEvento;
    @FXML private ListView<Item> listaItems;
    @FXML private Button mesIzquierda;
    @FXML private Button mesDerecha;
    @FXML private Label fechaActual;

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
    private Item itemEditado = null;

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
        actualiarLista();

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

        // Vista edicion tarea
        stageEdicionTarea = inicializarVentanaEdicionTarea();
        TareaEdicionGuardar.setOnAction(e -> cambiarDatosTarea());
        TareaEdicionEliminar.setOnAction(e -> eliminarItem());

        // Vista edicion Evento
        stageEdicionEvento = inicializarVentanaEdicionEvento();
        EventoEdicionGuardar.setOnAction(e -> cambiarDatosEvento());
        EventoEdicionEliminar.setOnAction(e -> eliminarItem());


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

    private void eliminarItem() {
        calendario.eliminar(itemEditado);
        guardarCalendario();
        actualiarLista();
        stageEdicionTarea.close();
        stageEdicionEvento.close();
    }

    private void cambiarDatosTarea() {
        var tarea = (Tarea) itemEditado;
        tarea.setTitulo(TareaEdicionTitulo.getText());
        tarea.setDescripcion(TareaEdicionDescripcion.getText());
        tarea.setTodoElDia(TareaEdicionTodoElDia.isSelected());
        tarea.setCompletada(TareaEdicionCompletada.isSelected());
        guardarCalendario();
        actualiarLista();
        stageEdicionTarea.close();
    }

    private void cambiarDatosEvento() {
        var evento = (Evento) itemEditado;
        evento.setTitulo(EventoEdicionTitulo.getText());
        evento.setDescripcion(EventoEdicionDescripcion.getText());
        guardarCalendario();
        actualiarLista();
        stageEdicionEvento.close();
    }

    private void descripcionItem(Item item) {
        if (item instanceof Tarea tarea) {
            TareaEdicionTitulo.setText(tarea.getTitulo());
            TareaEdicionDescripcion.setText(tarea.getDescripcion());
            TareaEdicionVencimiento.setText(tarea.getIdTiempo().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            TareaEdicionTodoElDia.setSelected(tarea.esTodoElDia());
            TareaEdicionAlarmas.setText(String.format("%d", tarea.getAlarmas().size()));
            stageEdicionTarea.show();

        }

        else if (item instanceof Evento evento) {
            EventoEdicionTitulo.setText(evento.getTitulo());
            EventoEdicionDescripcion.setText(evento.getDescripcion());
            EventoEdicionInicio.setText(evento.getIdTiempo().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            EventoEdicionFin.setText(evento.getFin().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
            EventoEdicionAlarmas.setText(String.format("%d", evento.getAlarmas().size()));
            EventoEdicionRepeticion.setText("No tiene");

            if (evento instanceof EventoRepetible repetible) {
                String str = null;
                var calculador = repetible.getCalculador();
                if (calculador instanceof CalculadorDiario) str = "Diario";
                else if (calculador instanceof CalculadorSemanal) str = "Semanal";
                else if (calculador instanceof CalculadorMensual) str = "Mensual";
                else if (calculador instanceof CalculadorAnual) str = "Anual";
                EventoEdicionRepeticion.setText(str);
            }

            stageEdicionEvento.show();
        }

        itemEditado = item;
    }

    private void listaClick() {
        Item item = listaItems.getSelectionModel().getSelectedItem();
        if (item != null) descripcionItem(item);
    }

    private void chequearAlarma() {
        if (calendario.getProximaAlarma() == null)
            return;

        if (calendario.getProximaAlarma().getFechaHoraDisparo() == null)
            return;

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
            actualiarLista();
        }
    }

    private void sumarMes(int i) {
        numeroIntervalo += i;
        actualiarLista();
    }

    private void actualiarLista() {
        var ahora = LocalDate.now();
        var desde = ahora.minusDays(ahora.getDayOfMonth()).plusMonths(numeroIntervalo);
        var hasta = desde.plusMonths(1).minusDays(ahora.getDayOfMonth()).plusMonths(numeroIntervalo);
        fechaActual.setText(desde.format(DateTimeFormatter.ofPattern("MM/yyyy")));
        items.clear();
        items.addAll(calendario.getItems(desde, hasta));
        listaItems.setItems(items);
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
        actualiarLista();
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
        actualiarLista();
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

    private static Calendario obtenerCalendario() {
        try {
            return Calendario.deserializar(new FileInputStream("src/main/calendario.bin"));
        } catch (Exception e) {
            return new Calendario("mail@fi.uba.ar");
        }
    }

    private void guardarCalendario() {
        try {
            calendario.serializar(new FileOutputStream("src/main/calendario.bin"));
        } catch (IOException e) {
            // No puede pasar.
            System.out.println("Error al guardar el calendario");
        }
    }

    // TODO: poner mas labels en agregar tareas y eventos
    // TODO: cambiar tipo de intervalo en la lista
}
