package co.edu.uniquindio;

import java.time.LocalDate;

public class Ocupante {
    private String nombre;
    private LocalDate fechaNacimiento;

    public Ocupante(String nombre, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
    }
    //Calculamos la edad de los ocupantes
    public int calcularEdad(LocalDate fechaEntrada) {
        if (fechaNacimiento == null || fechaEntrada == null) {
            return 0;
        }
        // Restamos los años
        int edad = fechaEntrada.getYear() - fechaNacimiento.getYear();
        // Verificamos si AÚN NO ha cumplido años en el año de la estancia:
        if (fechaEntrada.getMonthValue() < fechaNacimiento.getMonthValue() ||
                (fechaEntrada.getMonthValue() == fechaNacimiento.getMonthValue() && fechaEntrada.getDayOfMonth() < fechaNacimiento.getDayOfMonth())) {
            edad--;
        }
        return edad;
    }
    //verificamos si es pagante (regla mayor o igual a 6 años)
    public boolean esPagante(LocalDate fechaEntrada) {
        int edad = calcularEdad(fechaEntrada);
        if (edad >= 6) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {return nombre;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public LocalDate getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(LocalDate fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
}