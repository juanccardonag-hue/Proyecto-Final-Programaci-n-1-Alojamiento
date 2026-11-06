package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Huesped extends Persona {
    private String contrasenia;
    private List<Reserva> misReservas;

    // Constructor corrigiendo el parámetro de contraseña
    public Huesped(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        this.contrasenia = contrasenia;
        this.misReservas = new ArrayList<>();
    }

    // se registar el huesped y se envia a la lista
    public static void registrarHuespedMenu(List<Huesped> listaHuespedes) {
        String cedula = JOptionPane.showInputDialog("Ingresa tu número de cédula:");
        if (cedula == null || cedula.trim().isEmpty()) return;

        for (Huesped h : listaHuespedes) {
            if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedula.trim())) {
                JOptionPane.showMessageDialog(null, "⚠ Ya existe un huésped registrado con esa cédula.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String nombre = JOptionPane.showInputDialog("Ingresa tu nombre completo:");
        String fNacStr = JOptionPane.showInputDialog("Fecha de nacimiento (AAAA-MM-DD):");
        String telefono = JOptionPane.showInputDialog("Ingresa tu teléfono:");
        String correo = JOptionPane.showInputDialog("Ingresa tu correo electrónico:");
        String clave = JOptionPane.showInputDialog("Crea una contraseña:");

        if (nombre == null || fNacStr == null || clave == null || fNacStr.trim().isEmpty()) return;

        LocalDate fNac = LocalDate.parse(fNacStr.trim());
        Huesped nuevoHuesped = new Huesped(cedula.trim(), nombre.trim(), fNac, telefono != null ? telefono.trim() : "", correo != null ? correo.trim() : "", clave.trim());
        listaHuespedes.add(nuevoHuesped);
        JOptionPane.showMessageDialog(null, "¡Registro completado exitosamente! Ya puedes iniciar sesión.");
    }

    public static Huesped loginHuesped(List<Huesped> listaHuespedes) {
        String[] opcionesAcceso = {"Iniciar Sesión", "Registrarse", "Volver"};
        int seleccionAcceso = JOptionPane.showOptionDialog(
                null,
                "¡Bienvenido/a al portal de Huéspedes!\n¿Qué deseas hacer?",
                "Acceso Huésped",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesAcceso,
                opcionesAcceso[0]
        );

        if (seleccionAcceso == 1) { // Registrarse
            registrarHuespedMenu(listaHuespedes);
            return null;
        } else if (seleccionAcceso != 0) {
            return null;
        }

        // iniciar sesion
        String cedulaInput = JOptionPane.showInputDialog("Ingresa tu número de cédula:");
        if (cedulaInput == null || cedulaInput.trim().isEmpty()) return null;

        String claveInput = JOptionPane.showInputDialog("Ingresa tu contraseña:");
        if (claveInput == null || claveInput.trim().isEmpty()) return null;

        if (listaHuespedes != null) {
            for (Huesped h : listaHuespedes) {
                if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedulaInput.trim()) &&
                        h.getContrasena() != null && h.getContrasena().equals(claveInput.trim())) {
                    JOptionPane.showMessageDialog(null, "¡Bienvenido/a " + h.getNombre() + "! ˶˃ᵕ˂˶");
                    return h;
                }
            }
        }

        JOptionPane.showMessageDialog(null, "⚠ Cédula o contraseña incorrectas.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    public void actualizarPerfil(String nuevoNombre, LocalDate nuevaFecha, String nuevoTelefono, String nuevoCorreo) {
        setNombre(nuevoNombre);
        setFechaNacimiento(nuevaFecha);
        setTelefono(nuevoTelefono);
        setCorreo(nuevoCorreo);
    }

    public boolean agregarReservaHistorial(Reserva reserva) {
        if (reserva != null && !misReservas.contains(reserva)) {
            this.misReservas.add(reserva);
            return true;
        }
        return false;
    }

    public List<Apartamento> consultarApartamentosDisponibles(List<Apartamento> listaGeneralAptos, List<Reserva> todasReservasSistema, LocalDate inicio, LocalDate fin, int personas) {
        List<Apartamento> disponibles = new ArrayList<>();

        for (Apartamento apto : listaGeneralAptos) {
            if (apto.getCapaMaxPersona() >= personas && apto.getEstadoOperativo() != EstadoOperativo.FUERA_SERVICIO) {
                boolean libre = true;
                for (Reserva r : todasReservasSistema) {
                    if (r.getApartamento().getCodigo().equals(apto.getCodigo()) && r.estaActiva()) {
                        if (r.cruceCon(inicio, fin)) {
                            libre = false;
                            break;
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

    public String cotizarEstancia(Apartamento apto, LocalDate inicio, LocalDate fin) {
        String reporte = " ⁺‧₊˚ ཐི⋆Cotizacion de estancia⋆ཋྀ ˚₊‧⁺\n" +
                "Apartamento:" + apto.getCodigo() + "\n";
        long total = 0;
        LocalDate fechaActual = inicio;
        int noche = 1;

        while (fechaActual.isBefore(fin)) {
            long precioNoche;
            String temporada;

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

        reporte += "────────────────୨ৎ────────────────\n" + "Valor total: $" + total;
        return reporte;
    }

    public Reserva crearReservaPortal(String codigoReserva, Apartamento apto, LocalDate inicio, LocalDate fin, List<Ocupante> ocupantes) {
        long total = 0;
        LocalDate fecha = inicio;

        while (fecha.isBefore(fin)) {
            int mes = fecha.getMonthValue();
            if (mes == 6 || mes == 7 || mes == 12 || mes == 1) {
                total += apto.getPrecioAlta();
            } else {
                total += apto.getPrecioBaja();
            }
            fecha = fecha.plusDays(1);
        }

        Reserva nueva = new Reserva(codigoReserva, inicio, fin, Canal.PORTAL, this, apto, null, null);
        for (Ocupante o : ocupantes) {
            nueva.agregarOcupante(o);
        }

        agregarReservaHistorial(nueva);
        return nueva;
    }

    public boolean realizarAbono(String codigoReserva, long monto, MedioPago medioPago) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                r.registrarAbono(monto, medioPago);
                return true;
            }
        }
        return false;
    }

    public String consultarEstadoReservasFolio() {
        if (misReservas.isEmpty()) {
            return "El huésped no tiene reservas registradas ";
        }

        String resultado = " ‿︵‿︵₊୨ Mis reservas ୧₊‿︵‿︵\n";
        for (Reserva r : misReservas) {
            resultado += "Código: " + r.getCodigoReserva() + "\n" +
                    "Apartamento: " + r.getApartamento().getCodigo() + "\n" +
                    "Canal: " + r.getCanal() + "\n" +
                    "Fechas: " + r.getFechaEntrada() + " a " + r.getFechaSalida() + "\n" +
                    "Estado: " + r.getEstado() + "\n" +
                    "Valor Total: $" + r.getValorTotal() + "\n" +
                    "Abonado: $" + r.getTotalAbonado() + "\n" +
                    "Saldo Folio: $" + r.getSaldoFolio() + "\n" +
                    " ﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌ \n";
        }
        return resultado;
    }

    public double calcularDevolucion(String codigoReserva) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equals(codigoReserva)) {
                LocalDate hoy = LocalDate.now();
                long diasAnticipacion = java.time.temporal.ChronoUnit.DAYS.between(hoy, r.getFechaEntrada());

                if (diasAnticipacion > 7) {
                    return r.getTotalAbonado();
                } else if (diasAnticipacion >= 2) {
                    return r.getTotalAbonado() * 0.50;
                } else {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }

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

    // Pide datos para actualizar perfil
    public void actualizarPerfilMenu() {
        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre completo:", getNombre());
        String nuevaFechaStr = JOptionPane.showInputDialog("Nueva fecha de nacimiento (AAAA-MM-DD):", getFechaNacimiento());
        String nuevoTel = JOptionPane.showInputDialog("Nuevo teléfono:", getTelefono());
        String nuevoCorreo = JOptionPane.showInputDialog("Nuevo correo electrónico:", getCorreo());

        if (nuevoNombre == null || nuevaFechaStr == null || nuevaFechaStr.trim().isEmpty()) return;

        LocalDate nuevaFecha = LocalDate.parse(nuevaFechaStr.trim());
        actualizarPerfil(nuevoNombre.trim(), nuevaFecha, nuevoTel != null ? nuevoTel.trim() : "", nuevoCorreo != null ? nuevoCorreo.trim() : "");
        JOptionPane.showMessageDialog(null, "¡Perfil actualizado exitosamente!");
    }

    // Opción 3: Cotizar estancia seleccionando un apartamento y fechas
    public void cotizarEstanciaMenu(List<Apartamento> listaApartamentos) {
        String codApto = JOptionPane.showInputDialog("Código del apartamento a cotizar:");
        if (codApto == null || codApto.trim().isEmpty()) return;

        Apartamento apto = buscarApartamento(listaApartamentos, codApto.trim());
        if (apto == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún apartamento con ese código.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fInicioStr = JOptionPane.showInputDialog("Fecha de inicio (AAAA-MM-DD):");
        String fFinStr = JOptionPane.showInputDialog("Fecha de fin (AAAA-MM-DD):");
        if (fInicioStr == null || fFinStr == null || fInicioStr.trim().isEmpty() || fFinStr.trim().isEmpty()) return;

        LocalDate inicio = LocalDate.parse(fInicioStr.trim());
        LocalDate fin = LocalDate.parse(fFinStr.trim());

        String reporte = cotizarEstancia(apto, inicio, fin);
        JOptionPane.showMessageDialog(null, reporte);
    }

    // Consultar apartamentos disponibles y reservar mediante el portal
    public void crearReservaPortalMenu(List<Apartamento> listaApartamentos, List<Reserva> todasReservas) {
        String personasStr = JOptionPane.showInputDialog("Número de personas:");
        String fInicioStr = JOptionPane.showInputDialog("Fecha de inicio (AAAA-MM-DD):");
        String fFinStr = JOptionPane.showInputDialog("Fecha de fin (AAAA-MM-DD):");
        if (personasStr == null || fInicioStr == null || fFinStr == null || personasStr.trim().isEmpty() || fInicioStr.trim().isEmpty() || fFinStr.trim().isEmpty()) return;

        int personas = Integer.parseInt(personasStr.trim());
        LocalDate inicio = LocalDate.parse(fInicioStr.trim());
        LocalDate fin = LocalDate.parse(fFinStr.trim());

        List<Apartamento> disponibles = consultarApartamentosDisponibles(listaApartamentos, todasReservas, inicio, fin, personas);
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(null, "⚠ No hay apartamentos disponibles para esas fechas y número de personas.", "Sin disponibilidad ⚠", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String listaDisponibles = "Apartamentos disponibles:\n";
        for (Apartamento a : disponibles) {
            listaDisponibles += "- " + a.getCodigo() + " (Capacidad max: " + a.getCapaMaxPersona() + " personas)\n";
        }
        JOptionPane.showMessageDialog(null, listaDisponibles);

        String codApto = JOptionPane.showInputDialog("Ingresa el código del apartamento que deseas reservar:");
        if (codApto == null || codApto.trim().isEmpty()) return;

        Apartamento aptoSeleccionado = buscarApartamento(disponibles, codApto.trim());
        if (aptoSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "⚠ Apartamento no válido o no disponible.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Ocupante> ocupantes = new ArrayList<>();
        String codReserva = "Reservas portal" + (todasReservas.size() + 1);

        Reserva nuevaReserva = crearReservaPortal(codReserva, aptoSeleccionado, inicio, fin, ocupantes);
        todasReservas.add(nuevaReserva);
        JOptionPane.showMessageDialog(null, "¡Reserva creada con éxito desde el portal! \n Código de Reserva: " + codReserva);
    }

    //Realizar abono o pago anticipado a una reserva
    public void realizarAbonoMenu() {
        String codReserva = JOptionPane.showInputDialog("Ingresa el código de tu reserva:");
        if (codReserva == null || codReserva.trim().isEmpty()) return;

        String montoAbono = JOptionPane.showInputDialog("Monto a abonar ($):");
        MedioPago[] medios = MedioPago.values();
        MedioPago medio = (MedioPago) JOptionPane.showInputDialog(null, "Selecciona medio de pago:", "Medios de Pago",
                JOptionPane.QUESTION_MESSAGE, null, medios, medios[0]);

        if (montoAbono == null || medio == null || montoAbono.trim().isEmpty()) return;

        long monto = Long.parseLong(montoAbono.trim());
        if (realizarAbono(codReserva.trim(), monto, medio)) {
            JOptionPane.showMessageDialog(null, "¡Abono registrado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró ninguna reserva propia con ese código.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Consultar estado de reservas y folio
    public void consultarEstadoReservasFolioMenu() {
        String reporte = consultarEstadoReservasFolio();
        JOptionPane.showMessageDialog(null, reporte);
    }

    // cancelar reserva con cálculo de devolución
    public void cancelarReservaMenu() {
        String codReserva = JOptionPane.showInputDialog("Ingresa el código de la reserva a cancelar:");
        if (codReserva == null || codReserva.trim().isEmpty()) return;

        double devolucion = calcularDevolucion(codReserva.trim());
        if (cancelarReserva(codReserva.trim())) {
            JOptionPane.showMessageDialog(null, "Reserva cancelada exitosamente.\n Monto de devolución calculado: $" + devolucion);
        } else {
            JOptionPane.showMessageDialog(null, "⚠ No se pudo cancelar la reserva por favor verifica que el código sea correcto y que el estado sea pendiente o confirmada).", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    // metodos  de búsqueda
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

    // Getters y Setters
    public List<Reserva> getMisReservas() {
        return misReservas;
    }
    public String getContrasena() { return contrasenia; }
    public void setContrasena(String contrasena) { this.contrasenia = contrasena; }
}