package co.edu.uniquindio;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Administrador extends Empleado {
    private String usuario;
    private String contrasenia;

    public Administrador(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo, usuario, contrasenia);
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    // gestion de personal

    public boolean contratarEmpleado(List<Empleado> listaEmpleados, Empleado nuevoEmpleado) {
        if (nuevoEmpleado == null) {
            return false;
        }
        for (Empleado e : listaEmpleados) {
            if (e.getCedula().equals(nuevoEmpleado.getCedula())) {
                return false; // Evita dobles por la cedula
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

    // gestion aptos

    public boolean crearApartamento(List<Apartamento> listaApartamentos, Apartamento nuevoApto) {
        if (nuevoApto == null) {
            return false;
        }
        for (Apartamento a : listaApartamentos) {
            if (a.getCodigo().equals(nuevoApto.getCodigo())) {
                return false; // Ya existe ese código
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

    // eliminacio logica, esta cambia el estado operativo a FUERA_SERVICIO con un motivo
    public boolean eliminarApto(Apartamento apto, String motivo) {
        if (apto != null) {
            apto.cambiarEstado(EstadoOperativo.FUERA_SERVICIO, motivo);
            return true;
        }
        return false;
    }

    // cuadrar tarifas y temporadfas

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

    // registar bloques en aoptos

    public boolean registrarBloqueo(Apartamento apto, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        if (apto != null && fechaInicio != null && fechaFin != null && !fechaFin.isBefore(fechaInicio)) {
            Bloqueo nuevoBloqueo = new Bloqueo(fechaInicio, fechaFin, motivo);
            apto.getListBloqueo().add(nuevoBloqueo);
            return true;
        }
        return false;
    }

        // resportes:
    // Reporte 1: Días ocupados por apartamento en un rango de fechas
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

    // Reporte 2: Ingresos totales por periodo acumulados desde los pagos en folio
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

    // Reporte 3: Conteo de reservas filtradas por canal (incluyendo canceladas y no-show)
    public int contarReservasPorCanal(List<Reserva> listaReservas, Canal canalBuscado) {
        int contador = 0;
        for (Reserva r : listaReservas) {
            if (r.getCanal() == canalBuscado) {
                contador++;
            }
        }
        return contador;
    }

    // Reporte 4: Obtener lista de reservas activas con saldo pendiente en folio
    public List<Reserva> obtenerReservasConSaldoPendiente(List<Reserva> listaReservas) {
        List<Reserva> conSaldo = new ArrayList<>();
        for (Reserva r : listaReservas) {
            if (r.estaActiva() && r.getSaldoFolio() > 0) {
                conSaldo.add(r);
            }
        }
        return conSaldo;
    }

    // Reporte 5: Identificar el apartamento que registra la mayor cantidad de novedades
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
    // Configuración de la política de cancelación
    public boolean configurarPoliticaCancelacion(PoliticaCancelacion nuevaPolitica) {
        if (nuevaPolitica != null) {
            // Establece o actualiza la política de cancelación activa del sistema
            return true;
        }
        return false;
    }

    // getters y setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasenia; }
    public void setContrasena(String contrasena) { this.contrasenia = contrasena; }
}