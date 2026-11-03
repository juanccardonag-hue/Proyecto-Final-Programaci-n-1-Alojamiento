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
        String reporte = " ⁺‧₊˚ ཐི⋆Cotizacion de estancia⋆ཋྀ ˚₊‧⁺" + "\n" + "Apartamento:" + apto.getCodigo() + "\n";
        double total = 0.0;
        LocalDate fechaActual = inicio;
        int noche = 1;

        while (fechaActual.isBefore(fin)) {
            double precioNoche;
            String temporada;

            // Meses jun(6), jul(7), dic(12) y ene(1) se consideran Temporada Alta
            int mes = fechaActual.getMonthValue();
            if (mes == 6 || mes == 7 || mes == 12 || mes == 1) {
                precioNoche = apto.getPrecioAlta();
                temporada = "Alta ";
            } else {
                precioNoche = apto.getPrecioBaja();
                temporada = "Baja ";
            }

            reporte += "Noche: " + noche + " (Del: " + fechaActual + " - " + temporada + "): $" + precioNoche + "\n";
            total += precioNoche;
            fechaActual = fechaActual.plusDays(1);
            noche++;
        }

        reporte += "────────────────୨ৎ────────────────" + "\n" + "Valor total: $" + total;
        return reporte;
    }

    // crear Reserva por el Portal
    public Reserva crearReservaPortal(String codigoReserva, Apartamento apto, LocalDate inicio, LocalDate fin, List<Ocupante> ocupantes) {
        // Calcular total de la estancia
        long total = 0;
        LocalDate fecha = inicio;

        while (fecha.isBefore(fin)) { //comparar objetos de tiempo y comprobar si una fecha u hora es estrictamente anterior a otra
            int mes = fecha.getMonthValue();
            if (mes == 6 || mes == 7 || mes == 12 || mes == 1) {
                total += apto.getPrecioAlta();
            } else {
                total += apto.getPrecioBaja();
            }
            fecha = fecha.plusDays(1); //Se suma un dia a la variable que llame fecha
        }

        // Crear la reserva con el Portal
        Reserva nueva = new Reserva(codigoReserva, inicio, fin, Canal.PORTAL, this, apto, null, null);
        // Registrar los ocupantes con su fecha de nacimiento
        for (Ocupante o : ocupantes) {
            nueva.agregarOcupante(o);
        }

        // Agregar al historial de reservas del huésped
        agregarReservaHistorial(nueva);
        return nueva;
    }

    // Pago de anticipo y abaonos
    public boolean realizarAbono(String codigoReserva, double monto) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                r.registrarAbono(monto);
                return true;
            }
        }
        return false;
    }

    // Consulta de mis reservas, su estado y el saldo del folio
    public String consultarEstadoReservasYFolio() {
        if (misReservas.isEmpty()) {
            return "El huésped no tiene reservas registradas ";
        }

        String resultado = " ‿︵‿︵₊୨ Mis reservas ୧₊‿︵‿︵\n";
        for (Reserva r : misReservas) {
            resultado += "Código: " + r.getCodigoReserva() + "\n" +
                    "Apartamento: " + r.getApartamento().getCodigo() + "\n" +
                    "Canal: " + r.getCanal() + "\n" +
                    "Fechas: " + r.getFechaInicio() + " a " + r.getFechaFin() + "\n" +
                    "Estado: " + r.getEstado() + "\n" +
                    "Valor Total: $" + r.getValorTotal() + "\n" +
                    "Abonado: $" + r.getTotalAbonado() + "\n" +
                    "Saldo Folio: $" + r.getSaldoFolio() + "\n" +
                    " ﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌ \n";
        }
        return resultado;
    }

    // Calcular devolucionde cancelacion
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

    // Cancelar oendiente o confirmada
    public boolean cancelarReserva(String codigoReserva) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                if (r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.CONFIRMADA) {
                    r.setEstado(EstadoReserva.CANCELADA);
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