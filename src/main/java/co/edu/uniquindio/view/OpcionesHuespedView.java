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

        if (rutaIcono != null && !rutaIcono.isEmpty()) {
            java.net.URL imgURL = getClass().getResource(rutaIcono);
            if (imgURL != null) {
                ImageIcon iconoOriginal = new ImageIcon(imgURL);
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imgEscalada));
            } else {
                System.err.println("⚠ No se encontró el ícono en resources: " + rutaIcono);
            }
        }

        if (subtitulo != null && !subtitulo.isEmpty()) {
            boton.setText("<html><center><b>" + titulo + "</b><br><font size='2' color='#7F8C8D'>" + subtitulo + "</font></center></html>");
        } else {
            boton.setText(titulo);
        }

        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setVerticalTextPosition(SwingConstants.CENTER);
        boton.setIconTextGap(12);
        boton.setFocusPainted(false);

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 15);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setFont(fuenteNormal);
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