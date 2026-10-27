package co.edu.uniquindio;

import java.time.LocalDate;

public  abstract class Empleado extends Persona {
    private String usuario;
    private String contrasenia;

    public Empleado(String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(cedula, nombre, fechaNacimiento, telefono, correo);
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }
    public String getUsuario() {return usuario;}
    public void setUsuario(String usuario) {this.usuario = usuario;}
    public String getContrasenia() {return contrasenia;}
    public void setContrasenia(String contrasenia) {this.contrasenia = contrasenia;}
}
