package co.edu.uniquindio;

public class PoliticaCancelacion {
    private int diasDevolucionTotal;
    private int diasDevolucionParcial;
    private double porcentajeRetencion;

    public PoliticaCancelacion() {
        this.diasDevolucionTotal = 7;
        this.diasDevolucionParcial = 2;
        this.porcentajeRetencion = 0.50;
    }
    public PoliticaCancelacion(int diasDevolucionTotal, int diasDevolucionParcial, double porcentajeRetencion) {
        this.diasDevolucionTotal = diasDevolucionTotal;
        this.diasDevolucionParcial = diasDevolucionParcial;
        this.porcentajeRetencion = porcentajeRetencion;
    }
    // Getters y Setters
    public int getDiasDevolucionTotal() {
        return diasDevolucionTotal;
    }
    public int getDiasDevolucionParcial() {
        return diasDevolucionParcial;
    }
    public void setDiasDevolucionTotal(int diasDevolucionTotal) {this.diasDevolucionTotal = diasDevolucionTotal;}
    public void setDiasDevolucionParcial(int diasDevolucionParcial) {this.diasDevolucionParcial = diasDevolucionParcial;}
    public void setPorcentajeRetencion(double porcentajeRetencion) {this.porcentajeRetencion = porcentajeRetencion;}
    public double getPorcentajeRetencion() {
        return porcentajeRetencion;
    }
}