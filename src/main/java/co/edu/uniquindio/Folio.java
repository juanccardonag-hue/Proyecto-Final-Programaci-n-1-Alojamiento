package co.edu.uniquindio;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Folio {
    private String idFolio;
    private List<Cargo> listaCargo;
    private List<Pago> listaPago;

    public Folio(String idFolio) {
        this.idFolio = idFolio;
        this.listaCargo = new ArrayList<>();
        this.listaPago = new ArrayList<>();
    }
    //Agregamos cargo
    public void agregarCargo(String concepto, long monto) {
        Cargo nuevoCargo = new Cargo(concepto,monto, LocalDateTime.now());
        this.listaCargo.add(nuevoCargo);
    }
    //Registramos un pago
    public void registrarPago(long monto, MedioPago medioPago) {
        Pago nuevoPago = new Pago(monto, medioPago, LocalDateTime.now());
        this.listaPago.add(nuevoPago);
    }

    //Cuentas generales del Folio
    public long calcularTotalCargos() {
        long total = 0;
        for (int i = 0; i < listaCargo.size(); i++) {
            total = total + listaCargo.get(i).monto();
        }
        return total;
    }

    public long calcularTotalPagos() {
        long total = 0;
        for (int i = 0; i < listaPago.size(); i++) {
            total = total + listaPago.get(i).monto(); // Usa .monto() sin el 'get'
        }
        return total;
    }

    public long calcularSaldo() {
        return calcularTotalCargos() - calcularTotalPagos();
    }

    public String getIdFolio() { return idFolio; }
    public List<Pago> getListaPago() { return listaPago; }
    public List<Cargo> getListaCargo() { return listaCargo; }

}