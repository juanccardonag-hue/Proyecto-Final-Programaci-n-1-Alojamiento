package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;

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

        setContentPane(mainPanel);
        setTitle("Gestión Aparta-Suites UQ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        welcomeTittle.setText("<html><div style='text-align: center; width: 450px;'> ¡Bienvenido/a!<br> Este es el sistema de gestión de alojamientos de Aparta-Suites UQ</div></html>");
        welcomeTittle.setHorizontalAlignment(SwingConstants.CENTER);

        initListeners();
    }

    private void initListeners() {
        adminBtn.addActionListener(e -> {
            LoginAdmin dialogLogin = new LoginAdmin(this, empresa);
            dialogLogin.setVisible(true); // Se muestra la ventana y espera a que ingrese datos
        });
        //huespedBtn.addActionListener(e -> empresa.abrirMenuHuesped());

        recepcionistaBtn.addActionListener(e -> empresa.abrirMenuRecepcionista());
    }
}
