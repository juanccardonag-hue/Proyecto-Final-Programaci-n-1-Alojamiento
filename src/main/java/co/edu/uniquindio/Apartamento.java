package co.edu.uniquindio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Apartamento implements Serializable {

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
            Novedad novedad = new Novedad(LocalDateTime.now(), detalleCompleto);
            this.listNovedad.add(novedad);
        }
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getNumeroDormitorio() { return numeroDormitorio; }
    public void setNumeroDormitorio(int numeroDormitorio) { this.numeroDormitorio = numeroDormitorio; }

    public int getCapaMaxPersona() { return capaMaxPersona; }
    public void setCapaMaxPersona(int capaMaxPersona) { this.capaMaxPersona = capaMaxPersona; }

    public String getCaracteristica() { return caracteristica; }
    public void setCaracteristica(String caracteristica) { this.caracteristica = caracteristica; }

    public long getTarifaPersonaNoche() { return tarifaPersonaNoche; }
    public void setTarifaPersonaNoche(long tarifaPersonaNoche) { this.tarifaPersonaNoche = tarifaPersonaNoche; }

    public EstadoOperativo getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(EstadoOperativo estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public String getMotivoFueraServicio() { return motivoFueraServicio; }
    public void setMotivoFueraServicio(String motivoFueraServicio) { this.motivoFueraServicio = motivoFueraServicio; }

    public List<Bloqueo> getListBloqueo() { return listBloqueo; }
    public List<Novedad> getListNovedad() { return listNovedad; }
}