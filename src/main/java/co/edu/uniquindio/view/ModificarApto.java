package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class ModificarApto extends JDialog {
    private JPanel mainPanel;
    private JTextField textCodigo;
    private JTextField textDormitorios;
    private JTextField textCapacidad;
    private JTextField textCaracteristicas;
    private JTextField textTarifa;
    private JButton btnBuscar;
    private JButton btnModificar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Apartamento apartamentoEncontrado;

    public ModificarApto(JDialog parent, Empresa empresa){
        super(parent, "Modificar apartamento", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(850, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textCapacidad);
        aplicarBordeRedondo(textCaracteristicas);
        aplicarBordeRedondo(textDormitorios);
        aplicarBordeRedondo(textCodigo);
        aplicarBordeRedondo(textTarifa);

        icons();
        initListeners();
    }

    private void initListeners() {
        btnBuscar.addActionListener(e -> {
            String codigo = textCodigo.getText().trim();

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Por favor ingresa el código del apartamento a buscar ⚠",
                        "Campo vacío",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Apartamento apto = empresa.getAdministrador().buscarApartamento(empresa.getListaApartamentos(), codigo);

            if (apto != null) {
                this.apartamentoEncontrado = apto;
                textDormitorios.setText(String.valueOf(apto.getNumeroDormitorio()));
                textCapacidad.setText(String.valueOf(apto.getCapaMaxPersona()));
                textCaracteristicas.setText(apto.getCaracteristica());
                textTarifa.setText(String.valueOf(apto.getTarifaPersonaNoche()));
            } else {
                this.apartamentoEncontrado = null;
                JOptionPane.showMessageDialog(this,
                        "⚠ No se encontró ningún apartamento con el código: " + codigo + " ⚠",
                        "No encontrado",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            if (apartamentoEncontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Debe buscar un apartamento antes de guardar los cambios ⚠",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int dorm = Integer.parseInt(textDormitorios.getText().trim());
                int cap = Integer.parseInt(textCapacidad.getText().trim());
                String caracteristicas = textCaracteristicas.getText().trim();
                long tarifa = Long.parseLong(textTarifa.getText().trim());

                boolean exito = empresa.getAdministrador().modificarApartamento(
                        apartamentoEncontrado,
                        apartamentoEncontrado.getNombre(), // Mantiene el nombre actual del apartamento
                        dorm,
                        cap,
                        caracteristicas,
                        tarifa
                );

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                            "¡Apartamento modificado correctamente!",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "⚠ No se pudo modificar el apartamento ⚠",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Datos inválidos ⚠",
                        "Error de entrada",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancelar.addActionListener(e -> dispose());
    }
    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(btnCancelar,"","Cancelar");
        configurarBoton(btnModificar, "","Guardar");
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom   = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setText(titulo);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setMargin(new Insets(5, 10, 5, 10));

        Image imgBase = null;
        if (rutaIcono != null && !rutaIcono.trim().isEmpty()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
                imgBase = iconOriginal.getImage();
                boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.err.println("No se encontró la imagen: " + rutaIcono);
            }
        }

        final Image imagenOriginal = imgBase;

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }

                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // 1. Restaura el texto
                boton.setFont(fuenteNormal);

                // 2. Restaura el ícono
                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
            }
        });
    }
    void aplicarBordeRedondo(JTextField campo) {
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(java.awt.Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}