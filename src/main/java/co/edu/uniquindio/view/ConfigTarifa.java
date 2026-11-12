package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class ConfigTarifa extends JDialog{
    private JTextField textCodigo;
    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JTextField textTarifa;
    private JPanel mainPanel;

    private Empresa empresa;
    private Apartamento apartamentoEncontrado;

    public ConfigTarifa (JDialog parent, Empresa empresa){
        super(parent, "Configurar Tarifa apartamento", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(850, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textCodigo);
        aplicarBordeRedondo(textTarifa);

        configurarBoton(btnCancelar, "Cancelar");
        configurarBoton(btnGuardar, "Guardar");

        icons();
        initListeners();

    }
    private void initListeners(){
        btnBuscar.addActionListener(e -> {
            String codigo = textCodigo.getText().trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Por favor ingresa el código del apartamento ⚠",
                        "Campo vacío",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Apartamento apto = empresa.getAdministrador().buscarApartamento(empresa.getListaApartamentos(), codigo);

            if (apto != null) {
                this.apartamentoEncontrado = apto;
                textTarifa.setText(String.valueOf(apto.getTarifaPersonaNoche()));
                JOptionPane.showMessageDialog(this,
                        "Apartamento encontrado: " + apto.getNombre() + "\nTarifa actual: $" + apto.getTarifaPersonaNoche(),
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.apartamentoEncontrado = null;
                JOptionPane.showMessageDialog(this,
                        "⚠ No se encontró ningún apartamento con el código: " + codigo + " ⚠",
                        "No encontrado",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGuardar.addActionListener(e -> {
            if (apartamentoEncontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Debe buscar un apartamento antes de guardar la nueva tarifa ⚠",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                long nuevaTarifa = Long.parseLong(textTarifa.getText().trim());

                if (nuevaTarifa <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "⚠ La tarifa debe ser un valor mayor a cero ⚠",
                            "Tarifa Inválida",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean exito = empresa.getAdministrador().actualizarTarifaBase(apartamentoEncontrado, nuevaTarifa);

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                            "¡Tarifa base actualizada correctamente para el apartamento " + apartamentoEncontrado.getCodigo() + "!",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "⚠ No se pudo actualizar la tarifa ⚠",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Ingresa un valor numérico válido para la tarifa ⚠",
                        "Error de Formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancelar.addActionListener(e -> dispose());
    }

    public void icons() {
        configurarBoton(btnCancelar, "", "Cancelar");
        configurarBoton(btnGuardar, "", "Guardar Temporada");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);
        boton.setFont(new Font("Vensim Sans Mono", Font.BOLD, 14));
        boton.setForeground(new Color(0x2C3E50));
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
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}
