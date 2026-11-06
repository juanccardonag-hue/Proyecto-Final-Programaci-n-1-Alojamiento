package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;

public class MenuAdmin extends JDialog {
    private JPanel mainPanel;
    private JLabel tittleGestion;
    private JLabel tittleReporte;
    private JButton btnEmpleados;
    private JButton btnApartamentos;
    private JButton btnTemporadas;
    private JButton btnPolitica;
    private JButton btnBloqueos;
    private JButton btnControlAcceso;
    private JButton btnReporteOcupacion;
    private JButton btnReporteIngresos;
    private JButton btnReporteCanal;
    private JButton btnReporteSaldos;
    private JButton btnReporteNovedades;
    private JButton btnCerrarSesion;

    private Empresa empresa;

    public MenuAdmin(JFrame parent, Empresa empresa) {
        super(parent, "Menú Administrador", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(550, 450);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void initListeners() {
        Administrador admin = empresa.getAdministrador();

        //gestión
        btnEmpleados.addActionListener(e -> admin.gestionarEmpleados(empresa.getListaEmpleados()));
        btnApartamentos.addActionListener(e -> admin.gestionarApartamentos(empresa.getListaApartamentos()));
        btnTemporadas.addActionListener(e -> admin.gestionarTemporadaTarifas(empresa.getListaApartamentos(), empresa.getListaTemporadas()));
        btnPolitica.addActionListener(e -> admin.configurarPoliticaCancelacionMenu());
        btnBloqueos.addActionListener(e -> admin.gestionarBloqueos(empresa.getListaApartamentos()));
        btnControlAcceso.addActionListener(e -> admin.gestionarControlAcceso(empresa.getListaRecepcionistas(), empresa.getListaEmpleados()));

        // Reportes
        btnReporteOcupacion.addActionListener(e -> admin.reporteOcupacionApartamento(empresa.getListaApartamentos(), empresa.getListaReservas()));
        btnReporteIngresos.addActionListener(e -> admin.reporteIngresosPeriodo(empresa.getListaReservas()));
        btnReporteCanal.addActionListener(e -> admin.reporteReservasPorCanal(empresa.getListaReservas()));
        btnReporteSaldos.addActionListener(e -> admin.reporteSaldosPendientes(empresa.getListaReservas()));
        btnReporteNovedades.addActionListener(e -> admin.reporteApartamentoConMasNovedades(empresa.getListaApartamentos()));

        // Salir
        btnCerrarSesion.addActionListener(e -> dispose());
    }
}