package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Recepcionista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class MenuRecepcionista extends JDialog {
    private JPanel contentPane;

    private JButton btnDirecto;
    private JButton btnExterno;
    private JButton btnConfirmacion;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    private JButton btnPagos;
    private JButton btnCambioEstado;
    private JButton btnNovedades;
    private JButton btnNoShow;
    private JButton buttonCancel;

    private Empresa empresa;
    private Empleado recepcionistaActual;

    public MenuRecepcionista(JFrame parent, Empresa empresa, Empleado recepcionistaActual) {
        super(parent, "Menú Recepcionista", true);
        this.empresa = empresa;
        this.recepcionistaActual = recepcionistaActual;

        if (contentPane != null) {
            setContentPane(contentPane);
        }

        asignarIconosABotones();
        initListeners();

        setSize(950, 580);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void asignarIconosABotones() {
        configurarBoton(btnDirecto, "/registro.png", "Reservas canal directo");
        configurarBoton(btnExterno, "/reserva-Externa.png", "Reserva canal externo");
        configurarBoton(btnConfirmacion, "/reserva.png", "Confirmar reserva");
        configurarBoton(btnCheckIn, "/check-in.png", "Check-In");
        configurarBoton(btnCheckOut, "/check-out.png",  "Check-Out");
        configurarBoton(btnPagos, "/registro-Pago.png", "Registro de pagos y cargos");
        configurarBoton(btnCambioEstado, "/cambioApto.png","Cambio estado de apartamentos");
        configurarBoton(btnNovedades, "/registro-novedades.png", "Novedades");
        configurarBoton(btnNoShow, "/no-show.png", "Declaración No-Show");
        configurarBoton(buttonCancel, "/cerrar-sesion.png", "Cerrar Sesión/Volver");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
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

    private void initListeners() {
        Recepcionista recep = null;
        if (recepcionistaActual instanceof Recepcionista) {
            recep = (Recepcionista) recepcionistaActual;
        }

        final Recepcionista recepFinal = recep;

        // 1. Reservas Canal Directo
        if (btnDirecto != null) {
            btnDirecto.addActionListener(e -> {
                new ReservaDirectaRecepcionista(this, empresa, recepFinal).setVisible(true);
            });
        }

        // 2. Reservas Canal Externo
        if (btnExterno != null) {
            btnExterno.addActionListener(e -> {
                new ReservaExternaRecepcionista(this, empresa).setVisible(true);
            });
        }

        // 3. Confirmación de Reservas
        if (btnConfirmacion != null) {
            btnConfirmacion.addActionListener(e -> {
                new ConfirmarReservaRecepcionista(this, empresa).setVisible(true);
            });
        }

        // 4. Check-In
        if (btnCheckIn != null) {
            btnCheckIn.addActionListener(e -> {
                new CheckInRecepcionista(this, empresa).setVisible(true);
            });
        }

        // 5. Check-Out
        if (btnCheckOut != null) {
            btnCheckOut.addActionListener(e -> {
                new CheckOutRecepcionista(this, empresa).setVisible(true);
            });
        }

        // 6. Registro de Pagos y Cargos
        if (btnPagos != null) {
            btnPagos.addActionListener(e -> {
                new FolioPagosRecepcionista(this, empresa).setVisible(true);
            });
        }

        // 7. Cambio Estado Apartamentos
        if (btnCambioEstado != null) {
            btnCambioEstado.addActionListener(e -> {
                new ControlOperativoRecepcionista(this, empresa).setVisible(true);
            });
        }

        // 8. Novedades
        if (btnNovedades != null) {
            btnNovedades.addActionListener(e -> {
                new RegistrarNovedadRecepcionista(this, empresa, recepFinal).setVisible(true);
            });
        }

        // 9. No-Show
        if (btnNoShow != null) {
            btnNoShow.addActionListener(e -> {
                new DeclararNoShowRecepcionista(this, empresa).setVisible(true);
            });
        }

        // Volver / Cerrar Sesión
        if (buttonCancel != null) {
            buttonCancel.addActionListener(e -> dispose());
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}