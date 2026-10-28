package co.edu.uniquindio;

import java.time.LocalDate;

public class DetalleNocheReserva {
    private LocalDate fechaNoche;
    private int cantidadPagantes;
    private long tarifaNochePersona;
    private Temporada temporada;
    private long subtotal;

    public DetalleNocheReserva(LocalDate fechaNoche, int cantidadPagantes, long tarifaNochePersona, Temporada temporada) {
        this.fechaNoche = fechaNoche;
        this.cantidadPagantes = cantidadPagantes;
        this.tarifaNochePersona = tarifaNochePersona;
        this.temporada = temporada;
        this.subtotal = calcularSubtotal();
    }

    // Calcula el subtotal de una noche en particular
    private long calcularSubtotal() {
        long tarifaIndividual = tarifaNochePersona;

        // Si hay temporada configurada, calculamos el recargo y se lo sumamos
        if (temporada != null) {
            long recargo = (long) (tarifaNochePersona * temporada.getPorcentajeRecargo());
            tarifaIndividual = tarifaNochePersona + recargo;
        }

        // Multiplica el valor por persona por los pagantes
        return cantidadPagantes * tarifaIndividual;
    }
    public LocalDate getFechaNoche() {return fechaNoche;}
    public void setFechaNoche(LocalDate fechaNoche) {this.fechaNoche = fechaNoche;}
    public int getCantidadPagantes() {return cantidadPagantes;}
    public void setCantidadPagantes(int cantidadPagantes) {this.cantidadPagantes = cantidadPagantes;}
    public long getTarifaNochePersona() {return tarifaNochePersona;}
    public void setTarifaNochePersona(int tarifaNochePersona) {this.tarifaNochePersona = tarifaNochePersona;}
    public long getSubtotal() {return subtotal;}
    public void setSubtotal(long subtotal) {this.subtotal = subtotal;}
}