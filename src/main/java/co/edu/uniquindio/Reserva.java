package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

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
    private Empleado empleado;
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
        this.folio = new Folio(codigoFolio);
        this.ocupantes = new ArrayList<>();
        this.detallesReserva = new ArrayList<>();
    }

    // Constructor para reservas creadas por el huésped desde el portal
    public Reserva(String codigoFolio, LocalDate fechaEntrada, LocalDate fechaSalida, Canal canal, Huesped titular, Apartamento apartamento, long valorTotal) {
        this(codigoFolio, fechaEntrada, fechaSalida, canal, titular, apartamento, null, null);
        this.valorTotal = valorTotal;
    }

    // Calculamos el número de noches de la estancia
    public long calcularNoches() {
        if (fechaEntrada == null || fechaSalida == null) {
            return 0;
        }
        long noches = 0;
        LocalDate fechaAuxiliar = fechaEntrada;

        while (fechaAuxiliar.isBefore(fechaSalida)) {
            noches++;
            fechaAuxiliar = fechaAuxiliar.plusDays(1);
        }
        return noches;
    }

    // Cuenta cuántos ocupantes pagan tarifa (exonerando a menores de 6 años)
    public int contarOcupantesPagantes() {
        int contadorPagantes = 0;
        for (int i = 0; i < ocupantes.size(); i++) {
            Ocupante ocup = ocupantes.get(i);
            if (ocup.esPagante(fechaEntrada)) {
                contadorPagantes++;
            }
        }
        // Si aún no se han registrado ocupantes en la lista, se asume mínimo 1 pagante
        return Math.max(1, contadorPagantes);
    }

    // Busca si una fecha cae en alguna temporada de recargo
    private Temporada buscarTemporada(LocalDate fecha, List<Temporada> temporadas) {
        if (temporadas == null) return null;
        for (int i = 0; i < temporadas.size(); i++) {
            Temporada t = temporadas.get(i);
            if (t.aplicaEnFecha(fecha)) {
                return t;
            }
        }
        return null;
    }

    // Liquida el costo estancia noche a noche y lo registra en el Folio
    public void liquidarEstancia(List<Temporada> temporadas) {
        detallesReserva.clear();
        int pagantes = contarOcupantesPagantes();
        long totalEstancia = 0;
        LocalDate fechaAuxiliar = fechaEntrada;

        while (fechaAuxiliar.isBefore(fechaSalida)) {
            Temporada temp = buscarTemporada(fechaAuxiliar, temporadas);
            DetalleNocheReserva detalle = new DetalleNocheReserva(fechaAuxiliar, pagantes, apartamento.getTarifaPersonaNoche(), temp);

            detallesReserva.add(detalle);
            totalEstancia += detalle.getSubtotal();

            fechaAuxiliar = fechaAuxiliar.plusDays(1);
        }

        this.valorTotal = totalEstancia;
        if (this.folio != null) {
            this.folio.getListaCargo().clear();
            this.folio.agregarCargo("Alojamiento estancia (" + calcularNoches() + " noches)", valorTotal);
        }
    }

    // Cambia el estado de la reserva siguiendo
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
        return false;
    }

    // Verifica si la reserva está activa
    public boolean estaActiva() {
        return (this.estado == EstadoReserva.PENDIENTE ||
                this.estado == EstadoReserva.CONFIRMADA ||
                this.estado == EstadoReserva.EN_CURSO);
    }

    // Anti Doble Venta: comprueba solapamiento de fechas
    public boolean cruceCon(LocalDate entradaBuscar, LocalDate salidaBuscar) {
        return (entradaBuscar.isBefore(this.fechaSalida) && salidaBuscar.isAfter(this.fechaEntrada));
    }

    // Revisa si pasaron 24 horas desde la creación sin confirmarse
    public boolean estaVencida(LocalDateTime fechaHoraActual) {
        if (this.estado == EstadoReserva.PENDIENTE) {
            LocalDateTime fechaLimite = this.fechaHoraCreacion.plusHours(24);
            return fechaHoraActual.isAfter(fechaLimite);
        }
        return false;
    }

    // Agrega un ocupante a la reserva
    public boolean agregarOcupante(Ocupante ocupante) {
        if (ocupante != null) {
            this.ocupantes.add(ocupante);
            return true;
        }
        return false;
    }

    // Calcula el monto de devolución según políticas y días de antelación
    public long calcularDevolucionCancelacion(LocalDate fechaCancelacion) {
        if (fechaCancelacion == null || fechaCancelacion.isAfter(this.fechaEntrada)) {
            return 0;
        }

        long diasAntelacion = 0;
        LocalDate fechaAuxiliar = fechaCancelacion;
        while (fechaAuxiliar.isBefore(this.fechaEntrada)) {
            diasAntelacion++;
            fechaAuxiliar = fechaAuxiliar.plusDays(1);
        }

        long totalPagado = (long) getTotalAbonado();

        // Más de 7 días: devolución del 100% de lo pagado
        if (diasAntelacion > 7) {
            return totalPagado;
        }

        // Entre 2 y 7 días: retención del 50% del valor de la estancia
        if (diasAntelacion >= 2) {
            long retencion = getValorTotal() / 2;
            long devolucion = totalPagado - retencion;
            return Math.max(0, devolucion);
        }

        // Menos de 48 horas: retención del 100% del valor de la estancia
        long devolucion = totalPagado - getValorTotal();
        return Math.max(0, devolucion);
    }

    // Registra un abono y confirma la reserva si alcanza o supera el 30% del total
    public boolean registrarAbono(long monto, MedioPago medioPago) {
        if (monto > 0 && this.folio != null) {
            this.folio.registrarPago(monto, medioPago);

            if (this.estado == EstadoReserva.PENDIENTE && getTotalAbonado() >= (getValorTotal() * 0.30)) {
                cambiarEstado(EstadoReserva.CONFIRMADA);
            }
            return true;
        }
        return false;
    }
    // Retorna el total del Folio (estancia + cargos adicionales)
    public long getValorTotal() {
        if (this.folio != null && this.folio.calcularTotalCargos() > 0) {
            return (long) this.folio.calcularTotalCargos();
        }
        return this.valorTotal;
    }
    public double getTotalAbonado() {if (this.folio != null) {return this.folio.calcularTotalPagos();}return 0.0;}
    public double getSaldoFolio() {return (double) getValorTotal() - getTotalAbonado();}

    // Getters y Setters Estándar
    public String getCodigoReserva() { return this.codigoFolio; }
    public LocalDate getFechaInicio() { return this.fechaEntrada; }
    public LocalDate getFechaFin() { return this.fechaSalida; }
    public String getCodigoFolio() { return codigoFolio; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public Canal getCanal() { return canal; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva nuevoEstado) { this.estado = nuevoEstado; }
    public String getCodigoExterno() { return codigoExterno; }
    public void setCodigoExterno(String codigoExterno) { this.codigoExterno = codigoExterno; }
    public double getComisionPlataforma() { return comisionPlataforma; }
    public LocalDateTime getFechaHoraCreacion() { return fechaHoraCreacion; }
    public Huesped getTitular() { return titular; }
    public void setTitular(Huesped titular) { this.titular = titular; }
    public Apartamento getApartamento() { return apartamento; }
    public void setApartamento(Apartamento apartamento) { this.apartamento = apartamento; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public Folio getFolio() { return folio; }
    public List<Ocupante> getOcupantes() { return ocupantes; }
    public List<DetalleNocheReserva> getDetallesReserva() { return detallesReserva; }
}