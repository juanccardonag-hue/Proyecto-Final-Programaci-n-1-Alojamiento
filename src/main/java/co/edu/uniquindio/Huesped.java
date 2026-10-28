package co.edu.uniquindio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Huesped extends Persona implements Serializable {

    private List<Reserva> misReservas;

    public Huesped(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        this.misReservas = new ArrayList<>();
    }

    // Actualiza los datos de perfil del huésped
    public void actualizarPerfil(String nuevoNombre, LocalDate nuevaFecha, String nuevoTelefono, String nuevoCorreo) {
        setNombre(nuevoNombre);
        setFechaNacimiento(nuevaFecha);
        setTelefono(nuevoTelefono);
        setCorreo(nuevoCorreo);
    }

    // Agrega una reserva al historial evitando duplicados
    public boolean agregarReservaHistorial(Reserva reserva) {
        if (reserva != null && !misReservas.contains(reserva)) {
            this.misReservas.add(reserva);
            return true;
        }
        return false;
    }

    public List<Reserva> getMisReservas() {
        return misReservas;
    }
}