package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Huesped extends Persona {
    private String contrasenia;
    private List<Reserva> misReservas;

    public Huesped(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        this.contrasenia = contrasenia;
        this.misReservas = new ArrayList<>();
    }
    // Autenticación y Registro
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

        try {
            LocalDate fNac = LocalDate.parse(fNacStr.trim());
            Huesped nuevoHuesped = new Huesped(cedula.trim(), nombre.trim(), fNac, telefono != null ? telefono.trim() : "", correo != null ? correo.trim() : "", clave.trim());
            listaHuespedes.add(nuevoHuesped);
            JOptionPane.showMessageDialog(null, "¡Registro completado exitosamente! Ya puedes iniciar sesión.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "⚠ Formato de fecha inválido. Debe ser AAAA-MM-DD.", "Error de entrada ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Huesped loginHuesped(List<Huesped> listaHuespedes) {
        String[] opcionesAcceso = {"Iniciar Sesión", "Registrarse", "Volver"};
        int seleccionAcceso = JOptionPane.showOptionDialog(null, "¡Bienvenido/a al portal de Huéspedes!\n¿Qué deseas hacer?", "Acceso Huésped", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesAcceso, opcionesAcceso[0]);
        if (seleccionAcceso == 1) {
            registrarHuespedMenu(listaHuespedes);
            return null;
        } else if (seleccionAcceso != 0) {
            return null;
        }

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

    //Perfil
    public void actualizarPerfil(String nuevoNombre, LocalDate nuevaFecha, String nuevoTelefono, String nuevoCorreo) {
        setNombre(nuevoNombre);
        setFechaNacimiento(nuevaFecha);
        setTelefono(nuevoTelefono);
        setCorreo(nuevoCorreo);
    }

    public void actualizarPerfilMenu() {
        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre completo:", getNombre());
        String nuevaFechaStr = JOptionPane.showInputDialog("Nueva fecha de nacimiento (AAAA-MM-DD):", getFechaNacimiento());
        String nuevoTel = JOptionPane.showInputDialog("Nuevo teléfono:", getTelefono());
        String nuevoCorreo = JOptionPane.showInputDialog("Nuevo correo electrónico:", getCorreo());

        if (nuevoNombre == null || nuevaFechaStr == null || nuevaFechaStr.trim().isEmpty()) return;

        try {
            LocalDate nuevaFecha = LocalDate.parse(nuevaFechaStr.trim());
            actualizarPerfil(nuevoNombre.trim(), nuevaFecha, nuevoTel != null ? nuevoTel.trim() : "", nuevoCorreo != null ? nuevoCorreo.trim() : "");
            JOptionPane.showMessageDialog(null, "¡Perfil actualizado exitosamente!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "⚠ Formato de fecha inválido. Usa AAAA-MM-DD.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean agregarReservaHistorial(Reserva reserva) {
        if (reserva != null && !misReservas.contains(reserva)) {
            this.misReservas.add(reserva);
            return true;
        }
        return false;
    }

    // Búsqueda de disponibilidad considerando Bloqueos
    public List<Apartamento> consultarApartamentosDisponibles(List<Apartamento> listaGeneralAptos, List<Reserva> todasReservasSistema, LocalDate inicio, LocalDate fin, int personas) {
        List<Apartamento> disponibles = new ArrayList<>();

        for (Apartamento apto : listaGeneralAptos) {
            // Valida capacidad física y que NO esté fuera de servicio ni bloqueado por el Admin
            if (apto.getCapaMaxPersona() >= personas && !apto.estaBloqueadoRango(inicio, fin)) {
                boolean libre = true;
                for (Reserva r : todasReservasSistema) {
                    if (r.getApartamento().getCodigo().equalsIgnoreCase(apto.getCodigo()) && r.estaActiva()) {
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

    // Cotización Detallada con Acompañantes o solo
    public void cotizarEstanciaMenu(List<Apartamento> listaApartamentos, List<Temporada> listaTemporadas) {
        if (listaApartamentos == null || listaApartamentos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay apartamentos registrados en el sistema.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Desplegable con todos los apartamentos disponibles
        String[] opcionesAptos = new String[listaApartamentos.size()];
        for (int i = 0; i < listaApartamentos.size(); i++) {
            Apartamento a = listaApartamentos.get(i);
            opcionesAptos[i] = a.getCodigo() + " - " + a.getNombre() + " (Cap: " + a.getCapaMaxPersona() + " pers | Tarifa Base: $" + a.getTarifaPersonaNoche() + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(null,
                "Selecciona el apartamento que deseas cotizar:", "Cotizador de Estancias", JOptionPane.QUESTION_MESSAGE, null, opcionesAptos, opcionesAptos[0]);

        if (seleccion == null) return;

        String codAptoSeleccionado = seleccion.split(" - ")[0].trim();
        Apartamento apto = buscarApartamento(listaApartamentos, codAptoSeleccionado);

        String fInicioStr = JOptionPane.showInputDialog("Fecha de inicio de estancia (AAAA-MM-DD):");
        String fFinStr = JOptionPane.showInputDialog("Fecha de fin de estancia (AAAA-MM-DD):");
        String acompStr = JOptionPane.showInputDialog("Número de acompañantes (sin contarse a usted):", "0");

        if (fInicioStr == null || fFinStr == null || fInicioStr.trim().isEmpty() || fFinStr.trim().isEmpty()) return;

        try {
            LocalDate inicio = LocalDate.parse(fInicioStr.trim());
            LocalDate fin = LocalDate.parse(fFinStr.trim());
            int acompanantes = (acompStr != null && !acompStr.trim().isEmpty()) ? Integer.parseInt(acompStr.trim()) : 0;

            if (!inicio.isBefore(fin)) {
                JOptionPane.showMessageDialog(null, "⚠ La fecha de inicio debe ser anterior a la de fin.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Validación de indisponibilidad por bloqueos o Fuera de Servicio
            if (apto.estaBloqueadoRango(inicio, fin)) {
                JOptionPane.showMessageDialog(null, "⚠ El apartamento " + apto.getCodigo() + " está FUERA DE SERVICIO o bloqueado en las fechas seleccionadas.", "No Disponible ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int pagantes = 0;
            int exonerados = 0;

            // Evaluación del Titular
            int edadTitular = Period.between(getFechaNacimiento(), inicio).getYears();
            if (edadTitular >= 6) {pagantes++;}
            else {exonerados++;}

            // Evaluación de Acompañantes
            for (int i = 1; i <= acompanantes; i++) {
                String fNacAcomp = JOptionPane.showInputDialog("Fecha de nacimiento del acompañante " + i + " (AAAA-MM-DD):");
                if (fNacAcomp != null && !fNacAcomp.trim().isEmpty()) {
                    LocalDate fNac = LocalDate.parse(fNacAcomp.trim());
                    int edad = Period.between(fNac, inicio).getYears();
                    if (edad >= 6) pagantes++; else exonerados++;
                } else {
                    pagantes++; // Adulto por defecto si omite fecha
                }
            }

            // Construcción del reporte de cotización
            String reporte = " ⁺‧₊˚ ཐི⋆ Cotización Detallada ⋆ཋྀ ˚₊‧⁺\n" +
                    "Apartamento: " + apto.getCodigo() + " (" + apto.getNombre() + ")\n" +
                    "Titular: " + getNombre() + " (Edad: " + edadTitular + " años)\n" +
                    "Pagantes Totales (≥6 años): " + pagantes + "\n" +
                    "Exonerados (<6 años): " + exonerados + "\n" +
                    "Tarifa Base por persona/noche: $" + apto.getTarifaPersonaNoche() + "\n\n";

            long totalCotizacion = 0;
            LocalDate fechaAux = inicio;
            int noche = 1;
            while (fechaAux.isBefore(fin)) {
                Temporada temp = buscarTemporada(fechaAux, listaTemporadas);
                DetalleNocheReserva detalle = new DetalleNocheReserva(fechaAux, pagantes, apto.getTarifaPersonaNoche(), temp);
                String tipoTemp = (temp != null) ? temp.getTipo() + " (Recargo: " + (temp.getPorcentajeRecargo() * 100) + "%)" : "BAJA / REGULAR (0%)";
                reporte += "Noche " + noche + " (" + fechaAux + " | " + tipoTemp + "): $" + detalle.getSubtotal() + "\n";
                totalCotizacion += detalle.getSubtotal();
                fechaAux = fechaAux.plusDays(1);
                noche++;
            }
            reporte += "────────────────୨ৎ────────────────\n" +
                    "VALOR TOTAL ESTIMADO DE ESTANCIA: $" + totalCotizacion;
            JOptionPane.showMessageDialog(null, reporte);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "⚠ Datos de entrada o formato de fecha inválidos (AAAA-MM-DD).", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Reserva por el Portal
    public void crearReservaPortalMenu(List<Apartamento> listaApartamentos, List<Reserva> todasReservas, List<Temporada> listaTemporadas) {
        String personasStr = JOptionPane.showInputDialog("Número de personas acompañantes / grupo:");
        String fInicioStr = JOptionPane.showInputDialog("Fecha de inicio (AAAA-MM-DD):");
        String fFinStr = JOptionPane.showInputDialog("Fecha de fin (AAAA-MM-DD):");
        if (personasStr == null || fInicioStr == null || fFinStr == null || personasStr.trim().isEmpty() || fInicioStr.trim().isEmpty() || fFinStr.trim().isEmpty()) return;

        try {
            int personas = Integer.parseInt(personasStr.trim());
            LocalDate inicio = LocalDate.parse(fInicioStr.trim());
            LocalDate fin = LocalDate.parse(fFinStr.trim());

            if (!inicio.isBefore(fin)) {
                JOptionPane.showMessageDialog(null, "⚠ La fecha de inicio debe ser anterior a la de fin.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Apartamento> disponibles = consultarApartamentosDisponibles(listaApartamentos, todasReservas, inicio, fin, personas);
            if (disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠ No hay apartamentos disponibles o habilitados para esas fechas y número de personas.", "Sin disponibilidad ⚠", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Se crea una lista de opciones para que el usuario elija de un menú desplegable
            String[] opcionesAptos = new String[disponibles.size()];
            for (int i = 0; i < disponibles.size(); i++) {
                Apartamento a = disponibles.get(i);
                opcionesAptos[i] = a.getCodigo() + " - " + a.getNombre() + " (Cap: " + a.getCapaMaxPersona() + " pers | Base: $" + a.getTarifaPersonaNoche() + ")";
            }

            String seleccion = (String) JOptionPane.showInputDialog(null, "Selecciona el apartamento que deseas reservar:", "Apartamentos Disponibles", JOptionPane.QUESTION_MESSAGE, null, opcionesAptos, opcionesAptos[0]);

            if (seleccion == null) return;

            String codAptoSeleccionado = seleccion.split(" - ")[0].trim();
            Apartamento aptoSeleccionado = buscarApartamento(disponibles, codAptoSeleccionado);

            // Registro de ocupantes con fecha de nacimiento
            List<Ocupante> ocupantes = new ArrayList<>();
            for (int i = 1; i <= personas; i++) {
                String nomOcupante = JOptionPane.showInputDialog("Nombre del ocupante " + i + ":");
                String fNacOcupante = JOptionPane.showInputDialog("Fecha de nacimiento del ocupante " + i + " (AAAA-MM-DD):");
                if (nomOcupante != null && fNacOcupante != null && !nomOcupante.trim().isEmpty() && !fNacOcupante.trim().isEmpty()) {
                    try {
                        LocalDate fNac = LocalDate.parse(fNacOcupante.trim());
                        ocupantes.add(new Ocupante(nomOcupante.trim(), fNac));
                    } catch (Exception ex) {
                        ocupantes.add(new Ocupante(nomOcupante.trim(), LocalDate.of(2000, 1, 1)));
                    }
                }
            }

            String codReserva = "RES-PORTAL-" + (todasReservas.size() + 1);
            Reserva nuevaReserva = new Reserva(codReserva, inicio, fin, Canal.PORTAL, this, aptoSeleccionado, null, null);
            for (Ocupante oc : ocupantes) {
                nuevaReserva.agregarOcupante(oc);
            }
            nuevaReserva.liquidarEstancia(listaTemporadas);

            todasReservas.add(nuevaReserva);
            agregarReservaHistorial(nuevaReserva);

            long total = nuevaReserva.getValorTotal();
            long anticipoMinimo = (long)(total * 0.30);

            JOptionPane.showMessageDialog(null, "¡Reserva creada con éxito desde el portal!\n" +
                    "✿ Código de Reserva: " + codReserva + "\n" +
                    "✿ Valor Total Estancia: $" + total + "\n" +
                    "✿ Recuerde abonar al menos el 30% ($" + anticipoMinimo + ") en las próximas 24 horas para confirmarla.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ El número de personas debe ser un valor entero.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "⚠ Formato de fecha inválido. Usa AAAA-MM-DD.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Abonos
    public void realizarAbonoMenu() {
        String codReserva = JOptionPane.showInputDialog("Ingresa el código de tu reserva:");
        if (codReserva == null || codReserva.trim().isEmpty()) return;

        Reserva reservaEncontrada = null;
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equalsIgnoreCase(codReserva.trim())) {
                reservaEncontrada = r;
                break;
            }
        }

        if (reservaEncontrada == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró ninguna reserva propia con el código: " + codReserva, "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //No permite abonar a reservas inactivas o canceladas
        if (!reservaEncontrada.estaActiva()) {
            JOptionPane.showMessageDialog(null, "⚠ No se pueden realizar abonos a una reserva en estado " + reservaEncontrada.getEstado() + ".", "Operación no permitida ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String montoAbono = JOptionPane.showInputDialog("Monto a abonar ($):\n(Saldo Pendiente actual: $" + reservaEncontrada.getSaldoFolio() + ")");
        MedioPago[] medios = MedioPago.values();
        MedioPago medio = (MedioPago) JOptionPane.showInputDialog(null, "Selecciona medio de pago:", "Medios de Pago",
                JOptionPane.QUESTION_MESSAGE, null, medios, medios[0]);

        if (montoAbono == null || medio == null || montoAbono.trim().isEmpty()) return;

        try {
            long monto = Long.parseLong(montoAbono.trim());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(null, "⚠ El monto a abonar debe ser mayor a cero.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (reservaEncontrada.registrarAbono(monto, medio)) {
                JOptionPane.showMessageDialog(null, "¡Abono registrado exitosamente!\nNuevo Saldo Pendiente: $" + reservaEncontrada.getSaldoFolio());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ El monto debe ser un valor entero válido.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    //  Consultas y Cancelaciones
    public String consultarEstadoReservasFolio() {
        if (misReservas.isEmpty()) {
            return "El huésped no tiene reservas registradas.";
        }

        String resultado = " ‿︵‿︵₊୨ Mis reservas ୧₊‿︵‿︵\n";
        for (Reserva r : misReservas) {
            double abonado = r.getTotalAbonado();
            long total = r.getValorTotal();
            double saldo = total - abonado;

            String estadoSaldo;
            if (saldo <= 0) {
                estadoSaldo = "Saldo Pendiente: $0.0 (Cambio / Devuelta a favor: $" + Math.abs(saldo) + ")";
            } else {
                estadoSaldo = "Saldo Pendiente: $" + saldo;
            }

            resultado += "Código: " + r.getCodigoReserva() + "\n" +
                    "Apartamento: " + r.getApartamento().getCodigo() + "\n" +
                    "Canal: " + r.getCanal() + "\n" +
                    "Fechas: " + r.getFechaEntrada() + " a " + r.getFechaSalida() + "\n" +
                    "Estado: " + r.getEstado() + "\n" +
                    "Valor Total: $" + total + "\n" +
                    "Abonado: $" + abonado + "\n" +
                    estadoSaldo + "\n" +
                    " ﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌ \n";
        }
        return resultado;
    }

    public void consultarEstadoReservasFolioMenu() {
        String reporte = consultarEstadoReservasFolio();
        JOptionPane.showMessageDialog(null, reporte);
    }

    public double calcularDevolucion(String codigoReserva) {
        for (Reserva r : misReservas) {
            if (r.getCodigoReserva().equalsIgnoreCase(codigoReserva)) {
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
            if (r.getCodigoReserva().equalsIgnoreCase(codigoReserva)) {
                if (r.getEstado() == EstadoReserva.PENDIENTE || r.getEstado() == EstadoReserva.CONFIRMADA) {
                    r.setEstado(EstadoReserva.CANCELADA);
                    return true;
                }
            }
        }
        return false;
    }

    public void cancelarReservaMenu() {
        String codReserva = JOptionPane.showInputDialog("Ingresa el código de la reserva a cancelar:");
        if (codReserva == null || codReserva.trim().isEmpty()) return;

        double devolucion = calcularDevolucion(codReserva.trim());
        if (cancelarReserva(codReserva.trim())) {
            JOptionPane.showMessageDialog(null, "Reserva cancelada exitosamente.\nMonto de devolución calculado: $" + devolucion);
        } else {
            JOptionPane.showMessageDialog(null, "⚠ No se pudo cancelar la reserva (verifica que el código sea correcto y que su estado sea PENDIENTE o CONFIRMADA).", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
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
    private Temporada buscarTemporada(LocalDate fecha, List<Temporada> temporadas) {
        if (temporadas == null) return null;
        for (Temporada t : temporadas) {
            if (t.aplicaEnFecha(fecha)) return t;
        }
        return null;
    }
    public List<Reserva> getMisReservas() { return misReservas; }
    public String getContrasena() { return contrasenia; }
    public void setContrasena(String contrasena) { this.contrasenia = contrasena; }
}