package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class MenuAdmin extends JDialog {
    private JPanel mainPanel;
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
        setSize(1000, 750);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initListeners();
        icons();
    }

    private void initListeners() {
        Administrador admin = empresa.getAdministrador();

        //gestión
        btnEmpleados.addActionListener(e -> {
            MenuGestionEmpleado menuEmp = new MenuGestionEmpleado(this, empresa);
            menuEmp.setVisible(true);
        });
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

    public void icons() {
        // ---- SECCIÓN DE GESTIÓN ----
        configurarBoton(btnEmpleados, "/employees.png",
                "Gestionar Empleados",
                "Contratación<br>Edición de datos<br>Desvinculación");

        configurarBoton(btnApartamentos, "/departamento.png",
                "Modificar Apartamentos",
                "Creaciónr<br>Modificación<br>Eliminación");

        configurarBoton(btnTemporadas, "/calendar.png",
                "Fechas y Costos",
                "Fechas de temporada<br>Tarifas por apartamento<br>⠀ ⠀ ⠀ ⠀ ⠀");

        configurarBoton(btnPolitica, "/politica.png",
                "Política Cancelación",
                "<br>Reglas de reembolso<br> ⠀ ⠀ ⠀ ");

        configurarBoton(btnBloqueos, "/bloqueo.png",
                "Bloqueo de Fechas",
                "Motivos<br>Bloqueo por agenda<br> ⠀ ⠀ ⠀ ");

        configurarBoton(btnControlAcceso, "/llave.png",
                "Control de Acceso",
                "Permisos de usuario<br>Seguridad<br> ⠀ ⠀ ⠀ ");

        configurarBoton(btnReporteOcupacion, "/ocupado.png",
                "Ocupación por Apto",
                "Estadísticas<br>Porcentaje de uso<br>");

        configurarBoton(btnReporteIngresos, "/dinero.png",
                "Ingresos por Período",
                "Balance financiero<br>Recaudo<br>");

        configurarBoton(btnReporteCanal, "/web.png",
                "Reservas por Canal",
                "<br>Canal directo<br>");

        configurarBoton(btnReporteSaldos, "/pago.png",
                "Saldos Pendientes",
                "Cuentas por cobrar<br>Pagos incompletos");

        configurarBoton(btnReporteNovedades, "/novedades.png",
                "Apartamentos Novedades",
                "Reportes de daños<br>Mantenimientos");

        configurarBoton(btnCerrarSesion, "/cerrar-sesion.png",
                "<br>Cerrar sesion",
                "Volver al inicio");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo, String descripcion) {
        if (boton == null) return;

        int tamNormal = 48;
        int tamZoom = 54;

        Image imgBase = null;
        try {
            ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
            imgBase = iconOriginal.getImage();
            boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormal, tamNormal, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            System.err.println("No se encontró la imagen: " + rutaIcono);
        }

        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);

        boton.setMargin(new Insets(15, 10, 10, 10));

        String html = "<html><center><font face='Vensim Sans Mono'>"
                + "<b><font size='4' color='#2C3E50'>" + titulo + "</font></b><br>"
                + "<font size='2' color='gray'>" + descripcion + "</font>"
                + "</font></center></html>";

        boton.setText(html);

        final Image imagenOriginal = imgBase;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoom, tamZoom, Image.SCALE_SMOOTH)));
                }
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormal, tamNormal, Image.SCALE_SMOOTH)));
                }
            }
        });
    }
}