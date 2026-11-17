package co.edu.uniquindio.view;

import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class OcupacionApto extends JDialog {
    private JPanel mainPanel;
    private JTextField txtCodigoApto;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JLabel lblResultado;
    private JButton btnCalcular;
    private JButton btnCerrar;

    private Empresa empresa;

    public OcupacionApto(JDialog parent, Empresa empresa) {
        super(parent, "Ocupación por Apartamento", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(550, 420);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        aplicarBordeRedondo(txtCodigoApto);
        aplicarBordeRedondo(txtFechaInicio);
        aplicarBordeRedondo(txtFechaFin);

        configurarBoton(btnCalcular, "Calcular Ocupación");
        configurarBoton(btnCerrar, "Cerrar");

        initListeners();
    }

    private void initListeners() {
        btnCalcular.addActionListener(e -> calcularOcupacion());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void calcularOcupacion() {
        String codigo = txtCodigoApto.getText().trim();
        String strInicio = txtFechaInicio.getText().trim();
        String strFin = txtFechaFin.getText().trim();

        if (codigo.isEmpty() || strInicio.isEmpty() || strFin.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Por favor completa todos los campos ⚠",
                    "Campos Vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Administrador admin = empresa.getAdministrador();
        Apartamento apto = admin.buscarApartamento(empresa.getListaApartamentos(), codigo);

        if (apto == null) {
            JOptionPane.showMessageDialog(this,
                    "⚠ No se encontró ningún apartamento con el código: " + codigo + " ⚠",
                    "Apartamento No Encontrado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate fechaInicio = LocalDate.parse(strInicio);
            LocalDate fechaFin = LocalDate.parse(strFin);

            if (!fechaInicio.isBefore(fechaFin)) {
                JOptionPane.showMessageDialog(this,
                        "⚠ La fecha inicial debe ser anterior a la fecha final ⚠",
                        "Rango Inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Administrador administrador = empresa.getAdministrador();
            long nochesOcupadas = administrador.calcularNochesOcupadasApartamento(
                    apto,
                    empresa.getListaReservas(),
                    fechaInicio,
                    fechaFin
            );

            long nochesTotalesPeriodo = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
            double porcentaje = 0.0;
            if (nochesTotalesPeriodo > 0) {
                porcentaje = ((double) nochesOcupadas / nochesTotalesPeriodo) * 100.0;
            }

            String resultado = String.format(
                    "<html><b>Apartamento:</b> %s<br>" +
                            "<b>Noches Ocupadas:</b> %d de %d días<br>" +
                            "<b>Porcentaje de Ocupación:</b> %.2f%%</html>",
                    apto.getCodigo(), nochesOcupadas, nochesTotalesPeriodo, porcentaje
            );

            lblResultado.setText(resultado);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Formato de fecha inválido. Usa AAAA-MM-DD  ⚠",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
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