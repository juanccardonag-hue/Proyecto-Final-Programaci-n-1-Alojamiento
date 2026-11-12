package co.edu.uniquindio;

import java.time.LocalDateTime;

public record Novedad( String descripcion, LocalDateTime fechaHora) {
    @Override
    public String toString() {
        return "[" + fechaHora + "] " + descripcion;
    }
}

