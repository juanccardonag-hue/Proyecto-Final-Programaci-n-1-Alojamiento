package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

        pack(); // Ajusta la ventana respetando el layout definido en el .form
        setResizable(false);
        setLocationRelativeTo(parent);

        initListeners();
    }

    private void configurarTabla() {
        String[] columnas = {"Código", "Nombre", "Dormitorios", "Capacidad", "Tarifa Persona/Noche"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar la edición directa de celdas
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

            if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
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

            List<Apartamento> disponibles = huespedActual.consultarApartamentosDisponibles(empresa.getListaApartamentos(), empresa.getListaReservas(), inicio, fin, personas);

            tableModel.setRowCount(0);
            if (disponibles != null) {
                for (Apartamento apto : disponibles) {
                    tableModel.addRow(new Object[]{apto.getCodigo(), apto.getNombre(), apto.getNumeroDormitorio(), apto.getCapaMaxPersona() + " pers.", "$" + apto.getTarifaPersonaNoche()});
                }
            }

            if (disponibles == null || disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay apartamentos disponibles para ese rango de fechas y capacidad.",
                        "Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa las fechas en formato YYYY-MM-DD (ej: 2026-12-01).",
                    "Formato Incorrecto ⚠",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un número válido para el campo de personas.",
                    "Valor Incorrecto ⚠",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}