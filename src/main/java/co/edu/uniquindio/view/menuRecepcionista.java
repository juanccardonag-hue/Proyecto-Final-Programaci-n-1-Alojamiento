package co.edu.uniquindio.view;

import co.edu.uniquindio.Empleado;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Recepcionista;

import javax.swing.*;
import java.awt.*;

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
    private Recepcionista recepcionistaLogueado;

    private Empresa empresa;
    private Recepcionista recepcionistaActual;

    public menuRecepcionista(JFrame parent, Empresa empresa, Empleado empleado) {
        super(parent, "Gestión Operativa - Recepción", true);
        this.empresa = empresa;

        if (empleado instanceof Recepcionista) {
            this.recepcionistaActual = (Recepcionista) empleado;
        } else if (empleado != null) {
            this.recepcionistaActual = new Recepcionista(empleado.getCedula(), empleado.getNombre(), empleado.getFechaNacimiento(), empleado.getTelefono(), empleado.getCorreo(), empleado.getUsuario(), empleado.getContrasenia());
        }
        setContentPane(contentPane);
        setSize(950, 750);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
        icons();
    }

    private void initListeners() {
        // 1. Reservas Canal Directo
        if (btnDirecto != null) {
            btnDirecto.addActionListener(e -> {
                ReservaDirectaRecepcionista ventanaDirecta = new ReservaDirectaRecepcionista(this, empresa, recepcionistaActual);
                ventanaDirecta.setVisible(true);
            });
        }
        // 2. Reservas Canal Externo
        if (btnExterno != null) {
            btnExterno.addActionListener(e -> {
                ReservaExternaRecepcionista ventanaExterna = new ReservaExternaRecepcionista(this, empresa);
                ventanaExterna.setVisible(true);
            });
        }
        // 3. Confirmación de Reservas Pendientes
        if (btnConfirmacion != null) {
            btnConfirmacion.addActionListener(e -> {
                ConfirmarReservaRecepcionista ventanaConfirmar = new ConfirmarReservaRecepcionista(this, empresa);
                ventanaConfirmar.setVisible(true);
            });
        }

        // 4. Check-In de Grupos / Individual
        if (btnCheckIn != null) {
            btnCheckIn.addActionListener(e -> {
                CheckInRecepcionista ventanaCheckIn = new CheckInRecepcionista(this, empresa);
                ventanaCheckIn.setVisible(true);
            });
        }
        // 5. Check-Out
        if (btnCheckOut != null) {
            btnCheckOut.addActionListener(e -> {
                CheckOutRecepcionista v = new CheckOutRecepcionista(this, empresa);
                v.setVisible(true);
            });
        }

        // 6. Registro de Pagos y Cargos (Folio)
        if (btnPagos!= null) {
            btnPagos.addActionListener(e -> {
                FolioPagosRecepcionista v = new FolioPagosRecepcionista(this, empresa);
                v.setVisible(true);
            });
        }

        // 7. Control Operativo de Apartamentos
        if (btnCambioEstado != null) {
            btnCambioEstado.addActionListener(e -> {
                ControlOperativoRecepcionista v = new ControlOperativoRecepcionista(this, empresa);
                v.setVisible(true);
            });
        }

        // 8. Registrar Novedad en Apartamento
        if (btnNovedades != null) {
            btnNovedades.addActionListener(e -> {
                RegistrarNovedadRecepcionista v = new RegistrarNovedadRecepcionista(this, empresa, recepcionistaLogueado);
                v.setVisible(true);
            });
        }

        // 9. Declarar No-Show
        if (btnNoShow != null) {
            btnNoShow.addActionListener(e -> {
                DeclararNoShowRecepcionista v = new DeclararNoShowRecepcionista(this, empresa);
                v.setVisible(true);
            });
        }

        // 10. Cerrar Sesión / Salir
        if (buttonCancel != null) {
            buttonCancel.addActionListener(e -> dispose());
        }
    }

    public void icons() {
        configurarBoton(btnDirecto, "/registro.png",
                "Canal Directo",
                "Registro presencial<br>Reserva directa<br>⠀");

        configurarBoton(btnExterno, "/reserva-Externa.png",
                "Canal Externo",
                "Booking / Airbnb<br>Validar plataforma<br>⠀");

        configurarBoton(btnConfirmacion, "/reserva.png",
                "Confirmación",
                "Validación de abono<br>Confirmar estancia<br>⠀");

        configurarBoton(btnCheckIn, "/check-in.png",
                "Check-In",
                "Registro entrada<br>Entrega llaves<br>⠀");

        configurarBoton(btnCheckOut, "/check-out.png",
                "Check-Out",
                "Registro salida<br>Cierre de folio<br>⠀");

        configurarBoton(btnPagos, "/registro-Pago.png",
                "Registro Pagos",
                "Cargos y consumo<br>Abonos a la cuenta<br>⠀");

        configurarBoton(btnCambioEstado, "/cambioApto.png",
                "Estado Aptos",
                "Mantenimiento<br>Limpieza y bloqueo<br>⠀");

        configurarBoton(btnNovedades, "/registro-novedades.png",
                "Novedades",
                "Reporte de daños<br>Incidencias<br>⠀");

        configurarBoton(btnNoShow, "/no-show.png",
                "Declaración No-Show",
                "Inasistencia<br>Penalizaciones<br>⠀");

        configurarBoton(buttonCancel, "/cerrar-sesion.png",
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