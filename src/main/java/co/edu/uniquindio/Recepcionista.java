package co.edu.uniquindio;

import java.time.LocalDate;
import java.util.List;

public class Recepcionista extends Empleado {

    public Recepcionista(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo, usuario, contrasenia);
    }

    // Autenticación de Recepcionista
    public static Recepcionista autenticarRecepcionista(List<Recepcionista> listaRecepcionistas, String identificador, String contrasenia) {
        if (listaRecepcionistas == null || identificador == null || contrasenia == null) {
            return null;
        }
        String idLimpio = identificador.trim();
        String claveLimpia = contrasenia.trim();

        for (Recepcionista r : listaRecepcionistas) {
            boolean coincideId = (r.getUsuario() != null && r.getUsuario().equalsIgnoreCase(idLimpio))
                    || (r.getCedula() != null && r.getCedula().equalsIgnoreCase(idLimpio))
                    || (r.getCorreo() != null && r.getCorreo().equalsIgnoreCase(idLimpio));

            if (coincideId && r.getContrasenia() != null && r.getContrasenia().equals(claveLimpia)) {
                return r;
            }
        }
        return null;
    }

    // Reserva Directa
    public Reserva registrarReservaDirecta(Huesped titular, Apartamento apto, LocalDate entrada, LocalDate salida, List<Ocupante> ocupantes, List<Reserva> listaReservas, List<Temporada> listaTemporadas) {
        if (titular == null || apto == null || entrada == null || salida == null || listaReservas == null) {
            return null;
        }
        if (ocupantes != null && ocupantes.size() > apto.getCapaMaxPersona()) {
            return null;
        }
        if (!entrada.isBefore(salida) || apto.estaBloqueadoRango(entrada, salida)) {
            return null;
        }
        for (Reserva reser : listaReservas) {
            if (reser.getApartamento() != null &&
                    reser.getApartamento().getCodigo().equalsIgnoreCase(apto.getCodigo()) &&
                    reser.estaActiva() &&
                    reser.cruceCon(entrada, salida)) {
                return null;
            }
        }

        String idFolio = "FOL-" + (listaReservas.size() + 1);
        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.DIRECTA, titular, apto, this, null);

        if (ocupantes != null) {
            for (Ocupante oc : ocupantes) {
                reserva.agregarOcupante(oc);
            }
        }

        reserva.liquidarEstancia(listaTemporadas);
        titular.agregarReservaHistorial(reserva);
        listaReservas.add(reserva);
        return reserva;
    }

    // Reserva Externa 
    public static Reserva registrarReservaExterna(String codExterno, Huesped titular, Apartamento apto, LocalDate entrada, LocalDate salida, List<Ocupante> ocupantes, Recepcionista recepcionista, List<Reserva> listaReservas, List<Temporada> listaTemporadas) {
        if (codExterno == null || codExterno.trim().isEmpty() || titular == null || apto == null || entrada == null || salida == null || listaReservas == null) {
            return null;
        }
        if (ocupantes != null && ocupantes.size() > apto.getCapaMaxPersona()) {
            return null;
        }
        if (!entrada.isBefore(salida) || apto.estaBloqueadoRango(entrada, salida)) {
            return null;
        }
        for (Reserva reser : listaReservas) {
            if (reser.getCodigoExterno() != null && reser.getCodigoExterno().equalsIgnoreCase(codExterno.trim())) {
                return null;
            }
            if (reser.getApartamento() != null &&
                    reser.getApartamento().getCodigo().equalsIgnoreCase(apto.getCodigo()) &&
                    reser.estaActiva() &&
                    reser.cruceCon(entrada, salida)) {
                return null;
            }
        }

        String idFolio = "FOL-EXT-" + (listaReservas.size() + 1);
        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.EXTERNA, titular, apto, recepcionista, codExterno.trim());

        if (ocupantes != null) {
            for (Ocupante oc : ocupantes) {
                reserva.agregarOcupante(oc);
            }
        }

        reserva.liquidarEstancia(listaTemporadas);
        titular.agregarReservaHistorial(reserva);
        listaReservas.add(reserva);
        return reserva;
    }

    // Confirmación de Reserva
    public String confirmarReserva(Reserva reserva) {
        if (reserva == null) {
            return "Reserva no válida.";
        }
        return reserva.confirmar();
    }

    // Check-In y Check-Out
    public String realizarCheckIn(Reserva reserva) {
        if (reserva == null) {
            return "Reserva no válida.";
        }
        return reserva.procesarCheckIn();
    }

    public String realizarCheckOut(Reserva reserva) {
        if (reserva == null) {
            return "Reserva no válida.";
        }
        return reserva.procesarCheckOut();
    }

    // Gestión de Folio
    public static String registrarPagoFolio(Reserva reserva, long monto, MedioPago medioPago) {
        if (reserva == null || reserva.getFolio() == null) {
            return "Reserva o folio no encontrado.";
        }
        if (!reserva.estaActiva()) {
            return "No se pueden registrar pagos a una reserva en estado " + reserva.getEstado() + ".";
        }
        if (monto <= 0) {
            return "El monto a abonar debe ser mayor a cero.";
        }
        if (monto > (long) reserva.getSaldoFolio()) {
            return "El monto supera el saldo pendiente del folio ($" + String.format("%,d", (long) reserva.getSaldoFolio()) + ").";
        }
        if (reserva.registrarAbono(monto, medioPago)) {
            return "OK";
        }
        return "No se pudo registrar el pago en el folio.";
    }

    public static String registrarCargoAdicionalFolio(Reserva reserva, String concepto, long monto) {
        if (reserva == null || reserva.getFolio() == null) {
            return "Reserva o folio no encontrado.";
        }
        if (!reserva.estaActiva()) {
            return "No se pueden agregar cargos a una reserva en estado " + reserva.getEstado() + ".";
        }
        if (concepto == null || concepto.trim().isEmpty()) {
            return "El concepto del cargo es obligatorio.";
        }
        if (monto <= 0) {
            return "El monto del cargo debe ser mayor a cero.";
        }
        reserva.getFolio().agregarCargo(concepto.trim(), monto);
        return "OK";
    }

    // Control Operativo y Novedades de Apartamentos
    public String cambiarEstadoApartamento(Apartamento apto, EstadoOperativo nuevoEstado, String motivo) {
        if (apto == null) {
            return "Apartamento no válido.";
        }
        return apto.cambiarEstadoManual(nuevoEstado, motivo);
    }

    public String registrarNovedadApartamento(Apartamento apto, String descripcion) {
        if (apto == null) {
            return "Apartamento no válido.";
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción de la novedad es obligatoria.";
        }
        apto.registrarNovedad(descripcion.trim(), getNombre());
        return "OK";
    }

    // --- Declarar No-Show ---
    public String declararNoShow(Reserva reserva) {
        if (reserva == null) {
            return "Reserva no válida.";
        }
        return reserva.declararNoShow();
    }

    // --- Búsquedas reutilizables para las vistas de Recepción ---
    public static Reserva buscarReservaPorFolio(List<Reserva> listaReservas, String codigo) {
        if (listaReservas != null && codigo != null) {
            for (Reserva r : listaReservas) {
                if (r.getCodigoFolio() != null && r.getCodigoFolio().equalsIgnoreCase(codigo.trim())) {
                    return r;
                }
            }
        }
        return null;
    }

    public static Huesped buscarHuespedPorCedula(List<Huesped> listaHuespedes, String cedula) {
        if (listaHuespedes != null && cedula != null) {
            for (Huesped h : listaHuespedes) {
                if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedula.trim())) {
                    return h;
                }
            }
        }
        return null;
    }

    public static Apartamento buscarApartamentoPorCodigo(List<Apartamento> listaApartamentos, String codigo) {
        if (listaApartamentos != null && codigo != null) {
            for (Apartamento a : listaApartamentos) {
                if (a.getCodigo() != null && a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                    return a;
                }
            }
        }
        return null;
    }
}