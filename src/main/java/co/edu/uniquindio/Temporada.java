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

    public LocalDate getFechaInicio() {return fechaInicio;}
    public void setFechaInicio(LocalDate fechaInicio) {this.fechaInicio = fechaInicio;}
    public LocalDate getFechaFin() {return fechaFin;}
    public void setFechaFin(LocalDate fechaFin) {this.fechaFin = fechaFin;}
    public double getPorcentajeRecargo() {return porcentajeRecargo;}
    public void setPorcentajeRecargo(double porcentajeRecargo) {this.porcentajeRecargo = porcentajeRecargo;}
}