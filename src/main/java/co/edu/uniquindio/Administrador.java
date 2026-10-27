package co.edu.uniquindio;

import java.time.LocalDate;

public class Administrador  extends Empleado{
    public Administrador(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo, usuario, contrasenia);
    }
}
