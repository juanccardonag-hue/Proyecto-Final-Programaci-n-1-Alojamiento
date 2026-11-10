package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class EliminarApto extends JDialog {
    private JPanel mainPanel;
    private JTextField textCodigo;
    private JTextField textMotivo;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Apartamento apartamentoEncontrado;

    public EliminarApto(JDialog parent, Empresa empresa) {
        super(parent, "Eliminar Apartamento", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(850, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // Aplicar bordes redondeados a los inputs
        aplicarBordeRedondo(textCodigo);
        aplicarBordeRedondo(textMotivo);

        icons();
        initListeners();
    }

    private void initListeners() {
        // 1. EVENTO BUSCAR
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
                JOptionPane.showMessageDialog(this,
                        "¡Apartamento localizado!\n\n" +
                                "Código: " + apto.getCodigo() + "\n" +
                                "Dormitorios: " + apto.getNumeroDormitorio() + "\n" +
                                "Capacidad: " + apto.getCapaMaxPersona() + " personas\n" +
                                "Estado actual: " + apto.getEstadoOperativo(),
                        "Apartamento encontrado",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.apartamentoEncontrado = null;
                JOptionPane.showMessageDialog(this,
                        "⚠ No se encontró ningún apartamento con el código: " + codigo + " ⚠",
                        "No encontrado",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnEliminar.addActionListener(e -> {
            if (apartamentoEncontrado == null) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Debe buscar un apartamento existente antes de retirarlo de servicio ⚠",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String motivo = textMotivo.getText().trim();

            if (motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Debes ingresar el motivo por el cual entra fuera de servicio ⚠",
                        "Motivo requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = empresa.getAdministrador().eliminarApto(apartamentoEncontrado, motivo);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "¡El apartamento " + apartamentoEncontrado.getCodigo() + " fue marcado como FUERA DE SERVICIO!",
                        "Operación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠ No se pudo cambiar el estado del apartamento ⚠",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancelar.addActionListener(e -> dispose());
    }

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(btnCancelar, "", "Cancelar");
        configurarBoton(btnEliminar, "", "Fuera de servicio");
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
                boton.setFont(fuenteNormal);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
            }
        });
    }

    void aplicarBordeRedondo(JTextField campo) {
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