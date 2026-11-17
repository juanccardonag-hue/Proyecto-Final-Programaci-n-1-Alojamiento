package co.edu.uniquindio.view;

import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Temporada;
import co.edu.uniquindio.TipoTemporada;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DefinirTemporada extends JDialog{
    private JPanel mainPanel;
    private JComboBox <TipoTemporada> comboTipoTemporada;
    private JTextField textFechaInicio;
    private JTextField textFechaFin;
    private JTextField textRecargo;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Empresa empresa;

    public DefinirTemporada (JDialog parent, Empresa empresa){
        super(parent, "Definir Temporada Nueva", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(800, 650);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        aplicarBordeRedondo(textFechaInicio);
        aplicarBordeRedondo(textFechaFin);
        aplicarBordeRedondo(textRecargo);
        comboTipoTemporada.setModel(new DefaultComboBoxModel<>(TipoTemporada.values()));

        initListeners();
        configurarBoton(btnCancelar, "Cancelar");
        configurarBoton(btnGuardar, "Guardar");

    }
    private void initListeners() {
        btnGuardar.addActionListener(e -> {
            TipoTemporada tipoSel = (TipoTemporada) comboTipoTemporada.getSelectedItem();
            String fInicio = textFechaInicio.getText().trim();
            String fFin = textFechaFin.getText().trim();
            String recargo = textRecargo.getText().trim();

            if (fInicio.isEmpty() || fFin.isEmpty() || recargo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Por favor completa todos los campos ⚠",
                        "Campos vacíos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                LocalDate inicio = LocalDate.parse(fInicio);
                LocalDate fin = LocalDate.parse(fFin);
                double porcentajeRecargo = Double.parseDouble(recargo);

                // Validación de rango de fechas
                if (!inicio.isBefore(fin)) {
                    JOptionPane.showMessageDialog(this,
                            "⚠ La fecha inicial debe ser estrictamente anterior a la fecha final ⚠",
                            "Error de Fechas",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Temporada nuevaTemporada = new Temporada(tipoSel, inicio, fin, porcentajeRecargo);

                boolean exito = empresa.getAdministrador().agregarTemporada(empresa.getListaTemporadas(), nuevaTemporada);

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                            "¡Temporada " + tipoSel + " registrada exitosamente!\n\n" +
                                    "✿ Periodo: " + inicio + " al " + fin + "\n" +
                                    "✿ Recargo: " + (porcentajeRecargo * 100) + "%",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "⚠ Las fechas ingresadas se cruzan con una temporada ya registrada ⚠",
                            "Conflicto de Fechas",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "⚠ Formato de fecha inválido. Utiliza el formato AAAA-MM-DD (ejemplo: 2026-12-15) ⚠",
                        "Error de Fecha",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "⚠ El porcentaje de recargo debe ser un número decimal (ejemplo: 0.20 para 20%) ⚠",
                        "Error de Recargo",
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
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}
