package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private String codigoFolio;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private LocalDateTime fechaHoraCreacion;
    private int valorTotal;

    public Reserva(String codigoFolio, LocalDate fechaEntrada, LocalDate fechaSalida, LocalDateTime fechaHoraCreacion, int valorTotal) {
        this.codigoFolio = codigoFolio;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.valorTotal = valorTotal;
    }

    public String getCodigoFolio() {return codigoFolio;}
    public void setCodigoFolio(String codigoFolio) {this.codigoFolio = codigoFolio;}
    public LocalDate getFechaEntrada() {return fechaEntrada;}
    public void setFechaEntrada(LocalDate fechaEntrada) {this.fechaEntrada = fechaEntrada;}
    public LocalDate getFechaSalida() {return fechaSalida;}
    public void setFechaSalida(LocalDate fechaSalida) {this.fechaSalida = fechaSalida;}
    public int getValorTotal() {return valorTotal;}
    public void setValorTotal(int valorTotal) {this.valorTotal = valorTotal;}
    public LocalDateTime getFechaHoraCreacion() {return fechaHoraCreacion;}
    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {this.fechaHoraCreacion = fechaHoraCreacion;}
}
