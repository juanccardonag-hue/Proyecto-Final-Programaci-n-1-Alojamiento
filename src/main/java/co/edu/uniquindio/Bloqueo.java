package co.edu.uniquindio;

import java.time.LocalDate;

public record Bloqueo(LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
}
