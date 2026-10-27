package co.edu.uniquindio;

public class Folio {
    private String idFolio;
    private int saldo;

    public Folio(String idFolio, int saldo) {
        this.idFolio = idFolio;
        this.saldo = saldo;
    }

    public String getIdFolio() {return idFolio;}
    public void setIdFolio(String idFolio) {this.idFolio = idFolio;}
    public int getSaldo() {return saldo;}
    public void setSaldo(int saldo) {this.saldo = saldo;}
}
