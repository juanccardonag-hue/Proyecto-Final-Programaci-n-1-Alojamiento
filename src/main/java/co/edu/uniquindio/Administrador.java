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

    // --- Seguridad y Autenticación ---
    public boolean autenticar(String usuarioIngresado, String contrasenaIngresada) {
        if (usuarioIngresado == null || contrasenaIngresada == null) {
            return false;
        }
        return this.usuario.equalsIgnoreCase(usuarioIngresado.trim())
                && this.contrasenia.equals(contrasenaIngresada.trim());
    }

    // --- Control de Acceso y Credenciales ---
    public boolean cambiarCredenciales(String nuevoUsuario, String nuevaClave) {
        boolean actualizado = false;
        if (nuevoUsuario != null && !nuevoUsuario.trim().isEmpty()) {
            this.usuario = nuevoUsuario.trim();
            actualizado = true;
        }
        if (nuevaClave != null && !nuevaClave.trim().isEmpty()) {
            this.contrasenia = nuevaClave.trim();
            actualizado = true;
        }
        return actualizado;
    }

    public boolean registrarRecepcionista(List<Recepcionista> listaRecepcionistas, List<Empleado> listaEmpleados, Recepcionista nuevoRecepcionista) {
        if (nuevoRecepcionista == null || listaRecepcionistas == null || listaEmpleados == null) return false;

        // Registra al recepcionista como empleado y lo agrega a la lista con acceso al Login
        if (contratarEmpleado(listaEmpleados, nuevoRecepcionista)) {
            listaRecepcionistas.add(nuevoRecepcionista);
            return true;
        }
        return false;
    }

    public boolean registrarEmpleado(List<Empleado> listaEmpleados, Empleado nuevoEmpleado) {
        if (nuevoEmpleado == null || listaEmpleados == null) return false;
        return contratarEmpleado(listaEmpleados, nuevoEmpleado);
    }

    // --- Gestión de Apartamentos ---
    public Apartamento buscarApartamento(List<Apartamento> listaApartamentos, String codigo) {
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) return a;
        }
        return null;
    }

    public boolean crearApartamento(List<Apartamento> listaApartamentos, Apartamento nuevoApto) {
        if (nuevoApto == null) return false;
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equalsIgnoreCase(nuevoApto.getCodigo())) return false;
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

    public boolean cambiarEstadoApto(Apartamento apto, String motivo) {
        if (apto != null) {
            apto.cambiarEstado(EstadoOperativo.FUERA_SERVICIO, motivo);
            return true;
        }
        return false;
    }

    public boolean eliminarApartamento(List<Apartamento> listaApartamentos, Apartamento apto) {
        if (apto != null && listaApartamentos != null) {
            return listaApartamentos.remove(apto);
        }
        return false;
    }

    // --- Gestión de Personal ---
    public Empleado buscarEmpleado(List<Empleado> listaEmpleados, String cedula) {
        for (Empleado e : listaEmpleados) {
            if (e.getCedula().equalsIgnoreCase(cedula))
                return e;
        }
        return null;
    }
    public boolean contratarEmpleado(List<Empleado> listaEmpleados, Empleado nuevoEmpleado) {
        if (nuevoEmpleado == null || nuevoEmpleado.getCedula() == null) return false;

        for (Empleado e : listaEmpleados) {
            if (e.getCedula() != null && e.getCedula().equalsIgnoreCase(nuevoEmpleado.getCedula().trim())) {
                return false; // Ya existe por cédula
            }
        }

        listaEmpleados.add(nuevoEmpleado);
        return true;
    }

    public boolean registrarCredencialesRecepcionista(List<Empleado> listaEmpleados, List<Recepcionista> listaRecepcionistas, String cedula, String usuario, String contrasena) {
        Empleado emp = buscarEmpleado(listaEmpleados, cedula);
        if (emp == null) {
            return false;
        }

        if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }

        Recepcionista recep = new Recepcionista(
                emp.getCedula(),
                emp.getNombre(),
                LocalDate.now(),
                emp.getTelefono(),
                emp.getCorreo(),
                usuario.trim(),
                contrasena.trim()
        );

        listaRecepcionistas.add(recep);
        return true;
    }

    public boolean editarEmpleado(Empleado empleado, String nuevoNombre, String nuevoTelefono, String nuevoCorreo) {
        if (empleado != null && nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            empleado.setNombre(nuevoNombre.trim());

            if (nuevoTelefono != null) {
                empleado.setTelefono(nuevoTelefono.trim());
            } else {
                empleado.setTelefono("");
            }

            if (nuevoCorreo != null) {
                empleado.setCorreo(nuevoCorreo.trim());
            } else {
                empleado.setCorreo("");
            }
            return true;
        }
        return false;
    }

    public boolean desvincularEmpleado(List<Empleado> listaEmpleados, String cedula) {
        if (cedula == null) return false;
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getCedula().equalsIgnoreCase(cedula.trim())) {
                listaEmpleados.remove(i);
                return true;
            }
        }
        return false;
    }

    // --- Gestión de Tarifas y Temporadas ---
    public boolean agregarTemporada(List<Temporada> listaTemporadas, Temporada nuevaTemporada) {
        if (nuevaTemporada == null || listaTemporadas == null) return false;
        for (Temporada t : listaTemporadas) {
            if (t.cruceCon(nuevaTemporada.getFechaInicio(), nuevaTemporada.getFechaFin())) {
                return false;
            }
        }
        listaTemporadas.add(nuevaTemporada);
        return true;
    }

    public boolean actualizarTarifaBase(Apartamento apto, long nuevaTarifa) {
        if (apto != null && nuevaTarifa > 0) {
            apto.setTarifaPersonaNoche(nuevaTarifa);
            return true;
        }
        return false;
    }

    // --- Bloqueo de Apartamentos ---
    public boolean registrarBloqueo(Apartamento apto, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        if (apto != null && fechaInicio != null && fechaFin != null && !fechaFin.isBefore(fechaInicio)) {
            Bloqueo nuevoBloqueo = new Bloqueo(fechaInicio, fechaFin, motivo);
            apto.getListBloqueo().add(nuevoBloqueo);
            return true;
        }
        return false;
    }

    // --- Política de Cancelación ---

    public boolean configurarPoliticaCancelacion(PoliticaCancelacion nuevaPolitica) {
        if (nuevaPolitica != null) {
            this.politicaCancelacion = nuevaPolitica;
            return true;
        }
        return false;
    }

    // --- Reportes Avanzados ---
    public long calcularNochesOcupadasApartamento(Apartamento apto, List<Reserva> listaReservas, LocalDate fechaInicio, LocalDate fechaFin) {
        if (apto == null || listaReservas == null || fechaInicio == null || fechaFin == null) {
            return 0;
        }
        if (fechaInicio.isAfter(fechaFin)) {
            return 0;
        }
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
        if (listaReservas == null || fechaInicio == null || fechaFin == null) {
            return 0;
        }
        if (fechaInicio.isAfter(fechaFin)) {
            return 0;
        }
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
        if (listaReservas == null || canalBuscado == null) {
            return 0;
        }
        int contador = 0;
        for (Reserva r : listaReservas) {
            if (r.getCanal() == canalBuscado) {
                contador++;
            }
        }
        return contador;
    }

    public List<Reserva> obtenerReservasSaldoPendiente(List<Reserva> listaReservas) {
        List<Reserva> conSaldo = new ArrayList<>();
        if (listaReservas == null) {
            return conSaldo;
        }
        for (Reserva r : listaReservas) {
            if (r != null && r.estaActiva() && r.getSaldoFolio() > 0) {
                conSaldo.add(r);
            }
        }
        return conSaldo;
    }

    public Apartamento obtenerApartamentoNovedades(List<Apartamento> listaApartamentos) {
        if (listaApartamentos == null || listaApartamentos.isEmpty()) {
            return null;
        }

        Apartamento aptoMasNovedades = null;
        int maxNovedades = -1;

        for (Apartamento actual : listaApartamentos) {
            if (actual != null && actual.getListNovedad() != null) {
                int cantNovedadesActual = actual.getListNovedad().size();
                if (cantNovedadesActual > maxNovedades) {
                    maxNovedades = cantNovedadesActual;
                    aptoMasNovedades = actual;
                }
            }
        }
        return aptoMasNovedades;
    }

    private boolean codigoValido(String input) {
        return input != null && !input.trim().isEmpty();
    }

    // --- Getters y Setters ---
    public PoliticaCancelacion getPoliticaCancelacion() { return politicaCancelacion; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasenia; }
    public void setContrasena(String contrasena) { this.contrasenia = contrasena; }
}