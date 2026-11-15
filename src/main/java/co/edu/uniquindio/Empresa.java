package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String nit;
    private String nombreAlojamiento;
    private List<Huesped> listaHuespedes;
    private List<Recepcionista> listaRecepcionistas;
    private List<Temporada> listaTemporadas;
    private List<Reserva> listaReservas;
    private List<Apartamento> listaApartamentos;
    private List<Empleado> listaEmpleados;
    private Administrador administrador;

    public Empresa(String nit, String nombreAlojamiento) {
        this.nit = nit;
        this.nombreAlojamiento = nombreAlojamiento;
        this.listaHuespedes = new ArrayList<>();
        this.listaRecepcionistas = new ArrayList<>();
        this.listaEmpleados = new ArrayList<>();
        this.listaApartamentos = new ArrayList<>();
        this.listaTemporadas = new ArrayList<>();
        this.listaReservas = new ArrayList<>();

        this.administrador = new Administrador("41911188", "Jhon Mendez", LocalDate.of(1967, 11, 13), "3219393229", "mendezj@hotel.com", "adminPrincipal", "jhon-123");
    }

    // --- GETTERS Y SETTERS ---
    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombreAlojamiento() {
        return nombreAlojamiento;
    }

    public void setNombreAlojamiento(String nombreAlojamiento) {
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public List<Huesped> getListaHuespedes() {
        return listaHuespedes;
    }

    public List<Recepcionista> getListaRecepcionistas() {
        return listaRecepcionistas;
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public List<Apartamento> getListaApartamentos() {
        return listaApartamentos;
    }

    public List<Temporada> getListaTemporadas() {
        return listaTemporadas;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }
}