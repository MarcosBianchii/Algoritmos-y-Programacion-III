public enum Repeticion {
    NO_TIENE, DIARIA, SEMANAL, MENSUAL, ANUAL, NO_REPETIBLE;

    @Override
    public String toString() {
        return switch (this) {
            case NO_TIENE -> "No tiene";
            case DIARIA -> "Diaria";
            case SEMANAL -> "Semanal";
            case MENSUAL -> "Mensual";
            case ANUAL -> "Anual";
            default -> "";
        };
    }
}
