package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.awt.*;

public class menuHuesped extends JDialog {
    private JPanel contentPane;
    private JButton btnPerfil;
    private JButton btnReserva;
    private JButton btnDisponibilidad;
    private JButton btnEstancia;
    private JButton btnPago;
    private JButton btnConsulta;
    private JButton btnCancelar;
    private JButton btnCerrar;

    private Empresa empresa;
    private Huesped huespedActual;

    public menuHuesped(JFrame parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Menú Huésped - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        setContentPane(contentPane);
        setSize(950, 750);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
        icons();
    }

    private void initListeners() {
        // 1. Gestión de Perfil
        btnPerfil.addActionListener(e -> {
            GestionPerfilHuesped perfilView = new GestionPerfilHuesped(this, empresa, huespedActual);
            perfilView.setVisible(true);
        });

        // 2. Disponibilidad de Apartamentos
        btnDisponibilidad.addActionListener(e -> {
            DisponibilidadAptoHuesped disponibilidadView = new DisponibilidadAptoHuesped(this, empresa, huespedActual);
            disponibilidadView.setVisible(true);
        });

        // 3. Cotizar Estancia
        btnEstancia.addActionListener(e -> {
            CotizarEstanciaHuesped cotizarView = new CotizarEstanciaHuesped(this, empresa, huespedActual);
            cotizarView.setVisible(true);
        });

        // 4. Reserva Portal (Ya corregido y conectado)
        btnReserva.addActionListener(e -> {
            ReservaPortalHuesped reservaView = new ReservaPortalHuesped(this, empresa, huespedActual);
            reservaView.setVisible(true);
        });

        // 5. Cartera de Pagos (Conectado con 30% y validación de reserva)
        btnPago.addActionListener(e -> {
            PagosHuesped pagosView = new PagosHuesped(this, empresa, huespedActual);
            pagosView.setVisible(true);
        });
        // 6. Consultas e historial
        btnConsulta.addActionListener(e -> {
            ConsultasHuesped consultasView = new ConsultasHuesped(this, empresa, huespedActual);
            consultasView.setVisible(true);
        });
        //cancelacion de reservas
        btnCancelar.addActionListener(e -> {
            CancelarReservaHuesped cancelarView = new CancelarReservaHuesped(this, empresa, huespedActual);
            cancelarView.setVisible(true);
        });

        btnCerrar.addActionListener(e -> dispose());
    }

    public void icons() {
        configurarBoton(btnPerfil, "/perfil.png",
                "Gestión Perfil",
                "Información personal<br>Actualizar datos<br>⠀");

        configurarBoton(btnDisponibilidad, "/disponibilidad.png",
                "Disponibilidad Apto",
                "Consultar por fechas<br>Filtrar capacidad<br>⠀");

        configurarBoton(btnEstancia, "/cotizar.png",
                "Cotizar Estancia",
                "Calcular tarifas<br>Fechas y capacidad<br>⠀");

        configurarBoton(btnReserva, "/reserva.png",
                "Reserva Portal",
                "Crear nueva reserva<br>Confirmar estancia<br>⠀");

        configurarBoton(btnPago, "/pago.png",
                "Pagos",
                "Realizar abonos<br>Métodos de pago<br>⠀");

        configurarBoton(btnConsulta, "/consulta.png",
                "Consultas",
                "Historial de reservas<br>Estado de cuenta<br>⠀");

        configurarBoton(btnCancelar, "/cancelar.png",
                "Cancelación",
                "Gestionar reservas<br>Políticas de reembolso<br>⠀");

        configurarBoton(btnCerrar, "/cerrar-sesion.png",
                "Cerrar Sesión",
                "Volver al inicio<br>⠀");
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
                + "<b><font size='4' color='#4A2E35'>" + titulo + "</font></b><br>"
                + "<font size='2' color='#7A5A62'>" + descripcion + "</font>"
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