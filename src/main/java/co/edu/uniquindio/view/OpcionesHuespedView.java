package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;

import javax.swing.*;

public class OpcionesHuespedView extends JDialog {
    private JPanel contentPane;
    private JButton iniciarSesiónButton;
    private JButton registrarseButton;
    private JButton volverButton;

    private Empresa empresa;

    public OpcionesHuespedView(JFrame parent, Empresa empresa) {
        super(parent, "Portal del Huésped", true);
        this.empresa = empresa;

        setContentPane(contentPane);
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }
    private void initListeners() {
        // 1. Botón para ir al Login
        iniciarSesiónButton.addActionListener(e -> {
            dispose(); // Cierra esta ventana de opciones
            LoginHuesped login = new LoginHuesped((JFrame) getParent(), empresa);
            login.setVisible(true);
        });

        // 2. Botón para ir al Formulario de Registro
        registrarseButton.addActionListener(e -> {
            dispose(); // Cierra esta ventana de opciones
            RegistroHuespedView registro = new RegistroHuespedView((JFrame) getParent(), empresa);
            registro.setVisible(true);
        });

        // 3. Botón Volver
        volverButton.addActionListener(e -> dispose());
    }
}