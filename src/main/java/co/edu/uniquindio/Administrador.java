package co.edu.uniquindio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Empleado implements Serializable {

    private String usuario;
    private String contrasena;

    public Administrador(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasena) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // seguridad y autenticacion

    public boolean autenticar(String usuarioIngresado, String contrasenaIngresada) {
        if (usuarioIngresado != null && contrasenaIngresada != null) {
            return this.usuario.equals(usuarioIngresado) && this.contrasena.equals(contrasenaIngresada);
        }
        return false;
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


    // getters y setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}