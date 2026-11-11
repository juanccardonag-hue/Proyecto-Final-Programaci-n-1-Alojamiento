package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeTittle;
    private JLabel rolTittle;
    private JButton adminBtn;
    private JButton recepcionistaBtn;
    private JButton huespedBtn;

    private Administrador admin;
    private Empresa empresa;

    public UI(Empresa empresa) {
        this.empresa = empresa;
        this.admin = empresa.getAdministrador();

        if (mainPanel != null) {
            mainPanel.setOpaque(true);
            mainPanel.setBackground(new Color(240, 244, 248)); // #F0F4F8
            setContentPane(mainPanel);
        }

        // Configuración de la ventana
        setTitle("Gestión Aparta-Suites UQ");
        setSize(950, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        welcomeTittle.setText("<html><center>"
                + "<h1 style='color: #1B365D; font-family: Segoe UI;'>¡Bienvenido/a!</h1>"
                + "<p style='color: #335C8D; font-size: 13px;'>Sistema de gestión de alojamientos de <b>Aparta-Suites UQ</b></p>"
                + "</center></html>");
        welcomeTittle.setHorizontalAlignment(SwingConstants.CENTER);

        if (rolTittle != null) {
            rolTittle.setText("<html><center><b style='color: #2C3E50; font-size: 14px;'>Selecciona el rol a gestionar:</b></center></html>");
            rolTittle.setHorizontalAlignment(SwingConstants.CENTER);
        }

        estilizarBoton(adminBtn, "/empleado.png", "Soy administrador");
        estilizarBoton(huespedBtn, "/invitado.png", "Soy huésped");
        estilizarBoton(recepcionistaBtn, "/recepcionista.png", "Soy recepcionista");

        initListeners();
    }

    private void initListeners() {
        adminBtn.addActionListener(e -> {
            LoginAdmin dialogLogin = new LoginAdmin(this, empresa);
            dialogLogin.setVisible(true);
        });

        huespedBtn.addActionListener(e -> {
            OpcionesHuespedView opciones = new OpcionesHuespedView(this, empresa);
            opciones.setVisible(true);
        });

        recepcionistaBtn.addActionListener(e -> {
            LoginRecepcionista loginRecepcion = new LoginRecepcionista(this, empresa);
            loginRecepcion.setVisible(true);
        });
    }

    private void estilizarBoton(JButton btn, String rutaIcono, String texto) {
        if (btn == null) return;

        // 1. Cargar e instalar el ícono desde resources
        if (rutaIcono != null && !rutaIcono.isEmpty()) {
            java.net.URL imgURL = getClass().getResource(rutaIcono);
            if (imgURL != null) {
                ImageIcon iconoOriginal = new ImageIcon(imgURL);
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(imgEscalada));
            } else {
                System.err.println("⚠ No se encontró el ícono en resources: " + rutaIcono);
            }
        }

        // 2. Asignar el texto y ubicarlo debajo del ícono
        btn.setText("<html><center><b>" + texto + "</b></center></html>");
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setFocusPainted(false);

        // 3. Configurar fuentes para el efecto zoom
        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        btn.setFont(fuenteNormal);

        // 4. Efecto zoom al pasar el mouse
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setFont(fuenteZoom);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setFont(fuenteNormal);
            }
        });
    }

    private void createUIComponents() {
    }
}