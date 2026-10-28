package co.edu.uniquindio;

import java.time.LocalDateTime;

public record Pago(long monto,MedioPago medioPago, LocalDateTime fechaHora ) {
}
