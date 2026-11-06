package co.edu.uniquindio;

import java.time.LocalDate;

public class Temporada {
    private TipoTemporada tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeRecargo;

    public Temporada(TipoTemporada tipo, LocalDate fechaInicio, LocalDate fechaFin, double porcentajeRecargo) {
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeRecargo = porcentajeRecargo;
    }
    public Temporada(LocalDate fechaInicio, LocalDate fechaFin, double porcentajeRecargo) {
        this(TipoTemporada.MEDIA, fechaInicio, fechaFin, porcentajeRecargo);
    }

    // Comprueba si una fecha cae dentro del rango de esta temporada
    public boolean aplicaEnFecha(LocalDate fecha) {
        if (fecha == null || fechaInicio == null || fechaFin == null) {
            return false;
        }
        return (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin));
    }

    // Verifica si hay solapamiento de fechas con otro rango de temporada
    public boolean cruceCon(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null || fechaInicio == null || fechaFin == null) {
            return false;
        }
        return (inicio.isBefore(this.fechaFin) || inicio.isEqual(this.fechaFin)) &&
                (fin.isAfter(this.fechaInicio) || fin.isEqual(this.fechaInicio));
    }
    // Getters y Setters
    public TipoTemporada getTipo() { return tipo; }
    public void setTipo(TipoTemporada tipo) { this.tipo = tipo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public double getPorcentajeRecargo() { return porcentajeRecargo; }
    public void setPorcentajeRecargo(double porcentajeRecargo) { this.porcentajeRecargo = porcentajeRecargo; }
}