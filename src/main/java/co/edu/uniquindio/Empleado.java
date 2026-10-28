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
    public boolean autenticar(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasenia.equals(contrasenia);
    }
    public String getUsuario() {return usuario;}
    public String getContrasenia() {return contrasenia;}
}