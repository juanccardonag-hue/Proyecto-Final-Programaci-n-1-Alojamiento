package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class menuRecepcionista extends JDialog {
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

    public menuRecepcionista(JFrame parent, Empresa empresa, Empleado recepcionistaActual) {
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
        // Carga y asignación de imágenes desde la carpeta resources
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
            // Si la imagen no se encuentra, el botón conserva su texto normal
        }
    }

    private void initListeners() {
        // 1. Reservas Canal Directo
        if (btnDirecto != null) {
            btnDirecto.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 1: Registro de Reserva Directa", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 2. Reservas Canal Externo
        if (btnExterno != null) {
            btnExterno.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 2: Recepción de Reserva Externa", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 3. Confirmación de Reservas
        if (btnConfirmacion != null) {
            btnConfirmacion.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 3: Confirmación de Reservas Pendientes", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 4. Check-In
        if (btnCheckIn != null) {
            btnCheckIn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 4: Registro de Check-In", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 5. Check-Out
        if (btnCheckOut != null) {
            btnCheckOut.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 5: Registro de Check-Out", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 6. Registro de Pagos y Cargos
        if (btnPagos != null) {
            btnPagos.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 6: Pagos y Cargos al Folio", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 7. Cambio Estado Apartamentos
        if (btnCambioEstado != null) {
            btnCambioEstado.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 7: Cambio de Estado Operativo de Aptos", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 8. Novedades
        if (btnNovedades != null) {
            btnNovedades.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 8: Registrar Novedades en Apto", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        // 9. No-Show
        if (btnNoShow != null) {
            btnNoShow.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Módulo 9: Declarar No-Show", "Recepcionista", JOptionPane.INFORMATION_MESSAGE);
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