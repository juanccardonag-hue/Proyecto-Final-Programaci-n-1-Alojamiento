package co.edu.uniquindio;

import java.time.LocalDate;

public class Temporada {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeRecargo;

    public Temporada(LocalDate fechaInicio, LocalDate fechaFin, double porcentajeRecargo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeRecargo = porcentajeRecargo;
    }

    //vemos si una fecha cae dentro del rango de una temporada
    public boolean aplicaEnFecha(LocalDate fecha) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin));
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public double getPorcentajeRecargo() {
        return porcentajeRecargo;
    }
}
