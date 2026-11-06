package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Empleado {
    private String usuario;
    private String contrasenia;
    private PoliticaCancelacion politicaCancelacion = new PoliticaCancelacion(7, 2, 0.50);

    public Administrador(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo, usuario, contrasenia);
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    // Seguridad y autenticación
    public boolean autenticar(String usuarioIngresado, String contrasenaIngresada) {
        return this.usuario.equalsIgnoreCase(usuarioIngresado) && this.contrasenia.equals(contrasenaIngresada);
    }

    public boolean loginAdmin() {
        String usuarioInput = JOptionPane.showInputDialog("Ingresa tu usuario:");
        if (usuarioInput == null || usuarioInput.trim().isEmpty()) return false;

        String claveInput = JOptionPane.showInputDialog("Ingresa tu contraseña:");
        if (claveInput == null || claveInput.trim().isEmpty()) return false;

        if (autenticar(usuarioInput.trim(), claveInput.trim())) {
            JOptionPane.showMessageDialog(null, "¡Bienvenido/a Administrador/a " + getNombre() + "! ˶˃ᵕ˂˶");
            return true;
        }

        JOptionPane.showMessageDialog(null, "⚠ Usuario o contraseña de  incorrectos", "Error de Autenticación ⚠❗", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Control de acceso
    public void gestionarControlAcceso(List<Recepcionista> listaRecepcionistas, List<Empleado> listaEmpleados) {
        String[] opciones = {"Registrar nuevo recepcionista", "Cambiar mi usuario/contraseña", "Registrar nuevo empleado", "Volver"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "¿Qué acción deseas realizar?",
                "Control de Acceso y Credenciales",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) { // Registrar Recepcionista
            String cedula = JOptionPane.showInputDialog("Cédula del recepcionista:");
            String nombre = JOptionPane.showInputDialog("Nombre completo:");
            String usuario = JOptionPane.showInputDialog("Usuario:");
            String contrasena = JOptionPane.showInputDialog("Contraseña:");

            if (cedula != null && usuario != null && contrasena != null) {
                listaRecepcionistas.add(new Recepcionista(cedula.trim(), nombre.trim(), LocalDate.now(), "", "", usuario.trim(), contrasena.trim()));
                JOptionPane.showMessageDialog(null, "¡Recepcionista registrado exitosamente!");
            }

        } else if (seleccion == 1) { // Modificar credenciales del Admin
            String nuevoUsuario = JOptionPane.showInputDialog("Nuevo usuario (Tu usuario actual es: " + this.usuario + ")");
            String nuevaClave = JOptionPane.showInputDialog("Nueva contraseña:");

            if (nuevoUsuario != null && !nuevoUsuario.trim().isEmpty()) {
                this.usuario = nuevoUsuario.trim();
            }
            if (nuevaClave != null && !nuevaClave.trim().isEmpty()) {
                this.contrasenia = nuevaClave.trim();
            }
            JOptionPane.showMessageDialog(null, "¡Credenciales actualizadas correctamente!");

        } else if (seleccion == 2) { // Registrar Empleado
            String cedula = JOptionPane.showInputDialog("Cédula del empleado:");
            String nombre = JOptionPane.showInputDialog("Nombre completo:");
            String telefono = JOptionPane.showInputDialog("Teléfono:");
            String correo = JOptionPane.showInputDialog("Correo electrónico:");
            String usuario = JOptionPane.showInputDialog("Usuario:");
            String contrasena = JOptionPane.showInputDialog("Contraseña:");

            if (cedula != null && nombre != null && usuario != null && contrasena != null) {
                Empleado nuevoEmpleado = new Empleado(
                        cedula.trim(),
                        nombre.trim(),
                        LocalDate.now(),
                        telefono != null ? telefono.trim() : "",
                        correo != null ? correo.trim() : "",
                        usuario.trim(),
                        contrasena.trim()
                );

                boolean registrado = contratarEmpleado(listaEmpleados, nuevoEmpleado);
                if (registrado) {
                    JOptionPane.showMessageDialog(null, "¡Empleado registrado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ Ya existe un empleado registrado con la cédula: " + cedula, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Gestión de apartamentos
    public void gestionarApartamentos(List<Apartamento> listaApartamentos) {
        String[] opciones = {"Crear apartamento", "Modificar apartamento", "Poner fuera de servicio", "Volver"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "¿Qué acción deseas realizar sobre los apartamentos?",
                "Gestión de Apartamentos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) { // Crear Apartamento
            String codigo = JOptionPane.showInputDialog("Código del nuevo apartamento:");
            String nombre = JOptionPane.showInputDialog("Nombre / Identificador:");
            String dormitoriosStr = JOptionPane.showInputDialog("Número de dormitorios:");
            String capMaxStr = JOptionPane.showInputDialog("Capacidad máxima de personas:");
            String caracteristica = JOptionPane.showInputDialog("Características principales:");
            String tarifaStr = JOptionPane.showInputDialog("Tarifa base por noche:");

            if (codigo != null && nombre != null && dormitoriosStr != null && capMaxStr != null && tarifaStr != null) {
                try {
                    int numDormitorios = Integer.parseInt(dormitoriosStr.trim());
                    int capMax = Integer.parseInt(capMaxStr.trim());
                    long tarifaBase = Long.parseLong(tarifaStr.trim());

                    Apartamento nuevoApto = new Apartamento(
                            codigo.trim(),
                            nombre.trim(),
                            numDormitorios,
                            capMax,
                            caracteristica != null ? caracteristica.trim() : "",
                            tarifaBase
                    );

                    if (crearApartamento(listaApartamentos, nuevoApto)) {
                        JOptionPane.showMessageDialog(null, "¡Apartamento creado exitosamente!");
                    } else {
                        JOptionPane.showMessageDialog(null, "⚠ Ya existe un apartamento registrado con el código: " + codigo, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "⚠ Debes ingresar datos numéricos válidos para dormitorios, capacidad y tarifa.", "Error de entrada ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (seleccion == 1) { // Modificar Apartamento
            String codigo = JOptionPane.showInputDialog("Ingresa el código del apartamento a modificar:");
            if (codigo != null) {
                Apartamento aptoEncontrado = null;
                for (Apartamento a : listaApartamentos) {
                    if (a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                        aptoEncontrado = a;
                        break;
                    }
                }

                if (aptoEncontrado != null) {
                    String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", aptoEncontrado.getNombre());
                    String dormStr = JOptionPane.showInputDialog("Nuevo número de dormitorios:", aptoEncontrado.getNumeroDormitorio());
                    String capStr = JOptionPane.showInputDialog("Nueva capacidad máxima:", aptoEncontrado.getCapaMaxPersona());
                    String nuevaCaracteristica = JOptionPane.showInputDialog("Nuevas características:", aptoEncontrado.getCaracteristica());
                    String tarifaStr = JOptionPane.showInputDialog("Nueva tarifa base:", aptoEncontrado.getTarifaPersonaNoche());

                    if (nuevoNombre != null && dormStr != null && capStr != null && tarifaStr != null) {
                        try {
                            int dorm = Integer.parseInt(dormStr.trim());
                            int cap = Integer.parseInt(capStr.trim());
                            long tarifa = Long.parseLong(tarifaStr.trim());

                            if (modificarApartamento(aptoEncontrado, nuevoNombre.trim(), dorm, cap, nuevaCaracteristica != null ? nuevaCaracteristica.trim() : "", tarifa)) {
                                JOptionPane.showMessageDialog(null, "¡Apartamento modificado correctamente!");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "⚠ Datos numéricos inválidos.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún apartamento con el código: " + codigo, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (seleccion == 2) { // Poner fuera de servicio
            String codigo = JOptionPane.showInputDialog("Ingresa el código del apartamento a retirar de servicio:");
            if (codigo != null) {
                Apartamento aptoEncontrado = null;
                for (Apartamento a : listaApartamentos) {
                    if (a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                        aptoEncontrado = a;
                        break;
                    }
                }

                if (aptoEncontrado != null) {
                    String motivo = JOptionPane.showInputDialog("Ingresa el motivo del retiro de servicio:");
                    if (motivo != null && eliminarApto(aptoEncontrado, motivo.trim())) {
                        JOptionPane.showMessageDialog(null, "El apartamento fue marcado como FUERA DE SERVICIO.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún apartamento con el código: " + codigo, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    public void gestionarEmpleados(List<Empleado> listaEmpleados) {
        String[] opciones = {"Contratar empleado", "Editar empleado", "Desvincular empleado", "Volver"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "¿Qué acción deseas realizar en los empleados?",
                "Gestión de Personal",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) { // Contratar Empleado
            String cedula = JOptionPane.showInputDialog("Cédula del empleado:");
            String nombre = JOptionPane.showInputDialog("Nombre completo:");
            String telefono = JOptionPane.showInputDialog("Teléfono:");
            String correo = JOptionPane.showInputDialog("Correo electrónico:");
            String usuario = JOptionPane.showInputDialog("Usuario de acceso:");
            String contrasena = JOptionPane.showInputDialog("Contraseña:");

            if (cedula != null && nombre != null && usuario != null && contrasena != null) {
                Empleado nuevoEmpleado = new Empleado(
                        cedula.trim(),
                        nombre.trim(),
                        LocalDate.now(),
                        telefono != null ? telefono.trim() : "",
                        correo != null ? correo.trim() : "",
                        usuario.trim(),
                        contrasena.trim()
                );

                if (contratarEmpleado(listaEmpleados, nuevoEmpleado)) {
                    JOptionPane.showMessageDialog(null, "¡Empleado contratado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ Ya existe un empleado registrado con la cédula: " + cedula, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (seleccion == 1) { // Editar Empleado
            String cedula = JOptionPane.showInputDialog("Ingresa la cédula del empleado a editar:");
            if (codigoValido(cedula)) {
                Empleado empEncontrado = null;
                for (Empleado e : listaEmpleados) {
                    if (e.getCedula().equalsIgnoreCase(cedula.trim())) {
                        empEncontrado = e;
                        break;
                    }
                }

                if (empEncontrado != null) {
                    String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre:", empEncontrado.getNombre());
                    String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", empEncontrado.getTelefono());
                    String nuevoCorreo = JOptionPane.showInputDialog("Nuevo correo:", empEncontrado.getCorreo());

                    if (nuevoNombre != null && nuevoTelefono != null && nuevoCorreo != null) {
                        if (editarEmpleado(empEncontrado, nuevoNombre.trim(), nuevoTelefono.trim(), nuevoCorreo.trim())) {
                            JOptionPane.showMessageDialog(null, "¡Datos del empleado actualizados correctamente!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún empleado con la cédula: " + cedula, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (seleccion == 2) { // Desvincular Empleado
            String cedula = JOptionPane.showInputDialog("Ingresa la cédula del empleado a desvincular:");
            if (codigoValido(cedula)) {
                if (desvincularEmpleado(listaEmpleados, cedula.trim())) {
                    JOptionPane.showMessageDialog(null, "¡Empleado desvinculado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún empleado registrado con la cédula: " + cedula, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private boolean codigoValido(String input) { // Evita la repetición de código
        return input != null && !input.trim().isEmpty();
    }
    // 2. Métodos  para el menú
    public boolean crearApartamento(List<Apartamento> listaApartamentos, Apartamento nuevoApto) {
        if (nuevoApto == null) {
            return false;
        }
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equalsIgnoreCase(nuevoApto.getCodigo())) {
                return false;
            }
        }
        listaApartamentos.add(nuevoApto);
        return true;
    }

    public boolean modificarApartamento(Apartamento apto, String nuevoNombre, int numDormitorios, int capMax, String caracteristica, long tarifaBase) {
        if (apto != null) {
            apto.setNombre(nuevoNombre);
            apto.setNumeroDormitorio(numDormitorios);
            apto.setCapaMaxPersona(capMax);
            apto.setCaracteristica(caracteristica);
            apto.setTarifaPersonaNoche(tarifaBase);
            return true;
        }
        return false;
    }

    public boolean eliminarApto(Apartamento apto, String motivo) {
        if (apto != null) {
            apto.cambiarEstado(EstadoOperativo.FUERA_SERVICIO, motivo);
            return true;
        }
        return false;
    }

    // gestion de personal
    public boolean contratarEmpleado(List<Empleado> listaEmpleados, Empleado nuevoEmpleado) {
        if (nuevoEmpleado == null) return false;
        for (Empleado e : listaEmpleados) {
            if (e.getCedula().equals(nuevoEmpleado.getCedula())) {
                return false;
            }
        }
        listaEmpleados.add(nuevoEmpleado);
        return true;
    }

    public boolean editarEmpleado(Empleado empleado, String nuevoNombre, String nuevoTelefono, String nuevoCorreo) {
        if (empleado != null) {
            empleado.setNombre(nuevoNombre);
            empleado.setTelefono(nuevoTelefono);
            empleado.setCorreo(nuevoCorreo);
            return true;
        }
        return false;
    }

    public boolean desvincularEmpleado(List<Empleado> listaEmpleados, String cedula) {
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getCedula().equals(cedula)) {
                listaEmpleados.remove(i);
                return true;
            }
        }
        return false;
    }

    //Gestion de traifas y temproada
    public void gestionarTemporadaTarifas(List<Apartamento> listaApartamentos, List<Temporada> listaTemporadas) {
        String[] opciones = {"Definir temporada", "Configurar tarifas", "Volver"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "¿Qué acción deseas realizar?",
                "Gestión de Temporadas y Costos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) { // 1. Registrar Temporada
            String fInicio = JOptionPane.showInputDialog("Fecha de inicio de la temporada (AAAA-MM-DD):");
            String fFin = JOptionPane.showInputDialog("Fecha de fin de la temporada (AAAA-MM-DD):");
            String recargo = JOptionPane.showInputDialog("Porcentaje de recargo para la temporada (por ejemplo: 0.20 para 20%):");

            if (fInicio != null && fFin != null && recargo != null) {
                try {
                    LocalDate inicio = LocalDate.parse(fInicio.trim());
                    LocalDate fin = LocalDate.parse(fFin.trim());
                    double porcentajeRecargo = Double.parseDouble(recargo.trim());

                    Temporada nuevaTemporada = new Temporada(inicio, fin, porcentajeRecargo);

                    if (agregarTemporada(listaTemporadas, nuevaTemporada)) {
                        JOptionPane.showMessageDialog(null, "¡Temporada registrada exitosamente!\n\n" +
                                "✿ Periodo: del " + inicio + " al " + fin + "\n" +
                                "✿ Recargo aplicado: " + (porcentajeRecargo * 100) + "%");
                    } else {
                        JOptionPane.showMessageDialog(null, "⚠ La temporada ya se encuentra registrada.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "⚠ Datos de entrada inválidos ", "Error de entrada ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (seleccion == 1) { // 2. Definir Tarifas por Apartamento
            String codigo = JOptionPane.showInputDialog("Ingresa el código del apartamento a configurar:");
            if (codigo != null && !codigo.trim().isEmpty()) {
                Apartamento aptoEncontrado = null;
                for (Apartamento a : listaApartamentos) {
                    if (a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                        aptoEncontrado = a;
                        break;
                    }
                }

                if (aptoEncontrado != null) {
                    String tempBaja = JOptionPane.showInputDialog("Tarifa base para Temporada Baja ($):");
                    String tempAlta = JOptionPane.showInputDialog("Tarifa para Temporada Alta ($):");

                    if (tempBaja != null && tempAlta != null) {
                        try {
                            long precioBaja = Long.parseLong(tempBaja.trim());
                            long precioAlta = Long.parseLong(tempAlta.trim());

                            definirTarifasApartamento(aptoEncontrado, precioBaja, precioAlta);
                            JOptionPane.showMessageDialog(null, "¡Tarifas actualizadas correctamente para el apartamento: " + aptoEncontrado.getCodigo() + "!");
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "⚠ Debes ingresar valores numéricos válidos para los precios.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún apartamento con el código: " + codigo, "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    public void definirTarifasApartamento(Apartamento apto, long precioBaja, long precioAlta) {
        if (apto != null) {
            apto.setPrecioBaja(precioBaja);
            apto.setPrecioAlta(precioAlta);
        }
    }

    public boolean agregarTemporada(List<Temporada> listaTemporadas, Temporada nuevaTemporada) {
        if (nuevaTemporada != null && !listaTemporadas.contains(nuevaTemporada)) {
            listaTemporadas.add(nuevaTemporada);
            return true;
        }
        return false;
    }

    //Bloqueo de aptos
    public boolean registrarBloqueo(Apartamento apto, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        if (apto != null && fechaInicio != null && fechaFin != null && !fechaFin.isBefore(fechaInicio)) {
            Bloqueo nuevoBloqueo = new Bloqueo(fechaInicio, fechaFin, motivo);
            apto.getListBloqueo().add(nuevoBloqueo);
            return true;
        }
        return false;
    }

    public void gestionarBloqueos(List<Apartamento> listaApartamentos) {
        String codigo = JOptionPane.showInputDialog("Ingresa el código del apartamento a bloquear:");
        if (codigo == null || codigo.trim().isEmpty()) {
            return;
        }

        Apartamento aptoEncontrado = null;
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                aptoEncontrado = a;
                break;
            }
        }

        if (aptoEncontrado == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró ningún apartamento con el código: " + codigo, "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fInicio = JOptionPane.showInputDialog("Fecha de inicio del bloqueo (AAAA-MM-DD):");
        String fFin = JOptionPane.showInputDialog("Fecha de fin del bloqueo (AAAA-MM-DD):");
        String motivo = JOptionPane.showInputDialog("Motivo de indisponibilidad:");

        if (fInicio != null && fFin != null && motivo != null) {
            try {
                LocalDate fechaInicio = LocalDate.parse(fInicio.trim());
                LocalDate fechaFin = LocalDate.parse(fFin.trim());

                if (fechaFin.isBefore(fechaInicio)) {
                    JOptionPane.showMessageDialog(null, "⚠ La fecha final no puede ser anterior a la fecha inicial.", "Error de fechas ⚠", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (registrarBloqueo(aptoEncontrado, fechaInicio, fechaFin, motivo.trim())) {
                    JOptionPane.showMessageDialog(null, "¡Fechas fuera de servicio registradas con éxito!\n\n" +
                            "❤ Apartamento: " + aptoEncontrado.getCodigo() + "\n" +
                            "❤ Período no disponible desde: " + fechaInicio + " hasta: " + fechaFin + "\n" +
                            "❤ Motivo registrado: " + motivo.trim());
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ No se pudo registrar el bloqueo", "Error ⚠", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "⚠ Formato de fecha inválido. El formato es AAAA-MM-DD.", "Error de entrada ⚠", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Reportes
    public long calcularNochesOcupadasApartamento(Apartamento apto, List<Reserva> listaReservas, LocalDate fechaInicio, LocalDate fechaFin) {
        long nochesOcupadas = 0;
        for (Reserva r : listaReservas) {
            if (r.getApartamento().getCodigo().equals(apto.getCodigo()) && r.estaActiva()) {
                if (r.cruceCon(fechaInicio, fechaFin)) {
                    LocalDate actual = r.getFechaEntrada();
                    while (actual.isBefore(r.getFechaSalida())) {
                        if (!actual.isBefore(fechaInicio) && actual.isBefore(fechaFin)) {
                            nochesOcupadas++;
                        }
                        actual = actual.plusDays(1);
                    }
                }
            }
        }
        return nochesOcupadas;
    }

    public long calcularIngresosPeriodo(List<Reserva> listaReservas, LocalDate fechaInicio, LocalDate fechaFin) {
        long ingresosTotales = 0;
        for (Reserva r : listaReservas) {
            if (!r.getEstado().equals(EstadoReserva.CANCELADA)) {
                if (!r.getFechaEntrada().isBefore(fechaInicio) && !r.getFechaSalida().isAfter(fechaFin)) {
                    ingresosTotales += (long) r.getTotalAbonado();
                }
            }
        }
        return ingresosTotales;
    }

    public int contarReservasPorCanal(List<Reserva> listaReservas, Canal canalBuscado) {
    // Reporte 3: Conteo de reservas filtradas por canal (incluyendo canceladas y no-show)
        int contador = 0;
        for (Reserva r : listaReservas) {
            if (r.getCanal() == canalBuscado) {
                contador++;
            }
        }
        return contador;
    }


    // Reporte 4: Obtener lista de reservas activas con saldo pendiente en folio
    public List<Reserva> obtenerReservasSaldoPendiente(List<Reserva> listaReservas) {
        List<Reserva> conSaldo = new ArrayList<>();
        for (Reserva r : listaReservas) {
            if (r.estaActiva() && r.getSaldoFolio() > 0) {
                conSaldo.add(r);
            }
        }
        return conSaldo;
    }

    // Reporte 5: Identificar el apartamento que registra la mayor cantidad de novedades
    public Apartamento obtenerApartamentoNovedades(List<Apartamento> listaApartamentos) {
        if (listaApartamentos == null || listaApartamentos.isEmpty()) {
            return null;
        }
        Apartamento aptoMasNovedades = listaApartamentos.get(0);
        int maxNovedades = aptoMasNovedades.getListNovedad().size();

        for (int i = 1; i < listaApartamentos.size(); i++) {
            Apartamento actual = listaApartamentos.get(i);
            int cantNovedadesActual = actual.getListNovedad().size();
            if (cantNovedadesActual > maxNovedades) {
                maxNovedades = cantNovedadesActual;
                aptoMasNovedades = actual;
            }
        }
        return aptoMasNovedades;
    }
    public void reporteOcupacionApartamento(List<Apartamento> listaApartamentos, List<Reserva> listaReservas) {
        String codigo = JOptionPane.showInputDialog("Ingresa el código del apartamento:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        Apartamento aptoEncontrado = null;
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equalsIgnoreCase(codigo.trim())) {
                aptoEncontrado = a;
                break;
            }
        }

        if (aptoEncontrado == null) {
            JOptionPane.showMessageDialog(null, "⚠ No se encontró el apartamento con código: " + codigo, "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fInicio = JOptionPane.showInputDialog("Fecha inicio (AAAA-MM-DD):");
        String fFin = JOptionPane.showInputDialog("Fecha fin (AAAA-MM-DD):");

        if (fInicio != null && fFin != null) {
            try {
                LocalDate inicio = LocalDate.parse(fInicio.trim());
                LocalDate fin = LocalDate.parse(fFin.trim());

                long noches = calcularNochesOcupadasApartamento(aptoEncontrado, listaReservas, inicio, fin);
                JOptionPane.showMessageDialog(null, "El apartamento " + aptoEncontrado.getCodigo() + " tiene " + noches + " noches ocupadas en el periodo.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "⚠ Formato de fecha inválido. Usa AAAA-MM-DD", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void reporteIngresosPeriodo(List<Reserva> listaReservas) {
        String fInicio = JOptionPane.showInputDialog("Fecha inicio (AAAA-MM-DD):");
        String fFin = JOptionPane.showInputDialog("Fecha fin (AAAA-MM-DD):");

        if (fInicio != null && fFin != null) {
            try {
                LocalDate inicio = LocalDate.parse(fInicio.trim());
                LocalDate fin = LocalDate.parse(fFin.trim());

                long ingresos = calcularIngresosPeriodo(listaReservas, inicio, fin);
                JOptionPane.showMessageDialog(null, "Ingresos totales en el período: $" + ingresos);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "⚠ Formato de fecha inválido. Usa AAAA-MM-DD", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void reporteReservasPorCanal(List<Reserva> listaReservas) {
        Canal[] canales = Canal.values();
        Canal canalSeleccionado = (Canal) JOptionPane.showInputDialog(null,
                "Selecciona el canal de reserva:",
                "Reservas por Canal",
                JOptionPane.QUESTION_MESSAGE, null, canales, canales[0]);

        if (canalSeleccionado != null) {
            int total = contarReservasPorCanal(listaReservas, canalSeleccionado);
            JOptionPane.showMessageDialog(null, "Total de reservas realizadas vía " + canalSeleccionado + ": " + total);
        }
    }
    public void reporteSaldosPendientes(List<Reserva> listaReservas) {
        List<Reserva> pendientes = obtenerReservasSaldoPendiente(listaReservas);
        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay reservas activas con saldo pendiente.");
            return;
        }

        String mensaje = " ‧₊˚✧ Reservas con saldo pendiente ✧˚₊‧ \n\n";
        for (Reserva r : pendientes) {
            mensaje += "Código: " + r.getCodigoReserva() +
                    " | Apartamento: " + r.getApartamento().getCodigo() +
                    " | Saldo Pendiente: $" + r.getSaldoFolio() + "\n";
        }
        JOptionPane.showMessageDialog(null, mensaje);
    }


    public void reporteApartamentoConMasNovedades(List<Apartamento> listaApartamentos) {
        Apartamento apto = obtenerApartamentoNovedades(listaApartamentos);
        if (apto == null) {
            JOptionPane.showMessageDialog(null, "No hay apartamentos registrados.");
        } else {
            JOptionPane.showMessageDialog(null, "El apartamento con más novedades es: " + apto.getCodigo() +
                    " (" + apto.getNombre() + ") con " + apto.getListNovedad().size() + " novedades.");
        }
    }

    //PoliticaCancelacion
    public void configurarPoliticaCancelacionMenu() {
        String diasTotalStr = JOptionPane.showInputDialog(null,
                "Días de anticipación requeridos para la devolución total del dinero (100%):",
                politicaCancelacion.getDiasDevolucionTotal());
        String diasParcialStr = JOptionPane.showInputDialog(null,
                "Días mínimos de anticipación para aplicar devolución parcial (Retencion total)ejemplo: 2 equivale a 48 horas:",
                politicaCancelacion.getDiasDevolucionParcial());
        String pctRetencionStr = JOptionPane.showInputDialog(null,
                "Porcentaje de retención para cancelaciones parciales (ejemplo 0.50 para 50%):",
                politicaCancelacion.getPorcentajeRetencion());

        if (diasTotalStr != null && diasParcialStr != null && pctRetencionStr != null) {
            try {
                int diasTotal = Integer.parseInt(diasTotalStr.trim());
                int diasParcial = Integer.parseInt(diasParcialStr.trim());
                double pctRetencion = Double.parseDouble(pctRetencionStr.trim());

                PoliticaCancelacion nuevaPolitica = new PoliticaCancelacion(diasTotal, diasParcial, pctRetencion);

                if (configurarPoliticaCancelacion(nuevaPolitica)) {
                    JOptionPane.showMessageDialog(null, "꧁ Política de cancelación actualizada con éxito ꧂\n\n" +
                            "✦ Más de " + diasTotal + " días: Devolución del 100%\n" +
                            "✦ Entre " + diasParcial + " y " + diasTotal + " días la retención es del " + (pctRetencion * 100) + "%\n" +
                            "✦ Menos de " + diasParcial + " días / No-show: Retención del 100%");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "⚠ Debes ingresar valores numéricos válidos.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Configuración de la política de cancelación
    public boolean configurarPoliticaCancelacion(PoliticaCancelacion nuevaPolitica) {
        if (nuevaPolitica != null) {
            this.politicaCancelacion = nuevaPolitica;
            return true;
        }
        return false;
    }
    public PoliticaCancelacion getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    // Getters y Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasenia; }
    public void setContrasena(String contrasena) { this.contrasenia = contrasena; }
}