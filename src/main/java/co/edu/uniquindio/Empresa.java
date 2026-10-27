package co.edu.uniquindio;

public class Empresa {
    private String nit;
    private String nombreAlojamiento;

    public Empresa(String nit, String nombreAlojamiento) {
        this.nit = nit;
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public String getNit() {return nit;}
    public void setNit(String nit) {this.nit = nit;}
    public String getNombreAlojamiento() {return nombreAlojamiento;}
    public void setNombreAlojamiento(String nombreAlojamiento) {this.nombreAlojamiento = nombreAlojamiento;}
}
