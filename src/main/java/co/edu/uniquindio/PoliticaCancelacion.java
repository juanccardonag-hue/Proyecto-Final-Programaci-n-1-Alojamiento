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

    // Getters y Setters

    public int getDiasDevolucionTotal() {
        return diasDevolucionTotal;
    }
    public void setDiasDevolucionTotal(int diasDevolucionTotal) {
        this.diasDevolucionTotal = diasDevolucionTotal;
    }
    public int getDiasDevolucionParcial() {
        return diasDevolucionParcial;
    }
    public void setDiasDevolucionParcial(int diasDevolucionParcial) {
        this.diasDevolucionParcial = diasDevolucionParcial;
    }
    public double getPorcentajeRetencion() {
        return porcentajeRetencion;
    }
    public void setPorcentajeRetencion(double porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
    }
}