package co.edu.uniquindio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Huesped extends Persona implements Serializable {

    private List<Reserva> misReservas;

    public Huesped(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        this.misReservas = new ArrayList<>();
    }

    // Actualiza los datos de perfil del huésped
    public void actualizarPerfil(String nuevoNombre, LocalDate nuevaFecha, String nuevoTelefono, String nuevoCorreo) {
        setNombre(nuevoNombre);
        setFechaNacimiento(nuevaFecha);
        setTelefono(nuevoTelefono);
        setCorreo(nuevoCorreo);
    }

    // Agrega una reserva al historial evitando duplicados
    public boolean agregarReservaHistorial(Reserva reserva) {
        if (reserva != null && !misReservas.contains(reserva)) {
            this.misReservas.add(reserva);
            return true;
        }
        return false;
    }
    // consultar Apartamentos disponibles
    public List<Apartamento> consultarApartamentosDisponibles(List<Apartamento> listaGeneral, LocalDate inicio, LocalDate fin, int personas) {
        List<Apartamento> disponibles = new ArrayList<>();

        for (Apartamento apto : listaGeneral) {
            if (apto.getCapacidadMaxima() >= personas) {
                // Aqui se evrifica que no se cruce con otras
                boolean libre = true;
                for (Reserva r : misReservas) {
                    if (r.getApartamento().getCodigo().equals(apto.getCodigo()) && !r.getEstado().equals(" Cancelada ")) {
                        boolean seCruza = !(fin.isBefore(r.getFechaEntrada()) || fin.isEqual(r.getFechaEntrada()) ||
                                inicio.isAfter(r.getFechaSalida()) || inicio.isEqual(r.getFechaSalida()));
                        if (seCruza) {
                            libre = false;
                        }
                    }
                }
                if (libre) {
                    disponibles.add(apto);
                }
            }
        }
        return disponibles;
    }

    // cotizar Estancia con desglose noche por noche y temporada
    public String cotizarEstancia(Apartamento apto, LocalDate inicio, LocalDate fin) {
        String reporte = "=== COTIZACIÓN DE ESTANCIA ===\nApartamento: " + apto.getCodigo() + "\n";
        double total = 0.0;
        LocalDate fechaActual = inicio;
        int noche = 1;

        while (fechaActual.isBefore(fin)) {
            double precioNoche;
            String temporada;

            // Meses 6, 7, 12 y 1 se consideran Temporada Alta
            int mes = fechaActual.getMonthValue();
            if (mes == 6 || mes == 7 || mes == 12 || mes == 1) {
                precioNoche = apto.getPrecioAlta();
                temporada = "ALTA";
            } else {
                precioNoche = apto.getPrecioBaja();
                temporada = "BAJA";
            }

            reporte += "Noche " + noche + " (" + fechaActual + " - " + temporada + "): $" + precioNoche + "\n";
            total += precioNoche;
            fechaActual = fechaActual.plusDays(1);
            noche++;
        }

        reporte += "-----------------------------------\nVALOR TOTAL: $" + total;
        return reporte;
    }

    // crear Reserva por el Portal
    public Reserva crearReservaPortal(String codigoReserva, Apartamento apto, LocalDate inicio, LocalDate fin, List<Ocupante> ocupantes) {
        // Calcular total de la estancia
        double total = 0.0;
        LocalDate f = inicio;
        while (f.isBefore(fin)) {
            int mes = f.getMonthValue();
            if (mes == 6 || mes == 7 || mes == 12 || mes == 1) {
                total += apto.getPrecioAlta();
            } else {
                total += apto.getPrecioBaja();
            }
            f = f.plusDays(1);
        }

        // Crear la reserva con el Portal
        Reserva nueva = new Reserva(codigoReserva, this, apto, inicio, fin, "Portal", total);

        // Registrar los ocupantes con su fecha de nacimiento
        for (Ocupante o : ocupantes) {
            nueva.agregarOcupante(o);
        }

        // Agregar al historial de reservas del huésped
        agregarReservaHistorial(nueva);
        return nueva;
    }

    // 4. PAGO DE ANTICIPO Y ABONOS AL FOLIO
    public boolean realizarAbono(String codigoReserva, double monto) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                r.registrarAbono(monto);
                return true;
            }
        }
        return false;
    }

    // 5. CONSULTA DE MIS RESERVAS, ESTADO Y SALDO DEL FOLIO
    public String consultarEstadoReservasYFolio() {
        if (misReservas.isEmpty()) {
            return "El huésped no tiene reservas registradas.";
        }

        String resultado = "=== MIS RESERVAS ===\n";
        for (Reserva r : misReservas) {
            resultado += "Código: " + r.getCodigoReserva() + "\n" +
                    "Apartamento: " + r.getApartamento().getCodigo() + "\n" +
                    "Canal: " + r.getCanal() + "\n" +
                    "Fechas: " + r.getFechaInicio() + " a " + r.getFechaFin() + "\n" +
                    "Estado: " + r.getEstado() + "\n" +
                    "Valor Total: $" + r.getValorTotal() + "\n" +
                    "Abonado: $" + r.getTotalAbonado() + "\n" +
                    "Saldo Folio: $" + r.getSaldoFolio() + "\n" +
                    "-----------------------------------\n";
        }
        return resultado;
    }

    // 6. CALCULAR DEVOLUCIÓN DE CANCELACIÓN
    public double calcularDevolucion(String codigoReserva) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                LocalDate hoy = LocalDate.now();
                // Si cancela con 5 o más días de anticipación, se devuelve el 100% de lo abonado, sino el 50%
                if (hoy.plusDays(5).isBefore(r.getFechaInicio()) || hoy.plusDays(5).isEqual(r.getFechaInicio())) {
                    return r.getTotalAbonado();
                } else {
                    return r.getTotalAbonado() * 0.50;
                }
            }
        }
        return 0.0;
    }

    // 7. CANCELAR RESERVA PENDIENTE O CONFIRMADA
    public boolean cancelarReserva(String codigoReserva) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                if (r.getEstado().equals("PENDIENTE") || r.getEstado().equals("CONFIRMADA")) {
                    r.setEstado("CANCELADA");
                    return true;
                }
            }
        }
        return false;
    }

    public List<Reserva> getMisReservas() {
        return misReservas;
    }
}