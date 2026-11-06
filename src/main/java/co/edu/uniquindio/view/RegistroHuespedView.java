package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RegistroHuespedView extends JDialog {
    private JPanel contentPane;
    private JTextField inputCedula;
    private JTextField inputNombre;
    private JTextField inputFechaNacimiento;
    private JTextField inputTelefono;
    private JTextField inputCorreo;
    private JTextField inputContrasena;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    private Empresa empresa;

    public RegistroHuespedView(JFrame parent, Empresa empresa) {
        super(parent, "Registro de Nuevo Huésped", true);
        this.empresa = empresa;

        setContentPane(contentPane);
        setSize(480, 540);
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void initListeners() {
        btnRegistrar.addActionListener(e -> registrarHuesped());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void registrarHuesped() {
        String cedula = inputCedula.getText().trim();
        String nombre = inputNombre.getText().trim();
        String fechaStr = inputFechaNacimiento.getText().trim();
        String telefono = inputTelefono.getText().trim();
        String correo = inputCorreo.getText().trim();
        String contrasena = new String(inputContrasena.getText()).trim();

        // 1. Validar campos requeridos
        if (cedula.isEmpty() || nombre.isEmpty() || fechaStr.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos obligatorios.",
                    "Campos incompletos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Formatear la fecha ingresada
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Usa el formato AAAA-MM-DD (Ejemplo: 2000-05-15).",
                    "Fecha inválida ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validar duplicados de cédula y correo
        for (Huesped h : empresa.getListaHuespedes()) {
            if (h.getCedula().equalsIgnoreCase(cedula)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un usuario registrado con la cédula " + cedula,
                        "Cédula duplicada ❌",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (h.getCorreo().equalsIgnoreCase(correo)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un usuario registrado con el correo " + correo,
                        "Correo duplicado ❌",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 4. Instanciar y guardar en la lista de la empresa
        Huesped nuevoHuesped = new Huesped(cedula, nombre, fechaNacimiento, telefono, correo, contrasena);
        empresa.getListaHuespedes().add(nuevoHuesped);

        JOptionPane.showMessageDialog(this,
                "¡Registro exitoso! Por favor inicia sesión con tu usuario.",
                "Registro Exitoso 🎉",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();

        // 5. Redirigir a LoginHuesped
        LoginHuesped login = new LoginHuesped((JFrame) getParent(), empresa);
        login.setVisible(true);
    }
}