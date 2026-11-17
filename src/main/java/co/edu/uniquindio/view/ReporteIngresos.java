package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class ReporteIngresos extends JDialog {
    private JPanel mainPanel;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JLabel lblResultado;
    private JButton btnCalcular;
    private JButton btnCancelar;

    private Empresa empresa;

    public ReporteIngresos(JDialog parent, Empresa empresa) {
        super(parent, "Ingresos por Período", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(550, 420);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        aplicarBordeRedondo(txtFechaInicio);
        aplicarBordeRedondo(txtFechaFin);

        configurarBoton(btnCalcular, "Calcular Ingresos");
        configurarBoton(btnCancelar, "Cerrar");

        initListeners();
    }

    private void initListeners() {
        btnCalcular.addActionListener(e -> calcularIngresos());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void calcularIngresos() {
        String strInicio = txtFechaInicio.getText().trim();
        String strFin = txtFechaFin.getText().trim();

        if (strInicio.isEmpty() || strFin.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Ingresa la fecha inicial y la fecha final ⚠",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fechaInicio = LocalDate.parse(strInicio);
            LocalDate fechaFin = LocalDate.parse(strFin);

            if (fechaInicio.isAfter(fechaFin)) {
                JOptionPane.showMessageDialog(this,
                        "⚠ La fecha inicial debe ser anterior o igual a la fecha final ⚠",
                        "Rango inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Administrador admin = empresa.getAdministrador();
            long total = admin.calcularIngresosPeriodo(empresa.getListaReservas(), fechaInicio, fechaFin);

            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            String monedaFormateada = formatoMoneda.format(total);

            lblResultado.setText("<html><div style='text-align: center;'>" +
                    "<b>Total Ingresos Recaudados:</b><br>" +
                    "<font size='5' color='#27AE60'>" + monedaFormateada + "</font></div></html>");

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Formato de fecha inválido. Usa AAAA-MM-DD (ejemplo: 2026-01-15) ⚠",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        boton.setText(titulo);

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