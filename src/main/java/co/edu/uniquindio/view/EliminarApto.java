package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;

public class EliminarApto extends JDialog {
    private JPanel mainPanel;
    private JTextField textCodigo;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Apartamento apartamentoEncontrado;

    public EliminarApto(JDialog parent, Empresa empresa) {
        super(parent, "Eliminar Apartamento del Sistema", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(700, 450);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textCodigo);

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
                JOptionPane.showMessageDialog(this,
                        "¡Apartamento localizado!\n\n" +
                                "Código: " + apto.getCodigo() + "\n" +
                                "Nombre: " + apto.getNombre() + "\n" +
                                "Dormitorios: " + apto.getNumeroDormitorio() + "\n" +
                                "Capacidad: " + apto.getCapaMaxPersona() + " personas",
                        "Apartamento Encontrado",
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
                        "⚠ Debe buscar un apartamento antes de eliminarlo ⚠",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro/a de que deseas eliminar permanentemente el apartamento " + apartamentoEncontrado.getCodigo() + "?\nEsta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = empresa.getAdministrador().eliminarApartamento(empresa.getListaApartamentos(), apartamentoEncontrado);

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                            "¡El apartamento " + apartamentoEncontrado.getCodigo() + " ha sido eliminado correctamente!",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "⚠ No se pudo eliminar el apartamento del sistema ⚠",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancelar.addActionListener(e -> dispose());
    }

    public void icons() {
        configurarBoton(btnBuscar, "/Buscar.png", "Buscar");
        configurarBoton(btnCancelar, "","Cancelar");
        configurarBoton(btnEliminar, "","Eliminar Definitivamente");
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