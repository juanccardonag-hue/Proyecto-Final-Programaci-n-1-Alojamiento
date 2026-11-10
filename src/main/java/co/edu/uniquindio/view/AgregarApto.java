package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class AgregarApto extends JDialog {
    private JPanel mainPanel;
    private JTextField textCodigo;
    private JTextField textDormitorios;
    private JTextField textCapacidad;
    private JTextField textCaracteristicas;
    private JTextField textTarifa;
    private JButton btnAgregar;
    private JButton btnCancelar;
    private JLabel tittle;

    private Empresa empresa;
    private Apartamento apartamentoAgregar;

    public AgregarApto (JDialog parent, Empresa empresa) {
        super(parent, "Agregar apartamento", true);
        this.empresa = empresa;
        this.apartamentoAgregar = apartamentoAgregar;

        setContentPane(mainPanel);
        setSize(800, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textCodigo);
        aplicarBordeRedondo(textDormitorios);
        aplicarBordeRedondo(textCapacidad);
        aplicarBordeRedondo(textCaracteristicas);
        aplicarBordeRedondo(textTarifa);

        initListeners();
        configurarBoton(btnCancelar, "Cancelar");
        configurarBoton(btnAgregar, "Guardar");
    }
    private void initListeners() {
        btnAgregar.addActionListener(e -> agregarApto());
        btnCancelar.addActionListener(e -> dispose());
    }
    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;

        if (titulo != null && !titulo.isEmpty()) {
            boton.setText(titulo);
        }

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setFont(fuenteNormal);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
    }

    private void aplicarBordeRedondo(JTextField campo) {
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(java.awt.Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15); // 15 es el nivel de curvatura
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10) // Margen interno del texto
        ));
    }

    private void agregarApto() {
        String codigo = textCodigo.getText().trim();
        String numDormitorios = textDormitorios.getText().trim();
        String capacidadMax = textCapacidad.getText().trim();
        String caracteristicas = textCaracteristicas.getText().trim();
        String tarifaNoche = textTarifa.getText().trim();

        if (codigo.isEmpty() || numDormitorios.isEmpty() || capacidadMax.isEmpty() || tarifaNoche.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Por favor completa los campos requeridos ⚠",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int dormitorios = Integer.parseInt(numDormitorios);
            int capacidad = Integer.parseInt(capacidadMax);
            long tarifa = Long.parseLong(tarifaNoche);

            String nombre = "Apto " + codigo;
            Apartamento nuevoApto = new Apartamento(codigo, nombre, dormitorios, capacidad, caracteristicas, tarifa);

            boolean exito = empresa.getAdministrador().crearApartamento(
                    empresa.getListaApartamentos(),
                    nuevoApto
            );

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "¡Apartamento creado exitosamente!",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠ Ya existe un apartamento registrado con el código: " + codigo + " ⚠",
                        "Código duplicado",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Los dormitorios y la capacidad deben ser números enteros, y la tarifa un número válido sin puntos ni comas ⚠",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

