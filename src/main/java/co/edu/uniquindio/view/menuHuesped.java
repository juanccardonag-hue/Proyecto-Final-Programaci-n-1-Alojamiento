package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;

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

    public menuHuesped(JFrame parent, Empresa empresa) {
        super(parent, "Menú Huésped - Aparta-Suites UQ", true);
        this.empresa = empresa;

        setContentPane(contentPane);
        setSize(950, 750); // Tamaño para asegurar que la cuadrícula se vea completa y holgada
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
        icons();
    }

    private void initListeners() {
        btnPerfil.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Gestión Perfil");
        });

        // Evento para consultar disponibilidad por fechas
        btnDisponibilidad.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Disponibilidad Apartamento");
        });

        btnEstancia.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Cotizar Estancia");
        });

        btnReserva.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Reserva Portal");
        });

        btnPago.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Pagos");
        });

        btnConsulta.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Consultas");
        });

        btnCancelar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abrir módulo: Cancelación");
        });

        btnCerrar.addActionListener(e -> dispose());
    }

    public void icons() {
        configurarBoton(btnPerfil, "/perfil.png",
                "Gestión Perfil",
                "Información personal<br>Actualizar datos<br>⠀");

        // Configuración para la imagen /disponibilidad.png
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