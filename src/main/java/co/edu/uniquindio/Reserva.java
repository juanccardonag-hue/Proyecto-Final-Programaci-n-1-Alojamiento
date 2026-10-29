package co.edu.uniquindio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva implements Serializable {

    private String codigoFolio;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Canal canal;
    private EstadoReserva estado;
    private String codigoExterno;       // Código si la reserva viene de canal EXTERNA
    private double comisionPlataforma;  // 15% si es canal EXTERNA, 0.0 en los otros
    private long valorTotal;
    private LocalDateTime fechaHoraCreacion;
    private Huesped titular;
    private Apartamento apartamento;
    private Folio folio;
    private Empleado empleado;                 // El recepcionista o empleado que atendió (opcional en PORTAL)
    private List<Ocupante> ocupantes;
    private List<DetalleNocheReserva> detallesReserva;

    public Reserva(String codigoFolio, LocalDate fechaEntrada, LocalDate fechaSalida, Canal canal, Huesped titular, Apartamento apartamento, Empleado empleado, String codigoExterno) {

        this.codigoFolio = codigoFolio;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.canal = canal;
        this.estado = EstadoReserva.PENDIENTE;
        this.fechaHoraCreacion = LocalDateTime.now();
        this.titular = titular;
        this.apartamento = apartamento;
        this.empleado = empleado;
        this.codigoExterno = codigoExterno;

        // Si el canal es EXTERNA, se fija la comisión del 15%
        if (canal == Canal.EXTERNA) {
            this.comisionPlataforma = 0.15;
        } else {
            this.comisionPlataforma = 0.0;
        }
        // Se crea el Folio asociado a esta reserva
        this.folio = new Folio(codigoFolio);
        this.ocupantes = new ArrayList<>();
        this.detallesReserva = new ArrayList<>();
    }
    // FUNCIONALIDADES DE LÓGICA DE NEGOCIO

    // Calcula el número de noches de la estancia
    public long calcularNoches() {
        if (fechaEntrada == null || fechaSalida == null) {
            return 0;
        }
        long noches = 0;
        LocalDate fechaAuxiliar = fechaEntrada;

        // Mientras la fecha sea menor a la fecha de salida
        while (fechaAuxiliar.isBefore(fechaSalida)) {
            noches++;
            fechaAuxiliar = fechaAuxiliar.plusDays(1);
        }
        return noches;
    }

    //Contamos cuántos ocupantes pagan tarifa
    public int contarOcupantesPagantes() {
        int contadorPagantes = 0;
        for (int i = 0; i < ocupantes.size(); i++) {
            Ocupante ocup = ocupantes.get(i);
            if (ocup.esPagante(fechaEntrada)) {
                contadorPagantes++;
            }
        }
        return contadorPagantes;
    }

    // Buscamos fecha en temporada
    private Temporada buscarTemporada(LocalDate fecha, List<Temporada> temporadas) {
        for (int i = 0; i < temporadas.size(); i++) {
            Temporada t = temporadas.get(i);
            if (t.aplicaEnFecha(fecha)) {
                return t;
            }
        }
        return null;
    }

    // Liquidamos noche a noche
    public void liquidarEstancia(List<Temporada> temporadas) {
        detallesReserva.clear();
        int pagantes = contarOcupantesPagantes();
        long totalEstancia = 0;
        LocalDate fechaAuxiliar = fechaEntrada;

        while (fechaAuxiliar.isBefore(fechaSalida)) {

            Temporada temp = buscarTemporada(fechaAuxiliar, temporadas);

            DetalleNocheReserva detalle = new DetalleNocheReserva(fechaAuxiliar, pagantes, apartamento.getTarifaPersonaNoche(), temp);

            detallesReserva.add(detalle);
            totalEstancia = totalEstancia + detalle.getSubtotal();

            // Avanzamos al siguiente día
            fechaAuxiliar = fechaAuxiliar.plusDays(1);
        }

        this.valorTotal = totalEstancia;
        this.folio.getListaCargo().clear();
        this.folio.agregarCargo("Alojamiento estancia (" + calcularNoches() + " noches)", valorTotal);
    }


    // Cambiamos el estado de la reserva
    // Retorna true si el cambio es permitido según la tabla del proyecto, false si es inválido
    public boolean cambiarEstado(EstadoReserva nuevoEstado) {

        if (this.estado == EstadoReserva.PENDIENTE) {
            if (nuevoEstado == EstadoReserva.CONFIRMADA || nuevoEstado == EstadoReserva.CANCELADA) {
                this.estado = nuevoEstado;
                return true;
            }
        }
        if (this.estado == EstadoReserva.CONFIRMADA) {
            if (nuevoEstado == EstadoReserva.EN_CURSO || nuevoEstado == EstadoReserva.CANCELADA || nuevoEstado == EstadoReserva.NO_SHOW) {
                this.estado = nuevoEstado;
                return true;
            }
        }
        if (this.estado == EstadoReserva.EN_CURSO) {
            if (nuevoEstado == EstadoReserva.FINALIZADA) {
                this.estado = nuevoEstado;
                return true;
            }
        }

        // Si intenta cualquier otro cambio no permitido, se rechaza
        return false;
    }

    //Verificamos si una reserva esta activa (PENDIENTE, CONFIRMADA o EN_CURSO)
    public boolean estaActiva() {
        return (this.estado == EstadoReserva.PENDIENTE ||
                this.estado == EstadoReserva.CONFIRMADA ||
                this.estado == EstadoReserva.EN_CURSO);
    }

    // Anti doble Venta: Comprueba si esta reserva se cruza en fechas con otro rango
    public boolean cruceCon(LocalDate entradaBuscar, LocalDate salidaBuscar) {
        return (entradaBuscar.isBefore(this.fechaSalida) && salidaBuscar.isAfter(this.fechaEntrada));
    }

    // Revisa si pasaron 24 horas desde la creación sin confirmarse
    public boolean estaVencida(LocalDateTime fechaHoraActual) {
        if (this.estado == EstadoReserva.PENDIENTE) {
            // Le sumamos 24 horas a la fecha de creación para hallar la hora límite
            LocalDateTime fechaLimite = this.fechaHoraCreacion.plusHours(24);

            // Si la hora actual ya es después de la límite, la reserva venció
            return fechaHoraActual.isAfter(fechaLimite);
        }
        return false;
    }

    // Permite agregar un ocupante a la reserva
    public boolean agregarOcupante(Ocupante ocupante) {
        if (ocupante != null) {
            this.ocupantes.add(ocupante);
            return true;
        }
        return false;
    }

    public String getCodigoFolio() { return codigoFolio; }
    public void setCodigoFolio(String codigoFolio) { this.codigoFolio = codigoFolio; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public Canal getCanal() { return canal; }
    public void setCanal(Canal canal) { this.canal = canal; }

    public EstadoReserva getEstado() { return estado; }

    public String getCodigoExterno() { return codigoExterno; }
    public void setCodigoExterno(String codigoExterno) { this.codigoExterno = codigoExterno; }

    public double getComisionPlataforma() { return comisionPlataforma; }

    public long getValorTotal() { return valorTotal; }

    public LocalDateTime getFechaHoraCreacion() { return fechaHoraCreacion; }

    public Huesped getTitular() { return titular; }
    public void setTitular(Huesped titular) { this.titular = titular; }

    public Apartamento getApartamento() { return apartamento; }
    public void setApartamento(Apartamento apartamento) { this.apartamento = apartamento; }

    public Folio getFolio() { return folio; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public List<Ocupante> getOcupantes() { return ocupantes; }
    public List<DetalleNocheReserva> getDetallesReserva() { return detallesReserva; }

    @Override
    public String toString() {
        return "Reserva " + codigoFolio + " - " + estado + " (" + fechaEntrada + " a " + fechaSalida + ")";
    }
}