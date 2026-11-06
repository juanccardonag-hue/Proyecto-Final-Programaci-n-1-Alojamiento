package co.edu.uniquindio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Apartamento {

    private String codigo;
    private String nombre;
    private int numeroDormitorio;
    private int capaMaxPersona;
    private String caracteristica;
    private long tarifaPersonaNoche;
    private EstadoOperativo estadoOperativo;
    private String motivoFueraServicio;
    private List<Bloqueo> listBloqueo;
    private List<Novedad> listNovedad;
    private long precioBaja;
    private long precioAlta;

    public Apartamento(String codigo, String nombre, int numeroDormitorio, int capaMaxPersona, String caracteristica, long tarifaPersonaNoche) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroDormitorio = numeroDormitorio;
        this.capaMaxPersona = capaMaxPersona;
        this.caracteristica = caracteristica;
        this.tarifaPersonaNoche = tarifaPersonaNoche;
        this.estadoOperativo = EstadoOperativo.PREPARADO;
        this.motivoFueraServicio = null;
        this.listBloqueo = new ArrayList<>();
        this.listNovedad = new ArrayList<>();
        this.precioBaja = tarifaPersonaNoche;
        this.precioAlta = (long) (tarifaPersonaNoche * 1.5);
    }

    public Apartamento(String codigo, String nombre, int numeroDormitorio, int capaMaxPersona, String caracteristica, long tarifaPersonaNoche, long precioBaja, long precioAlta) {
        this(codigo, nombre, numeroDormitorio, capaMaxPersona, caracteristica, tarifaPersonaNoche);
        this.precioBaja = precioBaja;
        this.precioAlta = precioAlta;
    }

    //Cambiamos el estado del apartamento
    public void cambiarEstado(EstadoOperativo nuevoEstado, String motivo) {
        this.estadoOperativo = nuevoEstado;
        if (nuevoEstado == EstadoOperativo.FUERA_SERVICIO) {
            this.motivoFueraServicio = motivo;
        } else {
            this.motivoFueraServicio = null;
        }
    }
    // Registra una novedad
    public void registrarNovedad(String descripcion, String reportado) {
        if (descripcion != null) {
            String detalleCompleto = "Reportó (" + reportado + "): " + descripcion;
            Novedad novedad = new Novedad(detalleCompleto, LocalDateTime.now());
            this.listNovedad.add(novedad);
        }
    }
    // Verifica si el apartamento está fuera de servicio o bloqueado por el administrador en un rango de fechas
    public boolean estaBloqueadoRango(LocalDate inicio, LocalDate fin) {
        if (this.estadoOperativo == EstadoOperativo.FUERA_SERVICIO) {
            return true;
        }
        if (this.listBloqueo != null) {
            for (Bloqueo b : listBloqueo) {
                // Comprueba si el rango solicitado se cruza con algún bloqueo
                if (inicio.isBefore(b.fechaFin()) && fin.isAfter(b.fechaInicio())) {
                    return true;
                }
            }
        }
        return false;
    }
    public int getCapacidadMaxima() { return this.capaMaxPersona; }
    public long getPrecioBaja() { return precioBaja; }
    public long getPrecioAlta() { return precioAlta; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getNumeroDormitorio() { return numeroDormitorio; }
    public int getCapaMaxPersona() { return capaMaxPersona; }
    public String getCaracteristica() { return caracteristica; }
    public long getTarifaPersonaNoche() { return tarifaPersonaNoche; }
    public void setTarifaPersonaNoche(long tarifaPersonaNoche) { this.tarifaPersonaNoche = tarifaPersonaNoche; }
    public EstadoOperativo getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(EstadoOperativo estadoOperativo) { this.estadoOperativo = estadoOperativo; }
    public String getMotivoFueraServicio() { return motivoFueraServicio; }
    public List<Bloqueo> getListBloqueo() { return listBloqueo; }
    public List<Novedad> getListNovedad() { return listNovedad; }
    public void setNumeroDormitorio(int numeroDormitorio) {this.numeroDormitorio = numeroDormitorio;}
    public void setCapaMaxPersona(int capaMaxPersona) {this.capaMaxPersona = capaMaxPersona;}
    public void setCaracteristica(String caracteristica) {this.caracteristica = caracteristica;}
    public void setMotivoFueraServicio(String motivoFueraServicio) {this.motivoFueraServicio = motivoFueraServicio;}
    public void setListBloqueo(List<Bloqueo> listBloqueo) {this.listBloqueo = listBloqueo;}
    public void setListNovedad(List<Novedad> listNovedad) {this.listNovedad = listNovedad;}
    public void setPrecioBaja(long precioBaja) {this.precioBaja = precioBaja;}
    public void setPrecioAlta(long precioAlta) {this.precioAlta = precioAlta;}
}