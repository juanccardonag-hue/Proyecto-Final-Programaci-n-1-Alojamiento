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
        setSize(850, 580);
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

        estilizarBoton(adminBtn, "🔑", "Soy administrador");
        estilizarBoton(huespedBtn, "🧳", "Soy huésped");
        estilizarBoton(recepcionistaBtn, "👨‍💼", "Soy recepcionista");

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

    private void estilizarBoton(JButton btn, String emoji, String texto) {
        if (btn == null) return;

        btn.setText("<html><center><font size='5'>" + emoji + "</font><br><b>" + texto + "</b></center></html>");
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Blanco azulado suave
        btn.setBackground(new Color(245, 248, 252));
        btn.setForeground(new Color(27, 54, 93)); // #1B365D
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 205, 230), 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Efecto hover al pasar el mouse (se vuelve un azul más vivo al entrar y vuelve al original al salir)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 235, 248));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(245, 248, 252));
            }
        });
    }

    private void createUIComponents() {
    }
}