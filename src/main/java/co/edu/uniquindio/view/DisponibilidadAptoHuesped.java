package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DisponibilidadAptoHuesped extends JDialog {
    private JPanel contentPane;
    private JTextField inputFechaInicio;
    private JTextField inputFechaFinal;
    private JSpinner inputPersonas;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablaApartamentos;

    private Empresa empresa;
    private Huesped huespedActual;
    private DefaultTableModel tableModel;

    public DisponibilidadAptoHuesped(JDialog parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Disponibilidad de Apartamentos - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        setContentPane(contentPane);
        configurarTabla();

        if (inputPersonas != null) {
            inputPersonas.setModel(new SpinnerNumberModel(1, 1, 20, 1));
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();

        configurarBoton(buttonCancel,"","Cancelar");

        aplicarBordeRedondo(inputFechaFinal);
        aplicarBordeRedondo(inputFechaInicio);
    }
    public void icons() {
        configurarBoton(buttonOK, "/buscar.png", "Buscar dispo");
    }

    private void configurarBoton(JButton boton,String rutaIcono, String titulo) {
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

    private void configurarTabla() {
        String[] columnas = {"Código", "Nombre", "Dormitorios", "Capacidad", "Tarifa Persona/Noche"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (tablaApartamentos != null) {
            tablaApartamentos.setModel(tableModel);
        }
    }

    private void initListeners() {
        buttonOK.addActionListener(e -> buscarDisponibilidad());
        buttonCancel.addActionListener(e -> dispose());
    }

    private void buscarDisponibilidad() {
        String inicioStr = inputFechaInicio.getText().trim();
        String finStr = inputFechaFinal.getText().trim();

        if (inicioStr.isEmpty() || finStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresa las fechas de inicio y fin.",
                    "Campos vacíos ⚠",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);

            int personas = 1;
            if (inputPersonas != null && inputPersonas.getValue() != null) {
                personas = Integer.parseInt(inputPersonas.getValue().toString());
            }

            if (personas <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El número de personas debe ser mayor a 0.",
                        "Capacidad Inválida ⚠",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!inicio.isBefore(fin)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha final debe ser posterior a la fecha de inicio.",
                        "Fechas Inválidas ⚠",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (huespedActual == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un huésped autenticado activo.",
                        "Error de Sesión ⚠",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Apartamento> disponibles = huespedActual.consultarApartamentosDisponibles(
                    empresa.getListaApartamentos(),
                    empresa.getListaReservas(),
                    inicio,
                    fin,
                    personas
            );

            tableModel.setRowCount(0);
            if (disponibles != null) {
                for (Apartamento apto : disponibles) {
                    tableModel.addRow(new Object[]{
                            apto.getCodigo(),
                            apto.getNombre(),
                            apto.getNumeroDormitorio(),
                            apto.getCapaMaxPersona() + " pers.",
                            "$" + String.format("%,d", (long) apto.getTarifaPersonaNoche())
                    });
                }
            }

            if (disponibles == null || disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay apartamentos disponibles para ese rango de fechas y capacidad.",
                        "Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos. Verifica que las fechas tengan formato AAAA-MM-DD y que el número de personas sea correcto.",
                    "Formato Incorrecto ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}