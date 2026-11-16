package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class CheckInRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField txtCodigoReserva;
    private JButton btnBuscar;
    private JTextArea textDetalle;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Reserva reservaSeleccionada;

    public CheckInRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Registro de Check-In", true);
        this.empresa = empresa;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        }

        if (btnBuscar != null) {
            getRootPane().setDefaultButton(btnBuscar);
        }

        aplicarBordeRedondo(txtCodigoReserva);

        icons();
        initListeners();

        setSize(580, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(buttonCancel, "", "Cancelar");
        configurarBoton(buttonOK, "", "Confirmar Check-In");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom   = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setText(titulo);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setMargin(new Insets(5, 10, 5, 10));

        Image imgBase = null;
        if (rutaIcono != null && !rutaIcono.trim().isEmpty()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
                imgBase = iconOriginal.getImage();
                boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.err.println("No se encontró la imagen: " + rutaIcono);
            }
        }

        final Image imagenOriginal = imgBase;

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }

                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
            }
        });
    }

    private void aplicarBordeRedondo(JTextField campo) {
        if (campo == null) return;
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void initListeners() {
        if (btnBuscar != null) btnBuscar.addActionListener(e -> buscarReserva());
        if (buttonOK != null) buttonOK.addActionListener(e -> procesarCheckIn());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
    }

    private void buscarReserva() {
        String cod = txtCodigoReserva.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el folio/código de la reserva.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Reserva> listaReservas = null;
        if (empresa != null) {
            listaReservas = empresa.getListaReservas();
        }

        reservaSeleccionada = Recepcionista.buscarReservaPorFolio(listaReservas, cod);

        if (reservaSeleccionada != null) {
            LocalDate hoy = LocalDate.now();
            LocalDate entrada = reservaSeleccionada.getFechaEntrada();

            String nombreTitular = "N/A";
            String cedulaTitular = "N/A";
            if (reservaSeleccionada.getTitular() != null) {
                nombreTitular = reservaSeleccionada.getTitular().getNombre();
                cedulaTitular = reservaSeleccionada.getTitular().getCedula();
            }

            String nombreApto = "N/A";
            if (reservaSeleccionada.getApartamento() != null) {
                nombreApto = reservaSeleccionada.getApartamento().getNombre();
            }

            String detalle = " Folio: " + reservaSeleccionada.getCodigoFolio() + "\n" +
                    " Canal: " + reservaSeleccionada.getCanal() + "\n" +
                    " Estado Actual: " + reservaSeleccionada.getEstado() + "\n" +
                    " Titular: " + nombreTitular + "\n" +
                    " Cédula: " + cedulaTitular + "\n" +
                    " Apartamento: " + nombreApto + "\n" +
                    " Fechas: " + entrada + "  ->  " + reservaSeleccionada.getFechaSalida() + "\n" +
                    " Fecha Actual (Hoy): " + hoy + "\n";

            textDetalle.setText(detalle);

            if (reservaSeleccionada.getEstado() != EstadoReserva.CONFIRMADA) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Atención: La reserva está en estado '" + reservaSeleccionada.getEstado() + "'. Debe estar CONFIRMADA para realizar Check-In.",
                        "Reserva No Confirmada ⚠", JOptionPane.WARNING_MESSAGE);
            } else if (entrada != null && hoy.isBefore(entrada)) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Aún no es la fecha de ingreso.\nFecha programada de Check-In: " + entrada + " (Hoy es " + hoy + ").",
                        "Check-In Anticipado No Permitido ⚠", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            textDetalle.setText("");
            JOptionPane.showMessageDialog(this, "No se encontró ninguna reserva con el código: " + cod, "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarCheckIn() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Consulta y selecciona una reserva primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resultado = reservaSeleccionada.procesarCheckIn();

        if ("OK".equals(resultado)) {
            String nombreApto = "";
            if (reservaSeleccionada.getApartamento() != null) {
                nombreApto = reservaSeleccionada.getApartamento().getNombre();
            }

            JOptionPane.showMessageDialog(this,
                    "¡Check-In completado exitosamente!\nAcceso habilitado para el apartamento: " + nombreApto,
                    "Check-In Finalizado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "⚠ " + resultado,
                    "Check-In No Permitido ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}