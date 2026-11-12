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

        setSize(480, 360);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        configurarBoton(iniciarSesiónButton, "Iniciar Sesión");
        configurarBoton(registrarseButton, "Registrarse");
        configurarBoton(volverButton, "Volver");
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);
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