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

        // ----------------------------------------------------
        // 1. INTEGRAR EL FONDO CON LA IMAGEN DE RESOURCES
        // ----------------------------------------------------
        // Reemplaza "/fondo.jpg" con el nombre exacto de tu archivo en resources (ej. "/fondo.png")
        PanelConFondo fondo = new PanelConFondo("/fondo.jpg");
        fondo.setLayout(new BorderLayout());

        // Hacemos el mainPanel transparente para que se vea la imagen detrás
        mainPanel.setOpaque(false);
        fondo.add(mainPanel, BorderLayout.CENTER);

        // Establecemos el panel con fondo como el panel principal de la ventana
        setContentPane(fondo);

        // Configuración de la ventana
        setTitle("Gestión Aparta-Suites UQ");
        setSize(850, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ----------------------------------------------------
        // 2. ESTILOS Y TEXTOS BONITOS EN PALETA ROSA
        // ----------------------------------------------------
        welcomeTittle.setText("<html><center>"
                + "<h1 style='color: #4A2E35; font-family: Segoe UI;'>¡Bienvenido/a!</h1>"
                + "<p style='color: #7A5A62; font-size: 13px;'>Sistema de gestión de alojamientos de <b>Aparta-Suites UQ</b></p>"
                + "</center></html>");
        welcomeTittle.setHorizontalAlignment(SwingConstants.CENTER);

        if (rolTittle != null) {
            rolTittle.setText("<html><center><b style='color: #8C4A5A; font-size: 14px;'>Selecciona el rol a gestionar:</b></center></html>");
            rolTittle.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // Estilizar los 3 botones para que tengan ícono y no se corte el texto
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

        recepcionistaBtn.addActionListener(e -> empresa.abrirMenuRecepcionista());
    }

    // Método para darle diseño rosa translúcido a los botones del form
    private void estilizarBoton(JButton btn, String emoji, String texto) {
        if (btn == null) return;

        btn.setText("<html><center><font size='5'>" + emoji + "</font><br><b>" + texto + "</b></center></html>");
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Blanco ligeramente rosado y translúcido
        btn.setBackground(new Color(255, 245, 247, 230));
        btn.setForeground(new Color(74, 46, 53));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 165, 178), 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Efecto hover al pasar el ratón
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(253, 225, 233));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 245, 247, 230));
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}