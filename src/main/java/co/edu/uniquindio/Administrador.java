package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Persona {
    private String usuario;
    private String contrasenia;

    public Administrador(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
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

    // Gestión de personal y control de acceso
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
            String nuevoUsuario = JOptionPane.showInputDialog("Nuevo usuario (Tu usuario actualn es: " + this.usuario + ")");
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

    // Gestión de apartamentos
    public boolean crearApartamento(List<Apartamento> listaApartamentos, Apartamento nuevoApto) {
        if (nuevoApto == null) {
            return false;
        }
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equals(nuevoApto.getCodigo())) {
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

    public boolean registrarBloqueo(Apartamento apto, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        if (apto != null && fechaInicio != null && fechaFin != null && !fechaFin.isBefore(fechaInicio)) {
            Bloqueo nuevoBloqueo = new Bloqueo(fechaInicio, fechaFin, motivo);
            apto.getListBloqueo().add(nuevoBloqueo);
            return true;
        }
        return false;
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
        int contador = 0;
        for (Reserva r : listaReservas) {
            if (r.getCanal() == canalBuscado) {
                contador++;
            }
        }
        return contador;
    }

    public List<Reserva> obtenerReservasConSaldoPendiente(List<Reserva> listaReservas) {
        List<Reserva> conSaldo = new ArrayList<>();
        for (Reserva r : listaReservas) {
            if (r.estaActiva() && r.getSaldoFolio() > 0) {
                conSaldo.add(r);
            }
        }
        return conSaldo;
    }

    public Apartamento obtenerApartamentoConMasNovedades(List<Apartamento> listaApartamentos) {
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

    // Getters y Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasenia; }
    public void setContrasena(String contrasena) { this.contrasenia = contrasena; }
}