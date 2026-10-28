package co.edu.uniquindio;

import java.time.LocalDateTime;

public record Cargo(String concepto, long monto, LocalDateTime fechaHora) {
}
