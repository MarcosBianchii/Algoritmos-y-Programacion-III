# Integrantes:
* Franco Gentilini - 108733
* Marcos Bianchi - 108921

### Alarma:
* Guarda su fecha y hora de activación y algunas configuraciones sobre que hacer cuando se active.

### Item:
* Clase abstracta padre de Tarea y Evento. Guarda los datos y comportamiento común entre ambas.

### Tarea:
* Guarda su fecha y hora de vencimiento.

### Evento:
* Guarda sus fechas y horas de inicio y fin.
* Puede ser convertida a EventoRepetible.

### EventoRepetible:
* Hereda de Evento y tiene un CalculadorDeFechas el cual cambia según su tipo de repetición.
* Utiliza Strategy para cambiar su comportamiento.
* Es decorado por EventoRepetibleDecorator, el cual agrega la funcionalidad de guardar una referencia al repetible guardando su fecha y hora de inicio y fin en cierto momento.
