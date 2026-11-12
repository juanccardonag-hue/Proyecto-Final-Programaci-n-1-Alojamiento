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
        super(parent, "Menú Recepcionista - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.recepcionistaActual = recepcionistaActual;

        if (contentPane != null) {
            setContentPane(contentPane);
        }

        asignarIconosABotones();
        initListeners();

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void asignarIconosABotones() {
        configurarBotonConIcono(btnDirecto, "/registro.png");
        configurarBotonConIcono(btnExterno, "/reserva-Externa.png");
        configurarBotonConIcono(btnConfirmacion, "/reserva.png");
        configurarBotonConIcono(btnCheckIn, "/check-in.png");
        configurarBotonConIcono(btnCheckOut, "/check-out.png");
        configurarBotonConIcono(btnPagos, "/registro-Pago.png");
        configurarBotonConIcono(btnCambioEstado, "/cambioApto.png");
        configurarBotonConIcono(btnNovedades, "/registro-novedades.png");
        configurarBotonConIcono(btnNoShow, "/no-show.png");
        configurarBotonConIcono(buttonCancel, "/cerrar-sesion.png");
    }

    private void configurarBotonConIcono(JButton btn, String rutaImagen) {
        if (btn == null) return;
        try {
            URL imgURL = getClass().getResource(rutaImagen);
            if (imgURL != null) {
                ImageIcon iconOriginal = new ImageIcon(imgURL);
                Image imgEscalada = iconOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(imgEscalada));
                btn.setHorizontalTextPosition(SwingConstants.CENTER);
                btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            }
        } catch (Exception ignored) {
        }
    }

    private void initListeners() {
        Recepcionista recep = (recepcionistaActual instanceof Recepcionista)
                ? (Recepcionista) recepcionistaActual
                : null;

        // 1. Reservas Canal Directo
        if (btnDirecto != null) {
            btnDirecto.addActionListener(e -> {
                new ReservaDirectaRecepcionista(this, empresa, recep).setVisible(true);
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
                new RegistrarNovedadRecepcionista(this, empresa, recep).setVisible(true);
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