package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OpcionesHuespedView extends JDialog {
    private JPanel contentPane;
    private JButton iniciarSesiónButton;
    private JButton registrarseButton;
    private JButton volverButton;

    private Empresa empresa;

    public OpcionesHuespedView(JFrame parent, Empresa empresa) {
        super(parent, "Portal del Huésped", true);
        this.empresa = empresa;

        if (contentPane != null) {
            contentPane.setOpaque(false);
            contentPane.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
            setContentPane(contentPane);
        }

        aplicarEstilos();
        initListeners();

        setSize(700, 550);
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    private void aplicarEstilos() {
        configurarBoton(iniciarSesiónButton, "/avatar.png", "Iniciar Sesión", "");
        configurarBoton(registrarseButton, "/invitado.png", "Registrarse", "");
        configurarBoton(volverButton, "/volver.png", "Volver", "");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo, String subtitulo) {
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
        if (iniciarSesiónButton != null) {
            iniciarSesiónButton.addActionListener(e -> {
                dispose();
                LoginHuesped login = new LoginHuesped((JFrame) getParent(), empresa);
                login.setVisible(true);
            });
        }

        if (registrarseButton != null) {
            registrarseButton.addActionListener(e -> {
                dispose();
                RegistroHuespedView registro = new RegistroHuespedView((JFrame) getParent(), empresa);
                registro.setVisible(true);
            });
        }

        if (volverButton != null) {
            volverButton.addActionListener(e -> dispose());
        }
    }
}