package co.edu.uniquindio;

import java.time.LocalDate;

public class Temporada {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeRecargo;

    public Temporada(LocalDate fechaFin, LocalDate fechaInicio, double porcentajeRecargo) {
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.porcentajeRecargo = porcentajeRecargo;
    }
    //vemos si una fecha cae dentro del rango de una temporada
    public boolean aplicaEnFecha(LocalDate fecha) {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin));
    }

    public LocalDate getFechaInicio() {return fechaInicio;}
    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio;}
    public LocalDate getFechaFin() {return fechaFin;}
    public void setFechaFin(LocalDate fechaFin) {this.fechaFin = fechaFin;}
    public double getPorcentajeRecargo() {return porcentajeRecargo;}
    public void setPorcentajeRecargo(double porcentajeRecargo) {this.porcentajeRecargo = porcentajeRecargo;}
}