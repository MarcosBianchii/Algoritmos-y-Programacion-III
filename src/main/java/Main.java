import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    // Vista Principal
    @FXML private final Button agregarTarea = new Button("Agregar Tarea");
    @FXML private final Button agregarEvento = new Button("Agregar Evento");
    @FXML private final DatePicker fechaPicker = new DatePicker();
    private final ObservableList<Item> items = FXCollections.observableArrayList();
    @FXML private final ListView<Item> listaItems = new ListView<>(items);

    // Vista Agregar Evento
    @FXML private final DatePicker EventoFechaInicial = new DatePicker();
    @FXML private final TextField EventoTitulo = new TextField("Titulo...");
    @FXML private final TextArea EventoDescripcion = new TextArea("Descripcion...");
    @FXML private final DatePicker EventoFechaFinal = new DatePicker();
    @FXML private final ChoiceBox<Boolean> EventoRepeticion = new ChoiceBox<>(FXCollections.observableArrayList());
    @FXML private final Button EventoAgregarAlarmas = new Button("Agregar Alarmas");
    @FXML private final Button EventoListo = new Button("Listo");

    // Vista Agregar Tarea
    @FXML private final CheckBox TareaTodoElDia = new CheckBox("Todo el dia");
    @FXML private final Button TareaListo = new Button("Listo");
    @FXML private final Button TareaAgregarAlarmas = new Button("Agregar Alarmas");
    @FXML private final TextArea TareaDescripcion = new TextArea("Descripcion...");
    @FXML private final TextField TareaTitulo = new TextField("Titulo...");
    @FXML private final DatePicker TareaFechaInicial = new DatePicker();

    @Override
    public void start(Stage stage) throws Exception {
        var calendario = obtenerCalendario();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vista.fxml"));
        loader.setController(this);
        Pane contenedor = loader.load();
        var scene = new Scene(contenedor, 640, 640);

        agregarTarea.setOnAction(f -> {
            FXMLLoader tareaLoader = new FXMLLoader(getClass().getResource("vistaAgregarTarea.fxml"));
            tareaLoader.setController(this);
            Pane tareaContenedor = null;
            try {
                tareaContenedor = tareaLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            var tareaScene = new Scene(tareaContenedor, 640, 640);
            stage.setScene(tareaScene);
            stage.show();
        });

        stage.setScene(scene);
        stage.show();
        calendario.serializar(new FileOutputStream("src/main/calendario.bin"));
    }

    public static Calendario obtenerCalendario() {
        try {
            return Calendario.deserializar(new FileInputStream("src/main/calendario.bin"));
        } catch (Exception e) {
            return new Calendario("mail@fi.uba.ar");
        }
    }

    public static void guardarCalendario(Calendario calendario) throws IOException {
        try {
            calendario.serializar(new FileOutputStream("src/main/calendario.bin"));
        } catch (IOException e) {
            // No puede pasar.
            System.out.println("Error al guardar el calendario");
        }
    }
}
