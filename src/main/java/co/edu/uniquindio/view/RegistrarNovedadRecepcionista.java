package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistrarNovedadRecepcionista extends JDialog {
    private JPanel contentPane;
    private JComboBox<Apartamento> comboApartamentos;
    private JTextField textDescripcionNovedad;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Recepcionista recepcionistaActual;

    public RegistrarNovedadRecepcionista(JDialog parent, Empresa empresa, Recepcionista recepcionistaActual) {
        super(parent, "Registro de Novedad", true);
        this.empresa = empresa;
        this.recepcionistaActual = recepcionistaActual;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        if (buttonOK != null) {
            getRootPane().setDefaultButton(buttonOK);
        }

        cargarApartamentos();
        aplicarEstilos();
        initListeners();

        setSize(500, 340);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void cargarApartamentos() {
        if (comboApartamentos != null && empresa != null && empresa.getListaApartamentos() != null) {
            comboApartamentos.removeAllItems();
            for (Apartamento a : empresa.getListaApartamentos()) {
                comboApartamentos.addItem(a);
            }
        }
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(textDescripcionNovedad);
        configurarBoton(buttonOK,"","Registrar Novedad");
        configurarBoton(buttonCancel,"", "Cancelar");
    }

    private void aplicarBordeRedondo(JTextField campo) {
        if (campo == null) return;
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

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

    private void initListeners() {
        if (buttonOK != null) buttonOK.addActionListener(e -> registrarNovedad());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> dispose());
    }

    private void registrarNovedad() {
        Apartamento aptoSeleccionado = (Apartamento) comboApartamentos.getSelectedItem();
        String descripcion = textDescripcionNovedad.getText().trim();

        String resultado;
        if (recepcionistaActual != null) {
            resultado = recepcionistaActual.registrarNovedadApartamento(aptoSeleccionado, descripcion);
        } else if (aptoSeleccionado == null) {
            resultado = "Selecciona un apartamento válido.";
        } else if (descripcion.isEmpty()) {
            resultado = "La descripción de la novedad es obligatoria.";
        } else {
            aptoSeleccionado.registrarNovedad(descripcion, "Recepcionista");
            resultado = "OK";
        }

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this,
                    "¡Novedad registrada con éxito en el apartamento " + aptoSeleccionado.getCodigo() + "!",
                    "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "⚠ " + resultado, "Atención ⚠", JOptionPane.WARNING_MESSAGE);
        }
    }
}