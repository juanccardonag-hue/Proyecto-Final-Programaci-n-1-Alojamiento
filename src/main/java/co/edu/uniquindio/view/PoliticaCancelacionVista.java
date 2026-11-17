package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.PoliticaCancelacion;

import javax.swing.*;
import java.awt.*;

public class PoliticaCancelacionVista extends JDialog{
    private JPanel mainPanel;
    private JTextField textDiasTotal;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JTextField textDiasParcial;
    private JTextField textPorcentajeRetencion;

    private Empresa empresa;

    public PoliticaCancelacionVista(JDialog parent, Empresa empresa){
        super(parent, "Configurar politica de cancelación", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(700, 450);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textDiasParcial);
        aplicarBordeRedondo(textDiasTotal);
        aplicarBordeRedondo(textPorcentajeRetencion);

        configurarBoton(btnCancelar, "","Cancelar");
        configurarBoton(btnGuardar, "","Guardar política");

        initListeners();

        PoliticaCancelacion actual = empresa.getAdministrador().getPoliticaCancelacion();
        if (actual != null) {
            textDiasTotal.setText(String.valueOf(actual.getDiasDevolucionTotal()));
            textDiasParcial.setText(String.valueOf(actual.getDiasDevolucionParcial()));
            textPorcentajeRetencion.setText(String.valueOf(actual.getPorcentajeRetencion()));
        }
    }

    private void initListeners (){
        btnGuardar.addActionListener(e -> {
            String diasTotalStr = textDiasTotal.getText().trim();
            String diasParcialStr = textDiasParcial.getText().trim();
            String pctRetencionStr = textPorcentajeRetencion.getText().trim();

            if (diasTotalStr.isEmpty() || diasParcialStr.isEmpty() || pctRetencionStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Por favor completa todos los campos ⚠",
                        "Campos vacíos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int diasTotal = Integer.parseInt(diasTotalStr);
                int diasParcial = Integer.parseInt(diasParcialStr);
                double pctRetencion = Double.parseDouble(pctRetencionStr);

                if (diasTotal <= diasParcial) {
                    JOptionPane.showMessageDialog(this,
                            "⚠ Los días para devolución total deben ser mayores a los días de devolución parcial ⚠",
                            "Error de lógica",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (pctRetencion < 0 || pctRetencion > 1) {
                    JOptionPane.showMessageDialog(this,
                            "⚠ El porcentaje de retención debe estar entre 0.0 y 1.0 (ejemplo: 0.50 para 50%) ⚠",
                            "Porcentaje inválido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                PoliticaCancelacion nuevaPolitica = new PoliticaCancelacion(diasTotal, diasParcial, pctRetencion);
                boolean exito = empresa.getAdministrador().configurarPoliticaCancelacion(nuevaPolitica);

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                            "¡Política de cancelación actualizada con éxito! \n\n" +
                                    "✦ Más de " + diasTotal + " días: Devolución del 100% \n" +
                                    "✦ Entre " + diasParcial + " y " + diasTotal + " días: Retención del " + (pctRetencion * 100) + "%\n" +
                                    "✦ Menos de " + diasParcial + " días: Retención del 100%",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "⚠ No se pudo actualizar la política de cancelación ⚠",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Ingresa números enteros para días y decimales para el porcentaje ⚠",
                        "Error de Formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancelar.addActionListener(e -> dispose());
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

    private void aplicarBordeRedondo(JTextField campo) {
        if (campo == null) return;
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
