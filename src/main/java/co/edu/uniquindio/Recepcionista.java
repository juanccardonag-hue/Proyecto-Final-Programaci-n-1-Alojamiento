package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Recepcionista extends Empleado {

    public Recepcionista(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasena) {
        super(cedula, nombre, fechaNacimiento, telefono, correo, usuario, contrasena);
    }
    //Registro de reservas por Canal DIRECTO
    public Reserva registrarReservaDirecta(String idFolio, Huesped titular, Apartamento apto, LocalDate entrada, LocalDate salida, List<Ocupante> ocupantes, List<Temporada> temporadas) {

        if (apto == null || titular == null) {
            return null;
        }
        // Validación de capacidad máxima
        if (ocupantes.size() > apto.getCapaMaxPersona()) {
            return null;
        }
        // Validación de estado operativo
        if (apto.getEstadoOperativo() == EstadoOperativo.FUERA_SERVICIO) {
            return null;
        }

        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.DIRECTA, titular, apto, this, null);
        for (int i = 0; i < ocupantes.size(); i++) {
            reserva.agregarOcupante(ocupantes.get(i));
        }
        reserva.liquidarEstancia(temporadas);

        titular.agregarReservaHistorial(reserva);
        return reserva;
    }

    // Recepción de reservas del Canal EXTERNO
    public Reserva registrarReservaExterna(String idFolio, Huesped titular, Apartamento apto, LocalDate entrada, LocalDate salida, List<Ocupante> ocupantes, List<Temporada> temporadas, String codigoExterno) {

        if (apto == null || titular == null || codigoExterno == null) {
            return null;
        }

        if (codigoExterno.isEmpty() || ocupantes.size() > apto.getCapaMaxPersona()) {
            return null;
        }

        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.EXTERNA, titular, apto, this, codigoExterno);
        for (int i = 0; i < ocupantes.size(); i++) {
            reserva.agregarOcupante(ocupantes.get(i));
        }
        reserva.liquidarEstancia(temporadas);

        titular.agregarReservaHistorial(reserva);
        return reserva;
    }

    //Confirmación de reservas Pendientes
    public boolean confirmarReserva(Reserva reserva) {
        if (reserva == null) {
            return false;
        }

        // Si viene del PORTAL, exige haber pagado mínimo el 30% de anticipo
        if (reserva.getCanal() == Canal.PORTAL) {
            long anticipoRequerido = (long)(reserva.getValorTotal() * 0.30);
            long totalPagado = reserva.getFolio().calcularTotalPagos();

            if (totalPagado < anticipoRequerido) {
                return false;
            }
        }
        return reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
    }

    //Registro de llegada (Check-In)
    public boolean realizarCheckIn(Reserva reserva) {
        if (reserva == null) {
            return false;
        }

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            return false;
        }
        if (LocalDate.now().isBefore(reserva.getFechaEntrada())) {
            return false; // Aún no es la fecha de llegada
        }
        if (reserva.getApartamento().getEstadoOperativo() != EstadoOperativo.PREPARADO) {
            return false; // El apartamento no está listo
        }
        boolean exito = reserva.cambiarEstado(EstadoReserva.EN_CURSO);
        if (exito) {
            // El apartamento pasa a estar OCUPADO
            reserva.getApartamento().cambiarEstado(EstadoOperativo.OCUPADO, null);
        }
        return exito;
    }

    //Registro de salida (Check-Out)
    public boolean realizarCheckOut(Reserva reserva) {
        if (reserva == null) {
            return false;
        }
        if (reserva.getEstado() != EstadoReserva.EN_CURSO) {
            return false;
        }
        boolean exito = reserva.cambiarEstado(EstadoReserva.FINALIZADA);
        if (exito) {
            // El apartamento pasa a PENDIENTE DE PREPARACIÓN
            reserva.getApartamento().cambiarEstado(EstadoOperativo.PENDIENTE_PREPARACION, null);
        }
        return exito;
    }

    //Declarar No-Show
    public boolean declararNoShow(Reserva reserva) {
        if (reserva == null) {
            return false;
        }
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            return false;
        }
        return reserva.cambiarEstado(EstadoReserva.NO_SHOW);
    }

    //Registro de pagos parciales
    public boolean registrarPagoEnFolio(Reserva reserva, long monto, MedioPago medioPago) {
        if (reserva == null || reserva.getFolio() == null || monto <= 0) {
            return false;
        }
        reserva.getFolio().registrarPago(monto, medioPago);
        return true;
    }

    //Registro de cargos adicionales (desayuno, lavandería, etc.)
    public boolean registrarCargoEnFolio(Reserva reserva, String concepto, long monto) {
        if (reserva == null || reserva.getFolio() == null || monto <= 0) {
            return false;
        }
        reserva.getFolio().agregarCargo(concepto, monto);
        return true;
    }

    //Cambio de estado de un apartamento
    public void cambiarEstadoApartamento(Apartamento apartamento, EstadoOperativo nuevoEstado, String motivo) {
        if (apartamento != null) {
            apartamento.cambiarEstado(nuevoEstado, motivo);
        }
    }

    //Registro de novedades (daños o reportes en el apartamento)
    public void registrarNovedadApartamento(Apartamento apartamento, String descripcion) {
        if (apartamento != null) {
            apartamento.registrarNovedad(descripcion, getNombre());
        }
    }
    //Canal EXTERNO con validación de conflicto de canal
    public Reserva registrarReservaExterna(String idFolio, Huesped titular, Apartamento apto, LocalDate entrada, LocalDate salida, List<Ocupante> ocupantes, List<Temporada> temporadas, String codigoExterno, List<Reserva> reservasExistentes) {

        if (apto == null || titular == null || codigoExterno == null || reservasExistentes == null) {
            return null;
        }
        if (ocupantes.size() > apto.getCapaMaxPersona()) {
            return null;
        }
        if (apto.getEstadoOperativo() == EstadoOperativo.FUERA_SERVICIO) {
            return null;
        }

        //Un mismo código externo no puede crear dos reservas
        for (int i = 0; i < reservasExistentes.size(); i++) {
            Reserva reser = reservasExistentes.get(i);
            if (reser.getCodigoExterno() != null && reser.getCodigoExterno().equals(codigoExterno)) {
                JOptionPane.showMessageDialog(null,"RECHAZADA: El código externo " + codigoExterno + " ya fue registrado.");
                return null;
            }
        }

        //Conflicto de canal (Anti doble venta para reservas externas)
        for (int i = 0; i < reservasExistentes.size(); i++) {
            Reserva reser = reservasExistentes.get(i);

            // Si es para el mismo apartamento y está activa
            if (reser.getApartamento().getCodigo().equals(apto.getCodigo()) && reser.estaActiva()) {

                if (reser.cruceCon(entrada, salida)) {
                    JOptionPane.showMessageDialog(null,"CONFLICTO DE CANAL: La reserva externa (" + codigoExterno +
                            ") choca en fechas con la reserva existente " + reser.getCodigoFolio());
                    return null; // Se rechaza y NUNCA reemplaza la reserva existente
                }
            }
        }
        //Si pasa todas las validaciones, se crea la reserva normalmente
        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.EXTERNA, titular, apto, null, codigoExterno);

        for (int i = 0; i < ocupantes.size(); i++) {
            reserva.agregarOcupante(ocupantes.get(i));
        }
        reserva.liquidarEstancia(temporadas);
        titular.agregarReservaHistorial(reserva);

        return reserva;
    }
}
