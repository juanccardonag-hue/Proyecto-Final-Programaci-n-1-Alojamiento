package co.edu.uniquindio;

public class Apartamento {

    private String codigo;
    private String nombre;
    private int numeroDormitorio;
    private int capaMaxPersona;
    private String caracteristica;
    private int tarifaPersonaNoche;

    public Apartamento(String codigo, String nombre, int numeroDormitorio, int capaMaxPersona, String caracteristica, int tarifaPersonaNoche) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroDormitorio = numeroDormitorio;
        this.capaMaxPersona = capaMaxPersona;
        this.caracteristica = caracteristica;
        this.tarifaPersonaNoche = tarifaPersonaNoche;
    }
    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public int getNumeroDormitorio() {return numeroDormitorio;}
    public void setNumeroDormitorio(int numeroDormitorio) {this.numeroDormitorio = numeroDormitorio;}
    public int getCapaMaxPersona() {return capaMaxPersona;}
    public void setCapaMaxPersona(int capaMaxPersona) {this.capaMaxPersona = capaMaxPersona;}
    public String getCaracteristica() {return caracteristica;}
    public void setCaracteristica(String caracteristica) {this.caracteristica = caracteristica;}
    public int getTarifaPersonaNoche() {return tarifaPersonaNoche;}
    public void setTarifaPersonaNoche(int tarifaPersonaNoche) {this.tarifaPersonaNoche = tarifaPersonaNoche;}
}
