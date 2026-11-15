package co.edu.uniquindio.view;

import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;
import co.edu.uniquindio.Huesped;
import co.edu.uniquindio.Ocupante;
import co.edu.uniquindio.Reserva;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservaPortalHuesped extends JDialog {
    private JPanel contentPane;
    private JComboBox<Apartamento> comboApartamentos;
    private JTextField inputFechaInicio;
    private JTextField inputFechaFinal;
    private JSpinner inputPersonas;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Empresa empresa;
    private Huesped huespedActual;

    public ReservaPortalHuesped(JDialog parent, Empresa empresa, Huesped huespedActual) {
        super(parent, "Reserva Portal - Aparta-Suites UQ", true);
        this.empresa = empresa;
        this.huespedActual = huespedActual;

        if (contentPane != null) {
            setContentPane(contentPane);
        }

        if (buttonOK != null) {
            getRootPane().setDefaultButton(buttonOK);
        }

        cargarComboApartamentos();
        initListeners();

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void cargarComboApartamentos() {
        if (comboApartamentos != null && empresa != null && empresa.getListaApartamentos() != null) {
            comboApartamentos.removeAllItems();
            for (Apartamento apto : empresa.getListaApartamentos()) {
                comboApartamentos.addItem(apto);
            }
        }
    }

    private void initListeners() {
        if (buttonOK != null) buttonOK.addActionListener(e -> onOK());
        if (buttonCancel != null) buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        if (contentPane != null) {
            contentPane.registerKeyboardAction(e -> onCancel(),
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                    JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        }
    }

    private void onOK() {
        realizarReserva();
    }

    private void onCancel() {
        dispose();
    }

    private void realizarReserva() {
        Apartamento aptoSeleccionado = (Apartamento) comboApartamentos.getSelectedItem();
        String inicioStr = inputFechaInicio.getText().trim();
        String finStr = inputFechaFinal.getText().trim();

        if (aptoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un apartamento.", "Selección Requerida ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (inicioStr.isEmpty() || finStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa las fechas de inicio y fin.", "Campos Vacíos ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);

            int personasTotal = 1;
            if (inputPersonas != null && inputPersonas.getValue() != null) {
                personasTotal = Integer.parseInt(inputPersonas.getValue().toString());
            }

            if (personasTotal <= 0) {
                JOptionPane.showMessageDialog(this, "El número de personas debe ser mayor a 0.", "Capacidad Inválida ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (personasTotal > aptoSeleccionado.getCapaMaxPersona()) {
                JOptionPane.showMessageDialog(this, "El número de personas excede la capacidad máxima del apartamento (" + aptoSeleccionado.getCapaMaxPersona() + " pers.).", "Exceso de Capacidad ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!inicio.isBefore(fin)) {
                JOptionPane.showMessageDialog(this, "La fecha final debe ser posterior a la fecha de inicio.", "Fechas Inválidas ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (inicio.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha de inicio no puede ser anterior a la fecha actual.", "Fecha Inválida ⚠", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean estaDisponible = huespedActual.consultarApartamentosDisponibles(
                    empresa.getListaApartamentos(),
                    empresa.getListaReservas(),
                    inicio,
                    fin,
                    personasTotal
            ).contains(aptoSeleccionado);

            if (!estaDisponible) {
                JOptionPane.showMessageDialog(this, "El apartamento seleccionado no está disponible o se encuentra fuera de servicio en esas fechas.", "Sin Disponibilidad ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Ocupante> ocupantes = new ArrayList<>();
            ocupantes.add(new Ocupante(huespedActual.getNombre(), huespedActual.getFechaNacimiento()));

            int numAcompanantes = personasTotal - 1;
            for (int i = 1; i <= numAcompanantes; i++) {
                boolean registroValido = false;
                while (!registroValido) {
                    String nomOcupante = JOptionPane.showInputDialog(this,
                            "Nombre completo del Acompañante #" + i + ":",
                            "Registro de Acompañante " + i + "/" + numAcompanantes,
                            JOptionPane.QUESTION_MESSAGE);

                    if (nomOcupante == null) {
                        JOptionPane.showMessageDialog(this, "Proceso de reserva cancelado.", "Reserva Cancelada", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if (nomOcupante.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Dato Requerido ⚠", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    String fNacStr = JOptionPane.showInputDialog(this,
                            "Fecha de nacimiento de " + nomOcupante.trim() + " (AAAA-MM-DD):",
                            "Registro de Acompañante " + i + "/" + numAcompanantes,
                            JOptionPane.QUESTION_MESSAGE);

                    if (fNacStr == null) {
                        JOptionPane.showMessageDialog(this, "Proceso de reserva cancelado.", "Reserva Cancelada", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    try {
                        LocalDate fNac = LocalDate.parse(fNacStr.trim());
                        if (fNac.isAfter(LocalDate.now())) {
                            JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede ser una fecha futura.", "Fecha Inválida ⚠", JOptionPane.WARNING_MESSAGE);
                        } else {
                            ocupantes.add(new Ocupante(nomOcupante.trim(), fNac));
                            registroValido = true;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Formato incorrecto. Usa AAAA-MM-DD.", "Formato Inválido ⚠", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            Reserva nuevaReserva = huespedActual.crearReservaPortal(
                    aptoSeleccionado,
                    inicio,
                    fin,
                    ocupantes,
                    empresa.getListaReservas(),
                    empresa.getListaTemporadas()
            );

            if (nuevaReserva != null) {
                long totalEstancia = nuevaReserva.getValorTotal();
                long abonoMinimo30 = nuevaReserva.calcularRestanteAnticipo30();
                LocalDateTime limitePago = nuevaReserva.getFechaHoraCreacion().plusHours(24);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                JOptionPane.showMessageDialog(this,
                        "¡Solicitud de Reserva Registrada!\n\n" +
                                "• Código de Reserva: " + nuevaReserva.getCodigoFolio() + "\n" +
                                "• Titular: " + huespedActual.getNombre() + "\n" +
                                "• Apartamento: " + aptoSeleccionado.getNombre() + "\n" +
                                "• Fechas: " + inicio + " al " + fin + "\n" +
                                "• Ocupantes Registrados: " + ocupantes.size() + "\n\n" +
                                "COSTO TOTAL ESTANCIA: $" + String.format("%,d", totalEstancia) + "\n" +
                                "ABONO MÍNIMO (30%): $" + String.format("%,d", abonoMinimo30) + "\n\n" +
                                "⚠ INFORMACIÓN IMPORTANTE:\n" +
                                "Para CONFIRMAR su reserva debe realizar el abono mínimo del 30% antes de:\n" +
                                limitePago.format(dtf) + " (Plazo máximo: 24 horas) desde el módulo de PAGOS.",
                        "Reserva Registrada",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verifica que las fechas tengan formato AAAA-MM-DD y el número de personas sea válido.", "Datos Inválidos ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}