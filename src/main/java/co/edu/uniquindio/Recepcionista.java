package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Recepcionista extends Empleado {

    public Recepcionista(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo, usuario, contrasenia);
    }

    public static Recepcionista loginRecepcionista(List<Recepcionista> listaRecepcionistas) {
        String usuarioInput = JOptionPane.showInputDialog("Ingresa tu usuario de recepcionista:");
        if (usuarioInput == null || usuarioInput.trim().isEmpty()) {
            return null;
        }

        String claveInput = JOptionPane.showInputDialog("Ingresa tu contraseña:");
        if (claveInput == null || claveInput.trim().isEmpty()) {
            return null;
        }

        if (listaRecepcionistas != null) {
            for (Recepcionista r : listaRecepcionistas) {
                if (r.getUsuario() != null && r.getUsuario().equals(usuarioInput.trim()) &&
                        r.getContrasenia() != null && r.getContrasenia().equals(claveInput.trim())) {

                    JOptionPane.showMessageDialog(null, "¡Bienvenido/a Recepcionista " + r.getNombre() + "! ˶˃ᵕ˂˶");
                    return r;
                }
            }
        }

        JOptionPane.showMessageDialog(null,
                "⚠ Acceso denegado ⚠ Usuario o contraseña incorrectos.\nEl administrador debe registrarte antes.",
                "⚠ Error de Autenticación ⚠",
                JOptionPane.ERROR_MESSAGE);
        return null;
    }

    // registro de reservas por canal directo
    public void registrarReservaDirecta(List<Huesped> listaHuespedes, List<Apartamento> listaApartamentos, List<Reserva> listaReservas, List<Temporada> listaTemporadas) {
        String cedulaTitular = JOptionPane.showInputDialog("Cédula del huésped titular:");
        if (cedulaTitular == null || cedulaTitular.trim().isEmpty()) return;

        Huesped titular = buscarHuesped(listaHuespedes, cedulaTitular.trim());
        if (titular == null) {
            JOptionPane.showMessageDialog(null, "⚠ El huésped no está registrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codApto = JOptionPane.showInputDialog("Código del apartamento:");
        if (codApto == null || codApto.trim().isEmpty()) return;

        Apartamento apto = buscarApartamento(listaApartamentos, codApto.trim());
        if (apto == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún apartamento con ese código.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (apto.getEstadoOperativo() == EstadoOperativo.FUERA_SERVICIO) {
            JOptionPane.showMessageDialog(null, "⚠ El apartamento está fuera de servicio.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fEntrada = JOptionPane.showInputDialog("Fecha de entrada (AAAA-MM-DD):");
        String fSalida = JOptionPane.showInputDialog("Fecha de salida (AAAA-MM-DD):");
        if (fEntrada == null || fSalida == null || fEntrada.trim().isEmpty() || fSalida.trim().isEmpty()) return;

        LocalDate entrada = LocalDate.parse(fEntrada.trim());
        LocalDate salida = LocalDate.parse(fSalida.trim());
        String idFolio = "FOL-" + (listaReservas.size() + 1);

        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.DIRECTA, titular, apto, this, null);
        reserva.liquidarEstancia(listaTemporadas);

        titular.agregarReservaHistorial(reserva);
        listaReservas.add(reserva);
        JOptionPane.showMessageDialog(null, "¡Reserva directa registrada exitosamente! \n Folio: " + idFolio);
    }

    // resgitro de reservas por canal externo
    public void registrarReservaExterna(List<Huesped> listaHuespedes, List<Apartamento> listaApartamentos, List<Reserva> listaReservas, List<Temporada> listaTemporadas) {
        String codExterno = JOptionPane.showInputDialog("Código de la plataforma externa: ");
        if (codExterno == null || codExterno.trim().isEmpty()) return;

        String cedulaTitular = JOptionPane.showInputDialog("Cédula del huésped titular:");
        if (cedulaTitular == null || cedulaTitular.trim().isEmpty()) return;

        Huesped titular = buscarHuesped(listaHuespedes, cedulaTitular.trim());
        if (titular == null) {
            JOptionPane.showMessageDialog(null, "⚠ El huésped no está registrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codApto = JOptionPane.showInputDialog("Código del apartamento:");
        if (codApto == null || codApto.trim().isEmpty()) return;

        Apartamento apto = buscarApartamento(listaApartamentos, codApto.trim());
        if (apto == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró el apartamento.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (apto.getEstadoOperativo() == EstadoOperativo.FUERA_SERVICIO) {
            JOptionPane.showMessageDialog(null, "⚠ El apartamento está fuera de servicio.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Reserva reser : listaReservas) {
            if (reser.getCodigoExterno() != null && reser.getCodigoExterno().equals(codExterno.trim())) {
                JOptionPane.showMessageDialog(null, "RECHAZADA: El código externo " + codExterno + " ya fue registrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String fEntrada = JOptionPane.showInputDialog("Fecha de entrada (AAAA-MM-DD):");
        String fSalida = JOptionPane.showInputDialog("Fecha de salida (AAAA-MM-DD):");
        if (fEntrada == null || fSalida == null || fEntrada.trim().isEmpty() || fSalida.trim().isEmpty()) return;

        LocalDate entrada = LocalDate.parse(fEntrada.trim());
        LocalDate salida = LocalDate.parse(fSalida.trim());

        for (Reserva reser : listaReservas) {
            if (reser.getApartamento().getCodigo().equals(apto.getCodigo()) && reser.estaActiva()) {
                if (reser.cruceCon(entrada, salida)) {
                    JOptionPane.showMessageDialog(null, "La reserva externa (" + codExterno + ") se cruza en fechas con la reserva existente " + reser.getCodigoFolio(), "⚠ Error ⚠", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        String idFolio = "FOL-EXT-" + (listaReservas.size() + 1);
        Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.EXTERNA, titular, apto, null, codExterno.trim());
        reserva.liquidarEstancia(listaTemporadas);

        titular.agregarReservaHistorial(reserva);
        listaReservas.add(reserva);
        JOptionPane.showMessageDialog(null, "¡Reserva externa registrada exitosamente! \n Folio: " + idFolio);
    }

    // confirmaciom de reservas Pendientes
    public void confirmarReserva(List<Reserva> listaReservas) {
        String codigo = JOptionPane.showInputDialog("Ingresa el código de la reserva a confirmar:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        Reserva reserva = buscarReserva(listaReservas, codigo.trim());
        if (reserva == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró ninguna reserva con ese código.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.getCanal() == Canal.PORTAL) {
            long anticipoRequerido = (long)(reserva.getValorTotal() * 0.30);
            long totalPagado = reserva.getFolio().calcularTotalPagos();

            if (totalPagado < anticipoRequerido) {
                JOptionPane.showMessageDialog(null, "⚠ No se puede confirmar: requiere un pago anticipado mínimo del 30% ($" + anticipoRequerido + ").", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (reserva.cambiarEstado(EstadoReserva.CONFIRMADA)) {
            JOptionPane.showMessageDialog(null, "¡Reserva confirmada exitosamente!");
        } else {
            JOptionPane.showMessageDialog(null, "⚠ No se pudo cambiar el estado de la reserva.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    // rgistro de Check-In
    public void realizarCheckIn(List<Reserva> listaReservas) {
        String codigo = JOptionPane.showInputDialog("Ingresa el código/folio para Check-In:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        Reserva reserva = buscarReserva(listaReservas, codigo.trim());
        if (reserva == null) {
            JOptionPane.showMessageDialog(null, "⚠ Reserva no encontrada.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(null, "⚠ La reserva debe estar en estado CONFIRMADA.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (LocalDate.now().isBefore(reserva.getFechaEntrada())) {
            JOptionPane.showMessageDialog(null, "⚠ Aún no es la fecha de llegada de la reserva.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (reserva.getApartamento().getEstadoOperativo() != EstadoOperativo.PREPARADO) {
            JOptionPane.showMessageDialog(null, "⚠ El apartamento aún no está listp aun.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.cambiarEstado(EstadoReserva.EN_CURSO)) {
            reserva.getApartamento().cambiarEstado(EstadoOperativo.OCUPADO, null);
            JOptionPane.showMessageDialog(null, "¡Check-In realizado exitosamente! Apartamento marcado como Ocupado.");
        }
    }

    // registro de Check-Out
    public void realizarCheckOut(List<Reserva> listaReservas) {
        String codigo = JOptionPane.showInputDialog("Ingresa el código/folio para Check-Out:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        Reserva reserva = buscarReserva(listaReservas, codigo.trim());
        if (reserva == null) {
            JOptionPane.showMessageDialog(null, "⚠ Reserva no encontrada.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.getEstado() != EstadoReserva.EN_CURSO) {
            JOptionPane.showMessageDialog(null, "⚠ La reserva debe estar EN CURSO para hacer Check-Out.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.cambiarEstado(EstadoReserva.FINALIZADA)) {
            reserva.getApartamento().cambiarEstado(EstadoOperativo.PENDIENTE_PREPARACION, null);
            JOptionPane.showMessageDialog(null, "¡Check-Out realizado exitosamente! Apartamento pasa a pendiente de preparacion.");
        }
    }

    // registro de pagos y cargos en Folio
    public void gestionarFolio(List<Reserva> listaReservas) {
        String[] opciones = {"Registrar Pago", "Registrar Cargo Adicional", "Volver"};
        int seleccion = JOptionPane.showOptionDialog(null, "¿Qué acción deseas realizar?", "Gestión de Folio",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) {
            String cod = JOptionPane.showInputDialog("Código/Folio de la reserva:");
            if (cod == null || cod.trim().isEmpty()) return;
            Reserva reserva = buscarReserva(listaReservas, cod.trim());
            if (reserva != null && reserva.getFolio() != null) {
                String montoStr = JOptionPane.showInputDialog("Monto a abonar ($):");
                MedioPago[] medios = MedioPago.values();
                MedioPago medio = (MedioPago) JOptionPane.showInputDialog(null, "Selecciona medio de pago:", "Medios de Pago",
                        JOptionPane.QUESTION_MESSAGE, null, medios, medios[0]);
                if (montoStr != null && medio != null && !montoStr.trim().isEmpty()) {
                    long monto = Long.parseLong(montoStr.trim());
                    if (monto > 0) {
                        reserva.getFolio().registrarPago(monto, medio);
                        JOptionPane.showMessageDialog(null, "¡Pago registrado exitosamente!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "⚠ Reserva o folio no encontrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        } else if (seleccion == 1) {
            String cod = JOptionPane.showInputDialog("Código/Folio de la reserva:");
            if (cod == null || cod.trim().isEmpty()) return;
            Reserva reserva = buscarReserva(listaReservas, cod.trim());
            if (reserva != null && reserva.getFolio() != null) {
                String concepto = JOptionPane.showInputDialog("Concepto del cargo:");
                String montoStr = JOptionPane.showInputDialog("Monto del cargo ($):");
                if (concepto != null && montoStr != null && !montoStr.trim().isEmpty()) {
                    long monto = Long.parseLong(montoStr.trim());
                    if (monto > 0) {
                        reserva.getFolio().agregarCargo(concepto.trim(), monto);
                        JOptionPane.showMessageDialog(null, "¡Cargo registrado exitosamente!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "⚠ Reserva o folio no encontrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Cambio de estado operativo de apartamento
    public void cambiarEstadoApartamento(List<Apartamento> listaApartamentos) {
        String cod = JOptionPane.showInputDialog("Código del apartamento:");
        if (cod == null || cod.trim().isEmpty()) return;

        Apartamento apto = buscarApartamento(listaApartamentos, cod.trim());
        if (apto != null) {
            EstadoOperativo[] estados = EstadoOperativo.values();
            EstadoOperativo nuevoEstado = (EstadoOperativo) JOptionPane.showInputDialog(null,
                    "Selecciona el nuevo estado:", "Estado Operativo",
                    JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
            String motivo = JOptionPane.showInputDialog("Motivo:");
            if (nuevoEstado != null) {
                apto.cambiarEstado(nuevoEstado, motivo != null ? motivo.trim() : "");
                JOptionPane.showMessageDialog(null, "¡Estado actualizado correctamente!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "⚠ Apartamento no encontrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    // registro de novedades en apartamento
    public void registrarNovedadApartamento(List<Apartamento> listaApartamentos) {
        String cod = JOptionPane.showInputDialog("Código del apartamento:");
        if (cod == null || cod.trim().isEmpty()) return;

        Apartamento apto = buscarApartamento(listaApartamentos, cod.trim());
        if (apto != null) {
            String desc = JOptionPane.showInputDialog("Descripción de la novedad:");
            if (desc != null && !desc.trim().isEmpty()) {
                apto.registrarNovedad(desc.trim(), getNombre());
                JOptionPane.showMessageDialog(null, "¡Novedad registrada con éxito!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "⚠ Apartamento no encontrado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    // declaracion de No-Show
    public void declararNoShow(List<Reserva> listaReservas) {
        String cod = JOptionPane.showInputDialog("Código/Folio para declarar NO-SHOW:");
        if (cod == null || cod.trim().isEmpty()) return;

        Reserva reserva = buscarReserva(listaReservas, cod.trim());
        if (reserva == null) {
            JOptionPane.showMessageDialog(null, "⚠ Reserva no encontrada.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(null, "⚠ La reserva debe estar CONFIRMADA para declarar NO-SHOW.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reserva.cambiarEstado(EstadoReserva.NO_SHOW)) {
            JOptionPane.showMessageDialog(null, "Reserva declarada como NO-SHOW exitosamente.");
        }
    }

    // mtodos  de búsqueda
    private Huesped buscarHuesped(List<Huesped> listaHuespedes, String cedula) {
        if (listaHuespedes != null) {
            for (Huesped h : listaHuespedes) {
                if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedula)) {
                    return h;
                }
            }
        }
        return null;
    }

    private Apartamento buscarApartamento(List<Apartamento> listaApartamentos, String codigo) {
        if (listaApartamentos != null) {
            for (Apartamento a : listaApartamentos) {
                if (a.getCodigo() != null && a.getCodigo().equalsIgnoreCase(codigo)) {
                    return a;
                }
            }
        }
        return null;
    }

    private Reserva buscarReserva(List<Reserva> listaReservas, String codigo) {
        if (listaReservas != null) {
            for (Reserva r : listaReservas) {
                if (r.getCodigoFolio() != null && r.getCodigoFolio().equalsIgnoreCase(codigo)) {
                    return r;
                }
            }
        }
        return null;
    }
}